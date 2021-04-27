package ch.zhaw.pm4.loganalyser.test.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogServiceLocation;
import ch.zhaw.pm4.loganalyser.repository.LogConfigRepository;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import ch.zhaw.pm4.loganalyser.service.LogServiceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
class LogServiceServiceTest {

    @Autowired
    LogServiceRepository logServiceRepository;

    @MockBean
    LogConfigRepository logConfigRepository;

    LogServiceService logService;

    @BeforeEach
    void setUp() {
        logService = new LogServiceService(logServiceRepository, logConfigRepository);
    }

    @Test
    void testCreateService() {
        LogConfig logConfig = LogConfig
                .builder()
                .name("Nginx")
                .separator(" ")
                .columnCount(0)
                .headerLength(0)
                .build();

        LogServiceDTO sampleService = new LogServiceDTO();
        sampleService.setDescription("Test service");
        sampleService.setName("sample");
        sampleService.setLogConfig("Nginx");

        Optional<LogConfig> logConfigOptional = Optional.of(logConfig);
        Mockito.when(logConfigRepository.findById(Mockito.any())).thenReturn(logConfigOptional);

        logService.createLogService(sampleService);

        Assertions.assertEquals(1, logServiceRepository.count());

        Mockito.verify(logConfigRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    void testCreateService_LogConfigMissing() {
        LogServiceDTO sampleService = new LogServiceDTO();
        sampleService.setDescription("Test service");
        sampleService.setName("sample");
        sampleService.setLogConfig("Nginx");

        Assertions.assertThrows(RecordNotFoundException.class, () -> logService.createLogService(sampleService));
    }

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    @Sql("classpath:sql/getallservices.sql")
    void testGetAllLogServices() {
        LogServiceDTO logService1 = new LogServiceDTO();
        logService1.setDescription("Test service 1");
        logService1.setLogDirectory("/var/log/service1");
        logService1.setName("Service1");
        logService1.setLocation(LogServiceLocation.LOCAL);
        logService1.setLogConfig("nginx");
        logService1.setId(1);

        LogServiceDTO logService2 = new LogServiceDTO();
        logService2.setDescription("Test service 2");
        logService2.setLogDirectory("/var/log/service1");
        logService2.setName("Service2");
        logService2.setLocation(LogServiceLocation.LOCAL);
        logService2.setLogConfig("vsftpd");
        logService2.setId(2);

        Set<LogServiceDTO> compareSet = new HashSet<>();
        compareSet.add(logService1);
        compareSet.add(logService2);

        Set<LogServiceDTO> logServices = logService.getAllLogServices();

        Assertions.assertEquals(2, logServices.size());
        Assertions.assertIterableEquals(compareSet, logServices);
    }

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    @Sql("classpath:sql/getallservices.sql")
    void testDeleteLogServiceById() {
        LogServiceDTO compareDTO = new LogServiceDTO();
        compareDTO.setDescription("Test service 1");
        compareDTO.setLogDirectory("/var/log/service1");
        compareDTO.setName("Service1");
        compareDTO.setLocation(LogServiceLocation.LOCAL);
        compareDTO.setLogConfig("nginx");
        compareDTO.setId(1);

        Assertions.assertEquals(2, logServiceRepository.count());
        LogServiceDTO deletedService = logService.deleteLogServiceById(1);
        Assertions.assertEquals(1, logServiceRepository.count());
        Assertions.assertEquals(compareDTO, deletedService);
    }

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    @Sql("classpath:sql/getallservices.sql")
    void testDeleteLogServiceById_IdNotFound() {
        Assertions.assertEquals(2, logServiceRepository.count());
        Assertions.assertThrows(RecordNotFoundException.class, () -> logService.deleteLogServiceById(999));
        Assertions.assertEquals(2, logServiceRepository.count());
    }

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    @Sql("classpath:sql/getallservices.sql")
    void testGetLogServiceById() {
        LogServiceDTO compareDTO = new LogServiceDTO();
        compareDTO.setDescription("Test service 1");
        compareDTO.setLogDirectory("/var/log/service1");
        compareDTO.setName("Service1");
        compareDTO.setLocation(LogServiceLocation.LOCAL);
        compareDTO.setLogConfig("nginx");
        compareDTO.setId(1);

        LogServiceDTO logServiceDTO = logService.getLogServiceById(1);
        Assertions.assertEquals(compareDTO, logServiceDTO);
    }

    @Test
    void testGetLogServiceById_IdNotFound() {
        Assertions.assertThrows(RecordNotFoundException.class, () -> logService.getLogServiceById(999));
    }

}
