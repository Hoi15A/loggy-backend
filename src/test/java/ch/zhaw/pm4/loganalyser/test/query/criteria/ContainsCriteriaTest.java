package ch.zhaw.pm4.loganalyser.test.query.criteria;

import ch.zhaw.pm4.loganalyser.query.criteria.ContainsCriteria;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainsCriteriaTest {

    private static final String COLUMN_DUMMY = "TEST";

    private static final String EXACT = "helpful";

    private static final String VALID_VALUE_CONTAINS = "Loggy is a helpful analyzing tool.";
    private static final String VALID_VALUE_NOT_CONTAINS = "Loggy is amazing.";


    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/
    
    @Test
    void testContainsCriteria() {
        ContainsCriteria criteria = new ContainsCriteria(EXACT);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_VALUE_CONTAINS, COLUMN_DUMMY });
        list.add(new String[] { VALID_VALUE_NOT_CONTAINS, COLUMN_DUMMY });

        List<String[]> result = criteria.apply(list, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(list.get(0), result.get(0));
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testContainsCriteria_Contains_Null() {
        ContainsCriteria criteria = new ContainsCriteria(null);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_VALUE_CONTAINS, COLUMN_DUMMY });
        list.add(new String[] { VALID_VALUE_NOT_CONTAINS, COLUMN_DUMMY });

        assertThrows(NullPointerException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testContainsCriteria_NullListArgument() {
        ContainsCriteria criteria = new ContainsCriteria(EXACT);
        assertThrows(NullPointerException.class, () -> criteria.apply(null, 0));
    }

    @Test
    void testContainsCriteria_InvalidColumnIndex_IndexOutOfBound_Negative() {
        ContainsCriteria criteria = new ContainsCriteria(EXACT);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_VALUE_CONTAINS, COLUMN_DUMMY });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> criteria.apply(list, -1));
    }

    @Test
    void testContainsCriteria_InvalidColumnIndex_IndexOutOfBound_Positive() {
        ContainsCriteria criteria = new ContainsCriteria(EXACT);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_VALUE_CONTAINS, COLUMN_DUMMY });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> criteria.apply(list, 2));
    }

}
