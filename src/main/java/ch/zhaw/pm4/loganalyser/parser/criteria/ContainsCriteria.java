package ch.zhaw.pm4.loganalyser.parser.criteria;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ContainsCriteria implements Criteria {
    private final String contains;

    @Override
    public List<String[]> apply(List<String[]> rows, int columnIndex) {
        return rows.stream().filter(row -> row[columnIndex].contains(contains)).collect(Collectors.toList());
    }
}
