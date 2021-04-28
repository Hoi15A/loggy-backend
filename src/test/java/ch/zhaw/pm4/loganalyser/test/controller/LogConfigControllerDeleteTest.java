package ch.zhaw.pm4.loganalyser.test.controller;

import ch.zhaw.pm4.loganalyser.exception.ApiExceptionHandler;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.service.LogConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LogConfigControllerDeleteTest {

    @MockBean
    LogConfigService logConfigService;

    @Autowired
    MockMvc mockMvc;
    
    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testDeleteLogConfig() {
        // prepare
        LogConfigDTO dto = new LogConfigDTO();
        dto.setName("Test1");

        when(logConfigService.deleteLogConfigById(dto.getName())).thenReturn(dto);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/config/" + dto.getName())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.name").isNotEmpty())
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logConfigService, times(1)).deleteLogConfigById(dto.getName());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testDeleteNonExistingLogConfig() {
        // prepare
        String exceptionMessage = "This record does not exist";

        doThrow(new RecordNotFoundException(exceptionMessage)).when(logConfigService).deleteLogConfigById(anyString());

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/config/abc")
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
        verify(logConfigService, times(1)).deleteLogConfigById(anyString());
    }

}
