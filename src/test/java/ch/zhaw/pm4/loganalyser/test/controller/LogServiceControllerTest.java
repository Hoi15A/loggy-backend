package ch.zhaw.pm4.loganalyser.test.controller;

import ch.zhaw.pm4.loganalyser.exception.ApiExceptionHandler;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.service.LogServiceService;
import ch.zhaw.pm4.loganalyser.test.TestUtils;
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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LogServiceControllerTest {

    public static final long NON_EXISTING_ID = -2L;
    public static final long EXISTING_ID = 2L;

    @MockBean
    LogServiceService logServiceService;

    @Autowired
    MockMvc mockMvc;

    /* ****************************************************************************************************************
     * POSITIV TESTS
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

    @Test
    void testCreateLogService() {
        // prepare
        String content = TestUtils.loadResourceContent("LogService/testCreateLogService.json");

        doNothing().when(logServiceService).createLogService(any());

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/service/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logServiceService, times(1)).createLogService(any());
    }

    @Test
    void testDeleteLogService() {
        // prepare
        LogServiceDTO dto = new LogServiceDTO();
        dto.setId(EXISTING_ID);
        dto.setName("Test1");

        when(logServiceService.deleteLogServiceById(anyLong())).thenReturn(dto);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/service/" + dto.getId())
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
        verify(logServiceService, times(1)).deleteLogServiceById(anyLong());
    }

    /* ****************************************************************************************************************
     * NEGATIV TESTS
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

    @Test
    void testDeleteNonExistingColumnComponent() {
        // prepare
        LogServiceDTO dto = new LogServiceDTO();
        dto.setId(NON_EXISTING_ID);
        dto.setName("Test1");

        String exceptionMessage = "Could not delete id: " + dto.getId();

        doThrow(new RecordNotFoundException(exceptionMessage)).when(logServiceService).deleteLogServiceById(dto.getId());

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/service/" + dto.getId())
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
        verify(logServiceService, times(1)).deleteLogServiceById(dto.getId());
    }

    @Test
    void testCreateLogServiceWithCorruptedJSON() {
        //prepare
        String content = TestUtils.loadResourceContent("LogService/testCreateLogServiceWithCorruptedJSON.json");

        doNothing().when(logServiceService).createLogService(any());

        //execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/service/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.METHOD_ARGUMENT_NOT_VALID_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[*]", containsInAnyOrder(
                            LogServiceDTO.LOG_DIRECTORY_VALIDATION_MESSAGE,
                            LogServiceDTO.NAME_VALIDATION_MESSAGE,
                            LogServiceDTO.LOG_CONFIG_VALIDATION_MESSAGE,
                            LogServiceDTO.LOCATION_VALIDATION_MESSAGE
                    )))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        //verify
        verify(logServiceService, times(0)).createLogService(any());
    }

    @Test
    void testCreateLogServiceWithNonExistingLogConfig() {
        //prepare
        String content = TestUtils.loadResourceContent("LogService/testCreateLogServiceWithNonExistingLogConfig.json");
        String exceptionMessage = "Log config does not exists";

        doThrow(new RecordNotFoundException(exceptionMessage)).when(logServiceService).createLogService(any());

        //execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/service/")
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

        //verify
        verify(logServiceService, times(1)).createLogService(any());
    }

}
