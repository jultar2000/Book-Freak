package com.example.Bookmodule.book.event;

import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "order-module")
public interface OrderModuleEventClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/books/insert/{oid}")
    void insertBook(@PathVariable("oid") ObjectId oid);

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/v1/books/{Id}")
    void deleteBook(@PathVariable("Id") String oid);

}
