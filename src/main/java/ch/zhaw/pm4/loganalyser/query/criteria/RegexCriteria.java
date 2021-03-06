package ch.zhaw.pm4.loganalyser.query.criteria;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Criteria to filter when the column matches the given regex string
 */
@RequiredArgsConstructor
public class RegexCriteria implements Criteria {

    private final String regex;

    @Override
    public List<String[]> apply(List<String[]> rows, int columnIndex) {
        return rows.stream()
                .filter(row -> row[columnIndex].matches(regex))
                .collect(Collectors.toList());
    }

}
