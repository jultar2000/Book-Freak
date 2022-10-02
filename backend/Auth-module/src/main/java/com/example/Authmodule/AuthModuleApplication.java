package com.example.Authmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@EnableAsync
public class AuthModuleApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthModuleApplication.class, args);
	}
}