package ch.zhaw.pm4.loganalyser.test.controller;

import ch.zhaw.pm4.loganalyser.exception.ApiExceptionHandler;
import ch.zhaw.pm4.loganalyser.exception.RecordAlreadyExistsException;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.service.LogConfigService;
import ch.zhaw.pm4.loganalyser.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
class LogConfigControllerCreateTest {

    @MockBean
    LogConfigService logConfigService;

    @Autowired
    MockMvc mockMvc;
    
    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testCreateLogConfig() {
        // prepare
        String content = TestUtils.loadResourceContent("LogConfig/testCreateLogConfig.json");
        doNothing().when(logConfigService).createLogConfig(any());

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/config/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logConfigService, times(1)).createLogConfig(any());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testCreateAlreadyExistingLogConfig() {
        // prepare
        String content = TestUtils.loadResourceContent("LogConfig/testCreateLogConfig.json");

        String exceptionMessage = "A config with this name already exists.";

        doThrow(new RecordAlreadyExistsException(exceptionMessage)).when(logConfigService).createLogConfig(any());

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/config/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.RECORD_ALREADY_EXISTS_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[0]", is(exceptionMessage)))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logConfigService, times(1)).createLogConfig(any());
    }

    @Test
    void testCreateLogConfigWithCorruptedJSON() {
        //prepare
        String content = TestUtils.loadResourceContent("LogConfig/testUpdateLogConfigWithCorruptedJSON.json");

        doNothing().when(logConfigService).createLogConfig(any());

        //execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/config/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.METHOD_ARGUMENT_NOT_VALID_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[*]", containsInAnyOrder(
                            LogConfigDTO.NAME_VALIDATION_MESSAGE,
                            LogConfigDTO.COLUMN_COUNT_VALIDATION_MESSAGE,
                            LogConfigDTO.HEADER_LENGTH_VALIDATION_MESSAGE,
                            LogConfigDTO.SEPARATOR_VALIDATION_MESSAGE,
                            LogConfigDTO.COLUMN_COMPONENTS_VALIDATION_MESSAGE
                    )))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        //verify
        verify(logConfigService, times(0)).createLogConfig(any());
    }
    
}
