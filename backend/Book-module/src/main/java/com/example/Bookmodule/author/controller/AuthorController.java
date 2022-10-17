package com.example.Bookmodule.author.controller;

import com.example.Bookmodule.author.dto.CreateAuthorDto;
import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.author.service.AuthorService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/{oid}")
    public ResponseEntity<Void> addAuthor(@PathVariable("oid") ObjectId oid) {
        if (!authorService.insertAuthor(oid)) {
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
