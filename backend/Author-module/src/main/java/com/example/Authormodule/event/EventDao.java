package com.example.Authormodule.event;

import com.example.Authormodule.entity.Author;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EventDao {

    private final RestTemplate restTemplate;

    @Autowired
    public EventDao(@Value("${books.url}") String baseUrl) {
        restTemplate = new RestTemplateBuilder().rootUri(baseUrl).build();
    }

    public void deleteAuthor(ObjectId oid){
        restTemplate.delete("/authors/{Id}", oid);
    }

    public void createAuthor(Author author) {
        System.out.println(author.getOid());
        restTemplate.postForLocation("/authors", author);
    }
}
