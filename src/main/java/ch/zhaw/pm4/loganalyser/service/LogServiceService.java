package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.zhaw.pm4.loganalyser.util.DTOMapper.mapDTOToLogService;
import static ch.zhaw.pm4.loganalyser.util.DTOMapper.mapLogServicesToDTOs;

@Service
@Transactional
@RequiredArgsConstructor
public class LogServiceService {

    private final LogServiceRepository logServiceRepository;

    public void createLogService(LogServiceDTO logServiceDTO) {
        logServiceRepository.save(mapDTOToLogService(logServiceDTO));
    }

    /**
     * Returns all LogServices.
     * @return list of {@link LogServiceDTO}
     */
    public Set<LogServiceDTO> getAllLogServices()
    {
        List<LogService> logs = logServiceRepository.findAll();
        return mapLogServicesToDTOs(new HashSet<>(logs));
    }

}
