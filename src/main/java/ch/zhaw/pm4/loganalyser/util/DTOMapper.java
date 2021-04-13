package ch.zhaw.pm4.loganalyser.util;

import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Utility to map model and data transfer objects.
 */
@UtilityClass
public class DTOMapper {

    public static LogService mapDTOToLogService(LogServiceDTO logServiceDTO) {
        return LogService.builder()
                .id(logServiceDTO.getId())
                .name(logServiceDTO.getName())
                .description(logServiceDTO.getDescription())
                .logDirectory(logServiceDTO.getLogDirectory())
                .image(logServiceDTO.getImage())
                .logServiceLocation(logServiceDTO.getLocation())
                .build();
    }

    public static LogConfig mapDTOToLogConfig(LogConfigDTO logConfigDTO) {
        return LogConfig.builder()
                .name(logConfigDTO.getName())
                .columnCount(logConfigDTO.getColumnCount())
                .headerLength(logConfigDTO.getHeaderLength())
                .separator(logConfigDTO.getSeparator())
                .columnComponents(mapColumnComponentsDtoMapToColumnComponentsMap(logConfigDTO.getColumnComponents()))
                .build();
    }

    public static LogConfigDTO mapLogConfigToDTO(LogConfig logConfig) {
        LogConfigDTO logConfigDTO = new LogConfigDTO();
        logConfigDTO.setName(logConfig.getName());
        logConfigDTO.setColumnCount(logConfig.getColumnCount());
        logConfigDTO.setHeaderLength(logConfig.getHeaderLength());
        logConfigDTO.setSeparator(logConfig.getSeparator());
        logConfigDTO.setColumnComponents(mapColumnComponentsMapToDtoMap(logConfig.getColumnComponents()));
        return logConfigDTO;
    }

    private static Map<Integer, ColumnComponentDTO> mapColumnComponentsMapToDtoMap(Map<Integer, ColumnComponent> columnComponentMap) {
        Map<Integer, ColumnComponentDTO> dtoMap = new HashMap<>();
        columnComponentMap.forEach((key, value) -> dtoMap.put(key, mapColumnComponentToDTO(value)));
        return dtoMap;
    }

    private static Map<Integer, ColumnComponent> mapColumnComponentsDtoMapToColumnComponentsMap(Map<Integer, ColumnComponentDTO> dtoMap) {
        Map<Integer, ColumnComponent> map = new HashMap<>();
        dtoMap.forEach((key, value) -> map.put(key, mapDTOToColumnComponent(value)));
        return map;
    }

    public static LogServiceDTO mapLogServiceToDTO(LogService logService) {
        LogServiceDTO dto = new LogServiceDTO();
        dto.setId(logService.getId());
        dto.setName(logService.getName());
        dto.setDescription(logService.getDescription());
        dto.setImage(logService.getImage());
        dto.setLogDirectory(logService.getLogDirectory());
        dto.setLocation(logService.getLogServiceLocation());
        dto.setLogConfig(logService.getLogConfig().getName());
        return dto;
    }

    public static ColumnComponent mapDTOToColumnComponent(ColumnComponentDTO columnComponentDTO) {
        return ColumnComponent.builder()
                .name(columnComponentDTO.getName())
                .columnType(columnComponentDTO.getColumnType())
                .id(columnComponentDTO.getId())
                .format(columnComponentDTO.getFormat())
                .build();

    }

    public static ColumnComponentDTO mapColumnComponentToDTO(ColumnComponent columnComponent) {
        ColumnComponentDTO dto = new ColumnComponentDTO();
        dto.setColumnType(columnComponent.getColumnType());
        dto.setFormat(columnComponent.getFormat());
        dto.setId(columnComponent.getId());
        dto.setName(columnComponent.getName());
        return dto;
    }
}
