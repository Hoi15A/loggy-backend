package ch.zhaw.pm4.loganalyser.log;

import ch.zhaw.pm4.loganalyser.controller.PathController;
import ch.zhaw.pm4.loganalyser.model.dto.FileTreeDTO;
import ch.zhaw.pm4.loganalyser.service.PathService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

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

        Mockito.when(pathService.getRootFolder()).thenReturn(fileTreeDTOS);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/path"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id",
                                                              Matchers.contains(
                                                                      fileTreeDTOS.get(0).getId(),
                                                                      fileTreeDTOS.get(1).getId(),
                                                                      fileTreeDTOS.get(2).getId()
                                                              )))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].name",
                                                              Matchers.contains(
                                                                      fileTreeDTOS.get(0).getName(),
                                                                      fileTreeDTOS.get(1).getName(),
                                                                      fileTreeDTOS.get(2).getName()
                                                              )))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].fullpath",
                                                              Matchers.contains(
                                                                      fileTreeDTOS.get(0).getFullpath(),
                                                                      fileTreeDTOS.get(1).getFullpath(),
                                                                      fileTreeDTOS.get(2).getFullpath()
                                                              )))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].children",
                                                              Matchers.contains(
                                                                      fileTreeDTOS.get(0).getChildren(),
                                                                      fileTreeDTOS.get(1).getChildren(),
                                                                      fileTreeDTOS.get(2).getChildren()
                                                              )))
            ;

        } catch (Exception e) {
            Assertions.fail(e);
        }

        Mockito.verify(pathService, Mockito.times(1)).getRootFolder();
    }
}
