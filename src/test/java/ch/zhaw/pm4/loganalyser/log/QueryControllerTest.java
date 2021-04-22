package ch.zhaw.pm4.loganalyser.log;

import ch.zhaw.pm4.loganalyser.service.QueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QueryControllerTest {

    @MockBean
    QueryService queryService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getQueryForLogfileTest() {
        // prepare
        List<String[]> mockData = new ArrayList<>();
        String[] mockRow1 = {"30.03.2021","INFO","Loggy started"};
        String[] mockRow2 = {"31.03.2021","ERROR","Server is on fire"};
        mockData.add(mockRow1);
        mockData.add(mockRow2);

        when(queryService.runQueryForService(1, null)).thenReturn(mockData);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/query/1/null")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$[*]").isNotEmpty())
                    .andExpect(jsonPath("$[*].[*]").isNotEmpty())
                    .andExpect(jsonPath("$[0].[*]", allOf(
                            hasItem(mockRow1[0]),
                            hasItem(mockRow1[1]),
                            hasItem(mockRow1[2])
                    )))
                    .andExpect(jsonPath("$[1].[*]", allOf(
                            hasItem(mockRow2[0]),
                            hasItem(mockRow2[1]),
                            hasItem(mockRow2[2])
                    )))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(queryService, times(1)).runQueryForService(1, null);
    }
}
