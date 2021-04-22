package ch.zhaw.pm4.loganalyser.log;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class ControllerTest {

    String loadResourceContent(String resourceLocation) {
        try {
            File columnFile = ResourceUtils.getFile(resourceLocation);
            return new String(Files.readAllBytes(columnFile.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
