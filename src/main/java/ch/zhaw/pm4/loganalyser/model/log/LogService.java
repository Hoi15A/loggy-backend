package ch.zhaw.pm4.loganalyser.model.log;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.net.URI;

/**
 * Model object of a log service.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data @Builder
@Entity
@Table(name = "services")
public class LogService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private LogConfig logConfig;

    private String logDirectory;
    private String name;
    private String description;
    private URI image;
    private LogServiceLocation logServiceLocation;

}
