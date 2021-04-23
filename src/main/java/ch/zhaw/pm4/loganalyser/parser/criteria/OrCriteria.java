package ch.zhaw.pm4.loganalyser.parser.criteria;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class OrCriteria implements Criteria {
    
    private final Criteria criteria;
    private final Criteria otherCriteria;

    @Override
    public List<String[]> meetCriteria(List<String[]> rows, int key) {
        List<String[]> firstCriteriaItems = criteria.meetCriteria(rows, key);
        List<String[]> otherCriteriaItems = otherCriteria.meetCriteria(rows, key);

        for (String[] row: otherCriteriaItems) {
            if(!firstCriteriaItems.contains(row)){
                firstCriteriaItems.add(row);
            }
        }
        return firstCriteriaItems;
    }
}
