package com.example.users.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UsersModuleConfig {

	@Value("${resttemplate.connect.timeout}")
	private Long connectTimeout;
	@Value("${resttemplate.read.timeout}")
	private Long readTimeout;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.setConnectTimeout(Duration.ofMillis(connectTimeout))
				.setReadTimeout(Duration.ofMillis(readTimeout)).build();
	}
}