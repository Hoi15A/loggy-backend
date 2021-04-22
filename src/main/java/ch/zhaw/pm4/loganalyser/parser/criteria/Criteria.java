package ch.zhaw.pm4.loganalyser.parser.criteria;

import java.util.List;

public interface Criteria {
    List<String[]> meetCriteria(List<String[]> rows, int key);
}
