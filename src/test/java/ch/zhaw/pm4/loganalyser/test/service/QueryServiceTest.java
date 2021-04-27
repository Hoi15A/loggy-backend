package ch.zhaw.pm4.loganalyser.test.service;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.FileReadException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.parser.LogParser;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import ch.zhaw.pm4.loganalyser.service.QueryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class QueryServiceTest {

    LogServiceRepository logServiceRepositoryMock = mock(LogServiceRepository.class);
    LogParser logParserMock = mock(LogParser.class);

    @Autowired
    QueryService queryService;

    @Test
    void runQueryForServiceValid() {
        LogService logService = new LogService();
        LogConfig logConfig = new LogConfig();

        ColumnComponent compare = new ColumnComponent();
        compare.setName("Host");
        compare.setId(1);
        compare.setFormat("ff");
        compare.setColumnType(ColumnType.DATE);
        ColumnComponent compare2 = new ColumnComponent();
        compare2.setName("User");
        compare2.setId(2);
        compare2.setFormat("ff");
        compare2.setColumnType(ColumnType.DATE);
        Map<Integer, ColumnComponent> components = new TreeMap<>();
        components.put(2 ,compare);
        components.put(1 ,compare2);
        Map<Integer, ColumnComponent> sortedComponents = sortComponents(components);
        logConfig.setColumnComponents(sortedComponents);
        logService.setId(1);
        logService.setLogConfig(logConfig);
        mapTest(components, sortedComponents);
        queryService.setLogServiceRepository(logServiceRepositoryMock);
        queryService.setLogParser(logParserMock);

        when(logServiceRepositoryMock.findById(anyLong())).thenReturn(Optional.of(logService));
        
        assertEquals((Optional.of(logService)).get().getLogConfig().getColumnComponents(), sortedComponents);
    }

    @Test
    void runQueryForServiceInvalidServiceId() {
        queryService.setLogServiceRepository(logServiceRepositoryMock);
        when(logServiceRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> queryService.runQueryForService(-12L, List.of()));
    }

    @Test
    void runQueryForServiceLogParserFileNotFound() throws IOException {
        LogService logServiceMock = mock(LogService.class);

        Optional<LogService> optionalMock = Optional.of(logServiceMock);

        queryService.setLogServiceRepository(logServiceRepositoryMock);
        queryService.setLogParser(logParserMock);

        when(logParserMock.read(any())).thenThrow(new java.io.FileNotFoundException());
        when(logServiceRepositoryMock.findById(anyLong())).thenReturn(optionalMock);


        assertThrows(FileNotFoundException.class, () -> queryService.runQueryForService(1, List.of()));
    }

    @Test
    void runQueryForServiceLogParserReadError() throws IOException {
        LogService logServiceMock = mock(LogService.class);

        Optional<LogService> optionalMock = Optional.of(logServiceMock);

        queryService.setLogServiceRepository(logServiceRepositoryMock);
        queryService.setLogParser(logParserMock);

        when(logParserMock.read(any())).thenThrow(new IOException());
        when(logServiceRepositoryMock.findById(anyLong())).thenReturn(optionalMock);


        assertThrows(FileReadException.class, () -> queryService.runQueryForService(1, List.of()));
    }
    
    void mapTest(Map<Integer, ColumnComponent> providedMap, Map<Integer, ColumnComponent> receivedMap) {
        assertEquals(providedMap.size(), receivedMap.size());
        Assertions.assertNotNull(providedMap, "Provided Map is null;");
        Assertions.assertNotNull(receivedMap, "Received Map is null;");
        assertEquals(providedMap.size(), receivedMap.size(), "Size mismatch for maps;");
        Assertions.assertTrue(receivedMap.keySet().containsAll(providedMap.keySet()), "Missing keys in received map;");
    }

    public Map<Integer, ColumnComponent> sortComponents(Map<Integer, ColumnComponent> sortable) {
        return sortable.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey , Map.Entry::getValue, (a, b) -> a, TreeMap::new));
    }
}
