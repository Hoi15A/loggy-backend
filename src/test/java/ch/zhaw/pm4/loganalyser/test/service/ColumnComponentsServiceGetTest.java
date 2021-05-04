package ch.zhaw.pm4.loganalyser.test.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.repository.ColumnComponentRepository;
import ch.zhaw.pm4.loganalyser.service.ColumnComponentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class ColumnComponentsServiceGetTest {

    @Autowired
    ColumnComponentRepository columnComponentRepository;
    ColumnComponentService columnComponentService;

    @BeforeEach
    void setUp() {
        columnComponentService = new ColumnComponentService(columnComponentRepository);
    }

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    @Sql("classpath:sql/getcolumncomponents.sql")
    void testGetAllColumnComponents() {
        List<ColumnComponentDTO> columnDTOS = columnComponentService.getAllColumnComponents();

        assertEquals(columnComponentRepository.count(), columnDTOS.size());

        ColumnComponentDTO dto = columnDTOS.get(0);
        assertEquals(1, dto.getId());
        assertEquals(ColumnType.DATE, dto.getColumnType());
        assertEquals("ff", dto.getFormat());
        assertEquals("Host", dto.getName());

        dto = columnDTOS.get(1);
        assertEquals(2, dto.getId());
        assertEquals(ColumnType.DATE, dto.getColumnType());
        assertEquals("ff", dto.getFormat());
        assertEquals("User", dto.getName());
    }

    @Test
    @Sql("classpath:sql/getcolumncomponents.sql")
    void testGetColumnComponentById() {
        ColumnComponentDTO columnDTO = columnComponentService.getColumnComponentById(1L);
        assertEquals(1, columnDTO.getId());
        assertEquals(ColumnType.DATE, columnDTO.getColumnType());
        assertEquals("ff", columnDTO.getFormat());
        assertEquals("Host", columnDTO.getName());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetColumnComponentById_RecordNotFound() {
        assertThrows(RecordNotFoundException.class, () -> columnComponentService.getColumnComponentById(5L));
    }

}
