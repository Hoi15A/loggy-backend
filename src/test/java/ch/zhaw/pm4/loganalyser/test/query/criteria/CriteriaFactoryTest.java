package ch.zhaw.pm4.loganalyser.test.query.criteria;

import ch.zhaw.pm4.loganalyser.model.log.QueryComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.model.log.column.FilterType;
import ch.zhaw.pm4.loganalyser.query.criteria.CriteriaFactory;
import ch.zhaw.pm4.loganalyser.query.criteria.ExactCriteria;
import ch.zhaw.pm4.loganalyser.query.criteria.RangeCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CriteriaFactoryTest {

    QueryComponent qc;

    @BeforeEach
    void setUp() {
        qc = mock(QueryComponent.class);
    }

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetCriteria() {
        ColumnComponent columnComponentMock = mock(ColumnComponent.class);
        when(qc.getColumnComponent()).thenReturn(columnComponentMock);
        when(qc.getColumnComponent().getDateFormat()).thenReturn("dd-MM-yyyy");
        when(qc.getFrom()).thenReturn("24-03-1997");

        assertNotNull(CriteriaFactory.getCriteria(FilterType.REGEX, qc));
        assertNotNull(CriteriaFactory.getCriteria(FilterType.EXACT, qc));
        assertNotNull(CriteriaFactory.getCriteria(FilterType.RANGE, qc));
        assertNotNull(CriteriaFactory.getCriteria(FilterType.CONTAINS, qc));
    }

    @Test
    void testGetCriteria_Exact() {
        ColumnComponent columnComponentMock = mock(ColumnComponent.class);
        // default
        when(qc.getColumnComponent()).thenReturn(columnComponentMock);
        when(qc.getExact()).thenReturn("01-02-1997");
        when(qc.getDateFormat()).thenReturn("dd-MM-yyyy");
        when(qc.getColumnComponent().getDateFormat()).thenReturn("dd-MM-yyyy'T'HH:mm:ss");

        ExactCriteria criteria = (ExactCriteria) CriteriaFactory.getCriteria(FilterType.EXACT, qc);
        assertNull(criteria.getDateFormat());
        assertNull(criteria.getType());

        // date
        when(qc.getColumnComponent().getColumnType()).thenReturn(ColumnType.DATE);

        criteria = (ExactCriteria) CriteriaFactory.getCriteria(FilterType.EXACT, qc);
        assertEquals("dd-MM-yyyy'T'HH:mm:ss", criteria.getDateFormat());
        assertEquals(ColumnType.DATE, criteria.getType());
    }

    @Test
    void testGetCriteria_Range() {
        ColumnComponent columnComponentMock = mock(ColumnComponent.class);
        // default
        when(qc.getColumnComponent()).thenReturn(columnComponentMock);
        when(qc.getFrom()).thenReturn("01-02-1997");
        when(qc.getDateFormat()).thenReturn("dd-MM-yyyy");
        when(qc.getColumnComponent().getDateFormat()).thenReturn("dd-MM-yyyy'T'HH:mm:ss");
        when(qc.getColumnComponent().getColumnType()).thenReturn(ColumnType.IP);

        RangeCriteria criteria = (RangeCriteria) CriteriaFactory.getCriteria(FilterType.RANGE, qc);
        assertNotNull(criteria.getType());
        assertNotNull(criteria.getFrom());
        assertNull(criteria.getTo());
        assertNull(criteria.getLocale());
        assertNull(criteria.getDateFormat());

        // date
        when(qc.getColumnComponent().getColumnType()).thenReturn(ColumnType.DATE);

        criteria = (RangeCriteria) CriteriaFactory.getCriteria(FilterType.RANGE, qc);
        assertEquals("dd-MM-yyyy'T'HH:mm:ss", criteria.getDateFormat());
        assertNotNull(criteria.getLocale());
        assertNotNull(criteria.getDateFormat());
        assertNotNull(criteria.getFrom());
        assertNull(criteria.getTo());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testGetCriteria_FilterType_Null() {
        assertThrows(NullPointerException.class, () -> CriteriaFactory.getCriteria(null, qc));
    }

    @Test
    void testGetCriteria_QueryComponent_Null() {
        assertThrows(NullPointerException.class, () -> CriteriaFactory.getCriteria(FilterType.REGEX, null));
    }

}
