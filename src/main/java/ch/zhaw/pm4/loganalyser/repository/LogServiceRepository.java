package ch.zhaw.pm4.loganalyser.repository;

import ch.zhaw.pm4.loganalyser.model.log.LogService;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for the log services.
 */
public interface LogServiceRepository extends JpaRepository<LogService, Long> {

}
