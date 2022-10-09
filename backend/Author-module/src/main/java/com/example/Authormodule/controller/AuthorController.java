package com.example.Authormodule.controller;

import com.example.Authormodule.dto.CreateAuthorDto;
import com.example.Authormodule.dto.AuthorSimpleRequest;
import com.example.Authormodule.dto.GetAuthorDto;
import com.example.Authormodule.entity.Author;
import com.example.Authormodule.event.BookModuleEventClient;
import com.example.Authormodule.service.AuthorService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private final ModelMapper mapper;

    private final BookModuleEventClient bookModuleEventClient;

    @GetMapping("/all")
    public ResponseEntity<List<GetAuthorDto>> getAllAuthors() {
        List<GetAuthorDto> authorsDto =
                authorService.findAllAuthors()
                        .stream()
                        .map(author ->
                                mapper.map(author, GetAuthorDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(authorsDto);
    }

    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<GetAuthorDto>> getAuthorByNationality(@PathVariable("nationality")
                                                                              String nationality) {
        List<GetAuthorDto> authorsDto =
                authorService.findAuthorsByNationality(nationality)
                        .stream()
                        .map(author ->
                                mapper.map(author, GetAuthorDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(authorsDto);
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<GetAuthorDto>> getAuthorsBornAfterYear(@PathVariable("year") int year) {
        List<GetAuthorDto> authorsDto =
                authorService.findAuthorsBornAfterYear(year)
                        .stream()
                        .map(author ->
                                mapper.map(author, GetAuthorDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(authorsDto);
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<Author> getAuthor(@PathVariable("authorId") String authorId) {
        Author author = authorService.findAuthor(authorId);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    @GetMapping("/name-and-surname")
    public ResponseEntity<Author> getAuthorByNameAndSurname(@RequestBody AuthorSimpleRequest request) {
        Author author = authorService
                .findAuthorByNameAndSurname(request.getName(), request.getSurname());
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    @PostMapping("")
    public ResponseEntity<Void> addAuthor(@RequestBody CreateAuthorDto request) {
        Author author = mapper.map(request, Author.class);
        if (!authorService.createAuthor(author)) {
            return ResponseEntity.badRequest().build();
        }
        bookModuleEventClient.insertAuthor(author.getOid());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<Void> updateAuthor(@PathVariable("authorId") String authorId,
                                             @RequestBody Boolean isAlive) {
        if (!authorService.updateAuthorFields(authorId, isAlive)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("authorId") String authorId) {
        if (!authorService.deleteAuthor(authorId)) {
            return ResponseEntity.notFound().build();
        }
        bookModuleEventClient.deleteAuthor(authorId);
        return ResponseEntity.accepted().build();
    }
}
