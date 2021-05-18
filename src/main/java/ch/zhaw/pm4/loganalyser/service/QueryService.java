package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.FileReadException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.*;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.QueryComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.FilterType;
import ch.zhaw.pm4.loganalyser.query.criteria.Criteria;
import ch.zhaw.pm4.loganalyser.query.criteria.CriteriaFactory;
import ch.zhaw.pm4.loganalyser.query.parser.LogParser;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import ch.zhaw.pm4.loganalyser.util.DTOMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @Autowired
    private ColumnComponentService columnComponentService;

    @NonNull
    private LogServiceRepository logServiceRepository;

    private LogParser logParser = new LogParser();

    public TableDTO getSampleLogsByQuery() {
        // todo : to be removed
        List<ColumnDTO> tableData = new ArrayList<>();
        List<HeaderDTO> headers = new ArrayList<>();
        var sdf = new SimpleDateFormat("dd.MM.yyyy");
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

    /**
     * Runs a query on a service and returns all matched lines.
     * @param serviceId to be queried on.
     * @param  queryComponentDTOS {@link QueryComponentDTO} to be applied on the log files.
     * @throws RecordNotFoundException when the service does not exist.
     * @throws FileNotFoundException when the log file does not exist.
     * @throws FileReadException when there were complication while reading the log file.
     * @return List<String[]>
     */
    public List<String[]> runQueryForService(long serviceId, List<QueryComponentDTO> queryComponentDTOS) {
        Optional<LogService> logService = logServiceRepository.findById(serviceId);
        if (logService.isEmpty())
            throw new RecordNotFoundException(String.format("The service with id %d does not exist", serviceId));

        List<QueryComponent> queryComponents = mapQueryComponents(queryComponentDTOS);

        try {
            var service = logService.get();
            List<String[]> logEntries = logParser.read(service);

            for (QueryComponent component : queryComponents) {
                int componentIndex = getComponentIndex(component, service.getLogConfig().getColumnComponents());
                component.setColumnComponent(service.getLogConfig().getColumnComponents().get(componentIndex));
                var filterType = component.getFilterType();
                var criteria = createCriteria(filterType, component);
                logEntries = criteria.apply(logEntries, componentIndex);
            }

            return logEntries;
        } catch (java.io.FileNotFoundException ex1) {
            throw new FileNotFoundException("Log service file not found");
        } catch (IOException ex2) {
            throw new FileReadException("Something went wrong while reading the file");
        }
    }

    private List<QueryComponent> mapQueryComponents(List<QueryComponentDTO> queryComponentDTOS) {
        List<QueryComponent> queryComponents = queryComponentDTOS.stream()
                .map(DTOMapper::mapDTOToQueryComponent)
                .collect(Collectors.toList());

        queryComponents.forEach(queryComponent -> {
            ColumnComponentDTO dto = columnComponentService.getColumnComponentById(queryComponent.getColumnComponentId());
            queryComponent.setColumnComponent(DTOMapper.mapDTOToColumnComponent(dto));
        });
        return queryComponents;
    }

    private int getComponentIndex(QueryComponent component, Map<Integer, ColumnComponent> sortedComponents) {
        return sortedComponents.entrySet().stream()
                .filter(entry -> entry.getValue().getId() == component.getColumnComponent().getId())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("The column component (id = " + component.getColumnComponent().getId() + ") provided with the query was not found."));
    }

    private Criteria createCriteria(FilterType filterType, QueryComponent component) {
        return CriteriaFactory.getCriteria(filterType, component);
    }
}
