package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object for the logs presented as a table column.
 */
@Data
@AllArgsConstructor
public class ColumnDTO {

    private Date date;
    private String type;
    private String message;

}
