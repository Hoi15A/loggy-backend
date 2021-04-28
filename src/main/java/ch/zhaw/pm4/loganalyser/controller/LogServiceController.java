package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.service.LogServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

/**
 * API controller for {@link LogServiceDTO}
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/service")
public class LogServiceController {

    private final LogServiceService logServiceService;

    /**
     * Creates a log service and saves it into the database based on the provided {@link LogServiceDTO}.
     * @param logServiceDTO Data transfer object containing the data that should be saved in the database.
     * @return {@link ResponseEntity} with status 201 and an empty body.
     * @throws RecordNotFoundException when the provided {@link LogConfigDTO} for this {@link LogServiceDTO} does not exist.
     */
    @PostMapping("/")
    public ResponseEntity<String> createService(@Valid @RequestBody final LogServiceDTO logServiceDTO) {
        logServiceService.createLogService(logServiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Returns a transformed list of all {@link LogServiceDTO} inside the database.
     * @return {@link ResponseEntity} with status 200 and a list of {@link LogServiceDTO} inside the body.
     */
    @GetMapping("all")
    public ResponseEntity<Set<LogServiceDTO>> getAllLogServices() {
        return ResponseEntity.ok(logServiceService.getAllLogServices());
    }

    /**
     * Returns a transformed log service with the matching id.
     * @param id that should be fetched from the database.
     * @return {@link ResponseEntity} with status 200 and a {@link LogServiceDTO} inside the body.
     * @throws RecordNotFoundException when the provided id does not exist.
     */
    @GetMapping("{id}")
    public ResponseEntity<LogServiceDTO> getLogServiceById(@PathVariable("id") final long id) {
        return ResponseEntity.ok(logServiceService.getLogServiceById(id));
    }

    /**
     * Deletes a log service from the database.
     * @param id of the {@link LogServiceDTO}.
     * @return {@link ResponseEntity} with status 200 and the deleted {@link LogServiceDTO} inside the body.
     * @throws RecordNotFoundException when the provided id does not exist.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<LogServiceDTO> deleteLogService(@PathVariable("id") final long id) {
        return ResponseEntity.ok(logServiceService.deleteLogServiceById(id));
    }

}
