package ch.zhaw.pm4.loganalyser.query.criteria;

import ch.zhaw.pm4.loganalyser.exception.InvalidInputException;
import ch.zhaw.pm4.loganalyser.model.log.QueryComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.model.log.column.FilterType;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

            case EXACT: criteria = createExactCriteria(queryComponent);
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

    private Criteria createExactCriteria(QueryComponent qc) {
        ExactCriteria criteria;

        var columnComponent = qc.getColumnComponent();
        if (columnComponent.getColumnType() == ColumnType.DATE) {
            var defaultLocale = Locale.ENGLISH;
            var dtfRequest = DateTimeFormatter.ofPattern(qc.getDateFormat(), defaultLocale);
            var dtfLogService = DateTimeFormatter.ofPattern(columnComponent.getDateFormat(), defaultLocale);
            var exactDate = LocalDate.parse(qc.getExact(), dtfRequest).atStartOfDay().atOffset(ZoneOffset.UTC);

            criteria = new ExactCriteria(dtfLogService.format(exactDate));
            criteria.setDateFormat(columnComponent.getDateFormat());
            criteria.setType(ColumnType.DATE);
        } else {
            criteria = new ExactCriteria(qc.getExact());
        }

        return criteria;
    }

    private Criteria createRangeCriteria(QueryComponent qc) {
        var columnComponent = qc.getColumnComponent();
        if (columnComponent.getDateFormat() != null && columnComponent.getDateFormat().isBlank())
            throw new InvalidInputException("To query a date range a date format must be set on the column component!");

        if (qc.getFrom() == null && qc.getTo() == null)
            throw new InvalidInputException("Either the from or to value must be set.");

        RangeCriteria criteria;

        if(columnComponent.getColumnType() == ColumnType.DATE) {
            var defaultLocale = Locale.ENGLISH;
            var dtfRequest = DateTimeFormatter.ofPattern(qc.getDateFormat(), defaultLocale);
            var dtfLogService = DateTimeFormatter.ofPattern(columnComponent.getDateFormat(), defaultLocale);

            if(qc.getFrom() == null && qc.getTo() != null) {
                var toDate = LocalDate.parse(qc.getTo(), dtfRequest).atStartOfDay().plusDays(1).atOffset(ZoneOffset.UTC);
                criteria = new RangeCriteria(qc.getFrom(), dtfLogService.format(toDate));
            } else if(qc.getFrom() != null && qc.getTo() == null) {
                var fromDate = LocalDate.parse(qc.getFrom(), dtfRequest).atStartOfDay().atOffset(ZoneOffset.UTC);
                criteria = new RangeCriteria(dtfLogService.format(fromDate), qc.getTo());
            } else {
                var fromDate = LocalDate.parse(qc.getFrom(), dtfRequest).atStartOfDay().atOffset(ZoneOffset.UTC);
                var toDate = LocalDate.parse(qc.getTo(), dtfRequest).atStartOfDay().plusDays(1).atOffset(ZoneOffset.UTC);
                criteria = new RangeCriteria(dtfLogService.format(fromDate), dtfLogService.format(toDate));
            }

            criteria.setDateFormat(columnComponent.getDateFormat());
            criteria.setLocale(defaultLocale);
        } else {
            criteria = new RangeCriteria(qc.getFrom(), qc.getTo());
        }

        criteria.setType(columnComponent.getColumnType());
        return criteria;
    }

}
