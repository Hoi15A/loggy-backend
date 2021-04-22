package ch.zhaw.pm4.loganalyser.test.controller;

import ch.zhaw.pm4.loganalyser.exception.ApiExceptionHandler;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ColumnComponentsControllerTest extends ControllerTest {

    public static final long EXISTING_ID = 11L;

    @MockBean
    ColumnComponentService columnComponentService;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    /* ****************************************************************************************************************
     * POSITIV TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetAllColumnComponents() {
        // prepare
        String content = loadResourceContent("ColumnComponent/testGetAllColumnComponents.json");
        ColumnComponentDTO[] dtoArray = (ColumnComponentDTO[]) parseResourceContent(content, ColumnComponentDTO[].class);
        List<ColumnComponentDTO> dtos = Arrays.asList(dtoArray);

        when(columnComponentService.getAllColumnComponents()).thenReturn(dtos);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/column")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.[*].id").isNotEmpty())
                    .andExpect(jsonPath("$.[0].id", equalTo(dtos.get(0).getId()), Long.class))
                    .andExpect(jsonPath("$.[1].id", equalTo(dtos.get(1).getId()), Long.class))
                    .andExpect(jsonPath("$.[*].name").isNotEmpty())
                    .andExpect(jsonPath("$.[*].name", anyOf(
                            hasItem(dtos.get(0).getName()),
                            hasItem(dtos.get(1).getName())
                    )))
                    .andExpect(jsonPath("$.[*].format").isNotEmpty())
                    .andExpect(jsonPath("$.[*].format", anyOf(
                            hasItem(dtos.get(0).getFormat()),
                            hasItem(dtos.get(1).getFormat())
                    )))
                    .andExpect(jsonPath("$.[*].columnType").isNotEmpty())
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
        ColumnComponentDTO dto = new ColumnComponentDTO(EXISTING_ID, ColumnType.MESSAGE, "-|[a-zA-Z]+", "User");

        when(columnComponentService.getColumnComponentById(dto.getId())).thenReturn(dto);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/column/" + dto.getId())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                    .andExpect(jsonPath("$.name").isNotEmpty())
                    .andExpect(jsonPath("$.name", is(dto.getName())))
                    .andExpect(jsonPath("$.format").isNotEmpty())
                    .andExpect(jsonPath("$.format", is(dto.getFormat())))
                    .andExpect(jsonPath("$.columnType").isNotEmpty())
                    .andExpect(jsonPath("$.columnType", is(dto.getColumnType().toString())))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(1)).getColumnComponentById(dto.getId());
    }

    @Test
    void testCreateColumnComponent() {
        // prepare
        String content = loadResourceContent("ColumnComponent/testCreateColumnComponent.json");
        ColumnComponentDTO dto = (ColumnComponentDTO) parseResourceContent(content, ColumnComponentDTO.class);

        doNothing().when(columnComponentService).createColumn(dto);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/column/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(1)).createColumn(dto);
    }

    @Test
    void testUpdateExistingColumnComponent() {
        // prepare
        String content = loadResourceContent("ColumnComponent/testUpdateExistingColumnComponent.json");
        ColumnComponentDTO dto = (ColumnComponentDTO) parseResourceContent(content, ColumnComponentDTO.class);

        doNothing().when(columnComponentService).updateColumn(dto);

        // execute
        try {
            // todo: check for same id in dto and path variable
            mockMvc.perform(MockMvcRequestBuilders
                    .put("/column/" + dto.getId())
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(1)).updateColumn(dto);
    }

    @Test
    void testDeleteColumnComponentById() {
        // prepare
        ColumnComponentDTO dto = new ColumnComponentDTO();
        dto.setId(EXISTING_ID);

        when(columnComponentService.deleteColumnComponentById(dto.getId())).thenReturn(dto);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/column/" + dto.getId())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(1)).deleteColumnComponentById(dto.getId());
    }

    /* ****************************************************************************************************************
     * NEGATIV TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetNonExistingColumnComponent() {
        // prepare
        String content = loadResourceContent("ColumnComponent/testUpdateNonExistingColumnComponent.json");
        ColumnComponentDTO dto = (ColumnComponentDTO) parseResourceContent(content, ColumnComponentDTO.class);

        String exceptionMessage = "ID was not found";

        doThrow(new RecordNotFoundException(exceptionMessage)).when(columnComponentService).getColumnComponentById(dto.getId());

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/column/" + dto.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.RECORD_NOT_FOUND_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[0]", is(exceptionMessage)))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(1)).getColumnComponentById(dto.getId());
    }

    @Test
    void testUpdateNonExistingColumnComponent() {
        // prepare
        String content = loadResourceContent("ColumnComponent/testUpdateNonExistingColumnComponent.json");
        ColumnComponentDTO dto = (ColumnComponentDTO) parseResourceContent(content, ColumnComponentDTO.class);

        String exceptionMessage = dto.getName();

        doThrow(new RecordNotFoundException(exceptionMessage)).when(columnComponentService).updateColumn(dto);

        // execute
        try {
            // todo: check for non existing id in dto and path variable
            mockMvc.perform(MockMvcRequestBuilders
                    .put("/column/" + dto.getId())
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.RECORD_NOT_FOUND_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[0]", is(exceptionMessage)))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(1)).updateColumn(dto);
    }

    @Test
    void testDeleteNonExistingColumnComponent() {
        // prepare
        String content = loadResourceContent("ColumnComponent/testUpdateNonExistingColumnComponent.json");
        ColumnComponentDTO dto = (ColumnComponentDTO) parseResourceContent(content, ColumnComponentDTO.class);

        String exceptionMessage = "Could not delete id: " + dto.getId();

        doThrow(new RecordNotFoundException(exceptionMessage)).when(columnComponentService).deleteColumnComponentById(dto.getId());

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/column/" + dto.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.RECORD_NOT_FOUND_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[0]", is(exceptionMessage)))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(1)).deleteColumnComponentById(dto.getId());
    }

    @Test
    void testUpdateExistingColumnComponentWithCorruptedJSON() {
        testUpdateWithCorruptedJSON("ColumnComponent/testUpdateExistingColumnComponentWithCorruptedJSON.json");
    }

    @Test
    void testUpdateNonExistingColumnComponentWithCorruptedJSON() {
        testUpdateWithCorruptedJSON("ColumnComponent/testUpdateNonExistingColumnComponentWithCorruptedJSON.json");
    }

    private void testUpdateWithCorruptedJSON(String relativeResourceLocation) {
        // prepare
        String content = loadResourceContent(relativeResourceLocation);
        ColumnComponentDTO dto = (ColumnComponentDTO) parseResourceContent(content, ColumnComponentDTO.class);

        // execute
        try {
            // todo: check for (non) existing id in dto and path variable
            mockMvc.perform(MockMvcRequestBuilders
                    .put("/column/" + dto.getId())
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.METHOD_ARGUMENT_NOT_VALID_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[*]", containsInAnyOrder(
                            ColumnComponentDTO.FORMAT_VALIDATION_MESSAGE,
                            ColumnComponentDTO.NAME_VALIDATION_MESSAGE,
                            ColumnComponentDTO.COLUMN_TYPE_VALIDATION_MESSAGE
                    )))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(0)).updateColumn(any());
    }

    private Object parseResourceContent(String content, Class<?> clazz) {
        Object dto;
        try {
            dto = objectMapper.readValue(content, clazz);
        } catch (IOException e) {
            dto = null;
        }
        return Objects.requireNonNull(dto);
    }

}
