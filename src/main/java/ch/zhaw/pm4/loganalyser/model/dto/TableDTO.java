package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TableDTO {
    private List<HeaderDTO> headers;
    private List<ColumnDTO> tableData;
}
