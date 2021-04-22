package ch.zhaw.pm4.loganalyser.parser.criteria;

import java.util.List;

public class AndCriteria implements Criteria {

    private final Criteria criteria;
    private final Criteria otherCriteria;

    public AndCriteria(Criteria criteria, Criteria otherCriteria) {
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
    }

    @Override
    public List<String[]> meetCriteria(List<String[]> rows, int key) {

        List<String[]> firstCriteriaRows = criteria.meetCriteria(rows, key);
        return otherCriteria.meetCriteria(firstCriteriaRows, key);
    }
}
