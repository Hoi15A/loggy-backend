package ch.zhaw.pm4.loganalyser.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class overrides the default cors configuration.
 */
@Configuration
@EnableWebMvc
public class CORSAdvice implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "PUT", "OPTIONS", "DELETE", "POST", "PATCH")
                .allowedOrigins("http://localhost:8081", "https://zhaw.neat.moe", "http://direct.zhaw.neat.moe")
                .allowedHeaders("Origin", "Content-Type", "Accept");
    }

}
