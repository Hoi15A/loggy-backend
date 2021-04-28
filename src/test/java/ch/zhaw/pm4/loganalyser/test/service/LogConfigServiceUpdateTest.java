package ch.zhaw.pm4.loganalyser.test.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.repository.LogConfigRepository;
import ch.zhaw.pm4.loganalyser.service.LogConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
class LogConfigServiceUpdateTest {

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
    void testUpdateLogConfig() {
        String logConfigID = "sample";
        LogConfigDTO sampleConfig = LogConfigDTO.builder()
                .name(logConfigID)
                .separator(" ")
                .columnCount(0)
                .headerLength(0)
                .columnComponents(new HashMap<>())
                .build();

        logConfigService.createLogConfig(sampleConfig);

        sampleConfig.setSeparator("-");
        sampleConfig.setColumnCount(3);
        sampleConfig.setHeaderLength(1);

        logConfigService.updateLogConfig(sampleConfig);

        Optional<LogConfig> optional = logConfigRepository.findById(logConfigID);

        if (optional.isEmpty()) {
            fail("Config not in repo");
        }

        LogConfig logConfigAfter = optional.get();

        assertEquals(sampleConfig.getName(), logConfigAfter.getName());
        assertEquals(sampleConfig.getSeparator(), logConfigAfter.getSeparator());
        assertEquals(sampleConfig.getColumnCount(), logConfigAfter.getColumnCount());
        assertEquals(sampleConfig.getHeaderLength(), logConfigAfter.getHeaderLength());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testUpdateLogConfig_RecordNotFound() {
        LogConfigDTO sampleConfig = LogConfigDTO.builder()
                .name("nginx")
                .separator("|")
                .columnCount(5)
                .headerLength(1)
                .columnComponents(new HashMap<>())
                .build();

        assertThrows(RecordNotFoundException.class, () -> logConfigService.updateLogConfig(sampleConfig));
    }

}
