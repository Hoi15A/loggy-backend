package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.dto.LogConfigDTO;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.repository.ColumnComponentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest
class ColumnComponentsServiceTest {

    @Autowired
    ColumnComponentRepository columnComponentRepository;
    ColumnComponentService columnComponentService;

    @BeforeEach
    void setUp() {
        columnComponentService = new ColumnComponentService(columnComponentRepository);
    }

    @Test
    @Sql("classpath:sql/getcolumncomponents.sql")
    void testGetAllColumnComponents() {
        List<ColumnComponentDTO> columnDTOS = columnComponentService.getAllColumnComponents();
        Assertions.assertEquals(1, columnDTOS.get(0).getId());
        Assertions.assertEquals(ColumnType.DATE, columnDTOS.get(0).getColumnType());
        Assertions.assertEquals("ff", columnDTOS.get(0).getFormat());
        Assertions.assertEquals("Host", columnDTOS.get(0).getName());

        Assertions.assertEquals(2, columnDTOS.get(1).getId());
        Assertions.assertEquals(ColumnType.DATE, columnDTOS.get(1).getColumnType());
        Assertions.assertEquals("ff", columnDTOS.get(1).getFormat());
        Assertions.assertEquals("User", columnDTOS.get(1).getName());
    }

    @Test
    @Sql("classpath:sql/getcolumncomponents.sql")
    void testGetAllColumnComponentsById() {
        ColumnComponentDTO columnDTO = columnComponentService.getColumnComponentById(1L);
        Assertions.assertEquals(1, columnDTO.getId());
        Assertions.assertEquals(ColumnType.DATE, columnDTO.getColumnType());
        Assertions.assertEquals("ff", columnDTO.getFormat());
        Assertions.assertEquals("Host", columnDTO.getName());
    }

    @Test
    void testGetConfigNotAvailable() {
        Assertions.assertThrows(RecordNotFoundException.class, () -> columnComponentService.getColumnComponentById(5L));
    }

    @Test
    @Sql("classpath:sql/getcolumncomponents.sql")
    void testDeleteColumnComponentById() {
        ColumnComponentDTO compareDTO = new ColumnComponentDTO();
        compareDTO.setName("Host");
        compareDTO.setId(1);
        compareDTO.setFormat("ff");
        compareDTO.setColumnType(ColumnType.DATE);

        Assertions.assertEquals(2, columnComponentRepository.count());
        ColumnComponentDTO deletedColumnComponent = columnComponentService.deleteColumnComponentById(1);
        Assertions.assertEquals(1, columnComponentRepository.count());
        Assertions.assertEquals(compareDTO.getName(), deletedColumnComponent.getName());
        Assertions.assertEquals(compareDTO.getFormat(), deletedColumnComponent.getFormat());
        Assertions.assertEquals(compareDTO.getColumnType(), deletedColumnComponent.getColumnType());
        Assertions.assertEquals(compareDTO.getId(), deletedColumnComponent.getId());
    }

    @Test
    void testCreateColumnComponent() {
        ColumnComponentDTO testColumn = new ColumnComponentDTO();
        testColumn.setName("Host1");
        testColumn.setFormat("-");
        testColumn.setId(3L);
        testColumn.setColumnType(ColumnType.MESSAGE);

        columnComponentService.createColumn(testColumn);

        Assertions.assertEquals(1, columnComponentRepository.count());

    }
}
