package ch.zhaw.pm4.loganalyser.test.query.criteria;

import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.query.criteria.RangeCriteria;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RangeCriteriaTest {

    private static final String COLUMN_DUMMY = "TEST";

    private static final String VALID_IP_FROM = "192.168.0.4";
    private static final String VALID_IP_TO = "192.168.0.15";
    private static final String VALID_IP_IN_RANGE = "192.168.0.10";

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    // Positive tests in the corresponding test classes.

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRangeCriteria_NullListArgument() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, VALID_IP_TO);
        criteria.setType(ColumnType.TEXT);

        assertThrows(NullPointerException.class, () -> criteria.apply(null, 0));
    }

    @Test
    void testRangeCriteria_ColumnTypeNotSet() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, VALID_IP_TO);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { "column1", "column2" });

        assertThrows(UnsupportedOperationException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testRangeCriteria_ColumnTypeInvalid() {

    }

    @Test
    void testRangeCriteria_InvalidColumnIndex_IndexOutOfBound_Negative() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, VALID_IP_TO);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_IP_IN_RANGE, COLUMN_DUMMY });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> criteria.apply(list, -1));
    }

    @Test
    void testRangeCriteria_InvalidColumnIndex_IndexOutOfBound_Positive() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, VALID_IP_TO);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_IP_IN_RANGE, COLUMN_DUMMY });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> criteria.apply(list, 2));
    }

}
