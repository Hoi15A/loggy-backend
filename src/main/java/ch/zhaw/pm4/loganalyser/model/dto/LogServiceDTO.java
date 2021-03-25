package ch.zhaw.pm4.loganalyser.model.dto;

import ch.zhaw.pm4.loganalyser.model.log.LogServiceLocation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;

@NoArgsConstructor
@Getter @Setter
public class LogServiceDTO {
    private long id;
    @NotBlank
    private String logDirectory;
    @NotBlank
    private String name;
    private String description;
    private URI image;
    @NotNull
    private LogServiceLocation location;
    @NotNull
    private LogConfigDTO logConfigDTO;
}
