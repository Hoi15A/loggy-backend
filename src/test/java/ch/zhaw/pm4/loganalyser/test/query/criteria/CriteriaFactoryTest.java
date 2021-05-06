package ch.zhaw.pm4.loganalyser.test.query.criteria;

import ch.zhaw.pm4.loganalyser.model.log.QueryComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.FilterType;
import ch.zhaw.pm4.loganalyser.query.criteria.CriteriaFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

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
        assertNotNull(CriteriaFactory.getCriteria(FilterType.REGEX, qc));
        assertNotNull(CriteriaFactory.getCriteria(FilterType.EXACT, qc));
        assertNotNull(CriteriaFactory.getCriteria(FilterType.RANGE, qc));
        assertNotNull(CriteriaFactory.getCriteria(FilterType.CONTAINS, qc));
    }

    @Test
    void testGetCriteria_FilterType_Default_Case() {
        // todo: nothing yet as default
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
