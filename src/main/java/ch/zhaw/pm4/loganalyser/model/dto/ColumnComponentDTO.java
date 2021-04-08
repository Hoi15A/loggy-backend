package ch.zhaw.pm4.loganalyser.model.dto;

import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnComponentDTO {
    private long id;
    private ColumnType columnType;
    private String format;
    private String name;
}
