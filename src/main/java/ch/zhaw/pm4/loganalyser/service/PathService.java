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

    public File[] getRootFolder() {
        String rootFolder = "/";
        if (System.getProperty("os.name").toUpperCase().startsWith("WIN")) {
            rootFolder = "C:";
        }
        return Path.of(rootFolder).toFile().listFiles();
    }
}
