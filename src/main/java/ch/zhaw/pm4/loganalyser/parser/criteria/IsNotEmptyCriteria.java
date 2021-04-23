package ch.zhaw.pm4.loganalyser.parser.criteria;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class IsNotEmptyCriteria implements Criteria {

    private final Criteria criteria;

    @Override
    public List<String[]> meetCriteria(List<String[]> rows, int key) {
        List<String[]> criteriaItems = criteria.meetCriteria(rows, key);

        if(criteriaItems.isEmpty()) {
          //TODO
        }
        
        return criteriaItems;
    }
}
