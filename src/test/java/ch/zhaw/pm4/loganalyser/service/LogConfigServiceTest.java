package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.repository.LogConfigRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest
class LogConfigServiceTest {

    @Autowired
    LogConfigRepository logConfigRepository;
    LogConfigService logConfigService;

    @BeforeEach
    void setUp() {
        logConfigService = new LogConfigService(logConfigRepository);
    }

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    void testGetAllConfigs() {
        List<LogConfigDTO> configDTOS = logConfigService.getAllLogConfigs();
        Assertions.assertEquals(2, configDTOS.size());
        Assertions.assertEquals("nginx", configDTOS.get(0).getName());
        Assertions.assertEquals(8, configDTOS.get(0).getColumnCount());
        Assertions.assertEquals(0, configDTOS.get(0).getHeaderLength());
        Assertions.assertEquals(" ", configDTOS.get(0).getSeparator());

        Assertions.assertEquals("vsftpd", configDTOS.get(1).getName());
        Assertions.assertEquals(6, configDTOS.get(1).getColumnCount());
        Assertions.assertEquals(1, configDTOS.get(1).getHeaderLength());
        Assertions.assertEquals(" ", configDTOS.get(1).getSeparator());
    }

    @Test
    @Sql("classpath:sql/getlogconfigs.sql")
    void testGetConfigById() {
        LogConfigDTO configDTO = logConfigService.getLogConfigById("nginx");
        Assertions.assertEquals("nginx", configDTO.getName());
        Assertions.assertEquals(8, configDTO.getColumnCount());
        Assertions.assertEquals(0, configDTO.getHeaderLength());
        Assertions.assertEquals(" ", configDTO.getSeparator());
    }

    @Test
    void testGetConfigNotAvailable() {
        Assertions.assertThrows(RecordNotFoundException.class, () -> logConfigService.getLogConfigById("doesnotexist"));
    }
}
