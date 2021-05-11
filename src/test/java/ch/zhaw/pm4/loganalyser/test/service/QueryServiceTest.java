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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class QueryServiceTest {

    private static final String IP_REGEX = "(1?\\d{1,2}\\.){3}(1?\\d{1,2})"  // 0.0.0.0 - 199.199.199.199
            + "|(2[0-5]{2}\\.){3}(2[0-5]{2})"; // 200.200.200.200 - 255.255.255.255

    @Autowired QueryService queryService;

    @Mock LogServiceRepository logServiceRepositoryMock;
    @Mock LogParser logParserMock;
    @Mock LogService logServiceMock;
    @Mock LogConfig logConfigMock;

    Map<Integer, ColumnComponent> columnComponentMap = new HashMap<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        setUpColumnComponents();
        when(logServiceRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(logServiceMock));
        when(logServiceMock.getLogConfig()).thenReturn(logConfigMock);
        when(logConfigMock.getColumnComponents()).thenReturn(columnComponentMap);
    }

    void setUpColumnComponents() {
        ColumnComponent ipComp      = mock(ColumnComponent.class);
        ColumnComponent dateComp    = mock(ColumnComponent.class);
        ColumnComponent textComp    = mock(ColumnComponent.class);
        ColumnComponent doubleComp  = mock(ColumnComponent.class);
        ColumnComponent intComp     = mock(ColumnComponent.class);

        // id
        long l = 1;
        when(ipComp.getId()).thenReturn(++l);
        when(dateComp.getId()).thenReturn(++l);
        when(textComp.getId()).thenReturn(++l);
        when(doubleComp.getId()).thenReturn(++l);
        when(intComp.getId()).thenReturn(++l);

        // format
        when(ipComp.getFormat()).thenReturn(IP_REGEX);
        when(dateComp.getFormat()).thenReturn("(\\d{2}/){2}\\d{4}"); // dd/MM/yyyy
        when(textComp.getFormat()).thenReturn(".+");
        when(doubleComp.getFormat()).thenReturn("\\d+(\\.\\d+)?");
        when(intComp.getFormat()).thenReturn("\\d+");

        // date format
        when(dateComp.getDateFormat()).thenReturn("dd/MM/yyyy");

        // column type
        when(ipComp.getColumnType()).thenReturn(ColumnType.IP);
        when(dateComp.getColumnType()).thenReturn(ColumnType.DATE);
        when(textComp.getColumnType()).thenReturn(ColumnType.TEXT);
        when(doubleComp.getColumnType()).thenReturn(ColumnType.DOUBLE);
        when(intComp.getColumnType()).thenReturn(ColumnType.INTEGER);

        // collect
        int i = 0;
        columnComponentMap.put(++i, ipComp);
        columnComponentMap.put(++i, dateComp);
        columnComponentMap.put(++i, textComp);
        columnComponentMap.put(++i, doubleComp);
        columnComponentMap.put(++i, intComp);
    }

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRunQueryForService_exact() {
        // -- list of queries
        // -- exact
    }

    @Test
    void testRunQueryForService_contains() {
        // -- list of queries
        // -- contains
    }

    @Test
    void testRunQueryForService_regex() {
        // -- list of queries
        // -- regex
    }

    @Test
    void testRunQueryForService_range() {
        // -- list of queries
        // -- dateformat für daterange
        // -- ip range
        // -- number range (int and double)
    }

    @Test
    void testRunQueryForService_multiple() {
        // -- list of queries

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

    private Map<Integer, ColumnComponent> sortComponents(Map<Integer, ColumnComponent> sortable) {
        return sortable.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey , Map.Entry::getValue, (a, b) -> a, TreeMap::new));
    }

}
