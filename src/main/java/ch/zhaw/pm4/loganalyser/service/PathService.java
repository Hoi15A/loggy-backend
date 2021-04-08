package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.model.dto.FileTreeDTO;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Data
public class PathService {
    private int fileIdCounter;

    private Function<File, FileTreeDTO> mapFileToFileTreeDTO = file ->
            new FileTreeDTO(++fileIdCounter, file.getName(), file.getPath(), new ArrayList<>());

    public List<FileTreeDTO> getContentOfFolder(String folder) {
        return getContentOfFolder(folder, FileSystems.getDefault());
    }

    public List<FileTreeDTO> getContentOfFolder(String folder, FileSystem fs) {
        return Arrays.stream(Objects.requireNonNull(fs.getPath(folder).toFile().listFiles()))
                .filter(File::isDirectory)
                .map(mapFileToFileTreeDTO)
                .collect(Collectors.toList());
    }

    public List<FileTreeDTO> getRootFolder() {
        return getRootFolder(FileSystems.getDefault());
    }

    public List<FileTreeDTO> getRootFolder(FileSystem fs) {
        fileIdCounter = 0;
        return StreamSupport.stream(fs.getRootDirectories().spliterator(), false)
                .filter(Objects::nonNull)
                .map(file -> getContentOfFolder(file.toString(), fs))
                .collect(ArrayList::new, List::addAll, List::addAll);

    }

}
