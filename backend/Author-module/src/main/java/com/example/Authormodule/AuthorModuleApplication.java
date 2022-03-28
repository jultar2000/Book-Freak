package com.example.Authormodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AuthorModuleApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthorModuleApplication.class, args);
	}
}
