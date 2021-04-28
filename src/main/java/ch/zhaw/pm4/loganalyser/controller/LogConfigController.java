package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.exception.RecordAlreadyExistsException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.service.LogConfigService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * API controller for {@link LogConfigDTO}
 */
@AllArgsConstructor
@RestController
@RequestMapping("config")
public class LogConfigController {

    private final LogConfigService logConfigService;

    /**
     * Creates a log config and saves it into the database based on the provided {@link LogConfigDTO}.
     * @param logConfigDTO Data transfer object containing the data that should be saved in the database.
     * @return {@link ResponseEntity} with status 201 and an empty body.
     * @throws RecordAlreadyExistsException when the same name already exists.
     */
    @PostMapping("/")
    public ResponseEntity<String> createConfig(@Valid @RequestBody final LogConfigDTO logConfigDTO) {
        logConfigService.createLogConfig(logConfigDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Returns a transformed list of all log configs inside the database.
     * @return {@link ResponseEntity} with status 200 and a list of {@link LogConfigDTO} inside the body.
     */
    @GetMapping
    public ResponseEntity<List<LogConfigDTO>> getAllLogConfigs() {
        return ResponseEntity.ok(logConfigService.getAllLogConfigs());
    }

    /**
     * Returns a transformed log config with the matching id.
     * @param id that should be fetched from the database.
     * @return {@link ResponseEntity} with status 200 and a {@link LogConfigDTO} inside the body.
     * @throws RecordNotFoundException when the provided id does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LogConfigDTO> getLogConfigById(@PathVariable String id) {
        return ResponseEntity.ok(logConfigService.getLogConfigById(id));
    }

    /**
     * Updates a log config and saves it into the database based on the provided {@link LogConfigDTO}.
     * @param logConfigDTO Data transfer object containing the data that should be updated in the database.
     * @return {@link ResponseEntity} with status 204 with an empty body.
     * @throws RecordNotFoundException when the provided {@link LogConfigDTO} does not exist.
     */
    @PutMapping("/")
    public ResponseEntity<String> updateLogConfig(@Valid @RequestBody final LogConfigDTO logConfigDTO) {
        logConfigService.updateLogConfig(logConfigDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Deletes a log config from the database.
     * @param id of the {@link LogConfigDTO}.
     * @return {@link ResponseEntity} with status 200 and the deleted {@link LogConfigDTO} inside the body.
     * @throws RecordNotFoundException when the provided id does not exist.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<LogConfigDTO> deleteLogService(@PathVariable("id") final String id) {
        return ResponseEntity.ok(logConfigService.deleteLogConfigById(id));
    }

}
