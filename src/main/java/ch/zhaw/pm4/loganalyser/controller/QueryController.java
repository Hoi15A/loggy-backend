package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.FileReadException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.QueryComponentDTO;
import ch.zhaw.pm4.loganalyser.query.parser.LogParser;
import ch.zhaw.pm4.loganalyser.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * API controller to query log files for a given service.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/query")
public class QueryController {

    private final QueryService queryService;

    /**
     * Runs a query for a certain log service.
     * @param logServiceId of the log service.
     * @param page of the file which should be fetched. A page is {@value LogParser#PAGE_SIZE} lines long. Defaults to 0 if not specified.
     * @param queries to be applied as a filter on the log files.
     * @return {@link ResponseEntity} with status 200 and the filtered content inside the body.
     * @throws RecordNotFoundException when the service does not exist.
     * @throws FileNotFoundException when the log file does not exist.
     * @throws FileReadException when there were complication while reading the log file.
     */
    @PostMapping("{logServiceId}")
    public ResponseEntity<List<String[]>> getQueryForLogService(
            @PathVariable("logServiceId") final long logServiceId,
            @RequestParam(required = false, defaultValue = "0") final int page,
            @Valid @RequestBody final List<QueryComponentDTO> queries) {
        return ResponseEntity.ok(queryService.runQueryForService(logServiceId, queries, page));
    }

}
