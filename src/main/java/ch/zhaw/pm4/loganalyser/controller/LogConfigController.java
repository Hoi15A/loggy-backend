package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.service.LogConfigService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("config")
public class LogConfigController {

    private final LogConfigService logConfigService;

    /**
     * Creates a log config and saves into the database based on the provided {@link LogConfigDTO}.
     * @param logConfigDTO Data transfer object containing the data that should be saved in the database.
     * @return {@link ResponseEntity} with status 201 and an empty body.
     */
    @PostMapping("/")
    public ResponseEntity<String> createConfig(@Valid @RequestBody final LogConfigDTO logConfigDTO) {
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

    /**
     * Takes a logconfig id and deletes the config object from the database.
     * The deleted {@link LogConfigDTO} will be returned.
     * @param id of the {@link LogConfigDTO}
     * @return ResponseEntity<LogConfigDTO>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<LogConfigDTO> deleteLogService(@PathVariable("id") final String id) {
        return ResponseEntity.ok(logConfigService.deleteLogConfigById(id));
    }

    /**
     * Takes a logconfig DTO and updates it in the database.
     * @param logConfigDTO the data transfer object to be updated.
     * @return {@link ResponseEntity} with status 204.
     */
    @PutMapping("/")
    public ResponseEntity<String> putLogConfig(@Valid @RequestBody final LogConfigDTO logConfigDTO) {
        logConfigService.updateLogConfig(logConfigDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
