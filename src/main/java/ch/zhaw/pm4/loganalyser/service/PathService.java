package ch.zhaw.pm4.loganalyser.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
@Data
public class PathService {
    public File[] getContentOfFolder(String folder) {
        return Path.of(folder).toFile().listFiles();
    }

    public String getRootFolder() {
        return System.getenv("SystemDrive");
    }
}
