package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class LogConfigDTO {
    @NotBlank
    private String name;
    @Min(0)
    private int columnCount;
    @Min(0)
    private int headerLength;
    @NotEmpty
    private String separator;
}
