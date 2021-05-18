package ch.zhaw.pm4.loganalyser.test.query.criteria;

import ch.zhaw.pm4.loganalyser.exception.InvalidInputException;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.query.criteria.RangeCriteria;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RangeCriteriaIpTest {

    private static final String COLUMN_DUMMY = "TEST";

    private static final String VALID_IP_FROM = "192.168.0.4";
    private static final String VALID_IP_TO = "192.168.0.15";
    private static final String VALID_IP_IN_RANGE = "192.168.0.10";
    private static final String VALID_IP_NOT_IN_RANGE_LOWER_BOUND = "192.168.0.1";
    private static final String VALID_IP_NOT_IN_RANGE_UPPER_BOUND = "192.168.0.20";
    private static final String INVALID_IP_PATTERN = "134.122.1";
    private static final String INVALID_IP_MAX_VALUE = "300.168.0.0";
    private static final String INVALID_IP_NON_IP_CHARACTERS = "192.186.0.abc";

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRangeCriteria_IpInRange() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, VALID_IP_TO);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_IP_IN_RANGE, COLUMN_DUMMY });
        list.add(new String[] { null, COLUMN_DUMMY });

        List<String[]> result = criteria.apply(list, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(list.get(0), result.get(0));
    }

    @Test
    void testRangeCriteria_From_Open() {
        RangeCriteria criteria = new RangeCriteria(null, VALID_IP_TO);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { "Text", VALID_IP_IN_RANGE });

        assertFalse(criteria.apply(list, 1).isEmpty());
    }

    @Test
    void testRangeCriteria_To_Open() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, null);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { "Text", VALID_IP_IN_RANGE });

        assertFalse(criteria.apply(list, 1).isEmpty());
    }

    @Test
    void testRangeCriteria_IpNotInRange_LowerBound() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, VALID_IP_TO);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_IP_NOT_IN_RANGE_LOWER_BOUND, COLUMN_DUMMY });

        assertTrue(criteria.apply(list, 0).isEmpty());
    }

    @Test
    void testRangeCriteria_IpNotInRange_UpperBound() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, VALID_IP_TO);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_IP_NOT_IN_RANGE_UPPER_BOUND, COLUMN_DUMMY });

        assertTrue(criteria.apply(list, 0).isEmpty());
    }

    @Test
    void testRangeCriteria_InvalidIpPattern() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, VALID_IP_TO);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { INVALID_IP_PATTERN, COLUMN_DUMMY });

        assertTrue(criteria.apply(list, 0).isEmpty());
    }

    @Test
    void testRangeCriteria_InvalidIpValues_MaxValue() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, VALID_IP_TO);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { INVALID_IP_MAX_VALUE, COLUMN_DUMMY });

        assertTrue(criteria.apply(list, 0).isEmpty());
    }

    @Test
    void testRangeCriteria_InvalidIpValues_NonIpCharacters() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, VALID_IP_TO);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { INVALID_IP_NON_IP_CHARACTERS, COLUMN_DUMMY });

        assertTrue(criteria.apply(list, 0).isEmpty());
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRangeCriteria_InvalidIpPattern_FromValue() {
        RangeCriteria criteria = new RangeCriteria(INVALID_IP_PATTERN, VALID_IP_TO);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_IP_FROM, COLUMN_DUMMY });

        assertThrows(InvalidInputException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testRangeCriteria_InvalidIpPattern_ToValue() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, INVALID_IP_PATTERN);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_IP_FROM, COLUMN_DUMMY });

        assertThrows(InvalidInputException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testRangeCriteria_InvalidIpValues_MaxValue_FromValue() {
        RangeCriteria criteria = new RangeCriteria(INVALID_IP_MAX_VALUE, VALID_IP_TO);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_IP_FROM, COLUMN_DUMMY });
        assertThrows(InvalidInputException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testRangeCriteria_InvalidIpValues_MaxValue_ToValue() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, INVALID_IP_MAX_VALUE);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_IP_FROM, COLUMN_DUMMY });

        assertThrows(InvalidInputException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testRangeCriteria_InvalidIpValues_NonIpCharacters_FromValue() {
        RangeCriteria criteria = new RangeCriteria(INVALID_IP_NON_IP_CHARACTERS, VALID_IP_TO);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_IP_FROM, COLUMN_DUMMY });

        assertThrows(InvalidInputException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testRangeCriteria_InvalidIpValues_NonIpCharacters_ToValue() {
        RangeCriteria criteria = new RangeCriteria(VALID_IP_FROM, INVALID_IP_NON_IP_CHARACTERS);
        criteria.setType(ColumnType.IP);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_IP_FROM, COLUMN_DUMMY });

        assertThrows(InvalidInputException.class, () -> criteria.apply(list, 0));
    }

}
