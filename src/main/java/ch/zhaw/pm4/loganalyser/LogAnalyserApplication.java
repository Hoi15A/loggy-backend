package ch.zhaw.pm4.loganalyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Initializer class of the project.
 */
@SpringBootApplication
public class LogAnalyserApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(LogAnalyserApplication.class);
	}

	/**
	 * Entrypoint of the application.
	 * @param args as String[]
	 */
	public static void main(String[] args) {
		SpringApplication.run(LogAnalyserApplication.class, args);
	}

}
