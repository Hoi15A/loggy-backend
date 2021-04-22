package ch.zhaw.pm4.loganalyser.log;

import ch.zhaw.pm4.loganalyser.controller.ColumnComponentController;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.service.ColumnComponentService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;


import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ColumnComponentsControllerTest {

    public final static String BAD_REQUEST_MESSAGE = "Validierung fehlgeschlagen";

    @MockBean
    ColumnComponentService columnComponentService;

    @Autowired
    ColumnComponentController columnComponentController;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetAllColumnComponents() {
        List<ColumnComponentDTO> dtos = new ArrayList<>();
        dtos.add(new ColumnComponentDTO(1L, ColumnType.MESSAGE, "-|[a-zA-Z]+", "User"));
        dtos.add(new ColumnComponentDTO(2L, ColumnType.MESSAGE, "-", "Custom Separator"));

        Mockito.when(columnComponentService.getAllColumnComponents()).thenReturn(dtos);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/column")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].name").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].format").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].columnType").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].name",
                            Matchers.anyOf(hasItem(dtos.get(0).getName()),
                                    hasItem(dtos.get(1).getName()))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].format",
                            Matchers.anyOf(hasItem(dtos.get(0).getFormat()),
                                    hasItem(dtos.get(1).getFormat()))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id",
                            Matchers.is((int) dtos.get(0).getId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].columnType",
                            Matchers.contains((dtos.get(0).getColumnType().toString()),
                                    (dtos.get(1).getColumnType().toString()))))
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }
        Mockito.verify(columnComponentService, Mockito.times(1)).getAllColumnComponents();
    }

    @Test
    void testGetColumnComponentById() {
        ColumnComponentDTO dto = new ColumnComponentDTO(1L, ColumnType.MESSAGE, "-|[a-zA-Z]+", "User");

        Mockito.when(columnComponentService.getColumnComponentById(Mockito.anyLong())).thenReturn(dto);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/column/10")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.format").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.columnType").isNotEmpty())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }
        Mockito.verify(columnComponentService, Mockito.times(1)).getColumnComponentById(Mockito.anyLong());
    }

    @Test
    void testCreateColumnComponent() {
        try {
            File columnFile = ResourceUtils.getFile("classpath:testfiles/testCreateColumnComponent.json");
            String content = new String(Files.readAllBytes(columnFile.toPath()));
            Mockito.doNothing().when(columnComponentService).createColumn(Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders
                    .post("/column/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            Mockito.verify(columnComponentService, Mockito.times(1)).createColumn(Mockito.any());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testUpdateExistingColumnComponent() {
        try {
            File columnFile = ResourceUtils.getFile("classpath:testfiles/testUpdateExistingColumnComponent.json");
            String content = new String(Files.readAllBytes(columnFile.toPath()));
            Mockito.doNothing().when(columnComponentService).updateColumn(Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders
                    .put("/column/1")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            Mockito.verify(columnComponentService, Mockito.times(1)).updateColumn(Mockito.any());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testUpdateExistingColumnComponentWithCorruptedJSON() {
        try {
            File columnFile = ResourceUtils.getFile(
                    "classpath:testfiles/corruptedColumnComponent.json");
            String content = new String(Files.readAllBytes(columnFile.toPath()));
            Mockito.doNothing().when(columnComponentService).updateColumn(Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders
                    .put("/column/1") //existing column id
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            Mockito.verify(columnComponentService, Mockito.times(0)).updateColumn(Mockito.any());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testUpdateNonExistingColumnComponentWithCorruptedJSON() {
        try {
            File columnFile = ResourceUtils.getFile(
                    "classpath:testfiles/corruptedColumnComponent.json");
            String content = new String(Files.readAllBytes(columnFile.toPath()));
            Mockito.doNothing().when(columnComponentService).updateColumn(Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders
                    .put("/column/222222") //non existing column id
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(BAD_REQUEST_MESSAGE)));

            Mockito.verify(columnComponentService, Mockito.times(0)).updateColumn(Mockito.any());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testDeleteColumnComponentById() {
        ColumnComponentDTO columnComponentDTO = new ColumnComponentDTO();
        columnComponentDTO.setId(1);

        Mockito.when(columnComponentService.deleteColumnComponentById(1)).thenReturn(columnComponentDTO);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/column/1")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }
    }
}
