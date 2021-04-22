package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.repository.LogConfigRepository;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import ch.zhaw.pm4.loganalyser.util.DTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ch.zhaw.pm4.loganalyser.util.DTOMapper.mapDTOToLogService;
import static ch.zhaw.pm4.loganalyser.util.DTOMapper.mapLogServiceToDTO;

@Service
@Transactional
@RequiredArgsConstructor
public class LogServiceService {

    private final LogServiceRepository logServiceRepository;
    private final LogConfigRepository logConfigRepository;

    public void createLogService(LogServiceDTO logServiceDTO) {
        Optional<LogConfig> optionalLogConfig = logConfigRepository.findById(logServiceDTO.getLogConfig());
        if (optionalLogConfig.isEmpty()) throw new RecordNotFoundException(logServiceDTO.getLogConfig());

        LogService service = mapDTOToLogService(logServiceDTO);
        service.setLogConfig(optionalLogConfig.get());
        logServiceRepository.save(service);
    }

    /**
     * Returns all LogServices.
     * @return list of {@link LogServiceDTO}
     */
    public Set<LogServiceDTO> getAllLogServices() {
        return logServiceRepository
                .findAll()
                .stream()
                .map(DTOMapper::mapLogServiceToDTO)
                .collect(Collectors.toSet());
    }

    /**
     * Get a {@link LogService} by it's id
     * @param id of the {@link LogService}
     * @return the logservice or {@link RecordNotFoundException} when the logservice is not existent
     */
    public LogServiceDTO getLogServiceById(long id) {
        Optional<LogService> logService = logServiceRepository.findById(id);
        if(logService.isEmpty()) throw new RecordNotFoundException(String.valueOf(id));

        return mapLogServiceToDTO(logService.get());
    }
    
    /**
     * Deletes a {@link LogService} by it's id
     * @param id of the {@link LogService}
     * @return deleted {@link LogService}
     */
    public LogServiceDTO deleteLogServiceById(long id) {
        Optional<LogService> optionalLogService = logServiceRepository.findById(id);
        if (optionalLogService.isEmpty()) throw new RecordNotFoundException(String.valueOf(id));

        LogService logService = optionalLogService.get();
        LogConfig logConfig = logService.getLogConfig();
        logService.setLogConfig(null);
        logServiceRepository.save(logService);
        logServiceRepository.delete(logService);
        logService.setLogConfig(logConfig);
        return mapLogServiceToDTO(optionalLogService.get());
    }
}
