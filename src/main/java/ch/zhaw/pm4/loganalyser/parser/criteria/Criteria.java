package ch.zhaw.pm4.loganalyser.parser.criteria;

import java.util.List;

public interface Criteria {
    public List<String[]> meetCriteria(List<String[]> rows);
}
