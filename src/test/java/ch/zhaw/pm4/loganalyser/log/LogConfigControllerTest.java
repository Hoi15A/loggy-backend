package ch.zhaw.pm4.loganalyser.log;

import ch.zhaw.pm4.loganalyser.controller.LogConfigController;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.service.LogConfigService;
import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
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
        List<LogConfigDTO> dtos = new ArrayList<>();
        dtos.add(new LogConfigDTO("test1", 0, 0, " ", new HashMap<>()));
        dtos.add(new LogConfigDTO("test2", 0, 0, " ", new HashMap<>()));

        Mockito.when(logConfigService.getAllLogConfigs()).thenReturn(dtos);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/config")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].name").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].columnCount").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].headerLength").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].separator").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].name",
                            Matchers.anyOf(Matchers.hasItem(dtos.get(0).getName()),
                                    Matchers.hasItem(dtos.get(1).getName()))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].separator",
                            Matchers.anyOf(Matchers.hasItem(dtos.get(0).getSeparator()),
                                    Matchers.hasItem(dtos.get(1).getSeparator()))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].columnCount",
                            Matchers.anyOf(Matchers.hasItem(dtos.get(0).getColumnCount()),
                                    Matchers.hasItem(dtos.get(1).getColumnCount()))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].headerLength",
                            Matchers.anyOf(Matchers.hasItem(dtos.get(0).getHeaderLength()),
                                    Matchers.hasItem(dtos.get(1).getHeaderLength()))))
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }
        Mockito.verify(logConfigService, Mockito.times(1)).getAllLogConfigs();
    }

    @Test
    void testGetLogConfigById() {
        LogConfigDTO dto = new LogConfigDTO("test1", 1, 2, "|", new HashMap<>());

        Mockito.when(logConfigService.getLogConfigById(Mockito.any())).thenReturn(dto);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/config/test1")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").isString())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.columnCount").isNumber())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.headerLength").isNumber())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.separator").isString())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(dto.getName())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.columnCount", Matchers.equalTo(dto.getColumnCount())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.headerLength", Matchers.equalTo(dto.getHeaderLength())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.separator", Matchers.equalTo(dto.getSeparator())))
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }
        Mockito.verify(logConfigService, Mockito.times(1)).getLogConfigById(Mockito.any());
    }

    @Test
    void testCreateLogConfig() {
        try {
            File jsonFile = ResourceUtils.getFile("classpath:testfiles/testCreateLogConfig.json");
            String content = new String(Files.readAllBytes(jsonFile.toPath()));
            Mockito.doNothing().when(logConfigService).createLogConfig(Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders
                    .post("/config/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            Mockito.verify(logConfigService, Mockito.times(1)).createLogConfig(Mockito.any());
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
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").isNotEmpty())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }
    }

}
