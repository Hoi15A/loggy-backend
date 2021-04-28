package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.FileReadException;
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

/**
 * Performs a query operation for a specific service.
 */
@Setter
@Service
@RequiredArgsConstructor
public class QueryService {

    private final Logger logger = Logger.getLogger(QueryService.class.getName());

    @NonNull
    private LogServiceRepository logServiceRepository;

    private LogParser logParser = new LogParser();

    public TableDTO getSampleLogsByQuery() {
        // todo : to be removed
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
     * Runs a query on a service and returns all matched lines.
     * @param serviceId to be queried on.
     * @param query to be applied on the log files.
     * @return List<String[]>
     * @throws RecordNotFoundException when the service does not exist.
     * @throws FileNotFoundException when the log file does not exist.
     * @throws FileReadException when there were complication while reading the log file.
     */
    public List<String[]> runQueryForService(long serviceId, String query) {
        Optional<LogService> logService = logServiceRepository.findById(serviceId);
        if (logService.isEmpty()) throw new RecordNotFoundException(String.format("The service with id %d does not exist", serviceId));
        try {
            LogService service = logService.get();
            List<String[]> logEntries = logParser.read(null, service);
            Map<Integer, ColumnComponent> sortedComponents = sortComponents(service.getLogConfig().getColumnComponents());
            String[] header = new String[sortedComponents.size()];
            sortedComponents.forEach((key, value) -> header[key] = String.valueOf(sortedComponents.values().toArray(ColumnComponent[]::new)[key]));
            logEntries.add(0, header);
            return logEntries;
        } catch (java.io.FileNotFoundException ex1) {
            throw new FileNotFoundException("Log service file not found");
        } catch (IOException ex2) {
            throw new FileReadException("Something went wrong while reading the file");
        }
    }

    private Map<Integer, ColumnComponent> sortComponents(Map<Integer, ColumnComponent> sortable) {
        return sortable.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey , Map.Entry::getValue, (a,b) -> a, TreeMap::new));
    }

}
