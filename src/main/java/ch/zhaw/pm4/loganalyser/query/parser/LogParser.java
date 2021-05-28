package ch.zhaw.pm4.loganalyser.query.parser;

import ch.zhaw.pm4.loganalyser.exception.InvalidInputException;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import org.springframework.lang.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * This class collects all log files for a given {@link LogService}.
 * It filters and parses the necessary files / file contents and provides them for further processing.
 */
public class LogParser {

    private static final Logger LOGGER = Logger.getLogger(LogParser.class.getName());

    private Map<Integer, ColumnComponent> sortedColumns;

    /**
     * Reads log files from {@link LogService}, then filters and parses them.
     * @param service to be analyzed.
     * @return the parsed content of the log files.
     * @throws IOException when either a file is not found or there were complications while reading the file.
     */
    public List<String[]> read(LogService service) throws IOException {
        var logDirPath = Path.of(service.getLogDirectory());
        var logDir = logDirPath.toFile();

        if(!logDir.exists()) {
            throw new InvalidInputException("'" + service.getLogDirectory() + "' does not exist.");
        }

        var files = logDir.listFiles();
        if(files == null || files.length == 0) {
            throw new FileNotFoundException("'" + service.getLogDirectory() + "' is empty.");
        }

        LOGGER.info(() -> "Load logfiles in [" + service.getLogDirectory() + "]");
        List<String[]> rows = new ArrayList<>();

        for (File logfile : files) {
            if (logfile.isFile()) {
                LOGGER.info(() -> "Parse file [" + logfile.getName() + "]");
                rows.addAll(parse(logfile, service.getLogConfig()));
            }
        }

        LOGGER.info(() -> "Total parsed lines: " + rows.size());
        if (rows.size() == 0) {
            throw new InvalidInputException("No lines could be parsed. Please check the order of your column component and your patterns.");
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

        try (Stream<String> lines = Files.lines(logfile.toPath())) {
            lines.forEach(line -> {
                String[] parsed = parseLine(p, line);
                if (parsed != null) rows.add(parsed);
            });
            LOGGER.info(() -> "Parsed " + rows.size() + " lines");
        }

        return rows;
    }

    private @Nullable String[] parseLine(Pattern p, String line) {
        var m = p.matcher(line);
        List<String> values = new ArrayList<>();
        if (m.find()) {
            for(ColumnComponent c: sortedColumns.values()) {
                values.add(m.group(convertNameForCapturingGroup(c)));
            }
            Collections.reverse(values);
            return values.toArray(String[]::new);
        }
        return null;
    }


    private String concatenateRegex(LogConfig config) {
        var lineRegex = "";
        sortedColumns = new TreeMap<>(Collections.reverseOrder());
        sortedColumns.putAll(config.getColumnComponents());

        var lastComponentSeparator = true;
        for(ColumnComponent c : sortedColumns.values()) {
            lineRegex = String.format("(?<%s>%s)%s%s", convertNameForCapturingGroup(c), c.getFormat(),
                                      lastComponentSeparator ? "": config.getSeparator(), lineRegex);
            lastComponentSeparator = false;
        }

        String finalLineRegex = lineRegex;
        LOGGER.info(() -> "Built regex: " + finalLineRegex);
        return lineRegex;
    }

    private String convertNameForCapturingGroup(ColumnComponent columnComponent) {
        return columnComponent.getName().replace(" ", "");
    }

}
