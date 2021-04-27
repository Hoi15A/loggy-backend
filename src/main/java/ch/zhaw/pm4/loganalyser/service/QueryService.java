package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
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
     * Runs a query on a service and returns all matched lines
     *
     * @param serviceId          log service id
     * @param queryComponentDTOS TODO
     * @return rows
     */
    public List<String[]> runQueryForService(long serviceId, List<QueryComponentDTO> queryComponentDTOS) {
        List<QueryComponent> queryComponents = queryComponentDTOS.stream()
                .map(DTOMapper::mapDTOToQueryComponent)
                .collect(Collectors.toList());
        Optional<LogService> logService = logServiceRepository.findById(serviceId);
        if (logService.isEmpty()) throw new RecordNotFoundException(String.valueOf(serviceId));

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
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileNotFoundException("Logservice file not found");
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
