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

import java.io.File;
import java.nio.file.Path;

@SpringBootTest
@AutoConfigureMockMvc
class PathControllerTest {

    @MockBean
    PathService pathService;
    MockMvc mockMvc;
    PathController pathController;
    static final String ROOT_FOLDER = System.getenv("SystemDrive");

    @Autowired
    public PathControllerTest(MockMvc mockMvc, PathController pathController) {
        this.mockMvc = mockMvc;
        this.pathController = pathController;
    }

    @Test
    void testGetContentOfFolder() {
        File testFile = Path.of(ROOT_FOLDER, "folder").toFile();
        File[] testFolders = {Path.of(testFile.getPath(), "subfolder1").toFile(),
                Path.of(testFile.getPath(), "subfolder2").toFile()};

        Mockito.when(pathService.getContentOfFolder(Mockito.any())).thenReturn(testFolders);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/path?folder=/var/www"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*]",
                            Matchers.anyOf(Matchers.hasItem(testFolders[0].toString()),
                                    Matchers.hasItem(testFolders[1].toString()))));

        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

}
