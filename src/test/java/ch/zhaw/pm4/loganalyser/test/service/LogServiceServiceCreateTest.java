package ch.zhaw.pm4.loganalyser.test.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogServiceDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.repository.LogConfigRepository;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import ch.zhaw.pm4.loganalyser.service.LogServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
class LogServiceServiceCreateTest {

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
    void testCreateService() {
        LogConfig logConfig = LogConfig
                .builder()
                .name("Nginx")
                .separator(" ")
                .columnCount(0)
                .headerLength(0)
                .build();

        LogServiceDTO sampleService = LogServiceDTO.builder()
                .name("sample")
                .description("Test service")
                .logConfig("Nginx")
                .build();

        Optional<LogConfig> logConfigOptional = Optional.of(logConfig);
        when(logConfigRepository.findById(any())).thenReturn(logConfigOptional);

        logService.createLogService(sampleService);

        assertEquals(1, logServiceRepository.count());

        verify(logConfigRepository, Mockito.times(1)).findById(any());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testCreateService_LogConfigMissing_RecordNotFoundException() {
        LogServiceDTO sampleService = LogServiceDTO.builder()
                .name("sample")
                .description("Test service")
                .logConfig("Nginx")
                .build();

        assertThrows(RecordNotFoundException.class, () -> logService.createLogService(sampleService));
    }

}
