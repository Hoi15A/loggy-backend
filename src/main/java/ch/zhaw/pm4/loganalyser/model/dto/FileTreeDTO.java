package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileTreeDTO {
    private int id;
    private String name;
    private String fullpath;
    private List<FileTreeDTO> children;
}
