package ch.zhaw.pm4.LogAnalyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LogAnalyserApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(LogAnalyserApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(LogAnalyserApplication.class, args);
	}

	@RequestMapping("/")
	public String hello() {
		return "Hallo Welt!";
	}

}
