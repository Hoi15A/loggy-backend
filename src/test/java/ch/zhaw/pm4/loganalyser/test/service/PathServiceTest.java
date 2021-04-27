package ch.zhaw.pm4.loganalyser.test.service;

import ch.zhaw.pm4.loganalyser.model.dto.FileTreeDTO;
import ch.zhaw.pm4.loganalyser.service.PathService;
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

import static org.junit.jupiter.api.Assertions.*;
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

        setUpFolderMock(varFolderMock, "var", "/var");
        setUpFolderMock(homeFolderMock, "home", "/home");
        setUpFolderMock(etcFolderMock, "etc", "/etc");

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

    private void setUpFolderMock(File fileMock, String name, String fullPath) {
        when(fileMock.getName()).thenReturn(name);
        when(fileMock.getPath()).thenReturn(fullPath);
        when(fileMock.isDirectory()).thenReturn(true);
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
        when(fsMock.getRootDirectories()).thenReturn(List.of(rootFolderPathMock));
        when(fsMock.getPath(any())).thenReturn(rootFolderPathMock);
        when(rootFolderPathMock.toFile()).thenReturn(rootFolderMock);
        when(rootFolderMock.exists()).thenReturn(true);

        assertIterableEquals(expected, pathService.getRootFolder());

        // verify
        verify(fsMock, times(1)).getRootDirectories();
        verify(fsMock, times(1)).getPath(any());
        verify(rootFolderPathMock, times(1)).toFile();
        verify(rootFolderMock, times(1)).exists();
    }

    @Test
    void testSubFolderContents() {
        // prepare
        when(fsMock.getPath(any())).thenReturn(rootFolderPathMock);
        when(rootFolderPathMock.toFile()).thenReturn(rootFolderMock);
        when(rootFolderMock.exists()).thenReturn(true);

        // verify
        assertIterableEquals(expected, pathService.getContentOfFolder(""));

        verify(fsMock, times(1)).getPath(any());
        verify(rootFolderPathMock, times(1)).toFile();
        verify(rootFolderMock, times(1)).exists();
    }

}
