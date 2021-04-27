package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.RecordAlreadyExistsException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.repository.LogConfigRepository;
import ch.zhaw.pm4.loganalyser.util.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ch.zhaw.pm4.loganalyser.util.DTOMapper.*;

@Service
@AllArgsConstructor
public class LogConfigService {

    private final LogConfigRepository logConfigRepository;

    /**
     * Saves a new {@link LogConfig} to the database
     * @param logConfigDTO
     * @throws RecordAlreadyExistsException If a config with the same name already exists this exception will be thrown
     */
    public void createLogConfig(LogConfigDTO logConfigDTO) {
        Optional<LogConfig> logConfigOptional = logConfigRepository.findById(logConfigDTO.getName());
        if (logConfigOptional.isPresent()) throw new RecordAlreadyExistsException("A config with the name " + logConfigDTO.getName() + " already exists.");

        LogConfig config = mapDTOToLogConfig(logConfigDTO);
        logConfigRepository.save(config);
    }

    /**
     * Updates an existing {@link LogConfig}
     * @param logConfigDTO
     */
    public void updateLogConfig(LogConfigDTO logConfigDTO) {
        Optional<LogConfig> logConfigOptional= logConfigRepository.findById(logConfigDTO.getName());
        if (logConfigOptional.isEmpty()) throw new RecordNotFoundException("A config with the name " + logConfigDTO.getName() + " does not exist and cant be updated.");

        LogConfig config = mapDTOToLogConfig(logConfigDTO);
        logConfigRepository.save(config);
    }

    public List<LogConfigDTO> getAllLogConfigs() {
        return logConfigRepository
                .findAll()
                .stream()
                .map(DTOMapper::mapLogConfigToDTO)
                .collect(Collectors.toList());
    }

    public LogConfigDTO getLogConfigById(String id) {
        Optional<LogConfig> logConfigOptional = logConfigRepository.findById(id);
        if (logConfigOptional.isEmpty()) throw new RecordNotFoundException(id);

        return mapLogConfigToDTO(logConfigOptional.get());
    }

    /**
     * Deletes a {@link LogConfigService} by it's name
     * @param id of the {@link LogConfigService}
     * @return deleted {@link LogConfigService}
     */
    public LogConfigDTO deleteLogConfigById(String id) {
        Optional<LogConfig> optionalLogConfig = logConfigRepository.findById(id);
        if (optionalLogConfig.isEmpty()) throw new RecordNotFoundException(id);

        LogConfig logConfig = optionalLogConfig.get();
        logConfigRepository.delete(logConfig);
        return mapLogConfigToDTO(optionalLogConfig.get());
    }
}
