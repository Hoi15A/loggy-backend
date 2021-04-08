package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.model.dto.FileTreeDTO;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Data
public class PathService {
    private int fileIdCounter;

    private Function<File, FileTreeDTO> mapFileToFileTreeDTO = file ->
            new FileTreeDTO(++fileIdCounter, file.getName(), file.getPath(), new ArrayList<>());

    private Function<File[], List<FileTreeDTO>> mapFileArrayToFileTreeDtoList = files ->
         Arrays.stream(files)
                .filter(File::isDirectory)
                .map(mapFileToFileTreeDTO)
                .collect(Collectors.toList());

    public List<FileTreeDTO> getContentOfFolder(String folder) {
        return Arrays.stream(Objects.requireNonNull(Path.of(folder).toFile().listFiles()))
                .filter(File::isDirectory)
                .map(mapFileToFileTreeDTO)
                .collect(Collectors.toList());
    }
    public List<FileTreeDTO> getRootFolder() {
        fileIdCounter = 0;
        return Arrays.stream(File.listRoots())
                .map(File::listFiles)
                .filter(Objects::nonNull)
                .map(mapFileArrayToFileTreeDtoList)
                .collect(ArrayList::new, List::addAll, List::addAll);
    }
}
