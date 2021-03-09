package ch.zhaw.pm4.loganalyser.model.log;

import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class LogConfig {
    private final String name;
    private final int columnCount;
    private final int headerLength;
    private final String separator;
    private final List<ColumnComponent> columnComponents;
}
