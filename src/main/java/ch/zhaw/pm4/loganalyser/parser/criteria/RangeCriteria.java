package ch.zhaw.pm4.loganalyser.parser.criteria;

import ch.zhaw.pm4.loganalyser.exception.InvalidColumnTypeException;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.util.IP;
import lombok.RequiredArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Criteria class that filters based on a user provided range
 */
@RequiredArgsConstructor
public class RangeCriteria implements Criteria {

    private final Logger logger = Logger.getLogger(RangeCriteria.class.getName());

    private final ColumnType type;
    private final String from;
    private final String to;

    /**
     * Apply the RangeCriteria on a list
     * @param rows Input to be filtered
     * @param key Index of column in row that should be filtered on
     * @return Filtered List
     */
    @Override
    public List<String[]> apply(List<String[]> rows, int key) {
        List<String[]> filtered = new ArrayList<>();

        for (String[] row : rows) {
            switch (type) {
                case INTEGER:
                case DOUBLE:
                    if (isInNumberRange(row[key])) filtered.add(row);
                    break;
                case DATE:
                    if (isInDateRange(row[key])) filtered.add(row);
                    break;
                case IP:
                    try {
                        if (isIPInRange(row[key])) filtered.add(row);
                    } catch (UnknownHostException e) {
                        logger.log(Level.WARNING, "Unable to convert IP to string, ignoring it.", e);
                        e.printStackTrace();
                    }
                    break;
                default:
                    throw new InvalidColumnTypeException("An unsupported ColumnType was passed");
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
        /**
         * NGINX doesnt just output regular ISO dateformats from what I can tell.
         * As far as I can see parsing any arbitrary dateformat isnt a thing (excluding finding a lib for it)
         * Solution is to either only support nginx timestamps or:
         * https://stackoverflow.com/a/4024604/5457433
         * Support multiple by just trying predefined formats until it parses.
         *
         * TODO: Ask leo for opinion on that before wasting time.
         */
        return true;
    }

    private boolean isIPInRange(String str) throws UnknownHostException {
        var inputIP = IP.ipToLong(InetAddress.getByName(str));
        var fromIP = IP.ipToLong(InetAddress.getByName(from));
        var toIP = IP.ipToLong(InetAddress.getByName(to));

        return inputIP >= fromIP && inputIP <= toIP;
    }
}
