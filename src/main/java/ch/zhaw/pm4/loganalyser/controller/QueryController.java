package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.model.dto.TableDTO;
import ch.zhaw.pm4.loganalyser.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
