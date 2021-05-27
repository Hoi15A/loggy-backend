package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

/**
 * Data Transfer Object for the log files location.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data @Builder
public class FileTreeDTO {

    // NOTE: with one more data field change this code to use a builder

    private int id;
    private String name;
    private String fullpath;

    @Singular
    private List<FileTreeDTO> children;

}
