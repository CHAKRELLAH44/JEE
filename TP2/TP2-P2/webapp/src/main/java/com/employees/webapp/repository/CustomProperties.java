package com.employees.webapp.repository;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.employees.webapp")
public class CustomProperties {
    private String apiUrl;
}
//Lit la variable com.employees.webapp.apiUrl depuis application.properties