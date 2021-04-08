package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileTreeDTO {
    @Min(1)
    private int id;
    @NotBlank
    private String name;
    private String fullpath;
    private List<FileTreeDTO> children;
}
