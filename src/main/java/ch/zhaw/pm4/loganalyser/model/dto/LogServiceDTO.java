package ch.zhaw.pm4.loganalyser.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

@NoArgsConstructor
@Getter @Setter
public class LogServiceDTO {
    private String logDirectory;
    private String name;
    private String description;
    private URI image;
    private LogConfigDTO logConfigDTO;
}
