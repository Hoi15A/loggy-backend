package ch.zhaw.pm4.loganalyser.model.filter;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.List;

@UtilityClass
public class FilterFactory {

    public Filter getFilter(String type, List<Object> args) {
        Filter filter = null;

        switch (type) {
            case "msg":
                filter = createMessageFilter(args);
                break;

            case "date":
                filter = createDateFilter(args);
                break;

            default:
                break;
        }

        return filter;
    }

    private Filter createMessageFilter(List<Object> args) {
        Objects.requireNonNull(args);
        if (args.size() != 1) throw new IllegalArgumentException("It has to have exactly 1 argument");

        return new MessageFilter((String) args.get(0));
    }

    private Filter createDateFilter(List<Object> args) {
        Objects.requireNonNull(args);
        int size = args.size();
        if (size < 2 || size > 3) throw new IllegalArgumentException("It has to have exactly 2 or 3 arguments");

        LocalDate begin = (LocalDate) args.get(0);
        LocalDate end = (LocalDate) args.get(1);
        DateTimeFormatter formatter = (size == 2)
                ? DateTimeFormatter.ISO_LOCAL_DATE
                : (DateTimeFormatter) args.get(2);

        return new DateFilter(begin, end, formatter);
    }

}
