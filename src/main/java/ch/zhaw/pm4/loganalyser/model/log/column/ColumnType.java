package ch.zhaw.pm4.loganalyser.model.log.column;

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

    public FilterType[] getFilterTypes() {
        return filterTypes;
    }
}

enum FilterType {
    RANGE,
    REGEX,
    CONTAINS,
    EXACT
}
