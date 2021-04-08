package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.model.dto.FileTreeDTO;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class PathServiceTest {

    @Autowired
    PathService pathService;

    @Mock FileSystem fsMock;
    @Mock Path rootFolderPathMock;
    @Mock File rootFolderMock;
    @Mock File varFolderMock;
    @Mock File homeFolderMock;
    @Mock File etcFolderMock;

    List<FileTreeDTO> expected;
    File[] subFolders;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pathService.setFileSystem(fsMock);

        when(varFolderMock.getName()).thenReturn("var");
        when(varFolderMock.getPath()).thenReturn("/var");
        when(varFolderMock.isDirectory()).thenReturn(true);

        when(homeFolderMock.getName()).thenReturn("home");
        when(homeFolderMock.getPath()).thenReturn("/home");
        when(homeFolderMock.isDirectory()).thenReturn(true);

        when(etcFolderMock.getName()).thenReturn("etc");
        when(etcFolderMock.getPath()).thenReturn("/etc");
        when(etcFolderMock.isDirectory()).thenReturn(true);


        int i = 0;
        expected = new ArrayList<>();
        expected.add(new FileTreeDTO(++i, varFolderMock.getName(), varFolderMock.getPath(), new ArrayList<>()));
        expected.add(new FileTreeDTO(++i, etcFolderMock.getName(), etcFolderMock.getPath(), new ArrayList<>()));
        expected.add(new FileTreeDTO(++i, homeFolderMock.getName(), homeFolderMock.getPath(), new ArrayList<>()));

        subFolders = Arrays.array(varFolderMock, etcFolderMock, homeFolderMock);

        // mock fs.getPath(folder).toFile().listFiles()
        when(fsMock.getPath(anyString())).thenReturn(rootFolderPathMock);
        when(rootFolderPathMock.toFile()).thenReturn(rootFolderMock);
        when(rootFolderMock.listFiles()).thenReturn(subFolders);
    }

    @AfterEach
    void tearDown() {
        verify(fsMock, times(1)).getPath(anyString());
        verify(rootFolderPathMock, times(1)).toFile();
        verify(rootFolderMock, times(1)).listFiles();
        for (File subFolder: subFolders) {
            verify(subFolder, times(2)).getName();
            verify(subFolder, times(2)).getPath();
        }
    }

    @Test
    void testRootContents() {
        // prepare
        // mock fs.getRootDirectories() or fs.getRootDirectories().spliterator()
        when(fsMock.getRootDirectories()).thenReturn(List.of(rootFolderPathMock));

        Assertions.assertIterableEquals(expected, pathService.getRootFolder());

        // verify
        verify(fsMock, times(1)).getRootDirectories();
    }

    @Test
    void testSubFolderContents() {
        Assertions.assertIterableEquals(expected, pathService.getContentOfFolder(""));
    }

}
