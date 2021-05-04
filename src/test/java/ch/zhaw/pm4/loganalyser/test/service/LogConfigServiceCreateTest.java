package ch.zhaw.pm4.loganalyser.test.service;

import ch.zhaw.pm4.loganalyser.exception.RecordAlreadyExistsException;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.repository.LogConfigRepository;
import ch.zhaw.pm4.loganalyser.service.LogConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class LogConfigServiceCreateTest {

    @Autowired
    LogConfigRepository logConfigRepository;
    LogConfigService logConfigService;

    @BeforeEach
    void setUp() {
        logConfigService = new LogConfigService(logConfigRepository);
    }

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testCreateLogConfig() {
        LogConfigDTO sampleConfig = LogConfigDTO.builder()
                .name("sample")
                .separator(" ")
                .columnCount(0)
                .headerLength(0)
                .columnComponents(new HashMap<>())
                .build();

        logConfigService.createLogConfig(sampleConfig);

        assertEquals(1, logConfigRepository.count());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testCreateLogConfig_RecordAlreadyExists() {
        LogConfigDTO sampleConfig = LogConfigDTO.builder()
                .name("sample")
                .separator(" ")
                .columnCount(0)
                .headerLength(0)
                .columnComponents(new HashMap<>())
                .build();

        logConfigService.createLogConfig(sampleConfig);

        assertEquals(1, logConfigRepository.count());
        assertThrows(RecordAlreadyExistsException.class, () -> logConfigService.createLogConfig(sampleConfig));
    }
}
