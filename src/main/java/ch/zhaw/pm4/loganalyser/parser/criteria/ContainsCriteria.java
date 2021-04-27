package ch.zhaw.pm4.loganalyser.parser.criteria;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ContainsCriteria implements Criteria {
    private final String contains;

    @Override
    public List<String[]> apply(List<String[]> rows, int key) {
        return rows.stream().filter(row -> row[key].contains(contains)).collect(Collectors.toList());
    }
}
