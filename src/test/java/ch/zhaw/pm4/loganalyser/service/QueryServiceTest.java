package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.parser.LogParser;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class QueryServiceTest {

    LogServiceRepository logServiceRepositoryMock = mock(LogServiceRepository.class);
    LogParser logParserMock = mock(LogParser.class);
    @Autowired
    QueryService queryService;

    @Test
    void runQueryForServiceValid() throws IOException {
        LogService logServiceMock = mock(LogService.class);

        long mockServiceId = 1;
        Optional<LogService> optionalMock = Optional.of(logServiceMock);
        List<String[]> mockData = new ArrayList<>();
        String[] mockRow = {"this is valid"};
        mockData.add(mockRow);

        queryService.setLogServiceRepository(logServiceRepositoryMock);
        queryService.setLogParser(logParserMock);

        when(logParserMock.read(any(), any())).thenReturn(mockData);
        when(logServiceRepositoryMock.findById(any())).thenReturn(optionalMock);

        List<String[]> data = queryService.runQueryForService(mockServiceId, null);

        assertEquals(mockData, data);
        Mockito.verify(logServiceRepositoryMock, Mockito.times(1)).findById(mockServiceId);
        Mockito.verify(logParserMock, Mockito.times(1)).read(any(), any());
    }

    @Test
    void runQueryForServiceInvalidServiceId() {
        queryService.setLogServiceRepository(logServiceRepositoryMock);
        when(logServiceRepositoryMock.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(RecordNotFoundException.class, () -> queryService.runQueryForService(-12L, null));
    }

    @Test
    void runQueryForServiceLogParserFileNotFound() throws IOException {
        LogService logServiceMock = mock(LogService.class);

        Optional<LogService> optionalMock = Optional.of(logServiceMock);

        queryService.setLogServiceRepository(logServiceRepositoryMock);
        queryService.setLogParser(logParserMock);

        when(logParserMock.read(any(), any())).thenThrow(new IOException());
        when(logServiceRepositoryMock.findById(any())).thenReturn(optionalMock);


        Assertions.assertThrows(FileNotFoundException.class, () -> queryService.runQueryForService(1, null));
    }
}
