package com.example.Bookmodule.author.controller;

import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.author.service.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final ModelMapper mapper;

    @Autowired
    public AuthorController(AuthorService authorService, ModelMapper mapper) {
        this.authorService = authorService;
        this.mapper = mapper;
    }

    @PostMapping("")
    public ResponseEntity<Void> addAuthor(@RequestBody Author request_author) {
        if (!authorService.insertAuthor(mapper.map(request_author, Author.class))) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") String id) {
        if (!authorService.deleteAuthor(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }
}
