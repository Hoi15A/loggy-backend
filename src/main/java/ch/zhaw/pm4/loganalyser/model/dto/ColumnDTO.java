package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.Date;

/**
 * Data Transfer Object for the logs presented as a table column.
 */
@Data
public class ColumnDTO {

    @NonNull private Date date;
    @NonNull private String type;
    @NonNull private String message;

}
