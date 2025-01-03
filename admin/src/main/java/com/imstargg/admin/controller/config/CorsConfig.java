package com.imstargg.admin.controller.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(CorsConfig.class);

    private final List<String> allowedOrigins;

    public CorsConfig(@Value("${app.cors.allowed-origins:http://localhost:3000}") List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("CORS Allowed-Origin: {}", allowedOrigins);
        registry
                .addMapping("/**")
                .allowedOrigins(allowedOrigins.toArray(String[]::new))
                .allowCredentials(true)
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.PATCH.name()
                )
                .allowedHeaders("*")
                .exposedHeaders("*")
                .maxAge(1800)
                ;
    }
}
