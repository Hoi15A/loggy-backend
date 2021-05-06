package ch.zhaw.pm4.loganalyser.test.query.criteria;

import ch.zhaw.pm4.loganalyser.exception.InvalidInputException;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.query.criteria.RangeCriteria;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RangeCriteriaNumberTest {

    private static final String COLUMN_DUMMY = "TEST";

    private static final String VALID_NUMBER_FROM = "2";
    private static final String VALID_NUMBER_TO = "8";
    private static final String VALID_INTEGER_IN_RANGE = "5";
    private static final String VALID_DOUBLE_IN_RANGE = "5.5";
    private static final String VALID_NUMBER_NOT_IN_RANGE_LOWER_BOUND = "1.9";
    private static final String VALID_NUMBER_NOT_IN_RANGE_UPPER_BOUND = "9";
    private static final String INVALID_NUMBER = "a";

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRangeCriteria_NumberInRange() {
        RangeCriteria criteria = new RangeCriteria(VALID_NUMBER_FROM, VALID_NUMBER_TO);
        criteria.setType(ColumnType.INTEGER);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { null, COLUMN_DUMMY });
        list.add(new String[] { VALID_INTEGER_IN_RANGE, COLUMN_DUMMY });


        List<String[]> result = criteria.apply(list, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(list.get(1), result.get(0));

        criteria.setType(ColumnType.DOUBLE);
        list.add(new String[] { VALID_DOUBLE_IN_RANGE, COLUMN_DUMMY });

        result = criteria.apply(list, 0);

        list.remove(0);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertIterableEquals(list, result);
    }

    @Test
    void testRangeCriteria_From_Open() {
        RangeCriteria criteria = new RangeCriteria(null, VALID_NUMBER_TO);
        criteria.setType(ColumnType.INTEGER);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_INTEGER_IN_RANGE, COLUMN_DUMMY });

        assertFalse(criteria.apply(list, 0).isEmpty());
        criteria.setType(ColumnType.DOUBLE);
        assertFalse(criteria.apply(list, 0).isEmpty());
    }

    @Test
    void testRangeCriteria_To_Open() {
        RangeCriteria criteria = new RangeCriteria(VALID_NUMBER_FROM, null);
        criteria.setType(ColumnType.INTEGER);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_INTEGER_IN_RANGE, COLUMN_DUMMY });

        assertFalse(criteria.apply(list, 0).isEmpty());
        criteria.setType(ColumnType.DOUBLE);
        assertFalse(criteria.apply(list, 0).isEmpty());
    }

    @Test
    void testRangeCriteria_IpNotInRange_LowerBound() {
        RangeCriteria criteria = new RangeCriteria(VALID_NUMBER_FROM, VALID_NUMBER_TO);
        criteria.setType(ColumnType.INTEGER);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_NUMBER_NOT_IN_RANGE_LOWER_BOUND, COLUMN_DUMMY });

        assertTrue(criteria.apply(list, 0).isEmpty());
        criteria.setType(ColumnType.DOUBLE);
        assertTrue(criteria.apply(list, 0).isEmpty());
    }

    @Test
    void testRangeCriteria_IpNotInRange_UpperBound() {
        RangeCriteria criteria = new RangeCriteria(VALID_NUMBER_FROM, VALID_NUMBER_TO);
        criteria.setType(ColumnType.INTEGER);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_NUMBER_NOT_IN_RANGE_UPPER_BOUND, COLUMN_DUMMY });

        assertTrue(criteria.apply(list, 0).isEmpty());
        criteria.setType(ColumnType.DOUBLE);
        assertTrue(criteria.apply(list, 0).isEmpty());
    }

    @Test
    void testRangeCriteria_InvalidNumber() {
        RangeCriteria criteria = new RangeCriteria(VALID_NUMBER_FROM, VALID_NUMBER_TO);
        criteria.setType(ColumnType.INTEGER);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { INVALID_NUMBER, COLUMN_DUMMY });

        assertTrue(criteria.apply(list, 0).isEmpty());
        criteria.setType(ColumnType.DOUBLE);
        assertTrue(criteria.apply(list, 0).isEmpty());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRangeCriteria_InvalidIntegerFromValue() {
        RangeCriteria criteria = new RangeCriteria(INVALID_NUMBER, VALID_NUMBER_TO);
        criteria.setType(ColumnType.INTEGER);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_INTEGER_IN_RANGE, COLUMN_DUMMY });

        assertThrows(InvalidInputException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testRangeCriteria_InvalidIntegerToValue() {
        RangeCriteria criteria = new RangeCriteria(VALID_NUMBER_FROM, INVALID_NUMBER);
        criteria.setType(ColumnType.INTEGER);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_INTEGER_IN_RANGE, COLUMN_DUMMY });

        assertThrows(InvalidInputException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testRangeCriteria_InvalidDoubleFromValue() {
        RangeCriteria criteria = new RangeCriteria(INVALID_NUMBER, VALID_NUMBER_TO);
        criteria.setType(ColumnType.DOUBLE);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_INTEGER_IN_RANGE, COLUMN_DUMMY });

        assertThrows(InvalidInputException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testRangeCriteria_InvalidDoubleToValue() {
        RangeCriteria criteria = new RangeCriteria(VALID_NUMBER_FROM, INVALID_NUMBER);
        criteria.setType(ColumnType.DOUBLE);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_INTEGER_IN_RANGE, COLUMN_DUMMY });

        assertThrows(InvalidInputException.class, () -> criteria.apply(list, 0));
    }

}
