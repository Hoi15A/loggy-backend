package ch.zhaw.pm4.loganalyser.log;

import ch.zhaw.pm4.loganalyser.controller.PathController;
import ch.zhaw.pm4.loganalyser.model.dto.FileTreeDTO;
import ch.zhaw.pm4.loganalyser.service.PathService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PathControllerTest {

    @MockBean
    PathService pathService;
    MockMvc mockMvc;
    PathController pathController;

    @Autowired
    public PathControllerTest(MockMvc mockMvc, PathController pathController) {
        this.mockMvc = mockMvc;
        this.pathController = pathController;
    }

    @Test
    void testGetRootFolderContent() {
        int i = 0;
        List<FileTreeDTO> fileTreeDTOS = new ArrayList<>();
        fileTreeDTOS.add(new FileTreeDTO(++i, "var", "/var", new ArrayList<>()));
        fileTreeDTOS.add(new FileTreeDTO(++i, "home", "/home", new ArrayList<>()));
        fileTreeDTOS.add(new FileTreeDTO(++i, "etc", "/etc", new ArrayList<>()));

        when(pathService.getRootFolder()).thenReturn(fileTreeDTOS);

        try {
            performAndCheck("/path", fileTreeDTOS);
        } catch (Exception e) {
            fail(e);
        }

        verify(pathService, times(1)).getRootFolder();
    }

    @Test
    void testGetSubFolderContent() {
        String rootFolder = "/var";
        int i = 4;
        List<FileTreeDTO> fileTreeDTOS = new ArrayList<>();
        fileTreeDTOS.add(new FileTreeDTO(++i, "log", rootFolder + "/log", new ArrayList<>()));
        fileTreeDTOS.add(new FileTreeDTO(++i, "www", rootFolder + "/www", new ArrayList<>()));
        fileTreeDTOS.add(new FileTreeDTO(++i, "xyz", rootFolder + "/xyz", new ArrayList<>()));
        
        when(pathService.getContentOfFolder(anyString())).thenReturn(fileTreeDTOS);

        try {
            performAndCheck("/path?folder=" + rootFolder, fileTreeDTOS);
        } catch (Exception e) {
            fail(e);
        }
        
        verify(pathService, times(1)).getContentOfFolder(rootFolder);
    }

    private void performAndCheck(String apiRequest, List<FileTreeDTO> fileTreeDTOS) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(apiRequest))
                .andDo(MockMvcResultHandlers.print())
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
                                    )));
    }

}
