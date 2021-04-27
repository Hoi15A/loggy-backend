package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryComponentDTO {
    private int id;
    private String from;
    private String to;
    private String regex;
    private String exact;
    private String contains;
}
