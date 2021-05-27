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
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class LogServiceServiceUpdateTest {

    @Autowired
    LogServiceRepository logServiceRepository;

    @Autowired
    LogConfigRepository logConfigRepository;

    LogServiceService logServiceService;

    @BeforeEach
    void setUp() {
        logServiceService = new LogServiceService(logServiceRepository, logConfigRepository);
    }

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    @Sql("classpath:sql/getallservices.sql")
    void testUpdateService() {
        var service = LogServiceDTO.builder()
                .id(1)
                .name("Test service 1")
                .logDirectory("/new/directory")
                .description("New Description")
                .logConfig("nginx")
                .image(null)
                .location(LogServiceLocation.LOCAL)
                .build();

        logServiceService.updateLogService(service);

        assertEquals(service, logServiceService.getLogServiceById(1L));
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testUpdateNonExistentService() {
        var serviceName = "Test service 1";
        var service = LogServiceDTO.builder()
                .id(1)
                .name(serviceName)
                .logDirectory("/new/directory")
                .description("New Description")
                .logConfig("nginx")
                .image(null)
                .location(LogServiceLocation.LOCAL)
                .build();

        var ex = assertThrows(RecordNotFoundException.class, () -> logServiceService.updateLogService(service));
        assertEquals(String.format(LogServiceService.UPDATE_SERVICE_S, serviceName), ex.getMessage());
    }

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    @Sql("classpath:sql/getallservices.sql")
    void testUpdateNonExistentLogConfig() {
        var logConfig = "not_real";
        var service = LogServiceDTO.builder()
                .id(1)
                .name("Test service 1")
                .logDirectory("/new/directory")
                .description("New Description")
                .logConfig(logConfig)
                .image(null)
                .location(LogServiceLocation.LOCAL)
                .build();

        var ex = assertThrows(RecordNotFoundException.class, () -> logServiceService.updateLogService(service));
        assertEquals(String.format(LogServiceService.UPDATE_SERVICE_LOG_CONFIG_S, logConfig), ex.getMessage());
    }
}
