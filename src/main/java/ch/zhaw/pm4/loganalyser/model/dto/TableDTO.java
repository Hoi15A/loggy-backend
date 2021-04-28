package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * Data Transfer Object for the logs presented as a table.
 */
@Data
public class TableDTO {

    @NonNull private List<HeaderDTO> headers;
    @NonNull private List<ColumnDTO> tableData;

}
