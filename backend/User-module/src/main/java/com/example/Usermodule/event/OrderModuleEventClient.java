package com.example.Usermodule.event;

import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "order-module")
public interface OrderModuleEventClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/addresses/{oid}")
    void insertAddress(@PathVariable("oid") ObjectId oid);

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/v1/addresses/{Id}")
    void deleteAddress(@PathVariable("Id") String id);

    @RequestMapping(method = RequestMethod.PUT, value = "/api/v1/users/{username}")
    void updateUser(@PathVariable("username") String username, double funds);

}
