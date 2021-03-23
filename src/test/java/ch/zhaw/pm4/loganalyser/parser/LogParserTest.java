package ch.zhaw.pm4.loganalyser.parser;

import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LogParserTest {

    private static final String LFOLDER = "logs";

    @Test
    void readTest() throws Exception {
        // prepare
        LogService serviceMock = mock(LogService.class);
        File logFolder = new File(LogParserTest.class.getClassLoader().getResource(LFOLDER).getPath());
        when(serviceMock.getLogDirectory()).thenReturn(logFolder.toString());
        when(serviceMock.getLogConfig()).thenReturn(getLogConfig());

        LogParser parser = new LogParser();

        // execute
        List<String[]> result = parser.read(new ArrayList<>(), serviceMock);

        // validate
        assertEquals("192.168.1.1", result.get(0)[0]);
        assertEquals(30, result.size());
        for (String[] line : result) {
            assertEquals(9, line.length);
        }
    }

    private static LogConfig getLogConfig() {
        // register columns
        List<ColumnComponent> components = new ArrayList<>();
        components.add(new ColumnComponent(1L, "Host", ColumnType.CUSTOM, "(\\d{1,3}\\.){3}\\d{1,3}"));
        components.add(new ColumnComponent(9L, "Custom Seperator", ColumnType.CUSTOM, "-"));
        components.add(new ColumnComponent(2L, "User", ColumnType.CUSTOM, "-|[a-zA-Z]+"));
        components.add(new ColumnComponent(3L, "Timestamp", ColumnType.DATE, "\\[.+\\]"));
        components.add(new ColumnComponent(4L, "Request", ColumnType.MESSAGE, "\\\".+\\\""));
        components.add(new ColumnComponent(5L, "Response Code", ColumnType.CUSTOM, "\\d{1,3}"));
        components.add(new ColumnComponent(6L, "Byte Size", ColumnType.CUSTOM, "\\d+"));
        components.add(new ColumnComponent(7L, "Something", ColumnType.CUSTOM, "\\\".+\\\""));
        components.add(new ColumnComponent(8L, "Request Client", ColumnType.CUSTOM, "\\\".+\\\""));

        //register config
        LogConfig config = new LogConfig();
        config.setName("Nginx");
        config.setSeparator(" ");
        config.setHeaderLength(0);
        config.setColumnComponents(components);

        return config;
    }
}
