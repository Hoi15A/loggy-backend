package ch.zhaw.pm4.loganalyser.parser;

import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    private Map<Integer, ColumnComponent> sortedColumns;

    public List<String[]> read(LogService service) throws IOException {
        Path logDir = Path.of(service.getLogDirectory());

        List<String[]> rows = new ArrayList<>();
        for (File logfile : Objects.requireNonNull(logDir.toFile().listFiles())) {
            rows.addAll(parse(logfile, service.getLogConfig()));
        }

        return rows;
    }

    /**
     * 01.03.2021;info;Service started
     * 02.03.2021;warning;Could not interpret xyz
     * 03.03.2021;error;FileNotFound test.txt
     *
     * Assumptions:
     *      - Above file example log
     *      - Semicolon is delimiter (specified in config)
     *      - Ignoring header
     *      - Logfile is valid & sane
     *
     * @param logfile File that should be parsed
     * @param config Config to apply while parsing
     * @return Parsed logs
     * @throws FileNotFoundException If file could not be found.
     */
    private List<String[]> parse(File logfile, LogConfig config) throws IOException {
        List<String[]> rows = new ArrayList<>();
        Pattern p = Pattern.compile(concatenateRegex(config));
        try (BufferedReader br = new BufferedReader(new FileReader(logfile))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher m = p.matcher(line);
                List<String> values = new ArrayList<>();
                if (m.find()) {
                    for(ColumnComponent c: sortedColumns.values()) {
                        values.add(m.group(convertNameForCapturingGroup(c)));
                    }
                    Collections.reverse(values);
                    rows.add(values.stream().toArray(String[]::new));
                }
            }
        }
        return rows;
    }

    private String concatenateRegex(LogConfig config) {
        String lineRegex = "";
        sortedColumns = new TreeMap<>(Collections.reverseOrder());
        sortedColumns.putAll(config.getColumnComponents());

        for(ColumnComponent c : sortedColumns.values()) {
            lineRegex = String.format("(?<%s>%s)%s%s", convertNameForCapturingGroup(c), c.getFormat(),
                                      config.getSeparator(), lineRegex);
        }
        return lineRegex.trim();
    }

    private String convertNameForCapturingGroup(ColumnComponent columnComponent) {
        return columnComponent.getName().replace(" ", "");
    }

}
