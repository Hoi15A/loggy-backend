package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.FileReadException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.TableDTO;
import ch.zhaw.pm4.loganalyser.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API controller to query log files for a given service.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/query")
public class QueryController {

    private final QueryService queryService;

    @GetMapping("sample")
    public ResponseEntity<TableDTO> getSampleQuery() {
        // TODO: to be removed
        return ResponseEntity.ok(queryService.getSampleLogsByQuery());
    }

    /**
     * Runs a query for a certain log service.
     * @param id of the log service.
     * @param query to be applied as a filter on the log files.
     * @return {@link ResponseEntity} with status 200 and the filtered content inside the body.
     * @throws RecordNotFoundException when the service does not exist.
     * @throws FileNotFoundException when the log file does not exist.
     * @throws FileReadException when there were complication while reading the log file.
     */
    @GetMapping("{logServiceId}/{query}")
    public ResponseEntity<List<String[]>> getQueryForLogService(@PathVariable("logServiceId") final long id,
                                                                @PathVariable("query") final String query) {
        return ResponseEntity.ok(queryService.runQueryForService(id, query));
    }

}
