package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryComponentDTO {
    @NotNull
    private int id;
    private String from;
    private String to;
    private String regex;
    private String exact;
    private String contains;
}
