package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static ch.zhaw.pm4.loganalyser.util.DTOMapper.mapDTOToLogService;

@Service
@Transactional
@RequiredArgsConstructor
public class LogServiceService {

    private final LogServiceRepository logServiceRepository;

    public void createLogService(LogServiceDTO logServiceDTO) {
        logServiceRepository.save(mapDTOToLogService(logServiceDTO));
    }

}
