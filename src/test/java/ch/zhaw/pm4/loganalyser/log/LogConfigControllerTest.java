package ch.zhaw.pm4.loganalyser.log;

import ch.zhaw.pm4.loganalyser.controller.LogConfigController;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.service.LogConfigService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LogConfigControllerTest {
    @MockBean
    LogConfigService logConfigService;

    @Autowired
    LogConfigController logConfigController;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetAllLogConfigs() {
        Map<Integer, ColumnComponentDTO> components1 = new HashMap<>();
        components1.put(1, null);

        Map<Integer, ColumnComponentDTO> components2 = new HashMap<>();
        components2.put(2, null);

        List<LogConfigDTO> dtos = new ArrayList<>();
        dtos.add(new LogConfigDTO("test1", 0, 0, " ", components1));
        dtos.add(new LogConfigDTO("test2", 0, 0, " ", components2));

        Mockito.when(logConfigService.getAllLogConfigs()).thenReturn(dtos);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/config")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.[*].name").isNotEmpty())
                    .andExpect(jsonPath("$.[*].columnCount").isNotEmpty())
                    .andExpect(jsonPath("$.[*].headerLength").isNotEmpty())
                    .andExpect(jsonPath("$.[*].separator").isNotEmpty())
                    .andExpect(jsonPath("$.[*].columnComponents").isNotEmpty())
                    .andExpect(jsonPath("$.[*].name", anyOf(
                            hasItem(dtos.get(0).getName()),
                            hasItem(dtos.get(1).getName())
                    )))
                    .andExpect(jsonPath("$.[*].separator", anyOf(
                            hasItem(dtos.get(0).getSeparator()),
                            hasItem(dtos.get(1).getSeparator())
                    )))
                    .andExpect(jsonPath("$.[*].columnCount", anyOf(
                            hasItem(dtos.get(0).getColumnCount()),
                            hasItem(dtos.get(1).getColumnCount())
                    )))
                    .andExpect(jsonPath("$.[*].headerLength", anyOf(
                            hasItem(dtos.get(0).getHeaderLength()),
                            hasItem(dtos.get(1).getHeaderLength())
                    )))
                    .andExpect(jsonPath("$.[*].columnComponents.[*]", anyOf(
                            hasItem(components1.get(1)),
                            hasItem(components2.get(2))
                    )))
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }
        verify(logConfigService, times(1)).getAllLogConfigs();
    }

    @Test
    void testGetLogConfigById() {
        LogConfigDTO dto = new LogConfigDTO("test1", 1, 2, "|", new HashMap<>());

        Mockito.when(logConfigService.getLogConfigById(any())).thenReturn(dto);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/config/test1")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.name").isString())
                    .andExpect(jsonPath("$.columnCount").isNumber())
                    .andExpect(jsonPath("$.headerLength").isNumber())
                    .andExpect(jsonPath("$.separator").isString())
                    .andExpect(jsonPath("$.name", equalTo(dto.getName())))
                    .andExpect(jsonPath("$.columnCount", equalTo(dto.getColumnCount())))
                    .andExpect(jsonPath("$.headerLength", equalTo(dto.getHeaderLength())))
                    .andExpect(jsonPath("$.separator", equalTo(dto.getSeparator())))
                    .andExpect(jsonPath("$.columnComponents", equalTo(dto.getColumnComponents())))
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }
        verify(logConfigService, times(1)).getLogConfigById(any());
    }

    @Test
    void testCreateLogConfig() {
        try {
            File jsonFile = ResourceUtils.getFile("classpath:testfiles/testCreateLogConfig.json");
            String content = new String(Files.readAllBytes(jsonFile.toPath()));
            Mockito.doNothing().when(logConfigService).createLogConfig(any());

            mockMvc.perform(MockMvcRequestBuilders
                    .post("/config/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            verify(logConfigService, times(1)).createLogConfig(any());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testDeleteLogConfigByExistingId() {
        LogConfigDTO logConfigDTO1 = new LogConfigDTO();
        logConfigDTO1.setName("Test1");

        Mockito.when(logConfigService.deleteLogConfigById("Test1")).thenReturn(logConfigDTO1);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/config/Test1")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.name").isNotEmpty())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testPutLogConfigValid() {
        Mockito.doNothing().when(logConfigService).updateLogConfig(any());

        try {
            File jsonFile = ResourceUtils.getFile("classpath:testfiles/testCreateLogConfig.json");
            String content = new String(Files.readAllBytes(jsonFile.toPath()));

            mockMvc.perform(MockMvcRequestBuilders
                    .put("/config/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }

        verify(logConfigService, times(1)).updateLogConfig(any());
    }
}
