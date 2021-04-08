package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.service.ColumnComponentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("column")
public class ColumnController {

    private final ColumnComponentService columnComponentService;

    /**
     * Creates a column component and saves into the database based on the provided {@link ColumnComponentDTO}.
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
     * @return ResponseEntity with status 200 and a list of {@link ColumnComponentDTO} inside the body.
     */
    @GetMapping
    public ResponseEntity<List<ColumnComponentDTO>> getAllColumnComponents() {
        return ResponseEntity.ok(columnComponentService.getAllColumnComponents());
    }

    /**
     * Returns a transformed column component with the matching id.
     * @param id that should be fetched from the database.
     * @return {@link ResponseEntity} with status 200 and a {@link ColumnComponentDTO} inside the body.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ColumnComponentDTO> getColumnComponentById(@PathVariable long id) {
        return ResponseEntity.ok(columnComponentService.getColumnComponentById(id));
    }
}
