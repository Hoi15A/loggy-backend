package ch.zhaw.pm4.loganalyser.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
@Data
public class PathService {
    private Function<File[], String[]> mapFileArrayToStringArray = files -> {
        List<String> strings = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory())
                strings.add(file.toString());
        }
        return strings.toArray(String[]::new);
    };

    public String[][] getContentOfFolder(String folder) {
        return new String[][] {Arrays.stream(Objects.requireNonNull(Path.of(folder).toFile().listFiles()))
                .filter(File::isDirectory)
                .map(File::toString)
                .toArray(String[]::new)};
    }

    public String[][] getRootFolder() {
        return Arrays.stream(File.listRoots())
                .filter(File::isDirectory)
                .map(File::listFiles)
                .filter(Objects::nonNull)
                .map(mapFileArrayToStringArray)
                .toArray(String[][]::new);
    }
}
