package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for the logs presented as a table header.
 */
@Data
@AllArgsConstructor
public class HeaderDTO {

    String text;
    String value;

}
