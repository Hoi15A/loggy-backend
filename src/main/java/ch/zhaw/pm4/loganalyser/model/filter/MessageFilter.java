package ch.zhaw.pm4.loganalyser.model.filter;

import lombok.Data;
import lombok.NonNull;


@Data
public class MessageFilter implements Filter {

    @NonNull private String message;

    @Override
    public boolean apply(String valueToFilter) {
        return valueToFilter.contains(message);
    }
}
