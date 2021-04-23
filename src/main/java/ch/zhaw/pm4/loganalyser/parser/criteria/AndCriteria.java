package ch.zhaw.pm4.loganalyser.parser.criteria;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class AndCriteria implements Criteria {

    private final Criteria criteria;
    private final Criteria otherCriteria;

    @Override
    public List<String[]> meetCriteria(List<String[]> rows, int key) {

        List<String[]> firstCriteriaRows = criteria.meetCriteria(rows, key);
        return otherCriteria.meetCriteria(firstCriteriaRows, key);
    }
}
