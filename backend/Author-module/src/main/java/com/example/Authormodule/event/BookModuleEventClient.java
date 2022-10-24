package com.example.Authormodule.event;

import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "book-module")
public interface BookModuleEventClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/authors/{oid}")
    void insertAuthor(@PathVariable("oid") ObjectId oid);

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/v1/authors/{Id}")
    void deleteAuthor(@PathVariable("Id") String Id);
}
