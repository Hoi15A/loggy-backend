package ch.zhaw.pm4.loganalyser.query.criteria;

import ch.zhaw.pm4.loganalyser.exception.ExactCriteriaIsNullException;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Criteria to filter when the column is exactly the same as the given string
 */
@Getter
@RequiredArgsConstructor
public class ExactCriteria implements Criteria {

    private final String exact;
    @Setter
    private ColumnType type;
    @Setter
    private String dateFormat;

    @Override
    public List<String[]> apply(List<String[]> rows, int columnIndex) {
        if (exact == null) {
            throw new ExactCriteriaIsNullException("The exact value is not set");
        }
        if (type == ColumnType.DATE && dateFormat == null) {
            throw new ExactCriteriaIsNullException("The dateFormat value is not set");
        }

        if (type == ColumnType.DATE) {
            var dtfLogService = DateTimeFormatter.ofPattern(dateFormat, Locale.ENGLISH);

            var exactDateTime = LocalDateTime.parse(exact, dtfLogService);
            var exactDate = exactDateTime.toLocalDate();
            return rows.stream()
                    .filter(row -> {
                        try {
                            var dateTime = LocalDateTime.parse(row[columnIndex], dtfLogService);
                            var date = dateTime.toLocalDate();
                            return exactDate.isEqual(date);
                        } catch (DateTimeParseException e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        }
        return rows.stream()
                .filter(row -> row[columnIndex].equals(exact))
                .collect(Collectors.toList());
    }

}
