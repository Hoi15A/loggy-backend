package ch.zhaw.pm4.loganalyser.test.controller;

import ch.zhaw.pm4.loganalyser.exception.ApiExceptionHandler;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.service.ColumnComponentService;
import ch.zhaw.pm4.loganalyser.test.TestUtils;
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

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ColumnComponentsControllerGetTest {

    public static final long EXISTING_ID = 11L;

    @MockBean
    ColumnComponentService columnComponentService;

    @Autowired
    MockMvc mockMvc;

    final ObjectMapper objectMapper = new ObjectMapper();

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetAllColumnComponents() {
        // prepare
        String content = TestUtils.loadResourceContent("ColumnComponent/testGetAllColumnComponents.json");
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
        ColumnComponentDTO dto = ColumnComponentDTO.builder()
                .id(EXISTING_ID)
                .columnType(ColumnType.TEXT)
                .format("-|[a-zA-Z]+")
                .name("User")
                .build();

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

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetNonExistingColumnComponent() {
        // prepare
        String content = TestUtils.loadResourceContent("ColumnComponent/testUpdateNonExistingColumnComponent.json");
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
