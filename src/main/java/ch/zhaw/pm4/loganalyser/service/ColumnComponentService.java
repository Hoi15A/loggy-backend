package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.repository.ColumnComponentRepository;
import ch.zhaw.pm4.loganalyser.util.DTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Perform CRUD operations for the column components.
 */
@RequiredArgsConstructor
@Service
public class ColumnComponentService {

    private final ColumnComponentRepository columnComponentRepository;

    /**
     * Saves a new {@link ColumnComponent} to the database.
     * @param dto to be created.
     */
    public void createColumnComponent(ColumnComponentDTO dto) {
        var columnComponent = DTOMapper.mapDTOToColumnComponent(dto);
        columnComponentRepository.save(columnComponent);
    }

    /**
     * Returns a transformed list of all {@link ColumnComponent}.
     * @return a list of {@link ColumnComponentDTO}
     */
    public List<ColumnComponentDTO> getAllColumnComponents() {
        return columnComponentRepository
                .findAll()
                .stream()
                .map(DTOMapper::mapColumnComponentToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns a transformed {@link ColumnComponent}
     * @param id of the {@link ColumnComponent}
     * @return a {@link ColumnComponentDTO}
     * @throws RecordNotFoundException when the provided id does not exist.
     */
    public ColumnComponentDTO getColumnComponentById(long id) {
        Optional<ColumnComponent> columnComponentOptional = columnComponentRepository.findById(id);
        if(columnComponentOptional.isEmpty()) throw new RecordNotFoundException("Could not find column component with id " + id);

        return DTOMapper.mapColumnComponentToDTO(columnComponentOptional.get());
    }

    /**
     * Updates an existing {@link ColumnComponent} in the database.
     * @param columnComponentDTO to be updated.
     * @throws RecordNotFoundException when the provided {@link ColumnComponentDTO} does not exist.
     */
    public void updateColumn(ColumnComponentDTO columnComponentDTO) {
        Optional<ColumnComponent> oldColumnComponent = columnComponentRepository.findById(columnComponentDTO.getId());
        if(oldColumnComponent.isEmpty()) throw new RecordNotFoundException("Could not update non existing column component " + columnComponentDTO.getName());

        columnComponentRepository.save(DTOMapper.mapDTOToColumnComponent(columnComponentDTO));
    }

    /**
     * Deletes a {@link ColumnComponent} from the database.
     * @param id of the {@link ColumnComponent}
     * @return the deleted {@link ColumnComponentDTO}
     * @throws RecordNotFoundException when the provided id does not exist.
     */
    public ColumnComponentDTO deleteColumnComponentById(long id) {
        Optional<ColumnComponent> optionalColumnComponent = columnComponentRepository.findById(id);
        if (optionalColumnComponent.isEmpty()) throw new RecordNotFoundException("Could not delete id: " + id);

        var columnComponent = optionalColumnComponent.get();
        columnComponentRepository.delete(columnComponent);
        return DTOMapper.mapColumnComponentToDTO(columnComponent);
    }

}
