package ch.zhaw.pm4.loganalyser.repository;

import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnComponentRepository extends JpaRepository<ColumnComponent, Long> {
}
