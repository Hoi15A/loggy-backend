package ch.zhaw.pm4.loganalyser.test.controller;

import ch.zhaw.pm4.loganalyser.exception.ApiExceptionHandler;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.service.LogServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.anyOf;
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
class LogServiceControllerGetTest {

    public static final long NON_EXISTING_ID = -2L;
    public static final long EXISTING_ID = 2L;

    @MockBean
    LogServiceService logServiceService;

    @Autowired
    MockMvc mockMvc;

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void getAllLogServices() {
        // prepare
        Set<LogServiceDTO> data = new HashSet<>();
        LogServiceDTO logServiceDTO1 = new LogServiceDTO();
        LogServiceDTO logServiceDTO2 = new LogServiceDTO();
        logServiceDTO1.setName("Test1");
        logServiceDTO2.setName("Test2");
        data.add(logServiceDTO1);
        data.add(logServiceDTO2);

        when(logServiceService.getAllLogServices()).thenReturn(data);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/service/all")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.[*].name").isNotEmpty())
                    .andExpect(jsonPath("$.[*].name", anyOf(
                            hasItem(logServiceDTO1.getName()),
                            hasItem(logServiceDTO2.getName())
                    )))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logServiceService, times(1)).getAllLogServices();
    }

    @Test
    void testGetServiceLogById() {
        // prepare
        LogServiceDTO dto = new LogServiceDTO();
        dto.setId(EXISTING_ID);
        dto.setName("Test1");

        when(logServiceService.getLogServiceById(dto.getId())).thenReturn(dto);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/service/" + dto.getId())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.name").isNotEmpty())
                    .andExpect(jsonPath("$.name", is(dto.getName())))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logServiceService, times(1)).getLogServiceById(dto.getId());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetNonExistingColumnComponent() {
        // prepare
        LogServiceDTO dto = new LogServiceDTO();
        dto.setId(NON_EXISTING_ID);
        dto.setName("Test1");

        String exceptionMessage = "ID was not found";

        doThrow(new RecordNotFoundException(exceptionMessage)).when(logServiceService).getLogServiceById(dto.getId());

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/service/" + dto.getId())
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
        verify(logServiceService, times(1)).getLogServiceById(dto.getId());
    }

}
