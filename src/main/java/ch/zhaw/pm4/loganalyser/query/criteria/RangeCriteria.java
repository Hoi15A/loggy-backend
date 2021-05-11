package ch.zhaw.pm4.loganalyser.query.criteria;

import ch.zhaw.pm4.loganalyser.exception.InvalidColumnTypeException;
import ch.zhaw.pm4.loganalyser.exception.InvalidInputException;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.util.IP;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Criteria class that filters based on a user provided range
 */
@RequiredArgsConstructor
public class RangeCriteria implements Criteria {

    private final Logger logger = Logger.getLogger(RangeCriteria.class.getName());
    private static final String VALID_IP_REGEX = "(1?\\d{1,2}\\.){3}(1?\\d{1,2})"  // 0.0.0.0 - 199.199.199.199
                                                + "|(2[0-5]{2}\\.){3}(2[0-5]{2})"; // 200.200.200.200 - 255.255.255.255

    @Setter
    private ColumnType type = null;

    @Setter
    private String dateFormat;

    @Setter
    private boolean onlyDateInFormat;

    private final String from;
    private final String to;

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

    /*
     * NUMBER
     */

    private boolean isInNumberRange(String str) {
        if(str == null) return false;

        double input;

        try {
            input = Double.parseDouble(str);
        }  catch (Exception e) {
            return false;
        }

        var fromNumber = parseNumber(from, "from");
        var toNumber = parseNumber(to, "to");

        return isInputAfterNumber(input, fromNumber) && isInputBeforeNumber(input, toNumber);
    }

    private Double parseNumber(String str, String message) {
        try {
            if (str != null) return Double.parseDouble(str);
        } catch (Exception e) {
            throw new InvalidInputException(String.format("Unable to parse '%s' value as a number", message));
        }
        return null;
    }

    private boolean isInputAfterNumber(Double input, Double from) {
        if (from == null) return true;
        return input >= from;
    }

    private boolean isInputBeforeNumber(Double input, Double to) {
        if (to == null) return true;
        return input <= to;
    }

    /*
     * DATE
     */

    private boolean isInDateRange(String str) {
        try {
            var format = DateTimeFormatter.ofPattern(dateFormat);
            LocalDateTime date = parseColumn(str, format);
            var fromDate = parseDate(from, format);
            var toDate = parseDate(to, format);

            return date != null && isInputAfterDate(date, fromDate) && isInputBeforeDate(date, toDate);
        } catch (DateTimeParseException | IllegalArgumentException ex) {
            throw new InvalidInputException("Date format is not valid!");
        }
    }

    private LocalDateTime parseColumn(String str, DateTimeFormatter format) {
        LocalDateTime date = null;
        try {
            /*
             todo: question
             example 1:
                 from: dd/MM/yyyy
                 to: dd/MM/yyyy
                 str: dd/MM/yyyyTHH:mm:ss
             str throws here a DateTimeParseException (probably wrong)

             example 2:
                 from: dd/MM/yyyyTHH:mm:ss
                 to: dd/MM/yyyyTHH:mm:ss
                 str: dd/MM/yyyy
             str throws here a DateTimeParseException (probably ok or not a use case)
             */
            date = parseDate(str, format);
        } catch (DateTimeParseException ex) {
            logger.info(str + " was invalid!");
        }
        return date;
    }

    private LocalDateTime parseDate(String str, DateTimeFormatter format) {
        if (str == null) return null;
        return onlyDateInFormat
                ? LocalDate.parse(str, format).atStartOfDay()
                : LocalDateTime.parse(str, format);
    }

    private boolean isInputAfterDate(LocalDateTime input, LocalDateTime from) {
        if (from == null) return true;
        return input.isAfter(from);
    }

    private boolean isInputBeforeDate(LocalDateTime input, LocalDateTime to) {
        if (to == null) return true;
        return input.isBefore(to);
    }

    /*
     * IP
     */

    private boolean isIPInRange(String str) {
        if (str == null) return false;
        var isInRange = false;

        long inputIP;

        try {
            inputIP = IP.ipToLong(InetAddress.getByName(str));
        } catch (UnknownHostException e) {
            return false;
        }

        var fromIP = parseIP(from, "from");
        var toIP = parseIP(to, "to");

        isInRange = isInputAfterIP(inputIP, fromIP) && isInputBeforeIP(inputIP, toIP);
        return isInRange;
    }

    private Long parseIP(String str, String message) {
        var errMsg = "Unable to parse '" + message + "' value as an ip";
        try {
            if (str != null) {
                if (!str.matches(VALID_IP_REGEX)) throw new InvalidInputException(errMsg);
                return IP.ipToLong(InetAddress.getByName(str));
            }
        } catch (UnknownHostException e) {
            throw new InvalidInputException(errMsg);
        }
        return null;
    }

    private boolean isInputAfterIP(Long input, Long from) {
        if (from == null) return true;
        return input >= from;
    }

    private boolean isInputBeforeIP(Long input, Long to) {
        if (to == null) return true;
        return input <= to;
    }

}
