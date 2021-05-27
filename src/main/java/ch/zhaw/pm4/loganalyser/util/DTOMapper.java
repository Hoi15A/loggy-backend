package ch.zhaw.pm4.loganalyser.util;

import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.model.dto.QueryComponentDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.QueryComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility to map model and data transfer objects.
 */
@UtilityClass
public class DTOMapper {

    /**
     * Map {@link LogServiceDTO} to a {@link LogService}
     * @param logServiceDTO to be mapped.
     * @return {@link LogService}
     */
    public LogService mapDTOToLogService(LogServiceDTO logServiceDTO) {
        return LogService.builder()
                .id(logServiceDTO.getId())
                .name(logServiceDTO.getName())
                .description(logServiceDTO.getDescription())
                .logDirectory(logServiceDTO.getLogDirectory())
                .image(logServiceDTO.getImage())
                .logServiceLocation(logServiceDTO.getLocation())
                .build();
    }

    /**
     * Map {@link LogService} to a {@link LogServiceDTO}
     * @param logService to be mapped.
     * @return {@link LogServiceDTO}
     */
    public LogServiceDTO mapLogServiceToDTO(LogService logService) {
        return LogServiceDTO.builder()
                .id(logService.getId())
                .name(logService.getName())
                .description(logService.getDescription())
                .image(logService.getImage())
                .logDirectory(logService.getLogDirectory())
                .location(logService.getLogServiceLocation())
                .logConfig(logService.getLogConfig().getName())
                .build();
    }

    /**
     * Map {@link LogConfigDTO} to a {@link LogConfig}
     * @param logConfigDTO to be mapped.
     * @return {@link LogConfig}
     */
    public LogConfig mapDTOToLogConfig(LogConfigDTO logConfigDTO) {
        return LogConfig.builder()
                .name(logConfigDTO.getName())
                .columnCount(logConfigDTO.getColumnCount())
                .headerLength(logConfigDTO.getHeaderLength())
                .separator(logConfigDTO.getSeparator())
                .columnComponents(mapColumnComponentsDtoMapToColumnComponentsMap(logConfigDTO.getColumnComponents()))
                .build();
    }

    private Map<Integer, ColumnComponent> mapColumnComponentsDtoMapToColumnComponentsMap(Map<Integer, ColumnComponentDTO> dtoMap) {
        return dtoMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> mapDTOToColumnComponent(entry.getValue())));
    }

    /**
     * Map {@link LogConfig} to a {@link LogConfigDTO}
     * @param logConfig to be mapped.
     * @return {@link LogConfigDTO}
     */
    public LogConfigDTO mapLogConfigToDTO(LogConfig logConfig) {
        return LogConfigDTO.builder()
                .name(logConfig.getName())
                .columnCount(logConfig.getColumnCount())
                .headerLength(logConfig.getHeaderLength())
                .separator(logConfig.getSeparator())
                .columnComponents(mapColumnComponentsMapToDtoMap(logConfig.getColumnComponents()))
                .build();
    }

    private Map<Integer, ColumnComponentDTO> mapColumnComponentsMapToDtoMap(Map<Integer, ColumnComponent> columnComponentMap) {
        return columnComponentMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> mapColumnComponentToDTO(entry.getValue())));
    }

    /**
     * Map {@link ColumnComponentDTO} to a {@link ColumnComponent}
     * @param columnComponentDTO to be mapped.
     * @return {@link ColumnComponent}
     */
    public ColumnComponent mapDTOToColumnComponent(ColumnComponentDTO columnComponentDTO) {
        return ColumnComponent.builder()
                .name(columnComponentDTO.getName())
                .dateFormat(columnComponentDTO.getDateFormat())
                .columnType(columnComponentDTO.getColumnType())
                .id(columnComponentDTO.getId())
                .format(columnComponentDTO.getFormat())
                .build();
    }

    /**
     * Map {@link ColumnComponent} to a {@link ColumnComponentDTO}
     * @param columnComponent to be mapped.
     * @return {@link ColumnComponentDTO}
     */
    public ColumnComponentDTO mapColumnComponentToDTO(ColumnComponent columnComponent) {
        return ColumnComponentDTO.builder()
                .columnType(columnComponent.getColumnType())
                .dateFormat(columnComponent.getDateFormat())
                .format(columnComponent.getFormat())
                .filterTypes(columnComponent.getColumnType().getFilterTypes())
                .id(columnComponent.getId())
                .name(columnComponent.getName())
                .build();
    }

    /**
     * Map {@link QueryComponentDTO} to a {@link QueryComponent}
     * @param queryComponentDTO to be mapped.
     * @return {@link QueryComponent}
     */
    public QueryComponent mapDTOToQueryComponent(QueryComponentDTO queryComponentDTO) {
        return QueryComponent.builder()
                .columnComponentId(queryComponentDTO.getColumnComponentId())
                .filterType(queryComponentDTO.getFilterType())
                .dateFormat(queryComponentDTO.getDateFormat())
                .from(queryComponentDTO.getFrom())
                .to(queryComponentDTO.getTo())
                .regex(queryComponentDTO.getRegex())
                .exact(queryComponentDTO.getExact())
                .contains(queryComponentDTO.getContains())
                .build();
    }

}
