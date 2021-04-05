package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.service.LogConfigService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("config")
public class LogConfigController {

    private final LogConfigService logConfigService;

    /**
     * Creates a log service and saves into the database based on the provided {@link LogConfigDTO}.
     * @param logConfigDTO Data transfer object containing the data that should be saved in the database.
     * @return {@link ResponseEntity} with status 201 and an empty body.
     */
    @PostMapping("/")
    public ResponseEntity<String> createService(@Valid @RequestBody final LogConfigDTO logConfigDTO) {
        logConfigService.createLogConfig(logConfigDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Returns a transformed list of all log configs inside the database.
     * @return ResponseEntity with status 200 and a list of {@link LogConfigDTO} inside the body.
     */
    @GetMapping
    public ResponseEntity<List<LogConfigDTO>> getAllLogConfigs() {
        return ResponseEntity.ok(logConfigService.getAllLogConfigs());
    }

    /**
     * Returns a transformed log config with the matching id.
     * @param id that should be fetched from the database.
     * @return {@link ResponseEntity} with status 200 and a {@link LogConfigDTO} inside the body.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LogConfigDTO> getLogConfigById(@PathVariable String id) {
        return ResponseEntity.ok(logConfigService.getLogConfigById(id));
    }

}
