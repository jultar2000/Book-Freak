package com.example.Authormodule.event;

import com.example.Authormodule.entity.Author;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "book-module")
public interface EventClient {

    @RequestMapping(method = RequestMethod.POST, value = "api/v1/authors")
    void insertAuthor(Author author);

    @RequestMapping(method = RequestMethod.DELETE, value = "api/v1/authors/{Id}")
    void deleteAuthor(@PathVariable("Id") String Id);
}
