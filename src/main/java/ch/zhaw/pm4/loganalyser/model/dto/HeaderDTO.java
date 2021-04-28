package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.Data;
import lombok.NonNull;

/**
 * Data Transfer Object for the logs presented as a table header.
 */
@Data
public class HeaderDTO {

    @NonNull private String text;
    @NonNull private String value;

}
