package ch.zhaw.pm4.loganalyser.test.controller;

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
class ColumnComponentsControllerDeleteTest {

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
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testDeleteNonExistingColumnComponent() {
        // prepare
        String content = TestUtils.loadResourceContent("ColumnComponent/testUpdateNonExistingColumnComponent.json");
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
