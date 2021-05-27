package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
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

/**
 * Perform CRUD operations for the log services.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class LogServiceService {

    public static final String UPDATE_SERVICE_S = "Could not update non existing log service %s";
    public static final String UPDATE_SERVICE_LOG_CONFIG_S = "Could not create log service. Log config %s does not exist.";

    private final LogServiceRepository logServiceRepository;
    private final LogConfigRepository logConfigRepository;

    /**
     * Saves a new {@link LogService} to the database.
     * @param logServiceDTO to be created.
     * @throws RecordNotFoundException when the provided {@link LogConfigDTO} for this {@link LogServiceDTO} does not exist.
     */
    public void createLogService(LogServiceDTO logServiceDTO) {
        Optional<LogConfig> optionalLogConfig = logConfigRepository.findById(logServiceDTO.getLogConfig());
        if (optionalLogConfig.isEmpty()) throw new RecordNotFoundException("Could not create log service. Log config " + logServiceDTO.getLogConfig() + " does not exist.");

        var service = mapDTOToLogService(logServiceDTO);
        service.setLogConfig(optionalLogConfig.get());
        logServiceRepository.save(service);
    }

    /**
     * Returns a transformed list of all {@link LogService}.
     * @return a list of {@link LogServiceDTO}
     */
    public Set<LogServiceDTO> getAllLogServices() {
        return logServiceRepository
                .findAll()
                .stream()
                .map(DTOMapper::mapLogServiceToDTO)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a transformed {@link LogService}
     * @param id of the {@link LogService}
     * @return a {@link LogServiceDTO}
     * @throws RecordNotFoundException when the provided id does not exist.
     */
    public LogServiceDTO getLogServiceById(long id) {
        Optional<LogService> logService = logServiceRepository.findById(id);
        if(logService.isEmpty()) throw new RecordNotFoundException("Could not find log service with id " + id);

        return mapLogServiceToDTO(logService.get());
    }
    
    /**
     * Deletes a {@link LogService} from the database.
     * @param id of the {@link LogService}
     * @return the deleted {@link LogServiceDTO}
     * @throws RecordNotFoundException when the provided id does not exist.
     */
    public LogServiceDTO deleteLogServiceById(long id) {
        Optional<LogService> optionalLogService = logServiceRepository.findById(id);
        if (optionalLogService.isEmpty()) throw new RecordNotFoundException("Could not delete non existing log service with id " + id);

        var logService = optionalLogService.get();
        var logConfig = logService.getLogConfig();
        logService.setLogConfig(null);
        logServiceRepository.save(logService);
        logServiceRepository.delete(logService);
        logService.setLogConfig(logConfig);
        return mapLogServiceToDTO(optionalLogService.get());
    }

    /**
     * Updates a {@link LogService} in the database.
     * @param logServiceDTO the updated log service as a {@link LogServiceDTO}
     * @throws RecordNotFoundException when the LogService does not exist already
     */
    public void updateLogService(LogServiceDTO logServiceDTO) {
        Optional<LogService> logServiceOptional = logServiceRepository.findById(logServiceDTO.getId());
        if (logServiceOptional.isEmpty()) throw new RecordNotFoundException(String.format(UPDATE_SERVICE_S, logServiceDTO.getName()));

        var service = mapDTOToLogService(logServiceDTO);

        var logConfigOptional = logConfigRepository.findById(logServiceDTO.getLogConfig());
        if (logConfigOptional.isEmpty()) throw new RecordNotFoundException(String.format(UPDATE_SERVICE_LOG_CONFIG_S, logServiceDTO.getLogConfig()));

        service.setLogConfig(logConfigOptional.get());

        logServiceRepository.save(service);
    }
}
