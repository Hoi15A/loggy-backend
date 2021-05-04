package ch.zhaw.pm4.loganalyser.parser.criteria;

import ch.zhaw.pm4.loganalyser.model.log.QueryComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.FilterType;
import lombok.experimental.UtilityClass;

/**
 * Factory to create criteria based on {@link FilterType} and {@link QueryComponent}
 */
@UtilityClass
public class CriteriaFactory {

    public Criteria getCriteria(FilterType filterType, QueryComponent queryComponent) {
        Criteria criteria;

        switch (filterType) {
            case REGEX: criteria = new RegexCriteria(queryComponent.getRegex());
                break;

            case EXACT: criteria = new ExactCriteria(queryComponent.getExact());
                break;

            case RANGE: criteria = new RangeCriteria(queryComponent.getFrom(), queryComponent.getTo());
                break;

            case CONTAINS: criteria = new ContainsCriteria(queryComponent.getContains());
                break;

            default:
                criteria = null;
                break;
        }

        return criteria;
    }

}
