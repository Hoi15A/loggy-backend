package ch.zhaw.pm4.loganalyser.log;

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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LogServiceControllerTest extends ControllerTest {

    @MockBean
    LogServiceService logServiceService;

    @Autowired
    MockMvc mockMvc;

    /* ****************************************************************************************************************
     * POSITIV TESTS
     * ****************************************************************************************************************/

    @Test
    void testCreateLogService() {
        // prepare
        doNothing().when(logServiceService).createLogService(any());
        String content = loadResourceContent("testCreateLogService.json");

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/service/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logServiceService, times(1)).createLogService(any());
    }

    @Test
    void testCreateLogService_ConfigMissing() {
        //prepare
        doNothing().when(logServiceService).createLogService(any());
        String content = loadResourceContent("testCreateLogService_ConfigMissing.json");

        //execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/service/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e);
        }

        //verify
        verify(logServiceService, times(0)).createLogService(any());
    }

    @Test
    void testGetServiceLogById() {
        // prepare
        LogServiceDTO logServiceDTO1 = new LogServiceDTO();
        logServiceDTO1.setName("Test1");

        when(logServiceService.getLogServiceById(anyLong())).thenReturn(logServiceDTO1);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/service/2")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.name").isNotEmpty());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logServiceService, times(1)).getLogServiceById(anyLong());
    }

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
    void deleteLogServiceByExistingId() {
        // prepare
        LogServiceDTO logServiceDTO1 = new LogServiceDTO();
        logServiceDTO1.setName("Test1");

        when(logServiceService.deleteLogServiceById(anyLong())).thenReturn(logServiceDTO1);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/service/2")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.name").isNotEmpty())
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


}
