package ch.zhaw.pm4.loganalyser.parser.criteria;

import ch.zhaw.pm4.loganalyser.exception.InvalidColumnTypeException;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.util.IP;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Criteria class that filters based on a user provided range
 */
@RequiredArgsConstructor
public class RangeCriteria implements Criteria {

    private final Logger logger = Logger.getLogger(RangeCriteria.class.getName());

    @Setter
    private ColumnType type = null;
    private final String from;
    private final String to;

    private final List<String> dateFormatStrings = Arrays.asList(
            "dd/MMM/yyyy:HH:mm:ss Z",   // Nginx Format
            "yyyy-MM-dd'T'HH:mm:ss'Z'", // ISO 8601 UTC
            "yyyy-MM-dd'T'HH:mm:ssZ",   // ISO 8601 non UTC
            "DD-MM-YYYY"
    );

    /**
     * Apply the RangeCriteria on a list
     * @param rows Input to be filtered
     * @param columnIndex Index of column in row that should be filtered on
     * @return Filtered List
     */
    @Override
    public List<String[]> apply(List<String[]> rows, int columnIndex) {
        if (type == null) throw new UnsupportedOperationException("No ColumnType passed in setter");

        List<String[]> filtered = new ArrayList<>();

        for (String[] row : rows) {
            switch (type) {
                case INTEGER:
                case DOUBLE:
                    if (isInNumberRange(row[columnIndex])) filtered.add(row);
                    break;
                case DATE:
                    if (isInDateRange(row[columnIndex])) filtered.add(row);
                    break;
                case IP:
                    if (isIPInRange(row[columnIndex])) filtered.add(row);
                    break;
                default:
                    throw new InvalidColumnTypeException("An unsupported ColumnType was passed: " + type);
            }
        }

        return filtered;
    }

    private boolean isInNumberRange(String str) {
        var input = Double.parseDouble(str);
        var fromInt = Double.parseDouble(from);
        var toInt = Double.parseDouble(to);

        return input >= fromInt && input <= toInt;
    }

    private boolean isInDateRange(String str) {
        var isInRange = false;

        for (String formatString : dateFormatStrings) {
            try {
                var format = new SimpleDateFormat(formatString);
                var date = format.parse(str);
                var fromDate = format.parse(from);
                var toDate = format.parse(to);
                isInRange = date.compareTo(fromDate) > 0 && date.compareTo(toDate) < 0;
                break;
            } catch (ParseException ignored) {
               logger.info("Attempted to parse date and failed");
            }
        }

        return isInRange;
    }

    private boolean isIPInRange(String str) {
        var isInRange = false;

        try {
            var inputIP = IP.ipToLong(InetAddress.getByName(str));
            var fromIP = IP.ipToLong(InetAddress.getByName(from));
            var toIP = IP.ipToLong(InetAddress.getByName(to));

            isInRange = inputIP >= fromIP && inputIP <= toIP;
        } catch (UnknownHostException e) {
            logger.log(Level.WARNING, "Unable to convert one or more IP(s) to string, ignoring.", e);
        }

        return isInRange;
    }
}
