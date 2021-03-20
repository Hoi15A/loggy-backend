package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class LogConfigDTO {
    private String name;
    private int columnCount;
    private int headerLength;
    private String separator;
}
