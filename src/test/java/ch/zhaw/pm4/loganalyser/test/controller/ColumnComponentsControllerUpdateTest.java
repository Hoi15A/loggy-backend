package ch.zhaw.pm4.loganalyser.test.controller;

import ch.zhaw.pm4.loganalyser.controller.ColumnComponentController;
import ch.zhaw.pm4.loganalyser.exception.ApiExceptionHandler;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
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
import java.util.Objects;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ColumnComponentsControllerUpdateTest {

    @MockBean
    ColumnComponentService columnComponentService;

    @Autowired
    MockMvc mockMvc;

    final ObjectMapper objectMapper = new ObjectMapper();

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testUpdateExistingColumnComponent() {
        // prepare
        String content = TestUtils.loadResourceContent("ColumnComponent/testUpdateExistingColumnComponent.json");
        ColumnComponentDTO dto = (ColumnComponentDTO) parseResourceContent(content);

        doNothing().when(columnComponentService).updateColumn(dto);

        // execute
        try {
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

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testUpdateNonExistingColumnComponent() {
        // prepare
        String content = TestUtils.loadResourceContent("ColumnComponent/testUpdateNonExistingColumnComponent.json");
        ColumnComponentDTO dto = (ColumnComponentDTO) parseResourceContent(content);

        String exceptionMessage = dto.getName();

        doThrow(new RecordNotFoundException(exceptionMessage)).when(columnComponentService).updateColumn(dto);

        // execute
        try {
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
    void testUpdateExistingColumnComponentDifferentIdInRequest() {
        // prepare
        String content = TestUtils.loadResourceContent("ColumnComponent/testUpdateExistingColumnComponent.json");
        ColumnComponentDTO dto = (ColumnComponentDTO) parseResourceContent(content);

        doNothing().when(columnComponentService).updateColumn(dto);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                                    .put("/column/" + (dto.getId() + 1))
                                    .content(content)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.INTERNAL_SERVER_ERROR_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[0]", is(ColumnComponentController.UPDATE_EXCEPTION_MESSAGE)))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(columnComponentService, times(0)).updateColumn(dto);
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
        String content = TestUtils.loadResourceContent(relativeResourceLocation);
        ColumnComponentDTO dto = (ColumnComponentDTO) parseResourceContent(content);

        // execute
        try {
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

    private Object parseResourceContent(String content) {
        Object dto;
        try {
            dto = objectMapper.readValue(content, (Class<?>) ColumnComponentDTO.class);
        } catch (IOException e) {
            dto = null;
        }
        return Objects.requireNonNull(dto);
    }

}
