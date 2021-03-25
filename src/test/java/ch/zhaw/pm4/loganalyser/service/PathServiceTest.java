package ch.zhaw.pm4.loganalyser.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
class PathServiceTest {
    @TempDir
    Path testGetContentOfFolderDir;

    @Autowired
    PathService pathService;

    @Test
    void testGetContentOfFolder() {
        String[] testFolders = {"folder1", "folder2", "folder3"};
        Path basePath;
        try {
            basePath = Path.of(testGetContentOfFolderDir.toString(), "folder", "subfolder");
            Files.createDirectories(basePath);
            for (String testFolder : testFolders) {
                Files.createDirectory(Path.of(basePath.toString(), testFolder));
            }

            File[] folderContent = pathService.getContentOfFolder(basePath.toString());
            for (int i = 0; i < folderContent.length && i < testFolders.length; i++) {
                Assertions.assertEquals(Path.of(testGetContentOfFolderDir.toString(),
                        "folder", "subfolder", testFolders[i]).toFile(), folderContent[i]);
            }
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }
}
