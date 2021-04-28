package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.service.ColumnComponentService;
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
 * API controller for {@link ColumnComponentDTO}
 */
@AllArgsConstructor
@RestController
@RequestMapping("column")
public class ColumnComponentController {

    private final ColumnComponentService columnComponentService;

    /**
     * Creates a column component and saves it into the database based on the provided {@link ColumnComponentDTO}.
     * @param columnComponentDTO Data transfer object containing the data that should be saved in the database.
     * @return {@link ResponseEntity} with status 201 and an empty body.
     */
    @PostMapping("/")
    public ResponseEntity<String> createColumn(@Valid @RequestBody final ColumnComponentDTO columnComponentDTO) {
        columnComponentService.createColumn(columnComponentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Returns a transformed list of all column components inside the database.
     * @return {@link ResponseEntity} with status 200 and a list of {@link ColumnComponentDTO} inside the body.
     */
    @GetMapping
    public ResponseEntity<List<ColumnComponentDTO>> getAllColumnComponents() {
        return ResponseEntity.ok(columnComponentService.getAllColumnComponents());
    }

    /**
     * Returns a transformed column component with the matching id.
     * @param id of {@link ColumnComponentDTO} that should be fetched from the database.
     * @return {@link ResponseEntity} with status 200 and a {@link ColumnComponentDTO} inside the body.
     * @throws RecordNotFoundException when the provided id does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ColumnComponentDTO> getColumnComponentById(@PathVariable long id) {
        return ResponseEntity.ok(columnComponentService.getColumnComponentById(id));
    }

    /**
     * Updates a column component and saves it into the database based on the provided {@link ColumnComponentDTO}.
     * @param columnComponentDTO Data transfer object containing the data that should be updated in the database.
     * @return {@link ResponseEntity} with status 200 and the updated {@link ColumnComponentDTO} inside the body.
     * @throws RecordNotFoundException when the provided {@link ColumnComponentDTO} does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ColumnComponentDTO> updateColumn(@Valid @RequestBody final ColumnComponentDTO columnComponentDTO) {
        // TODO: check or set dto id with pathvariable
        columnComponentService.updateColumn(columnComponentDTO);
        return ResponseEntity.ok(columnComponentDTO);
    }

    /**
     * Deletes a column component from the database.
     * @param id of the {@link ColumnComponentDTO}.
     * @return {@link ResponseEntity} with status 200 and the deleted {@link ColumnComponentDTO} inside the body.
     * @throws RecordNotFoundException when the provided id does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ColumnComponentDTO> deleteColumnComponentById(@PathVariable long id) {
        return ResponseEntity.ok(columnComponentService.deleteColumnComponentById(id));
    }

}
