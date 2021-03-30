package ch.zhaw.pm4.loganalyser.repository;

import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogConfigRepository extends JpaRepository<LogConfig, String> {
}
