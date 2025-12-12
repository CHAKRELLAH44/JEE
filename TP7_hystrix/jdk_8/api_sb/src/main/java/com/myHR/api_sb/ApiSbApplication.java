package com.myHR.api_sb;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCircuitBreaker
@EnableHystrixDashboard
public class ApiSbApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSbApplication.class, args);
	}

	// 🔥 Très important : Servlet Hystrix pour rendre le Dashboard compatible
	@Bean
	public ServletRegistrationBean<HystrixMetricsStreamServlet> hystrixStreamServlet() {
		HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
		ServletRegistrationBean<HystrixMetricsStreamServlet> registrationBean =
				new ServletRegistrationBean<>(streamServlet, "/hystrix.stream");

		registrationBean.setName("HystrixMetricsStreamServlet");
		registrationBean.setLoadOnStartup(1);

		return registrationBean;
	}
}
