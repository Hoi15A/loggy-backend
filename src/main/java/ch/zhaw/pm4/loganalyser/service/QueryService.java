package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.FileReadException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnDTO;
import ch.zhaw.pm4.loganalyser.model.dto.HeaderDTO;
import ch.zhaw.pm4.loganalyser.model.dto.QueryComponentDTO;
import ch.zhaw.pm4.loganalyser.model.dto.TableDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.QueryComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.parser.LogParser;
import ch.zhaw.pm4.loganalyser.parser.criteria.Criteria;
import ch.zhaw.pm4.loganalyser.parser.criteria.RegexCriteria;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import ch.zhaw.pm4.loganalyser.util.DTOMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Performs a query operation for a specific service.
 */
@RequiredArgsConstructor
@Setter
@Service
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
        } catch (ParseException e) {
            logger.log(Level.WARNING, "Unable to parse the given date", e);
        }

        headers.add(new HeaderDTO("Date", "date"));
        headers.add(new HeaderDTO("Type", "type"));
        headers.add(new HeaderDTO("Message", "message"));

        return new TableDTO(headers, tableData);

    }

    private Map<Integer, ColumnComponent> sortComponents(Map<Integer, ColumnComponent> sortable) {
        return sortable.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, TreeMap::new));
    }

    /**
     * Runs a query on a service and returns all matched lines.
     * @param serviceId to be queried on.
     * @param queries to be applied on the log files.
     * @throws RecordNotFoundException when the service does not exist.
     * @throws FileNotFoundException when the log file does not exist.
     * @throws FileReadException when there were complication while reading the log file.
     * @return List<String[]>
     */
    public List<String[]> runQueryForService(long serviceId, List<QueryComponentDTO> queries) {
        List<QueryComponent> queryComponents = queries.stream()
                .map(DTOMapper::mapDTOToQueryComponent)
                .collect(Collectors.toList());

        Optional<LogService> logService = logServiceRepository.findById(serviceId);
        if (logService.isEmpty())
            throw new RecordNotFoundException(String.format("The service with id %d does not exist", serviceId));

        try {
            LogService service = logService.get();
            List<String[]> logEntries = logParser.read(service);
            Map<Integer, ColumnComponent> sortedComponents = sortComponents(service.getLogConfig().getColumnComponents());

            for (QueryComponent component : queryComponents) {
                int componentIndex = getComponentIndex(component, sortedComponents);
                Criteria criteria = createCriteria(component);
                logEntries = criteria.apply(logEntries, componentIndex);
            }

            return logEntries;
        } catch (java.io.FileNotFoundException ex1) {
            throw new FileNotFoundException("Log service file not found");
        } catch (IOException ex2) {
            throw new FileReadException("Something went wrong while reading the file");
        }
    }

    private int getComponentIndex(QueryComponent component, Map<Integer, ColumnComponent> sortedComponents) {
        for (Map.Entry<Integer, ColumnComponent> entry : sortedComponents.entrySet()) {
            if (entry.getValue().getId() == component.getId()) return entry.getKey();
        }
        return -1;
    }

    private Criteria createCriteria(QueryComponent component) {
        // TODO: Create Factory for Criteria
        return new RegexCriteria("<regex here>");
    }

}
