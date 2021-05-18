package ch.zhaw.pm4.loganalyser.query.parser;

import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * This class collects all log files for a given {@link LogService}.
 * It filters and parses the necessary files / file contents and provides them for further processing.
 */
public class LogParser {

    private Map<Integer, ColumnComponent> sortedColumns;

    /**
     * Reads log files from {@link LogService}, then filters and parses them.
     * @param service to be analyzed
     * @return the parsed content of the log files.
     * @throws IOException when either a file is not found or there were complications while reading the file.
     */
    public List<String[]> read(LogService service) throws IOException {
        var logDir = Path.of(service.getLogDirectory());

        List<String[]> rows = new ArrayList<>();
        for (File logfile : Objects.requireNonNull(logDir.toFile().listFiles())) {
            if (logfile.isFile()) {
                rows.addAll(parse(logfile, service.getLogConfig()));
            }
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
     * @throws IOException If file could not be found or there were complications while reading the file.
     */
    private List<String[]> parse(File logfile, LogConfig config) throws IOException {
        List<String[]> rows = new ArrayList<>();
        var p = Pattern.compile(concatenateRegex(config));
        try (var br = new BufferedReader(new FileReader(logfile))) {
            String line;
            while ((line = br.readLine()) != null) {
                var m = p.matcher(line);
                List<String> values = new ArrayList<>();
                if (m.find()) {
                    for(ColumnComponent c: sortedColumns.values()) {
                        values.add(m.group(convertNameForCapturingGroup(c)));
                    }
                    Collections.reverse(values);
                    rows.add(values.toArray(String[]::new));
                }
            }
        }
        return rows;
    }

    private String concatenateRegex(LogConfig config) {
        var lineRegex = "";
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
