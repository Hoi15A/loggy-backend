package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.repository.ColumnComponentRepository;
import ch.zhaw.pm4.loganalyser.util.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ch.zhaw.pm4.loganalyser.util.DTOMapper.mapDTOToColumnComponent;


@Service
@AllArgsConstructor
public class ColumnComponentService {

    private final ColumnComponentRepository columnComponentRepository;

    public void createColumn(ColumnComponentDTO columnComponentDTO) {
        ColumnComponent columnComponent = mapDTOToColumnComponent(columnComponentDTO);
        columnComponentRepository.save(columnComponent);
    }

    public ColumnComponentDTO deleteColumnComponentById(long id) {
        Optional<ColumnComponent> optionalColumnComponent = columnComponentRepository.findById(id);
        if (optionalColumnComponent.isEmpty()) throw new RecordNotFoundException("Could not delete id: " + id);
        ColumnComponent columnComponent = optionalColumnComponent.get();
        columnComponentRepository.delete(columnComponent);
        return DTOMapper.mapColumnComponentToDTO(columnComponent);
    }

    public List<ColumnComponentDTO> getAllColumnComponents() {
        return columnComponentRepository
                .findAll()
                .stream()
                .map(DTOMapper::mapColumnComponentToDTO)
                .collect(Collectors.toList());
    }

    public ColumnComponentDTO getColumnComponentById(long id) {
        Optional<ColumnComponent> columnComponentOptional = columnComponentRepository.findById(id);
        if(columnComponentOptional.isEmpty()) throw new RecordNotFoundException("ID was not found");
        return DTOMapper.mapColumnComponentToDTO(columnComponentOptional.get());
    }
}
