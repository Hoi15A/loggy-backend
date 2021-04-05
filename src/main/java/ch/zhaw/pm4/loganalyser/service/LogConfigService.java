package ch.zhaw.pm4.loganalyser.service;

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

import static ch.zhaw.pm4.loganalyser.util.DTOMapper.mapDTOToLogConfig;

@Service
@AllArgsConstructor
public class LogConfigService {

    private final LogConfigRepository logConfigRepository;

    public void createLogConfig(LogConfigDTO logConfigDTO) {
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
        return DTOMapper.mapLogConfigToDTO(logConfigOptional.get());
    }

}
