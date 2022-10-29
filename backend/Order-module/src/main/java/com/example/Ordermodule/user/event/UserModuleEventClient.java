package com.example.Ordermodule.user.event;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "user-module")
public interface UserModuleEventClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/api/v1/users/{username}/user-funds")
    void updateUser(@PathVariable("username") String username, double funds);

}
