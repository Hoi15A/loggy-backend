package ch.zhaw.pm4.loganalyser.test.controller;

import ch.zhaw.pm4.loganalyser.exception.ApiExceptionHandler;
import ch.zhaw.pm4.loganalyser.exception.PathNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.FileTreeDTO;
import ch.zhaw.pm4.loganalyser.service.PathService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PathControllerTest {

    @MockBean
    PathService pathService;

    @Autowired
    MockMvc mockMvc;

    /* ****************************************************************************************************************
     * POSITIV TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetRootFolderContent() {
        // prepare
        int i = 0;
        List<FileTreeDTO> fileTreeDTOS = new ArrayList<>();
        fileTreeDTOS.add(createFileTreeDTO(++i, "var", "/var"));
        fileTreeDTOS.add(createFileTreeDTO(++i, "home", "/home"));
        fileTreeDTOS.add(createFileTreeDTO(++i, "etc", "/etc"));

        when(pathService.getRootFolder()).thenReturn(fileTreeDTOS);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/path"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.[*].id", contains(
                            fileTreeDTOS.get(0).getId(),
                            fileTreeDTOS.get(1).getId(),
                            fileTreeDTOS.get(2).getId()
                    )))
                    .andExpect(jsonPath("$.[*].name", contains(
                            fileTreeDTOS.get(0).getName(),
                            fileTreeDTOS.get(1).getName(),
                            fileTreeDTOS.get(2).getName()
                    )))
                    .andExpect(jsonPath("$.[*].fullpath", contains(
                            fileTreeDTOS.get(0).getFullpath(),
                            fileTreeDTOS.get(1).getFullpath(),
                            fileTreeDTOS.get(2).getFullpath()
                    )))
                    .andExpect(jsonPath("$.[*].children", contains(
                            fileTreeDTOS.get(0).getChildren(),
                            fileTreeDTOS.get(1).getChildren(),
                            fileTreeDTOS.get(2).getChildren()
                    )))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(pathService, times(1)).getRootFolder();
    }

    @Test
    void testGetSubFolderContent() {
        // prepare
        String rootFolder = "/var";
        int i = 4;
        List<FileTreeDTO> fileTreeDTOS = new ArrayList<>();
        fileTreeDTOS.add(createFileTreeDTO(++i, "log", rootFolder + "/log"));
        fileTreeDTOS.add(createFileTreeDTO(++i, "www", rootFolder + "/www"));
        fileTreeDTOS.add(createFileTreeDTO(++i, "xyz", rootFolder + "/xyz"));
        
        when(pathService.getContentOfFolder(rootFolder)).thenReturn(fileTreeDTOS);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/path?folder=" + rootFolder))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.[*].id", contains(
                            fileTreeDTOS.get(0).getId(),
                            fileTreeDTOS.get(1).getId(),
                            fileTreeDTOS.get(2).getId()
                    )))
                    .andExpect(jsonPath("$.[*].name", contains(
                            fileTreeDTOS.get(0).getName(),
                            fileTreeDTOS.get(1).getName(),
                            fileTreeDTOS.get(2).getName()
                    )))
                    .andExpect(jsonPath("$.[*].fullpath", contains(
                            fileTreeDTOS.get(0).getFullpath(),
                            fileTreeDTOS.get(1).getFullpath(),
                            fileTreeDTOS.get(2).getFullpath()
                    )))
                    .andExpect(jsonPath("$.[*].children", contains(
                            fileTreeDTOS.get(0).getChildren(),
                            fileTreeDTOS.get(1).getChildren(),
                            fileTreeDTOS.get(2).getChildren()
                    )))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(pathService, times(1)).getContentOfFolder(rootFolder);
    }

    private FileTreeDTO createFileTreeDTO(int id, String name, String path) {
        return FileTreeDTO.builder()
                .id(id)
                .name(name)
                .fullpath(path)
                .children(new ArrayList<>())
                .build();
    }

    /* ****************************************************************************************************************
     * NEGATIV TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetSubFolderDoesNotExists() {
        // prepare
        String rootFolder = "/abc";
        String exceptionMessage = "Folder " + rootFolder + " does not exist";
        doThrow(new PathNotFoundException(exceptionMessage)).when(pathService).getContentOfFolder(rootFolder);

        // execute
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/path?folder=" + rootFolder))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$.message", is(ApiExceptionHandler.PATH_NOT_FOUND_MESSAGE)))
                    .andExpect(jsonPath("$.details.[*]").isNotEmpty())
                    .andExpect(jsonPath("$.details.[0]", is(exceptionMessage)))
                    .andDo(print());
        } catch (Exception e) {
            fail(e);
        }

        // verify
        verify(pathService, times(1)).getContentOfFolder(rootFolder);
    }

}
