package ch.zhaw.pm4.loganalyser.model.log.column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder @Data
@Entity
@Table(name = "components")
public class ColumnComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private ColumnType columnType;
    private String format;
}
