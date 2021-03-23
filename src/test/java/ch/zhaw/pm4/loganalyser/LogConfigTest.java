package ch.zhaw.pm4.loganalyser;

import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

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

        Pattern p = Pattern.compile(concatenateRegex(config));
        for(String line: lines) {
            Matcher m = p.matcher(line);
            String[] values = new String[components.size()];
            if (m.find()) {
                int i = 0;
                for(ColumnComponent c: components) {
                    String name = convertNameForCapturingGroup(c);
                    values[i++] = m.group(name);
                    assertNotNull(m.group(name));
                }
                System.out.println(Arrays.toString(values));
            }
        }
    }

    private String concatenateRegex(LogConfig config) {
        String lineRegex = "";
        Collections.reverse(config.getColumnComponents());
        for(ColumnComponent c : config.getColumnComponents()) {
            lineRegex = String.format("(?<%s>%s)%s%s", convertNameForCapturingGroup(c), c.getFormat(),
                                      config.getSeparator(), lineRegex);
        }
        return lineRegex.trim();
    }

    private String convertNameForCapturingGroup(ColumnComponent columnComponent) {
        return columnComponent.getName().replace(" ", "");
    }


}



