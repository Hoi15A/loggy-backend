package ch.zhaw.pm4.loganalyser.log;

import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.service.ColumnComponentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ColumnComponentsControllerTest extends ControllerTest {

    public final static String BAD_REQUEST_MESSAGE = "Validierung fehlgeschlagen";

    @MockBean
    ColumnComponentService columnComponentService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetAllColumnComponents() {
        // prepare
        List<ColumnComponentDTO> dtos = new ArrayList<>();
        dtos.add(new ColumnComponentDTO(1L, ColumnType.MESSAGE, "-|[a-zA-Z]+", "User"));
        dtos.add(new ColumnComponentDTO(2L, ColumnType.MESSAGE, "-", "Custom Separator"));

        when(columnComponentService.getAllColumnComponents()).thenReturn(dtos);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/column")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.[*].name").isNotEmpty())
                    .andExpect(jsonPath("$.[*].format").isNotEmpty())
                    .andExpect(jsonPath("$.[*].id").isNotEmpty())
                    .andExpect(jsonPath("$.[*].columnType").isNotEmpty())
                    .andExpect(jsonPath("$.[*].name", anyOf(
                            hasItem(dtos.get(0).getName()),
                            hasItem(dtos.get(1).getName())
                    )))
                    .andExpect(jsonPath("$.[*].format", anyOf(
                            hasItem(dtos.get(0).getFormat()),
                            hasItem(dtos.get(1).getFormat())
                    )))
                    .andExpect(jsonPath("$.[0].id", equalTo(dtos.get(0).getId()), Long.class))
                    .andExpect(jsonPath("$.[1].id", equalTo(dtos.get(1).getId()), Long.class))
                    .andExpect(jsonPath("$.[*].columnType", contains(
                            dtos.get(0).getColumnType().toString(),
                            dtos.get(1).getColumnType().toString()
                    )))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(1)).getAllColumnComponents();
    }

    @Test
    void testGetColumnComponentById() {
        // prepare
        ColumnComponentDTO dto = new ColumnComponentDTO(1L, ColumnType.MESSAGE, "-|[a-zA-Z]+", "User");

        when(columnComponentService.getColumnComponentById(anyLong())).thenReturn(dto);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/column/10")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.name").isNotEmpty())
                    .andExpect(jsonPath("$.format").isNotEmpty())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.columnType").isNotEmpty())
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(1)).getColumnComponentById(anyLong());
    }

    @Test
    void testCreateColumnComponent() {
        // prepare
        doNothing().when(columnComponentService).createColumn(any());
        String content = loadResourceContent("classpath:testfiles/testCreateColumnComponent.json");

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/column/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // verify
        verify(columnComponentService, times(1)).createColumn(any());
    }

    @Test
    void testUpdateExistingColumnComponent() {
        // prepare
        doNothing().when(columnComponentService).updateColumn(any());
        String content = loadResourceContent("classpath:testfiles/testUpdateExistingColumnComponent.json");

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .put("/column/1")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(1)).updateColumn(any());
    }

    @Test
    void testUpdateExistingColumnComponentWithCorruptedJSON() {
        // prepare
        doNothing().when(columnComponentService).updateColumn(any());
        String content = loadResourceContent("classpath:testfiles/corruptedColumnComponent.json");

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .put("/column/1") //existing column id
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(0)).updateColumn(any());
    }

    @Test
    void testUpdateNonExistingColumnComponentWithCorruptedJSON() {
        // prepare
        doNothing().when(columnComponentService).updateColumn(any());
        String content = loadResourceContent("classpath:testfiles/corruptedColumnComponent.json");

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .put("/column/222222") //non existing column id
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(BAD_REQUEST_MESSAGE)));
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(0)).updateColumn(any());
    }

    @Test
    void testDeleteColumnComponentById() {
        // prepare
        ColumnComponentDTO columnComponentDTO = new ColumnComponentDTO();
        columnComponentDTO.setId(1);

        when(columnComponentService.deleteColumnComponentById(anyLong())).thenReturn(columnComponentDTO);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/column/1")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(1)).deleteColumnComponentById(anyLong());
    }

}
