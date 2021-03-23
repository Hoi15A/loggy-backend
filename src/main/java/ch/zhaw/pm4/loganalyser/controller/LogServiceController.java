package ch.zhaw.pm4.loganalyser.controller;

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

    /**
     * Returns a specified {@link LogServiceDTO} from the database.
     * @param id long
     * @return ResponseEntity<LogServiceDTO>
     */
    @GetMapping("{id}")
    public ResponseEntity<LogServiceDTO> getLogServiceById(@PathVariable("id") final long id) {
        return ResponseEntity.ok(logServiceService.getLogServiceById(id));
    }

    /**
     * Takes a logservice id and deletes the service object from the database.
     * The deleted {@link LogServiceDTO} will be returned.
     * @param id of the {@link LogServiceDTO}
     * @return ResponseEntity<LogServiceDTO>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<LogServiceDTO> deleteLogService(@PathVariable("id") final long id) {
        return ResponseEntity.ok(logServiceService.deleteLogServiceById(id));
    }
}
