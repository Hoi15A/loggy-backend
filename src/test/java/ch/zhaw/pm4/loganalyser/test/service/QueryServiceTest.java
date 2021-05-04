package ch.zhaw.pm4.loganalyser.test.service;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.FileReadException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.QueryComponentDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.query.parser.LogParser;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import ch.zhaw.pm4.loganalyser.service.QueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class QueryServiceTest {

    final LogServiceRepository logServiceRepositoryMock = mock(LogServiceRepository.class);
    final LogParser logParserMock = mock(LogParser.class);

    @Autowired
    QueryService queryService;

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRunQueryForService() {
        ColumnComponent compare = ColumnComponent.builder()
                .id(1)
                .name("Host")
                .format("ff")
                .columnType(ColumnType.DATE)
                .build();

        ColumnComponent compare2 = ColumnComponent.builder()
                .id(1)
                .name("User")
                .format("ff")
                .columnType(ColumnType.DATE)
                .build();

        Map<Integer, ColumnComponent> components = new TreeMap<>();
        components.put(2, compare);
        components.put(1, compare2);

        Map<Integer, ColumnComponent> sortedComponents = sortComponents(components);

        LogConfig logConfig = new LogConfig();
        logConfig.setColumnComponents(sortedComponents);

        LogService logService = new LogService();
        logService.setId(1);
        logService.setLogConfig(logConfig);

        mapTest(components, sortedComponents);

        queryService.setLogServiceRepository(logServiceRepositoryMock);
        queryService.setLogParser(logParserMock);

        when(logServiceRepositoryMock.findById(anyLong())).thenReturn(Optional.of(logService));

        // todo: execute runQueryForService and split this test into multiple test cases

        assertEquals((Optional.of(logService)).get().getLogConfig().getColumnComponents(), sortedComponents);
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRunQueryForService_RecordNotFound() {
        queryService.setLogServiceRepository(logServiceRepositoryMock);
        when(logServiceRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        List<QueryComponentDTO> queries = new ArrayList<>();
        assertThrows(RecordNotFoundException.class, () -> queryService.runQueryForService(-12L, queries));
    }

    @Test
    void testRunQueryForService_FileNotFound() throws IOException {
        LogService logServiceMock = mock(LogService.class);

        Optional<LogService> optionalMock = Optional.of(logServiceMock);

        queryService.setLogServiceRepository(logServiceRepositoryMock);
        queryService.setLogParser(logParserMock);

        when(logParserMock.read(any())).thenThrow(new java.io.FileNotFoundException());
        when(logServiceRepositoryMock.findById(anyLong())).thenReturn(optionalMock);

        List<QueryComponentDTO> queries = new ArrayList<>();
        assertThrows(FileNotFoundException.class, () -> queryService.runQueryForService(1, queries));
    }

    @Test
    void testRunQueryForService_FileReadException() throws IOException {
        LogService logServiceMock = mock(LogService.class);

        Optional<LogService> optionalMock = Optional.of(logServiceMock);

        queryService.setLogServiceRepository(logServiceRepositoryMock);
        queryService.setLogParser(logParserMock);

        when(logParserMock.read(any())).thenThrow(new IOException());
        when(logServiceRepositoryMock.findById(anyLong())).thenReturn(optionalMock);

        List<QueryComponentDTO> queries = new ArrayList<>();
        assertThrows(FileReadException.class, () -> queryService.runQueryForService(1, queries));
    }

    private void mapTest(Map<Integer, ColumnComponent> providedMap, Map<Integer, ColumnComponent> receivedMap) {
        assertEquals(providedMap.size(), receivedMap.size());
        assertNotNull(providedMap, "Provided Map is null;");
        assertNotNull(receivedMap, "Received Map is null;");
        assertEquals(providedMap.size(), receivedMap.size(), "Size mismatch for maps;");
        assertTrue(receivedMap.keySet().containsAll(providedMap.keySet()), "Missing keys in received map;");
    }

    private Map<Integer, ColumnComponent> sortComponents(Map<Integer, ColumnComponent> sortable) {
        return sortable.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey , Map.Entry::getValue, (a, b) -> a, TreeMap::new));
    }

}
