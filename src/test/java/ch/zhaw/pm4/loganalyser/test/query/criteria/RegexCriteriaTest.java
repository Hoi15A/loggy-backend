package ch.zhaw.pm4.loganalyser.test.query.criteria;

import ch.zhaw.pm4.loganalyser.query.criteria.RegexCriteria;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegexCriteriaTest {

    private static final String COLUMN_DUMMY = "TEST";

    private static final String REGEX_DUMMY = "";
    private static final String VALID_REGEX = "(1?\\d{1,2}\\.){3}(1?\\d{1,2})"  // 0.0.0.0 - 199.199.199.199
                                             + "|(2[0-5]{2}\\.){3}(2[0-5]{2})"; // 200.200.200.200 - 255.255.255.255

    private static final String VALID_VALUE = "192.186.0.1";
    private static final String INVALID_VALUE_RANGE = "300.186.0.1";
    private static final String INVALID_VALUE_PATTERN = "300.186.0";
    private static final String INVALID_VALUE_CHARACTERS = "192.186.0.x";


    /* ****************************************************************************************************************
     * POSITIVE TESTS
     * ****************************************************************************************************************/
    
    @Test
    void testRegexCriteria() {
        RegexCriteria criteria = new RegexCriteria(VALID_REGEX);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { INVALID_VALUE_RANGE, COLUMN_DUMMY });
        list.add(new String[] { VALID_VALUE, COLUMN_DUMMY });
        list.add(new String[] { INVALID_VALUE_PATTERN, COLUMN_DUMMY });
        list.add(new String[] { INVALID_VALUE_CHARACTERS, COLUMN_DUMMY });

        List<String[]> result = criteria.apply(list, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(list.get(1), result.get(0));
    }

    /* ****************************************************************************************************************
     * NEGATIVE TESTS
     * ****************************************************************************************************************/

    @Test
    void testRegexCriteria_Regex_Null() {
        RegexCriteria criteria = new RegexCriteria(null);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { INVALID_VALUE_RANGE, COLUMN_DUMMY });
        list.add(new String[] { VALID_VALUE, COLUMN_DUMMY });
        list.add(new String[] { INVALID_VALUE_PATTERN, COLUMN_DUMMY });
        list.add(new String[] { INVALID_VALUE_CHARACTERS, COLUMN_DUMMY });

        assertThrows(NullPointerException.class, () -> criteria.apply(list, 0));
    }

    @Test
    void testRegexCriteria_NullListArgument() {
        RegexCriteria criteria = new RegexCriteria(REGEX_DUMMY);
        assertThrows(NullPointerException.class, () -> criteria.apply(null, 0));
    }

    @Test
    void testRegexCriteria_InvalidColumnIndex_IndexOutOfBound_Negative() {
        RegexCriteria criteria = new RegexCriteria(REGEX_DUMMY);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_VALUE, COLUMN_DUMMY });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> criteria.apply(list, -1));
    }

    @Test
    void testRegexCriteria_InvalidColumnIndex_IndexOutOfBound_Positive() {
        RegexCriteria criteria = new RegexCriteria(REGEX_DUMMY);

        List<String[]> list = new ArrayList<>();
        list.add(new String[] { VALID_VALUE, COLUMN_DUMMY });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> criteria.apply(list, 2));
    }

}
