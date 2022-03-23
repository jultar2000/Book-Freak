package com.example.Authormodule.controller;

import com.example.Authormodule.dto.CreateAuthorDto;
import com.example.Authormodule.dto.AuthorNameSurnameRequest;
import com.example.Authormodule.dto.GetAuthorsDto;
import com.example.Authormodule.entity.Author;
import com.example.Authormodule.service.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final ModelMapper mapper;

    @Autowired
    public AuthorController(AuthorService authorService, ModelMapper mapper) {
        this.authorService = authorService;
        this.mapper = mapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetAuthorsDto>> getAllAuthors() {
        List<GetAuthorsDto> authorsDto =
                authorService.findAllAuthors()
                        .stream()
                        .map(author ->
                                mapper.map(author, GetAuthorsDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(authorsDto);
    }

    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<GetAuthorsDto>> getAuthorByNationality(@PathVariable("nationality")
                                                                              String nationality) {
        List<GetAuthorsDto> authorsDto =
                authorService.findAuthorsByNationality(nationality)
                        .stream()
                        .map(author ->
                                mapper.map(author, GetAuthorsDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(authorsDto);
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<GetAuthorsDto>> getAuthorsBornAfterYear(@PathVariable("year") int year) {
        List<GetAuthorsDto> authorsDto =
                authorService.findAuthorsBornAfterYear(year)
                        .stream()
                        .map(author ->
                                mapper.map(author, GetAuthorsDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(authorsDto);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable("id") String id) {
        Author author = authorService.findAuthor(id);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    @GetMapping("/nameAndSurname")
    public ResponseEntity<Author> getAuthorByNameAndSurname(@RequestBody AuthorNameSurnameRequest request) {
        Author author = authorService
                .findAuthorByNameAndSurname(request.getName(), request.getSurname());
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    @PostMapping("")
    public ResponseEntity<Void> addAuthor(@RequestBody CreateAuthorDto request) {
        Author author = authorService.createAuthor(mapper.map(request, Author.class));
        if (author == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAuthor(@PathVariable("id") String id,
                                             @RequestBody Boolean isAlive) {
        if (!authorService.updateAuthorFields(id, isAlive)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") String id) {
        if (!authorService.deleteAuthor(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }
}
