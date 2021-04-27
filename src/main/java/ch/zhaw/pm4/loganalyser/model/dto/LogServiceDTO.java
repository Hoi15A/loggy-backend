package ch.zhaw.pm4.loganalyser.model.dto;

import ch.zhaw.pm4.loganalyser.model.log.LogServiceLocation;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;

@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode
@ToString
public class LogServiceDTO {

    public static final String LOG_DIRECTORY_VALIDATION_MESSAGE = "The log directory path cannot have only whitespaces or be empty";
    public static final String NAME_VALIDATION_MESSAGE = "Name cannot have only whitespaces or be empty";
    public static final String LOG_CONFIG_VALIDATION_MESSAGE = "The name of the log configuration cannot have only whitespaces or be empty";
    public static final String LOCATION_VALIDATION_MESSAGE = "Location of the log service has not been set";

    private long id;

    @NotBlank(message = LOG_DIRECTORY_VALIDATION_MESSAGE)
    private String logDirectory;

    @NotBlank(message = NAME_VALIDATION_MESSAGE)
    private String name;

    private String description;

    private URI image;

    @NotNull(message = LOCATION_VALIDATION_MESSAGE)
    private LogServiceLocation location;

    @NotBlank(message = LOG_CONFIG_VALIDATION_MESSAGE)
    private String logConfig;

}
