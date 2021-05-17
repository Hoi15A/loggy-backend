package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.RecordAlreadyExistsException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.repository.LogConfigRepository;
import ch.zhaw.pm4.loganalyser.util.DTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ch.zhaw.pm4.loganalyser.util.DTOMapper.mapDTOToLogConfig;
import static ch.zhaw.pm4.loganalyser.util.DTOMapper.mapLogConfigToDTO;

/**
 * Perform CRUD operations for the log configs.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class LogConfigService {

    private final LogConfigRepository logConfigRepository;

    /**
     * Saves a new {@link LogConfig} to the database.
     * @param logConfigDTO to be created.
     * @throws RecordAlreadyExistsException when the same name already exists.
     */
    public void createLogConfig(LogConfigDTO logConfigDTO) {
        Optional<LogConfig> logConfigOptional = logConfigRepository.findById(logConfigDTO.getName());
        if (logConfigOptional.isPresent()) throw new RecordAlreadyExistsException("Could not create already existing log config " + logConfigDTO.getName());

        var config = mapDTOToLogConfig(logConfigDTO);
        logConfigRepository.save(config);
    }

    /**
     * Returns a transformed list of all {@link LogConfig}.
     * @return a list of {@link LogConfigDTO}
     */
    public List<LogConfigDTO> getAllLogConfigs() {
        return logConfigRepository
                .findAll()
                .stream()
                .map(DTOMapper::mapLogConfigToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns a transformed {@link LogConfig}
     * @param id of the {@link LogConfig}
     * @return a {@link LogConfigDTO}
     * @throws RecordNotFoundException when the provided id does not exist.
     */
    public LogConfigDTO getLogConfigById(String id) {
        Optional<LogConfig> logConfigOptional = logConfigRepository.findById(id);
        if (logConfigOptional.isEmpty()) throw new RecordNotFoundException("Could not find log config " + id);

        return mapLogConfigToDTO(logConfigOptional.get());
    }

    /**
     * Updates an existing {@link LogConfig} in the database.
     * @param logConfigDTO to be updated.
     * @throws RecordNotFoundException when the provided {@link LogConfigDTO} does not exist.
     */
    public void updateLogConfig(LogConfigDTO logConfigDTO) {
        Optional<LogConfig> logConfigOptional= logConfigRepository.findById(logConfigDTO.getName());
        if (logConfigOptional.isEmpty()) throw new RecordNotFoundException("Could not update non existing log config " + logConfigDTO.getName());

        var config = mapDTOToLogConfig(logConfigDTO);
        logConfigRepository.save(config);
    }

    /**
     * Deletes a {@link LogConfig} from the database.
     * @param id of the {@link LogConfig}.
     * @return the deleted {@link LogConfigDTO}.
     * @throws RecordNotFoundException when the provided id does not exist.
     */
    public LogConfigDTO deleteLogConfigById(String id) {
        Optional<LogConfig> optionalLogConfig = logConfigRepository.findById(id);
        if (optionalLogConfig.isEmpty()) throw new RecordNotFoundException("Could not delete non existing log config " + id);

        var logConfig = optionalLogConfig.get();
        logConfigRepository.delete(logConfig);
        return mapLogConfigToDTO(optionalLogConfig.get());
    }

}
