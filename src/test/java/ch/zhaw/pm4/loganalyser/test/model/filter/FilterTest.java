package ch.zhaw.pm4.loganalyser.test.model.filter;

import ch.zhaw.pm4.loganalyser.model.filter.DateFilter;
import ch.zhaw.pm4.loganalyser.model.filter.Filter;
import ch.zhaw.pm4.loganalyser.model.filter.MessageFilter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class FilterTest {

    @Test
    void messageTest() {
        String[] message = {"Service started", "Error during runtime xyz", "Service stopped"};

        Filter messageFilter = new MessageFilter("Service");
        assertTrue(messageFilter.apply(message[0]));
        assertFalse(messageFilter.apply(message[1]));
        assertTrue(messageFilter.apply(message[2]));

        ((MessageFilter) messageFilter).setMessage("started");
        assertTrue(messageFilter.apply(message[0]));
        assertFalse(messageFilter.apply(message[1]));
        assertFalse(messageFilter.apply(message[2]));
    }

    @Test
    void dateTest() {
        String[] dateDefault = {"1997-02-02", "1997-01-02", "1996-05-05", "2021-03-02", "2021-03-20"};
        LocalDate begin = LocalDate.of(1997, Month.JANUARY, 2);
        LocalDate end = LocalDate.of(2021, Month.MARCH, 2);

        Filter dateFilter = new DateFilter(begin, end, DateTimeFormatter.ISO_LOCAL_DATE);
        assertTrue(dateFilter.apply(dateDefault[0]));
        assertTrue(dateFilter.apply(dateDefault[1]));
        assertFalse(dateFilter.apply(dateDefault[2]));
        assertTrue(dateFilter.apply(dateDefault[3]));
        assertFalse(dateFilter.apply(dateDefault[4]));

        String[] dateGerman = {"02.02.1997", "02.01.1997", "05.05.1996", "02.03.2021", "20.03.2021"};
        ((DateFilter) dateFilter).setDateTimeFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        assertTrue(dateFilter.apply(dateGerman[0]));
        assertTrue(dateFilter.apply(dateGerman[1]));
        assertFalse(dateFilter.apply(dateGerman[2]));
        assertTrue(dateFilter.apply(dateGerman[3]));
        assertFalse(dateFilter.apply(dateGerman[4]));
    }
}
