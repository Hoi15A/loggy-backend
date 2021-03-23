package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.service.LogServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service")
public class LogServiceController {

    private final LogServiceService logServiceService;

    @PostMapping("/")
    public ResponseEntity<String> createService(@Valid @RequestBody final LogServiceDTO logServiceDTO) {
        logServiceService.createLogService(logServiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Returns all {@link LogServiceDTO} from the database.
     * @return ResponseEntity<List<Logs>>
     */
    @GetMapping("all")
    public ResponseEntity<Set<LogServiceDTO>> getAllLogServices() {
        return ResponseEntity.ok(logServiceService.getAllLogServices());
    }
}
