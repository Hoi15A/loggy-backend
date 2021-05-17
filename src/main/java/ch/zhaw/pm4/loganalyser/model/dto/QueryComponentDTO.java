package ch.zhaw.pm4.loganalyser.model.dto;

import ch.zhaw.pm4.loganalyser.model.log.column.FilterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data @Builder
public class QueryComponentDTO {
    @NotNull
    private long columnComponentId;
    private FilterType filterType;
    private String from;
    private String to;
    private String dateFormat;
    private String regex;
    private String exact;
    private String contains;
}
