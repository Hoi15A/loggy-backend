package ch.zhaw.pm4.loganalyser.repository;

import ch.zhaw.pm4.loganalyser.model.log.LogService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogServiceRepository extends JpaRepository<LogService, Long> {
}
