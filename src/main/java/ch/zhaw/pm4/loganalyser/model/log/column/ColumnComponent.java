package ch.zhaw.pm4.loganalyser.model.log.column;

import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ColumnComponent {
    private final String name;
    private final ColumnType columnType;
    private final Pattern format;
}
