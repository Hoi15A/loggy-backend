package ch.zhaw.pm4.loganalyser.parser;

import ch.zhaw.pm4.loganalyser.model.filter.Filter;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LogParser {

    public List<String[]> read(List<Filter> filters, LogService service) throws FileNotFoundException {
        Path logDir = service.getLogDirectory();

        List<String[]> rows = new ArrayList<>();

        for (File logfile : logDir.toFile().listFiles()) {
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
     * @throws FileNotFoundException
     */
    private List<String[]> parse(File logfile, LogConfig config) throws FileNotFoundException {
        List<String[]> rows;

        try (Scanner sc = new Scanner(logfile)) {
            sc.useDelimiter(config.getSeparator());
            int colCount = config.getColumnCount();

            rows = new ArrayList<>();

            while (sc.hasNext()) {

                String[] cols = new String[colCount];
                for (int i = 0; i < colCount; i++) {
                    cols[i] = sc.next();
                }
                rows.add(cols);
            }
        }

        return rows;
    }
}
