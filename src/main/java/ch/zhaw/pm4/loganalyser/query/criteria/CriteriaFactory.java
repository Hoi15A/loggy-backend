package ch.zhaw.pm4.loganalyser.query.criteria;

import ch.zhaw.pm4.loganalyser.exception.InvalidInputException;
import ch.zhaw.pm4.loganalyser.model.log.QueryComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.model.log.column.FilterType;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

            case RANGE: criteria = createRangeCriteria(queryComponent);
                break;

            case CONTAINS: criteria = new ContainsCriteria(queryComponent.getContains());
                break;

            default:
                criteria = null;
                break;
        }

        return criteria;
    }

    private Criteria createRangeCriteria(QueryComponent qc) {
        var columnComponent = qc.getColumnComponent();
        if (columnComponent.getDateFormat() != null && columnComponent.getDateFormat().isBlank())
            throw new InvalidInputException("To query a date range a date format must be set on the column component!");

        RangeCriteria criteria;

        if(columnComponent.getColumnType() == ColumnType.DATE) {
            var dtfRequest = DateTimeFormatter.ofPattern(qc.getDateFormat());
            var dtfLogService = DateTimeFormatter.ofPattern(columnComponent.getDateFormat());

            var fromDate = LocalDate.parse(qc.getFrom(), dtfRequest).atStartOfDay();
            var toDate = LocalDate.parse(qc.getTo(), dtfRequest).plusDays(1).atStartOfDay();
            criteria = new RangeCriteria(dtfLogService.format(fromDate), dtfLogService.format(toDate));
            criteria.setDateFormat(columnComponent.getDateFormat());
        } else {
            criteria = new RangeCriteria(qc.getFrom(), qc.getTo());
        }

        criteria.setType(columnComponent.getColumnType());
        return criteria;
    }

}
