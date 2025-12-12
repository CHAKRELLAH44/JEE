package com.myhr.api_sb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableCircuitBreaker   // Activates Hystrix
@EnableHystrixDashboard // Activates the Dashboard
public class ApiSbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiSbApplication.class, args);
    }

}