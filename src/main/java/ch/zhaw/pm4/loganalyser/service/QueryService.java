package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.FileReadException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.dto.QueryComponentDTO;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Performs a query operation for a specific service.
 */
@RequiredArgsConstructor
@Setter
@Service
public class QueryService {

    private static final Logger LOGGER = Logger.getLogger(QueryService.class.getName());

    public static final long PAGE_SIZE = 500L;

    @NonNull
    private ColumnComponentService columnComponentService;

    @NonNull
    private LogServiceRepository logServiceRepository;

    private LogParser logParser = new LogParser();

    /**
     * Runs a query on a service and returns all matched lines.
     * @param serviceId to be queried on.
     * @param  queryComponentDTOS {@link QueryComponentDTO} to be applied on the log files.
     * @param page of the file which should be fetched. A page is {@value QueryService#PAGE_SIZE} lines long.
     * @throws RecordNotFoundException when the service does not exist.
     * @throws FileNotFoundException when the log file does not exist.
     * @throws FileReadException when there were complication while reading the log file.
     * @return List<String[]>
     */
    public List<String[]> runQueryForService(long serviceId, List<QueryComponentDTO> queryComponentDTOS, int page) {
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

                List<String[]> finalLogEntries = logEntries;
                LOGGER.info(() -> "Lines remaining after query applied on [" + component.getColumnComponent().getName() + "]: " + finalLogEntries.size());
            }

            return pageList(logEntries, page);
        } catch (java.io.FileNotFoundException ex1) {
            throw new FileNotFoundException("Log service file not found", ex1);
        } catch (IOException ex2) {
            throw new FileReadException("Something went wrong while reading the file");
        }
    }

    private List<String[]> pageList(List<String[]> logEntries, int page) {
        LOGGER.info(() -> "Skipped " + (page * PAGE_SIZE) + " lines");
        return logEntries.stream()
                .skip(page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .collect(Collectors.toList());
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
