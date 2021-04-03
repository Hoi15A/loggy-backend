package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.parser.LogParser;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class QueryServiceTest {

    LogServiceRepository logServiceRepositoryMock = mock(LogServiceRepository.class);
    @Autowired
    QueryService queryService;

    @Test
    void runQueryForServiceInvalidServiceId() {
        queryService.setLogServiceRepository(logServiceRepositoryMock);
        when(logServiceRepositoryMock.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(RecordNotFoundException.class, () -> queryService.runQueryForService(-12L, null));
    }

    @Test
    void runQueryForServiceLogParserFileNotFound() throws IOException {
        LogService logServiceMock = mock(LogService.class);
        LogParser logparserMock = mock(LogParser.class);
        Optional<LogService> optionalMock = Optional.of(logServiceMock);

        queryService.setLogServiceRepository(logServiceRepositoryMock);
        queryService.setLogParser(logparserMock);

        when(logparserMock.read(any(), any())).thenThrow(new IOException());
        when(logServiceRepositoryMock.findById(any())).thenReturn(optionalMock);


        Assertions.assertThrows(FileNotFoundException.class, () -> queryService.runQueryForService(1, null));
    }
}
