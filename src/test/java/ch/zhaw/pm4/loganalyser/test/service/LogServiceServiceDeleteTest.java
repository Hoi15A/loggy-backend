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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class LogServiceServiceDeleteTest {

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
    void testDeleteLogServiceById() {
        LogServiceDTO compareDTO = LogServiceDTO.builder()
                .id(1)
                .name("Service1")
                .description("Test service 1")
                .logDirectory("/var/log/service1")
                .location(LogServiceLocation.LOCAL)
                .logConfig("nginx")
                .build();

        assertEquals(2, logServiceRepository.count());

        LogServiceDTO deletedService = logService.deleteLogServiceById(1);

        assertEquals(1, logServiceRepository.count());
        assertEquals(compareDTO, deletedService);
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    @Sql("classpath:sql/getallservices.sql")
    void testDeleteLogServiceById_IdNotFound() {
        assertEquals(2, logServiceRepository.count());
        assertThrows(RecordNotFoundException.class, () -> logService.deleteLogServiceById(999));
        assertEquals(2, logServiceRepository.count());
    }

}
