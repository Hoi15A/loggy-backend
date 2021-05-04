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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class ColumnComponentsServiceDeleteTest {

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
    void testDeleteColumnComponentById() {
        ColumnComponentDTO compareDTO = ColumnComponentDTO.builder()
                .id(1)
                .name("Host")
                .format("ff")
                .columnType(ColumnType.DATE)
                .build();

        assertEquals(2, columnComponentRepository.count());
        ColumnComponentDTO deletedColumnComponent = columnComponentService.deleteColumnComponentById(compareDTO.getId());
        assertEquals(1, columnComponentRepository.count());
        assertEquals(compareDTO, deletedColumnComponent);
    }

    /* ****************************************************************************************************************
     * NEGATIV TESTS
     * ****************************************************************************************************************/

    @Test
    @Sql("classpath:sql/getcolumncomponents.sql")
    void testDeleteColumnComponentById_RecordNotFound() {
        assertEquals(2, columnComponentRepository.count());
        assertThrows(RecordNotFoundException.class, () -> columnComponentService.deleteColumnComponentById(9999));
        assertEquals(2, columnComponentRepository.count());
    }

}
