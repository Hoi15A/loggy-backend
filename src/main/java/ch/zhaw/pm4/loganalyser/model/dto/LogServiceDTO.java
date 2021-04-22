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

    public static final String LOG_DIRECTORY_VALIDATION_MESSAGE = "Der Pfad zur Logdatei darf nicht leer sein und auch nicht nur aus Leerzeichen bestehen";
    public static final String NAME_VALIDATION_MESSAGE = "Der Name des Log-Service darf nicht leer sein und auch nicht nur aus Leerzeichen bestehen";
    public static final String LOG_CONFIG_VALIDATION_MESSAGE = "Der Name der Log-Konfiguration darf nicht leer sein und auch nicht nur aus Leerzeichen bestehen";
    public static final String LOCATION_VALIDATION_MESSAGE = "Der Standort wurde nicht gesetzt";

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
