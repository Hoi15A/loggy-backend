package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnDTO;
import ch.zhaw.pm4.loganalyser.model.dto.HeaderDTO;
import ch.zhaw.pm4.loganalyser.model.dto.TableDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.parser.LogParser;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryService {

    @Setter
    @NonNull
    private LogServiceRepository logServiceRepository;

    private final Logger logger = Logger.getLogger(QueryService.class.getName());
    @Setter
    private LogParser logParser = new LogParser();

    public TableDTO getSampleLogsByQuery() {

        List<ColumnDTO> tableData = new ArrayList<>();
        List<HeaderDTO> headers = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            tableData.add(new ColumnDTO(sdf.parse("01.03.2021"), "info", "Service started"));
            tableData.add(new ColumnDTO(sdf.parse("02.03.2021"), "warning", "Could not interpret xyz"));
            tableData.add(new ColumnDTO(sdf.parse("03.03.2021"), "error", "FileNotFound test.txt"));
        }
        catch (ParseException e) {
            logger.log(Level.WARNING, "Unable to parse the given date", e);
        }

        headers.add(new HeaderDTO("Date", "date"));
        headers.add(new HeaderDTO("Type", "type"));
        headers.add(new HeaderDTO("Message", "message"));

        return new TableDTO(headers, tableData);

    }

    /**
     * Runs a query on a service and returns all matched lines
     * @param serviceId log service id
     * @param query TODO
     * @return rows
     */
    public List<String[]> runQueryForService(long serviceId, String query) {
        Optional<LogService> logService = logServiceRepository.findById(serviceId);
        if (logService.isEmpty()) throw new RecordNotFoundException(String.valueOf(serviceId));
        try {
            LogService service = logService.get();
            List<String[]> logEntries = logParser.read(null, service);
            Map<Integer, ColumnComponent> sortedComponents = sortComponents(service.getLogConfig().getColumnComponents());
            String[] header = new String[sortedComponents.size()];
            sortedComponents.forEach((key, value) -> header[key] = String.valueOf(sortedComponents.values().toArray(ColumnComponent[]::new)[key]));
            logEntries.add(0, header);
            return logEntries;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new FileNotFoundException("Logservice file not found");
        }
    }

    private Map<Integer, ColumnComponent> sortComponents(Map<Integer, ColumnComponent> sortable) {
        return sortable.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey , Map.Entry::getValue, (a,b) -> a, TreeMap::new));
    }
}
