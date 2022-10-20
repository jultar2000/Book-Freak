package com.example.Authmodule.event;

import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "order-module")
public interface OrderModuleEventClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/users/{oid}/{username}")
    void insertUser(@PathVariable("oid") ObjectId oid, @PathVariable("oid") String username);

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/v1/users/{Id}")
    void deleteUser(@PathVariable("Id") String id);

}
