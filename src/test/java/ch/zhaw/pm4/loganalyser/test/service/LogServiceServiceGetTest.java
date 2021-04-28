package ch.zhaw.pm4.loganalyser.test.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogServiceLocation;
import ch.zhaw.pm4.loganalyser.repository.LogConfigRepository;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import ch.zhaw.pm4.loganalyser.service.LogServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class LogServiceServiceGetTest {

    @Autowired
    LogServiceRepository logServiceRepository;

    @MockBean
    LogConfigRepository logConfigRepository;

    LogServiceService logService;

    @BeforeEach
    void setUp() {
        logService = new LogServiceService(logServiceRepository, logConfigRepository);
    }

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    @Sql("classpath:sql/getallservices.sql")
    void testGetAllLogServices() {
        LogServiceDTO logService1 = LogServiceDTO.builder()
                .id(1)
                .name("Service1")
                .description("Test service 1")
                .logDirectory("/var/log/service1")
                .location(LogServiceLocation.LOCAL)
                .logConfig("nginx")
                .build();

        LogServiceDTO logService2 = LogServiceDTO.builder()
                .id(2)
                .name("Service2")
                .description("Test service 2")
                .logDirectory("/var/log/service1")
                .location(LogServiceLocation.LOCAL)
                .logConfig("vsftpd")
                .build();

        Set<LogServiceDTO> compareSet = new HashSet<>();
        compareSet.add(logService1);
        compareSet.add(logService2);

        Set<LogServiceDTO> logServices = logService.getAllLogServices();

        assertEquals(logServiceRepository.count(), logServices.size());
        assertIterableEquals(compareSet, logServices);
    }

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    @Sql("classpath:sql/getallservices.sql")
    void testGetLogServiceById() {
        LogServiceDTO compareDTO = LogServiceDTO.builder()
                .id(1)
                .name("Service1")
                .description("Test service 1")
                .logDirectory("/var/log/service1")
                .location(LogServiceLocation.LOCAL)
                .logConfig("nginx")
                .build();

        assertEquals(compareDTO, logService.getLogServiceById(compareDTO.getId()));
    }


    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetLogServiceById_RecordNotFound() {
        assertThrows(RecordNotFoundException.class, () -> logService.getLogServiceById(999));
    }

}
