package ch.zhaw.pm4.loganalyser.test.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.repository.LogConfigRepository;
import ch.zhaw.pm4.loganalyser.service.LogConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class LogConfigServiceDeleteTest {

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
    @Sql("classpath:sql/getlogconfigs.sql")
    void testDeleteLogConfig() {
        LogConfigDTO compareDTO = LogConfigDTO.builder()
                .name("nginx")
                .columnCount(8)
                .headerLength(0)
                .build();

        assertEquals(2, logConfigRepository.count());

        LogConfigDTO deletedService = logConfigService.deleteLogConfigById(compareDTO.getName());

        assertEquals(1, logConfigRepository.count());
        assertEquals(compareDTO.getName(), deletedService.getName());
        assertEquals(compareDTO.getColumnCount(), deletedService.getColumnCount());
        assertEquals(compareDTO.getHeaderLength(), deletedService.getHeaderLength());
        assertEquals(" ", deletedService.getSeparator());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    void testDeleteLogConfig_RecordNotFound() {
        LogConfigDTO compareDTO = LogConfigDTO.builder()
                .name("NonExisting")
                .columnCount(8)
                .headerLength(0)
                .build();

        assertEquals(2, logConfigRepository.count());
        var name = compareDTO.getName();
        assertThrows(RecordNotFoundException.class, () -> logConfigService.deleteLogConfigById(name));
        assertEquals(2, logConfigRepository.count());
    }
}
