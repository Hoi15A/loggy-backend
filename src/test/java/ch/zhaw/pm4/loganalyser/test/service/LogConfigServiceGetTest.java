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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class LogConfigServiceGetTest {

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
    void testGetAllLogConfigs() {
        List<LogConfigDTO> configDTOS = logConfigService.getAllLogConfigs();

        assertEquals(logConfigRepository.count(), configDTOS.size());

        assertEquals("nginx", configDTOS.get(0).getName());
        assertEquals(8, configDTOS.get(0).getColumnCount());
        assertEquals(0, configDTOS.get(0).getHeaderLength());
        assertEquals(" ", configDTOS.get(0).getSeparator());

        assertEquals("vsftpd", configDTOS.get(1).getName());
        assertEquals(6, configDTOS.get(1).getColumnCount());
        assertEquals(1, configDTOS.get(1).getHeaderLength());
        assertEquals(" ", configDTOS.get(1).getSeparator());
    }

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    void testGetLogConfigById() {
        LogConfigDTO configDTO = logConfigService.getLogConfigById("nginx");

        assertEquals("nginx", configDTO.getName());
        assertEquals(8, configDTO.getColumnCount());
        assertEquals(0, configDTO.getHeaderLength());
        assertEquals(" ", configDTO.getSeparator());
    }

    /* ****************************************************************************************************************
     * NGEATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetLogConfigById_RecordNotFound() {
        assertThrows(RecordNotFoundException.class, () -> logConfigService.getLogConfigById("doesnotexist"));
    }

}
