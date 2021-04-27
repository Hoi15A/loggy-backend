package ch.zhaw.pm4.loganalyser.parser.criteria;

import java.util.List;

public interface Criteria {
    List<String[]> apply(List<String[]> rows, int key);
}
