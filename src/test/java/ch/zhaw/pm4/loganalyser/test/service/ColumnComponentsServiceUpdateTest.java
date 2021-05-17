package ch.zhaw.pm4.loganalyser.test.service;

import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnComponentDTO;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.model.log.column.FilterType;
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
class ColumnComponentsServiceUpdateTest {

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
    void testColumnComponentUpdate() {
        ColumnComponentDTO testColumn = ColumnComponentDTO.builder()
                .id(2L) // is an existing id
                .name("newName")
                .format("newFormat")
                .filterTypes(List.of(FilterType.REGEX, FilterType.CONTAINS).toArray(FilterType[]::new))
                .columnType(ColumnType.TEXT)
                .build();

        columnComponentService.updateColumn(testColumn);

        assertEquals(testColumn, columnComponentService.getColumnComponentById(testColumn.getId()));
    }


    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    @Sql("classpath:sql/getcolumncomponents.sql")
    void testColumnComponentUpdate_RecordNotFound() {
        ColumnComponentDTO testColumn = ColumnComponentDTO.builder()
                .id(999L) // is an existing id
                .name("newName")
                .format("newFormat")
                .columnType(ColumnType.TEXT)
                .build();

        assertThrows(RecordNotFoundException.class, () -> columnComponentService.updateColumn(testColumn));
    }
}
