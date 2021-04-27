package ch.zhaw.pm4.loganalyser.test.controller;

import ch.zhaw.pm4.loganalyser.exception.ApiExceptionHandler;
import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.FileReadException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
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

    public static final String GET_REQUEST_ID_QUERY = "/query/%d/%s";
    public static final int VALID_SERVICE_ID = 1;
    public static final int INVALID_SERVICE_ID = -1;

    @MockBean
    QueryService queryService;

    @Autowired
    MockMvc mockMvc;

    /* ****************************************************************************************************************
     * POSITIV TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetQueryForLogService() {
        // prepare
        List<String[]> mockData = new ArrayList<>();
        String[] mockRow1 = {"30.03.2021","INFO","Loggy started"};
        String[] mockRow2 = {"31.03.2021","ERROR","Server is on fire"};
        mockData.add(mockRow1);
        mockData.add(mockRow2);

        String queryString = "null";

        when(queryService.runQueryForService(VALID_SERVICE_ID, queryString)).thenReturn(mockData);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get(String.format(GET_REQUEST_ID_QUERY, VALID_SERVICE_ID, queryString))
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
        verify(queryService, times(1)).runQueryForService(VALID_SERVICE_ID, queryString);
    }

    /* ****************************************************************************************************************
     * NEGATIV TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetQueryForLogServiceServiceNotFound() {
        String queryString = "null";
        String exceptionMessage = String.format("The service with id %d does not exist", INVALID_SERVICE_ID);

        doThrow(new RecordNotFoundException(exceptionMessage)).when(queryService).runQueryForService(INVALID_SERVICE_ID, queryString);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get(String.format(GET_REQUEST_ID_QUERY, INVALID_SERVICE_ID, queryString))
                    .accept(MediaType.APPLICATION_JSON))
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
        verify(queryService, times(1)).runQueryForService(INVALID_SERVICE_ID, queryString);
    }

    @Test
    void testGetQueryForLogServiceFileNotFound() {
        long serviceId = VALID_SERVICE_ID;
        String queryString = "null";
        String exceptionMessage = "The file wanted to process has not been found";

        doThrow(new FileNotFoundException(exceptionMessage)).when(queryService).runQueryForService(serviceId, queryString);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                                    .get(String.format(GET_REQUEST_ID_QUERY, serviceId, queryString))
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.INTERNAL_SERVER_ERROR_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[0]", is(exceptionMessage)))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(queryService, times(1)).runQueryForService(serviceId, queryString);
    }

    @Test
    void testGetQueryForLogServiceReadError() {
        long serviceId = VALID_SERVICE_ID;
        String queryString = "null";
        String exceptionMessage = "The file wanted to process had an error while reading";

        doThrow(new FileReadException(exceptionMessage)).when(queryService).runQueryForService(serviceId, queryString);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders
                                    .get(String.format(GET_REQUEST_ID_QUERY, serviceId, queryString))
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.INTERNAL_SERVER_ERROR_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[0]", is(exceptionMessage)))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(queryService, times(1)).runQueryForService(serviceId, queryString);
    }

    // todo: query invalid

}
