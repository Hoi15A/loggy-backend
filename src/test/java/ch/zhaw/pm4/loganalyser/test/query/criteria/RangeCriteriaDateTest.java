package ch.zhaw.pm4.loganalyser.test.query.criteria;

import ch.zhaw.pm4.loganalyser.exception.InvalidInputException;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.query.criteria.RangeCriteria;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RangeCriteriaDateTest {

    private static final String COLUMN_DUMMY = "TEST";

    private static final String VALID_FORMAT = "dd/MMM/yyyy:HH:mm:ss Z";

    private static final String VALID_DATE_FROM_ZONE1 = "01/Mar/2021:01:00:50 +0100";
    private static final String VALID_DATE_TO_ZONE1 = "30/Jun/2021:13:30:50 +0100";
    private static final String VALID_DATE_IN_RANGE_ZONE1 = "02/May/2021:13:30:50 +0100";
    private static final String VALID_DATE_IN_RANGE_ZONE2 = "02/May/2021:13:30:50 +0200";
    private static final String VALID_DATE_IN_RANGE_ZONE1_ISO_8601_UTC = "2021-04-02'T'13:30:50'+0100'";
    private static final String VALID_DATE_NOT_IN_RANGE_ZONE1_LOWER_BOUND = "28/Feb/2021:23:59:59 +0100";
    private static final String VALID_DATE_NOT_IN_RANGE_ZONE1_UPPER_BOUND = "02/Jul/2021:14:00:00 +0100";
    private static final String INVALID_DATE_ZONE1 = "23/TES/2021:00:00:01 +0100";

    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRangeCriteria() {
        RangeCriteria criteria = new RangeCriteria(VALID_DATE_FROM_ZONE1, VALID_DATE_TO_ZONE1);
        criteria.setType(ColumnType.DATE);
        criteria.setDateFormat(VALID_FORMAT);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_DATE_IN_RANGE_ZONE1, COLUMN_DUMMY });                 // ok
        list.add(new String[] { null, COLUMN_DUMMY });                                      // filter out
        list.add(new String[] { VALID_DATE_IN_RANGE_ZONE2, COLUMN_DUMMY });                 // ok
        list.add(new String[] { VALID_DATE_NOT_IN_RANGE_ZONE1_LOWER_BOUND, COLUMN_DUMMY }); // filter out
        list.add(new String[] { VALID_DATE_NOT_IN_RANGE_ZONE1_UPPER_BOUND, COLUMN_DUMMY }); // filter out
        // todo: check if multiple format should be supported in the same column component
        //list.add(new String[] { VALID_DATE_IN_RANGE_ZONE1_ISO_8601_UTC, COLUMN_DUMMY });    // ok
        list.add(new String[] { INVALID_DATE_ZONE1, COLUMN_DUMMY });                        // filter out

        List<String[]> result = criteria.apply(list, 0);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(list.get(0), result.get(0));
        assertEquals(list.get(2), result.get(1));
        //assertEquals(list.get(4), result.get(2));
    }

    @Test
    void testRangeCriteria_InvalidDateValue_From_Open() {
        RangeCriteria criteria = new RangeCriteria(null, VALID_DATE_TO_ZONE1);
        criteria.setType(ColumnType.DATE);
        criteria.setDateFormat(VALID_FORMAT);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_DATE_IN_RANGE_ZONE1, COLUMN_DUMMY });                 // ok
        list.add(new String[] { null, COLUMN_DUMMY });                                      // filter out
        list.add(new String[] { VALID_DATE_NOT_IN_RANGE_ZONE1_LOWER_BOUND, COLUMN_DUMMY }); // ok
        list.add(new String[] { VALID_DATE_NOT_IN_RANGE_ZONE1_UPPER_BOUND, COLUMN_DUMMY }); // filter out
        list.add(new String[] { INVALID_DATE_ZONE1, COLUMN_DUMMY });                        // filter out

        List<String[]> result = criteria.apply(list, 0);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(list.get(0), result.get(0));
        assertEquals(list.get(2), result.get(1));
    }

    @Test
    void testRangeCriteria_InvalidDateValue_To_Open() {
        RangeCriteria criteria = new RangeCriteria(VALID_DATE_FROM_ZONE1, null);
        criteria.setType(ColumnType.DATE);
        criteria.setDateFormat(VALID_FORMAT);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_DATE_IN_RANGE_ZONE1, COLUMN_DUMMY });                 // ok
        list.add(new String[] { null, COLUMN_DUMMY });                                      // filter out
        list.add(new String[] { VALID_DATE_NOT_IN_RANGE_ZONE1_LOWER_BOUND, COLUMN_DUMMY }); // filter out
        list.add(new String[] { VALID_DATE_NOT_IN_RANGE_ZONE1_UPPER_BOUND, COLUMN_DUMMY }); // ok
        list.add(new String[] { INVALID_DATE_ZONE1, COLUMN_DUMMY });                        // filter out

        List<String[]> result = criteria.apply(list, 0);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(list.get(0), result.get(0));
        assertEquals(list.get(3), result.get(1));
    }

    @Test
    void testRangeCriteria_InvalidDateValue_Format() {
        // todo: custom date format not implemented yet!
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRangeCriteria_InvalidDateValue_Format_FromValue() {
        RangeCriteria criteria = new RangeCriteria(INVALID_DATE_ZONE1, VALID_DATE_TO_ZONE1);
        criteria.setType(ColumnType.DATE);
        criteria.setDateFormat(VALID_FORMAT);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { null, COLUMN_DUMMY });

        assertThrows(InvalidInputException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testRangeCriteria_InvalidDateValue_Format_ToValue() {
        RangeCriteria criteria = new RangeCriteria(VALID_DATE_FROM_ZONE1, INVALID_DATE_ZONE1);
        criteria.setType(ColumnType.DATE);
        criteria.setDateFormat(VALID_FORMAT);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { null, COLUMN_DUMMY });

        assertThrows(InvalidInputException.class, () -> criteria.apply(list, 0));
    }

}
