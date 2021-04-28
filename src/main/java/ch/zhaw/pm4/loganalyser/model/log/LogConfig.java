package ch.zhaw.pm4.loganalyser.model.log;

import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Map;

/**
 * Model object of a log config.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data @Builder
@Entity
@Table(name = "configs")
public class LogConfig {
    @Id
    private String name;

    private int columnCount;
    private int headerLength;
    private String separator;

    @ManyToMany
    private Map<Integer, ColumnComponent> columnComponents;
}
