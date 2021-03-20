package ch.zhaw.pm4.loganalyser.util;

import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;

public class DTOMapper {

    public static LogService mapDTOToLogService(LogServiceDTO logServiceDTO) {
        return LogService.builder()
                .name(logServiceDTO.getName())
                .description(logServiceDTO.getDescription())
                .logDirectory(logServiceDTO.getLogDirectory())
                .image(logServiceDTO.getImage())
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

}
