package ch.zhaw.pm4.loganalyser.model.log.column;

import lombok.*;

import javax.persistence.*;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder @Data
@Entity
@Table(name = "components")
public class ColumnComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private ColumnType columnType;
    private Pattern format;
}
