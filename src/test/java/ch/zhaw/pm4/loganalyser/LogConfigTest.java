package ch.zhaw.pm4.loganalyser;

import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LogConfigTest {

    private static final String LFOLDER = "logs";

    List<String> readLogLines(String filename) {
        try (Scanner sc = new Scanner(Objects.requireNonNull(LogConfigTest
                .class
                .getClassLoader()
                .getResourceAsStream(Path.of(LFOLDER, filename).toString())))) {

            List<String> lines = new ArrayList<>();
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
            return lines;
        }
    }

    @Test
    void testMatchColumn() {
        String filename = "nginx-access.log";
        List<String> lines = readLogLines(filename);
        // register columns
        List<ColumnComponent> components = new ArrayList<>();
        components.add(new ColumnComponent(1L, "Host", ColumnType.CUSTOM, "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"));
        components.add(new ColumnComponent(2L, "User", ColumnType.CUSTOM, "- -|[a-zA-Z]+"));
        components.add(new ColumnComponent(3L, "Timestamp", ColumnType.DATE, "\\[.+\\]"));
        components.add(new ColumnComponent(4L, "Request", ColumnType.MESSAGE, "\\\".+\\\""));
        components.add(new ColumnComponent(5L, "Response Code", ColumnType.CUSTOM, "\\d{1,3}"));
        components.add(new ColumnComponent(6L, "Byte Size", ColumnType.CUSTOM, "\\d+"));
        components.add(new ColumnComponent(7L, "Something", ColumnType.CUSTOM, "\\\".+\\\""));
        components.add(new ColumnComponent(8L, "Request Client", ColumnType.CUSTOM, "\\\".+\\\""));

        //register config
        LogConfig config = new LogConfig();
        config.setName("Nginx");
        config.setHeaderLength(0);
        config.setColumnComponents(components);

        Pattern p = Pattern.compile(concatinateRegex(config));
        for(String line: lines) {
            Matcher m = p.matcher(line);
            assertTrue(m.find());
        }
    }

    private String concatinateRegex(LogConfig config) {
        String lineregex = "";
        Collections.reverse(config.getColumnComponents());
        for(ColumnComponent c : config.getColumnComponents()) {
            lineregex = String.format("(%s)%s%s", c.getFormat(), " ", lineregex);
        }
        return lineregex.trim();
    }

}



