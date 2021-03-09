package ch.zhaw.pm4.loganalyser.model.log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.net.URI;
import java.nio.file.Path;

@Getter
@Setter
@RequiredArgsConstructor
public class LogService {

    private final LogConfig logConfig;
    private final Path logDirectory;
    private final String name;
    private final String description;
    private final URI image;
    private final LogServiceLocation logServiceLocation;

}
