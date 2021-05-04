package ch.zhaw.pm4.loganalyser.test.controller;

import ch.zhaw.pm4.loganalyser.exception.ApiExceptionHandler;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
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

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
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
class LogConfigControllerGetTest {

    @MockBean
    LogConfigService logConfigService;

    @Autowired
    MockMvc mockMvc;
    
    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetAllLogConfigs() {
        // prepare
        Map<Integer, ColumnComponentDTO> components1 = new HashMap<>();
        components1.put(1, null);

        Map<Integer, ColumnComponentDTO> components2 = new HashMap<>();
        components2.put(2, null);

        List<LogConfigDTO> dtos = new ArrayList<>();
        dtos.add(LogConfigDTO.builder().name("test1").columnCount(0).headerLength(0).separator(" ").columnComponents(components1).build());
        dtos.add(LogConfigDTO.builder().name("test2").columnCount(0).headerLength(0).separator(" ").columnComponents(components2).build());

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
        LogConfigDTO dto = LogConfigDTO.builder()
                .name("test1")
                .columnCount(1)
                .headerLength(2)
                .separator("|")
                .columnComponents(new HashMap<>())
                .build();

        when(logConfigService.getLogConfigById(dto.getName())).thenReturn(dto);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/config/" + dto.getName())
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
        verify(logConfigService, times(1)).getLogConfigById(dto.getName());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetNonExistingLogConfig() {
        // prepare
        String exceptionMessage = "ID was not found";

        doThrow(new RecordNotFoundException(exceptionMessage)).when(logConfigService).getLogConfigById(anyString());

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/config/nginx") // non existing column id
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
        verify(logConfigService, times(1)).getLogConfigById(anyString());
    }

}
