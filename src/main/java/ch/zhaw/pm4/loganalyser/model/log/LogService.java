package ch.zhaw.pm4.loganalyser.model.log;

import lombok.*;

import javax.persistence.*;
import java.net.URI;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data @Builder
@Entity
@Table(name = "services")
public class LogService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private LogConfig logConfig;
    private String logDirectory;
    private String name;
    private String description;
    private URI image;
    private LogServiceLocation logServiceLocation;
}
