package ch.zhaw.pm4.loganalyser.model.dto;

import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnComponentDTO {

    public static final String COLUMN_TYPE_VALIDATION_MESSAGE = "Der Spaltentyp wurde nicht gesetzt";
    public static final String FORMAT_VALIDATION_MESSAGE = "Das Format wurde nicht gesetzt";
    public static final String NAME_VALIDATION_MESSAGE = "Der Name darf nicht leer sein";

    private long id;

    @NotNull(message = COLUMN_TYPE_VALIDATION_MESSAGE)
    private ColumnType columnType;

    @NotNull(message = FORMAT_VALIDATION_MESSAGE)
    private String format;

    @NotBlank(message = NAME_VALIDATION_MESSAGE)
    private String name;

}
