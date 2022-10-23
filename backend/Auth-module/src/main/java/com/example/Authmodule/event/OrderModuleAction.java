package com.example.Authmodule.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class OrderModuleAction {

    RestTemplate restTemplate;

    @Autowired
    public OrderModuleAction(@Value("${zuul.routes.order-module.url}") String url) {
        restTemplate = new RestTemplateBuilder().rootUri(url).build();
    }

    public void insertUser(UserDto userDto) {
        restTemplate.postForLocation("/api/v1/users", userDto);
    }

    public void deleteUser(String id) {
        restTemplate.delete("/api/v1/users/{Id}", id);
    }
}
