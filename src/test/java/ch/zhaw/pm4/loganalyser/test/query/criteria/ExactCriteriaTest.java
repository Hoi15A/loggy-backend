package ch.zhaw.pm4.loganalyser.test.query.criteria;

import ch.zhaw.pm4.loganalyser.exception.ExactCriteriaIsNullException;
import ch.zhaw.pm4.loganalyser.query.criteria.ExactCriteria;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExactCriteriaTest {

    private static final String COLUMN_DUMMY = "TEST";

    private static final String VALID_EXACT = "Is this exact?!?";
    private static final String VALID_NOT_EXACT = "Is this exactly?!?";

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testExactCriteria_ExactExists() {
        ExactCriteria criteria = new ExactCriteria(VALID_EXACT);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_EXACT, COLUMN_DUMMY });
        list.add(new String[] { COLUMN_DUMMY, COLUMN_DUMMY });

        List<String[]> result = criteria.apply(list, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(list.get(0), result.get(0));
    }
    
    @Test
    void testExactCriteria_ExactNotExists() {
        ExactCriteria criteria = new ExactCriteria(VALID_EXACT);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_NOT_EXACT, COLUMN_DUMMY });
        list.add(new String[] { COLUMN_DUMMY, COLUMN_DUMMY });

        List<String[]> result = criteria.apply(list, 0);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testExactCriteria_NullListArgument() {
        ExactCriteria criteria = new ExactCriteria(VALID_EXACT);

        assertThrows(NullPointerException.class, () -> criteria.apply(null, 0));
    }

    @Test
    void testExactCriteria_ContainsNull() {
        ExactCriteria criteria = new ExactCriteria(null);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_EXACT, COLUMN_DUMMY });
        list.add(new String[] { VALID_EXACT, COLUMN_DUMMY });

        assertThrows(ExactCriteriaIsNullException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testExactCriteria_InvalidColumnIndex_IndexOutOfBound_Negative() {
        ExactCriteria criteria = new ExactCriteria(VALID_EXACT);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_EXACT, COLUMN_DUMMY });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> criteria.apply(list, -1));
    }

    @Test
    void testExactCriteria_InvalidColumnIndex_IndexOutOfBound_Positive() {
        ExactCriteria criteria = new ExactCriteria(VALID_EXACT);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_EXACT, COLUMN_DUMMY });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> criteria.apply(list, 2));
    }

}
