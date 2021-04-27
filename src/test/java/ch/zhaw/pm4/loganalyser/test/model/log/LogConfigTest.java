package ch.zhaw.pm4.loganalyser.test.model.log;

import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        int i = 0;
        Map<Integer, ColumnComponent> componentMap = new TreeMap<>();
        componentMap.put(++i, new ColumnComponent(1L, "Host", ColumnType.IP, "(\\d{1,3}\\.){3}\\d{1,3}"));
        componentMap.put(++i, new ColumnComponent(9L, "Custom Seperator", ColumnType.TEXT, "-"));
        componentMap.put(++i, new ColumnComponent(2L, "User", ColumnType.TEXT, "-|[a-zA-Z]+"));
        componentMap.put(++i, new ColumnComponent(3L, "Timestamp", ColumnType.DATE, "\\[.+\\]"));
        componentMap.put(++i, new ColumnComponent(4L, "Request", ColumnType.TEXT, "\\\".+\\\""));
        componentMap.put(++i, new ColumnComponent(5L, "Response Code", ColumnType.INTEGER, "\\d{1,3}"));
        componentMap.put(++i, new ColumnComponent(6L, "Byte Size", ColumnType.INTEGER, "\\d+"));
        componentMap.put(++i, new ColumnComponent(7L, "Something", ColumnType.TEXT, "\\\".+\\\""));
        componentMap.put(++i, new ColumnComponent(8L, "Request Client", ColumnType.TEXT, "\\\".+\\\""));

        //register config
        LogConfig config = new LogConfig();
        config.setName("Nginx");
        config.setSeparator(" ");
        config.setHeaderLength(0);
        config.setColumnComponents(componentMap);

        Pattern p = Pattern.compile(concatenateRegex(config));
        for(String line: lines) {
            Matcher m = p.matcher(line);
            String[] values = new String[componentMap.values().size()];
            if (m.find()) {
                int j = 0;
                for(ColumnComponent c: componentMap.values()) {
                    String name = convertNameForCapturingGroup(c);
                    values[j++] = m.group(name);
                    assertNotNull(m.group(name));
                }
                System.out.println(Arrays.toString(values));
            }
        }
    }

    private String concatenateRegex(LogConfig config) {
        String lineRegex = "";
        Map<Integer, ColumnComponent> reversedOrder = new TreeMap<>(Collections.reverseOrder());
        reversedOrder.putAll(config.getColumnComponents());
        for(ColumnComponent c : reversedOrder.values()) {
            lineRegex = String.format("(?<%s>%s)%s%s", convertNameForCapturingGroup(c), c.getFormat(),
                                      config.getSeparator(), lineRegex);
        }
        return lineRegex.trim();
    }

    private String convertNameForCapturingGroup(ColumnComponent columnComponent) {
        return columnComponent.getName().replace(" ", "");
    }


}



