package ch.zhaw.pm4.loganalyser.log;

import ch.zhaw.pm4.loganalyser.controller.LogServiceController;
import ch.zhaw.pm4.loganalyser.service.LogServiceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LogServiceControllerTest {

    @MockBean
    private LogServiceService logServiceService;
    private static final String JLOCATION = "testfiles";
    LogServiceController logServiceController;
    MockMvc mockMvc;


    @Autowired
    public LogServiceControllerTest(LogServiceController logServiceController, MockMvc mockMvc) {
        this.logServiceController = logServiceController;
        this.mockMvc = mockMvc;
    }


    String loadJsonTestFile(String filename) {
        try (Scanner sc = new Scanner(Objects.requireNonNull(LogServiceController
                .class
                .getClassLoader()
                .getResourceAsStream(Path.of(JLOCATION, filename).toString())))) {
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            return sb.toString();
        }
    }

    @Test
    void testCreateLogService() {
        String content = loadJsonTestFile("testCreateLogService.json");
        try {

            Mockito.doNothing().when(logServiceService).createLogService(Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders
                    .post("/service/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            Mockito.verify(logServiceService, Mockito.times(1)).createLogService(Mockito.any());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testCreateLogService_ConfigMissing() {
        String content = loadJsonTestFile("testCreateLogService_ConfigMissing.json");

        try {
            Mockito.doNothing().when(logServiceService).createLogService(Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders
                    .post("/service/")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            Mockito.verify(logServiceService, Mockito.times(0)).createLogService(Mockito.any());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void getAllLogServices() {

        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/service/all")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
        } catch (Exception e) {
            fail(e);
        }
    }
}
