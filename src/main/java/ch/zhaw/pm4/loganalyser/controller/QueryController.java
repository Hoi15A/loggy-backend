package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.model.dto.QueryComponentDTO;
import ch.zhaw.pm4.loganalyser.model.dto.TableDTO;
import ch.zhaw.pm4.loganalyser.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/query")
public class QueryController {

    private final QueryService queryService;

    @GetMapping("sample")
    public ResponseEntity<TableDTO> getSampleQuery() {
        return ResponseEntity.ok(queryService.getSampleLogsByQuery());
    }

    /**
     * Runs a query for a certain log service
     *
     * @param logServiceId
     * @return
     */
    @GetMapping("{logServiceId}")
    public ResponseEntity<List<String[]>> getQueryForLogService(
            @PathVariable("logServiceId") final long logServiceId,
            @Valid @RequestBody final List<QueryComponentDTO> queryComponents) {
        return ResponseEntity.ok(queryService.runQueryForService(logServiceId, queryComponents));
    }
}
