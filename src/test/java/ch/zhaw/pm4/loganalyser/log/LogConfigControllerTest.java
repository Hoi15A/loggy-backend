package ch.zhaw.pm4.loganalyser.log;

import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LogConfigControllerTest  extends ControllerTest {

    @MockBean
    LogConfigService logConfigService;

    @Autowired
    MockMvc mockMvc;

    /* ****************************************************************************************************************
     * POSITIV TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetAllLogConfigs() {
        // prepare
        Map<Integer, ColumnComponentDTO> components1 = new HashMap<>();
        components1.put(1, null);

        Map<Integer, ColumnComponentDTO> components2 = new HashMap<>();
        components2.put(2, null);

        List<LogConfigDTO> dtos = new ArrayList<>();
        dtos.add(new LogConfigDTO("test1", 0, 0, " ", components1));
        dtos.add(new LogConfigDTO("test2", 0, 0, " ", components2));

        when(logConfigService.getAllLogConfigs()).thenReturn(dtos);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/config")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.[*].name").isNotEmpty())
                    .andExpect(jsonPath("$.[*].name", anyOf(
                            hasItem(dtos.get(0).getName()),
                            hasItem(dtos.get(1).getName())
                    )))
                    .andExpect(jsonPath("$.[*].columnCount").isNotEmpty())
                    .andExpect(jsonPath("$.[*].columnCount", anyOf(
                            hasItem(dtos.get(0).getColumnCount()),
                            hasItem(dtos.get(1).getColumnCount())
                    )))
                    .andExpect(jsonPath("$.[*].headerLength").isNotEmpty())
                    .andExpect(jsonPath("$.[*].headerLength", anyOf(
                            hasItem(dtos.get(0).getHeaderLength()),
                            hasItem(dtos.get(1).getHeaderLength())
                    )))
                    .andExpect(jsonPath("$.[*].separator").isNotEmpty())
                    .andExpect(jsonPath("$.[*].separator", anyOf(
                            hasItem(dtos.get(0).getSeparator()),
                            hasItem(dtos.get(1).getSeparator())
                    )))
                    .andExpect(jsonPath("$.[*].columnComponents").isNotEmpty())
                    .andExpect(jsonPath("$.[*].columnComponents.[*]", anyOf(
                            hasItem(components1.get(1)),
                            hasItem(components2.get(2))
                    )))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logConfigService, times(1)).getAllLogConfigs();
    }

    @Test
    void testGetLogConfigById() {
        // prepare
        LogConfigDTO dto = new LogConfigDTO("test1", 1, 2, "|", new HashMap<>());

        when(logConfigService.getLogConfigById(any())).thenReturn(dto);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/config/test1")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.name").isString())
                    .andExpect(jsonPath("$.name", equalTo(dto.getName())))
                    .andExpect(jsonPath("$.columnCount").isNumber())
                    .andExpect(jsonPath("$.columnCount", equalTo(dto.getColumnCount())))
                    .andExpect(jsonPath("$.headerLength").isNumber())
                    .andExpect(jsonPath("$.headerLength", equalTo(dto.getHeaderLength())))
                    .andExpect(jsonPath("$.separator").isString())
                    .andExpect(jsonPath("$.separator", equalTo(dto.getSeparator())))
                    .andExpect(jsonPath("$.columnComponents", equalTo(dto.getColumnComponents())))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logConfigService, times(1)).getLogConfigById(any());
    }

    @Test
    void testCreateLogConfig() {
        // prepare
        String content = loadResourceContent("testCreateLogConfig.json");
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

    @Test
    void testDeleteLogConfigByExistingId() {
        // prepare
        LogConfigDTO logConfigDTO1 = new LogConfigDTO();
        logConfigDTO1.setName("Test1");

        when(logConfigService.deleteLogConfigById(anyString())).thenReturn(logConfigDTO1);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/config/Test1")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.name").isNotEmpty())
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logConfigService, times(1)).deleteLogConfigById(anyString());
    }

    @Test
    void testUpdateLogConfigValid() {
        // prepare
        String content = loadResourceContent("testCreateLogConfig.json");
        doNothing().when(logConfigService).updateLogConfig(any());

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .put("/config/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(logConfigService, times(1)).updateLogConfig(any());
    }

    /* ****************************************************************************************************************
     * NEGATIV TESTS
     * ****************************************************************************************************************/


}
