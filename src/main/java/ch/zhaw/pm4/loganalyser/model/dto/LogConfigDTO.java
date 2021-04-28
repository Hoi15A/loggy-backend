package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Data Transfer Object for a {@link ch.zhaw.pm4.loganalyser.model.log.LogConfig}.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class LogConfigDTO {

    public static final String NAME_VALIDATION_MESSAGE = "Name cannot have only whitespaces or be empty";
    public static final String COLUMN_COUNT_VALIDATION_MESSAGE = "Column count cannot be a negativ number";
    public static final String HEADER_LENGTH_VALIDATION_MESSAGE = "Header length cannot be a negativ number";
    public static final String SEPARATOR_VALIDATION_MESSAGE = "Separator cannot be empty";
    public static final String COLUMN_COMPONENTS_VALIDATION_MESSAGE = "Column components is not set";

    @NotBlank(message = NAME_VALIDATION_MESSAGE)
    private String name;

    @Min(value = 0, message = COLUMN_COUNT_VALIDATION_MESSAGE)
    private int columnCount;

    @Min(value = 0, message = HEADER_LENGTH_VALIDATION_MESSAGE)
    private int headerLength;

    @NotEmpty(message = SEPARATOR_VALIDATION_MESSAGE)
    private String separator;

    @NotNull(message = COLUMN_COMPONENTS_VALIDATION_MESSAGE)
    private Map<Integer, ColumnComponentDTO> columnComponents;

}
