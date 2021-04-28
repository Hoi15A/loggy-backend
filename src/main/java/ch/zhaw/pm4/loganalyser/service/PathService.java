package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.PathNotFoundException;
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

/**
 * Provides information about the filesystem.
 */
@Service
@Data
public class PathService {

    private int fileIdCounter;

    private Function<File, FileTreeDTO> mapFileToFileTreeDTO = file ->
            new FileTreeDTO(++fileIdCounter, file.getName(), file.getPath(), new ArrayList<>());

    private FileSystem fileSystem = FileSystems.getDefault();

    /**
     * Returns a list of all root folders on the filesystem.
     * @return List<FileTreeDTO>
     */
    public List<FileTreeDTO> getRootFolder() {
        fileIdCounter = 0;
        return StreamSupport.stream(fileSystem.getRootDirectories().spliterator(), false)
                .filter(Objects::nonNull)
                .map(file -> getContentOfFolder(file.toString()))
                .collect(ArrayList::new, List::addAll, List::addAll);
    }

    /**
     * Returns a list of all sub folders (not recursive) from a provided directory.
     * @param folder to list its sub folders
     * @return List<FileTreeDTO>
     * @throws PathNotFoundException when the directory does not exist.
     */
    public List<FileTreeDTO> getContentOfFolder(String folder) {
        File folderFile = fileSystem.getPath(folder).toFile();
        if (!folderFile.exists()) throw new PathNotFoundException("The provided path (" + folder + ") does not exist");

        return Arrays.stream(Objects.requireNonNull(folderFile.listFiles()))
                .filter(File::isDirectory)
                .map(mapFileToFileTreeDTO)
                .collect(Collectors.toList());
    }

}
