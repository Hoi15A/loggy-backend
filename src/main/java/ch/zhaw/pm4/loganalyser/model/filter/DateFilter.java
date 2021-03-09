package ch.zhaw.pm4.loganalyser.model.filter;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class DateFilter implements Filter {

    @NonNull private LocalDate begin;
    @NonNull private LocalDate end;
    @NonNull private DateTimeFormatter dateTimeFormatter;

    @Override
    public boolean apply(String valueToFilter) {
        // TODO: catch DateTimeParseException somewhere
        LocalDate date = (dateTimeFormatter.equals(DateTimeFormatter.ISO_LOCAL_DATE)) // default yyyy-MM-dd
                ? LocalDate.parse(valueToFilter)
                : LocalDate.parse(valueToFilter, dateTimeFormatter);
        boolean isOutsideRange = begin.isAfter(date) || end.isBefore(date);
        return !isOutsideRange;
    }

}
