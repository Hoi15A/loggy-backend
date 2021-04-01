package ch.zhaw.pm4.loganalyser.log;

import ch.zhaw.pm4.loganalyser.controller.QueryController;
import ch.zhaw.pm4.loganalyser.service.QueryService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class QueryControllerTest {

    @MockBean
    private QueryService queryService;
    QueryController queryController;
    MockMvc mockMvc;

    @Autowired
    public QueryControllerTest(QueryController queryController, MockMvc mockMvc) {
        this.queryController = queryController;
        this.mockMvc = mockMvc;
    }

    @Test
    void getQueryForLogfileTest() {

        List<String[]> mockData = new ArrayList<>();
        String[] mockRow1 = {"30.03.2021","INFO","Loggy started"};
        String[] mockRow2 = {"31.03.2021","ERROR","Server is on fire"};
        mockData.add(mockRow1);
        mockData.add(mockRow2);

        Mockito.when(queryService.runQueryForService(1, null)).thenReturn(mockData);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/query/1/null")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[*]").isNotEmpty())
                    // TODO: actually validate contents
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }

        Mockito.verify(queryService, Mockito.times(1)).runQueryForService(1, null);
    }
}
