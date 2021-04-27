package ch.zhaw.pm4.loganalyser.model.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class QueryComponent {
    private int id;
    private String from;
    private String to;
    private String regex;
    private String exact;
    private String contains;
}
