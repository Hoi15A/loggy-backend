package ch.zhaw.pm4.loganalyser.model.log.column;

import lombok.Getter;

/**
 * Enumeration of the column types.
 */
@Getter
public enum ColumnType {

    DATE(FilterType.RANGE, FilterType.EXACT),
    ENUM(FilterType.EXACT),
    TEXT(FilterType.REGEX, FilterType.CONTAINS),
    INTEGER(FilterType.RANGE, FilterType.EXACT),
    DOUBLE(FilterType.RANGE, FilterType.EXACT),
    IP(FilterType.RANGE, FilterType.EXACT);

    private final FilterType[] filterTypes;

    ColumnType(FilterType... filterTypes) {
        this.filterTypes = filterTypes;
    }

}
