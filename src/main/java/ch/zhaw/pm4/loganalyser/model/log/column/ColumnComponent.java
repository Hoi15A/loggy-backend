package ch.zhaw.pm4.loganalyser.model.log.column;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.regex.Pattern;

@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Entity
public class ColumnComponent {
    @Id
    private String name;
    private ColumnType columnType;
    private Pattern format;
}
