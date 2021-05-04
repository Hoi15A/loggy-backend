package ch.zhaw.pm4.loganalyser.parser.criteria;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Criteria to filter when the column is exactly the same as the given string
 */
@RequiredArgsConstructor
public class ExactCriteria implements Criteria {
    private final String exact;

    @Override
    public List<String[]> apply(List<String[]> rows, int columnIndex) {
        return rows.stream().filter(row -> row[columnIndex].equals(exact)).collect(Collectors.toList());
    }
}
