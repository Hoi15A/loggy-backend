package ch.zhaw.pm4.loganalyser.util;

import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DTOMapper {

    public static LogService mapDTOToLogService(LogServiceDTO logServiceDTO) {
        return LogService.builder()
                .name(logServiceDTO.getName())
                .description(logServiceDTO.getDescription())
                .logDirectory(logServiceDTO.getLogDirectory())
                .image(logServiceDTO.getImage())
                .logServiceLocation(logServiceDTO.getLocation())
                .logConfig(mapDTOToLogConfig(logServiceDTO.getLogConfigDTO()))
                .build();
    }

    public static LogConfig mapDTOToLogConfig(LogConfigDTO logConfigDTO) {
        return LogConfig.builder()
                .name(logConfigDTO.getName())
                .columnCount(logConfigDTO.getColumnCount())
                .headerLength(logConfigDTO.getHeaderLength())
                .separator(logConfigDTO.getSeparator())
                .build();
    }

    public static LogServiceDTO mapLogServiceToDTO(LogService logService) {
        LogServiceDTO dto = new LogServiceDTO();
        dto.setName(logService.getName());
        dto.setDescription(logService.getDescription());
        dto.setImage(logService.getImage());
        dto.setLogDirectory(logService.getLogDirectory());
        return dto;
    }

    public static Set<LogServiceDTO> mapLogServicesToDTOs(Set<LogService> logServices) {
        if(logServices == null) return new HashSet<>();
        return logServices.stream()
                .map(DTOMapper::mapLogServiceToDTO)
                .collect(Collectors.toSet());
    }

}
