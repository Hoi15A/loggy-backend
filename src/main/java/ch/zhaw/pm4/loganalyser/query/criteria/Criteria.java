package ch.zhaw.pm4.loganalyser.query.criteria;

import java.util.List;

/**
 * Criteria interface based on filter Pattern to filter logs
 */
public interface Criteria {
    /**
     * Applies the criteria on a specific column of a list of rows
     *
     * @param rows List of rows that should be filtered on
     * @param columnIndex Index of the column on the row the filter should specifically apply to
     * @return Filtered List
     */
    List<String[]> apply(List<String[]> rows, int columnIndex);
}
