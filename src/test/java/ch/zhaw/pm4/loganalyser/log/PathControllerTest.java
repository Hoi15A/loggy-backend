package ch.zhaw.pm4.loganalyser.log;

import ch.zhaw.pm4.loganalyser.controller.PathController;
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
        String[][] files = {{"/var", "/home", "/etc"}};
        Mockito.when(pathService.getRootFolder()).thenReturn(files);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/path"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].[*]").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].[*]",
                            Matchers.allOf(Matchers.hasItem(files[0][0]), Matchers.hasItem(files[0][1]), Matchers.hasItem(files[0][2]))));
        } catch (Exception e) {
            Assertions.fail(e);
        }

        Mockito.verify(pathService, Mockito.times(1)).getRootFolder();
    }
}
