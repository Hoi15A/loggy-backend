package ch.zhaw.pm4.loganalyser.log;

import ch.zhaw.pm4.loganalyser.controller.LogServiceController;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.service.LogServiceService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

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
    void testGetServiceLogById() {
        LogServiceDTO logServiceDTO1 = new LogServiceDTO();
        logServiceDTO1.setName("Test1");

        Mockito.when(logServiceService.getLogServiceById(2)).thenReturn(logServiceDTO1);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/service/2")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").isNotEmpty());
        }
        catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void getAllLogServices() {

        Set<LogServiceDTO> data = new HashSet<>();
        LogServiceDTO logServiceDTO1 = new LogServiceDTO();
        LogServiceDTO logServiceDTO2 = new LogServiceDTO();
        logServiceDTO1.setName("Test1");
        logServiceDTO2.setName("Test2");
        data.add(logServiceDTO1);
        data.add(logServiceDTO2);

        Mockito.when(logServiceService.getAllLogServices()).thenReturn(data);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/service/all")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].name").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*].name",
                            Matchers.anyOf(Matchers.hasItem(logServiceDTO1.getName()),
                                    Matchers.hasItem(logServiceDTO2.getName()))))
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }
        Mockito.verify(logServiceService, Mockito.times(1)).getAllLogServices();
    }

    @Test
    void deleteLogServiceByExistingId() {
        LogServiceDTO logServiceDTO1 = new LogServiceDTO();
        logServiceDTO1.setName("Test1");

        Mockito.when(logServiceService.deleteLogServiceById(2)).thenReturn(logServiceDTO1);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/service/2")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").isNotEmpty())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            fail(e);
        }
    }

}
