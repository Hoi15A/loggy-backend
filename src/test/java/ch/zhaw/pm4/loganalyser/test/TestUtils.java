package ch.zhaw.pm4.loganalyser.test;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestUtils {

    private TestUtils() {}

    public static String loadResourceContent(String relativeResourceLocation) {
        try {
            File columnFile = ResourceUtils.getFile("classpath:testfiles/" + relativeResourceLocation);
            return new String(Files.readAllBytes(columnFile.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
