package com.example.Authmodule.event;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "book-module")
public interface EventClient {
    @RequestMapping(method = RequestMethod.POST, value = "api/v1/user")
    void insertCurrentUser();
}
