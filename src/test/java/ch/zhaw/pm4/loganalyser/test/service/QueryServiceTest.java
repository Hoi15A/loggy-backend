package ch.zhaw.pm4.loganalyser.test.service;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.FileReadException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.dto.QueryComponentDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.model.log.column.FilterType;
import ch.zhaw.pm4.loganalyser.query.parser.LogParser;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import ch.zhaw.pm4.loganalyser.service.ColumnComponentService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class QueryServiceTest {

    private static final String IP_REGEX = "(1?\\d{1,2}\\.){3}(1?\\d{1,2})" // 0.0.0.0 - 199.199.199.199
                                         + "|(2[0-5]{2}\\.){3}(2[0-5]{2})"; // 200.200.200.200 - 255.255.255.255

    private static final String LOGS_TEST_PAGING = "logs/testPaging";

    private static final long SERVICE_ID = 99L;

    //                                                              ip,                date,          text,                           double,   int
    private static final String[] entryLocalhost   = new String[] { "127.0.0.1"      , "11/05/2021 - 23:59:59", "ERROR: Everything is on fire", "12.0"  , "8"       };
    private static final String[] entryHomeNetwork = new String[] { "192.168.1.2"    , "12/05/2021 - 12:59:59", "WARN: Coffemachine broke"    , "6.9"   , "42"      };
    private static final String[] entryMinIp       = new String[] { "0.0.0.0"        , "13/05/2021 - 11:59:59", "INFO: Nginx restarted"       , "123.7" , "112"     };
    private static final String[] entryMaxIp       = new String[] { "255.255.255.255", "24/12/2121 - 00:00:00", "CRITICAL: Server died"       , "5555.5", "5555555" };

    @Autowired QueryService queryService;

    @Mock ColumnComponentService columnComponentServiceMock;
    @Mock LogServiceRepository logServiceRepositoryMock;
    @Mock LogParser logParserMock;
    @Mock LogService logServiceMock;
    @Mock LogConfig logConfigMock;

    final Map<Integer, ColumnComponent> columnComponentMap = new HashMap<>();

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        queryService.setColumnComponentService(columnComponentServiceMock);
        queryService.setLogServiceRepository(logServiceRepositoryMock);
        queryService.setLogParser(logParserMock);

        setUpColumnComponents();
        when(logServiceRepositoryMock.findById(SERVICE_ID)).thenReturn(Optional.of(logServiceMock));
        when(logServiceMock.getLogConfig()).thenReturn(logConfigMock);
        when(logConfigMock.getColumnComponents()).thenReturn(columnComponentMap);
        when(logParserMock.read(logServiceMock)).thenReturn(getParsedEntries());
    }

    List<String[]> getParsedEntries() {
        List<String[]> list = new ArrayList<>();
        list.add(entryLocalhost);
        list.add(entryHomeNetwork);
        list.add(entryMinIp);
        list.add(entryMaxIp);
        return list;
    }

    void setUpColumnComponents() {
        ColumnComponent ipComp      = mock(ColumnComponent.class);
        ColumnComponent dateComp    = mock(ColumnComponent.class);
        ColumnComponent textComp    = mock(ColumnComponent.class);
        ColumnComponent doubleComp  = mock(ColumnComponent.class);
        ColumnComponent intComp     = mock(ColumnComponent.class);

        ColumnComponentDTO ipCompDTO      = mock(ColumnComponentDTO.class);
        ColumnComponentDTO dateCompDTO    = mock(ColumnComponentDTO.class);
        ColumnComponentDTO textCompDTO    = mock(ColumnComponentDTO.class);
        ColumnComponentDTO doubleCompDTO  = mock(ColumnComponentDTO.class);
        ColumnComponentDTO intCompDTO     = mock(ColumnComponentDTO.class);

        // id
        long l = 0;
        when(ipComp.getId()).thenReturn(++l);
        when(dateComp.getId()).thenReturn(++l);
        when(textComp.getId()).thenReturn(++l);
        when(doubleComp.getId()).thenReturn(++l);
        when(intComp.getId()).thenReturn(++l);

        l = 0;
        when(ipCompDTO.getId()).thenReturn(++l);
        when(dateCompDTO.getId()).thenReturn(++l);
        when(textCompDTO.getId()).thenReturn(++l);
        when(doubleCompDTO.getId()).thenReturn(++l);
        when(intCompDTO.getId()).thenReturn(++l);

        // format
        when(ipComp.getFormat()).thenReturn(IP_REGEX);
        when(dateComp.getFormat()).thenReturn("(\\d{2}/){2}\\d{4}"); // dd/MM/yyyy
        when(textComp.getFormat()).thenReturn(".+");
        when(doubleComp.getFormat()).thenReturn("\\d+(\\.\\d+)?");
        when(intComp.getFormat()).thenReturn("\\d+");

        when(ipCompDTO.getFormat()).thenReturn(IP_REGEX);
        when(dateCompDTO.getFormat()).thenReturn("(\\d{2}/){2}\\d{4}"); // dd/MM/yyyy
        when(textCompDTO.getFormat()).thenReturn(".+");
        when(doubleCompDTO.getFormat()).thenReturn("\\d+(\\.\\d+)?");
        when(intCompDTO.getFormat()).thenReturn("\\d+");

        // date format
        when(dateComp.getDateFormat()).thenReturn("dd/MM/yyyy - HH:mm:ss");

        when(dateCompDTO.getDateFormat()).thenReturn("dd/MM/yyyy - HH:mm:ss");

        // column type
        when(ipComp.getColumnType()).thenReturn(ColumnType.IP);
        when(dateComp.getColumnType()).thenReturn(ColumnType.DATE);
        when(textComp.getColumnType()).thenReturn(ColumnType.TEXT);
        when(doubleComp.getColumnType()).thenReturn(ColumnType.DOUBLE);
        when(intComp.getColumnType()).thenReturn(ColumnType.INTEGER);

        when(ipCompDTO.getColumnType()).thenReturn(ColumnType.IP);
        when(dateCompDTO.getColumnType()).thenReturn(ColumnType.DATE);
        when(textCompDTO.getColumnType()).thenReturn(ColumnType.TEXT);
        when(doubleCompDTO.getColumnType()).thenReturn(ColumnType.DOUBLE);
        when(intCompDTO.getColumnType()).thenReturn(ColumnType.INTEGER);

        // collect
        int i = -1;
        columnComponentMap.put(++i, ipComp);
        columnComponentMap.put(++i, dateComp);
        columnComponentMap.put(++i, textComp);
        columnComponentMap.put(++i, doubleComp);
        columnComponentMap.put(++i, intComp);

        i = -1;
        when(columnComponentServiceMock.getColumnComponentById(++i)).thenReturn(ipCompDTO);
        when(columnComponentServiceMock.getColumnComponentById(++i)).thenReturn(dateCompDTO);
        when(columnComponentServiceMock.getColumnComponentById(++i)).thenReturn(textCompDTO);
        when(columnComponentServiceMock.getColumnComponentById(++i)).thenReturn(doubleCompDTO);
        when(columnComponentServiceMock.getColumnComponentById(++i)).thenReturn(intCompDTO);
    }

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRunQueryForService_exact() {
        // prepare
        QueryComponentDTO dto = QueryComponentDTO.builder()
                .columnComponentId(2L)
                .filterType(FilterType.EXACT)
                .exact("ERROR: Everything is on fire")
                .build();

        List<String[]> filtered = new ArrayList<>();
        filtered.add(entryLocalhost);

        // execute
        List<String[]> result = queryService.runQueryForService(SERVICE_ID, List.of(dto), 0);

        // verify
        assertNotNull(result);
        assertEquals(filtered.size(), result.size());
        assertIterableEquals(filtered, result);
    }

    @Test
    void testRunQueryForService_contains() {
        // prepare
        QueryComponentDTO dto = QueryComponentDTO.builder()
                .columnComponentId(3L)
                .filterType(FilterType.CONTAINS)
                .contains("12")
                .build();

        List<String[]> filtered = new ArrayList<>();
        filtered.add(entryLocalhost);
        filtered.add(entryMinIp);

        // execute
        List<String[]> result = queryService.runQueryForService(SERVICE_ID, List.of(dto), 0);

        // verify
        assertNotNull(result);
        assertEquals(filtered.size(), result.size());
        assertIterableEquals(filtered, result);
    }

    @Test
    void testRunQueryForService_regex() {
        // prepare
        QueryComponentDTO dto = QueryComponentDTO.builder()
                .columnComponentId(2L)
                .filterType(FilterType.REGEX)
                .regex("(ERROR|CRITICAL):.*")
                .build();

        List<String[]> filtered = new ArrayList<>();
        filtered.add(entryLocalhost);
        filtered.add(entryMaxIp);

        // execute
        List<String[]> result = queryService.runQueryForService(SERVICE_ID, List.of(dto), 0);

        // verify
        assertNotNull(result);
        assertEquals(filtered.size(), result.size());
        assertIterableEquals(filtered, result);
    }

    @Test
    void testRunQueryForService_range() {
        // prepare
        QueryComponentDTO ipRange = QueryComponentDTO.builder()
                .columnComponentId(0L)
                .filterType(FilterType.RANGE)
                .from("192.168.1.0")
                .to("192.168.3.0")
                .build();

        QueryComponentDTO dateRange = QueryComponentDTO.builder()
                .columnComponentId(1L)
                .filterType(FilterType.RANGE)
                .dateFormat("dd/MM/yyyy")
                .from("11/05/2021")
                .to("13/05/2021")
                .build();

        QueryComponentDTO doubleRange = QueryComponentDTO.builder()
                .columnComponentId(3L)
                .filterType(FilterType.RANGE)
                .from("10")
                .to("200")
                .build();


        QueryComponentDTO integerRange = QueryComponentDTO.builder()
                .columnComponentId(4L)
                .filterType(FilterType.RANGE)
                .from("-7")
                .to("7")
                .build();

        List<String[]> ipFiltered = new ArrayList<>();
        ipFiltered.add(entryHomeNetwork);

        List<String[]> dateFiltered = new ArrayList<>();
        dateFiltered.add(entryLocalhost);
        dateFiltered.add(entryHomeNetwork);
        dateFiltered.add(entryMinIp);

        List<String[]> doubleFiltered = new ArrayList<>();
        doubleFiltered.add(entryLocalhost);
        doubleFiltered.add(entryMinIp);

        List<String[]> integerFiltered = new ArrayList<>();

        // execute
        List<String[]> ipResult = queryService.runQueryForService(SERVICE_ID, List.of(ipRange), 0);
        List<String[]> dateResult = queryService.runQueryForService(SERVICE_ID, List.of(dateRange), 0);
        List<String[]> doubleResult = queryService.runQueryForService(SERVICE_ID, List.of(doubleRange), 0);
        List<String[]> integerResult = queryService.runQueryForService(SERVICE_ID, List.of(integerRange), 0);

        // verify
        verifyRange(ipResult, ipFiltered);
        verifyRange(dateResult, dateFiltered);
        verifyRange(doubleResult, doubleFiltered);
        verifyRange(integerResult, integerFiltered);
    }

    void verifyRange(List<String[]> result, List<String[]> filtered) {
        assertNotNull(result);
        assertEquals(filtered.size(), result.size());
        assertIterableEquals(filtered, result);
    }

    @Test
    void testRunQueryForService_multiple() {
        // -- list of queries
        // TODO: implement
    }

    @Test
    void testReadWithPaging() throws Exception {
        /*
        // prepare
        LogService serviceMock = mock(LogService.class);
        File logFolder = new File(getClass().getClassLoader().getResource(LOGS_TEST_PAGING).toURI().getPath());
        when(serviceMock.getLogDirectory()).thenReturn(logFolder.toString());
        when(serviceMock.getLogConfig()).thenReturn(getLogConfig());

        LogParser parser = new LogParser();

        // execute
        List<String[]> resultPageZero = parser.read(serviceMock, 0);
        List<String[]> resultPageOne = parser.read(serviceMock, 1);

        // validate
        assertEquals("192.168.1.1", resultPageZero.get(0)[0]);
        assertEquals(500, resultPageZero.size());
        for (String[] line : resultPageZero) {
            assertEquals(9, line.length);
        }

        assertEquals("63.143.42.248", resultPageOne.get(0)[0]);
        assertEquals(40, resultPageOne.size());
        for (String[] line : resultPageOne) {
            assertEquals(9, line.length);
        }
         */
        // todo: anpassen von parser zu queryservice
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRunQueryForService_RecordNotFound() {
        when(logServiceRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        List<QueryComponentDTO> queries = new ArrayList<>();
        assertThrows(RecordNotFoundException.class, () -> queryService.runQueryForService(-12L, queries, 0));
    }

    @Test
    void testRunQueryForService_FileNotFound() throws IOException {
        LogService logServiceMock = mock(LogService.class);

        when(logParserMock.read(any())).thenThrow(new java.io.FileNotFoundException());
        when(logServiceRepositoryMock.findById(anyLong())).thenReturn(Optional.of(logServiceMock));

        List<QueryComponentDTO> queries = new ArrayList<>();
        assertThrows(FileNotFoundException.class, () -> queryService.runQueryForService(1, queries, 0));
    }

    @Test
    void testRunQueryForService_FileReadException() throws IOException {
        LogService logServiceMock = mock(LogService.class);

        when(logParserMock.read(any())).thenThrow(new IOException());
        when(logServiceRepositoryMock.findById(anyLong())).thenReturn(Optional.of(logServiceMock));

        List<QueryComponentDTO> queries = new ArrayList<>();
        assertThrows(FileReadException.class, () -> queryService.runQueryForService(1, queries, 0));
    }

}
