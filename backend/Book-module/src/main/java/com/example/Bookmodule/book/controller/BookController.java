package com.example.Bookmodule.book.controller;


import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.author.service.AuthorService;
import com.example.Bookmodule.book.dto.BooksKeywordRequest;
import com.example.Bookmodule.book.dto.BooksRatingRequest;
import com.example.Bookmodule.book.dto.CreateBookDto;
import com.example.Bookmodule.book.dto.GetBooksDto;
import com.example.Bookmodule.book.entity.Book;
import com.example.Bookmodule.book.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final ModelMapper mapper;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService, ModelMapper mapper) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.mapper = mapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetBooksDto>> getAllBooks() {
        List<GetBooksDto> booksDto =
                bookService
                        .findAllBooks()
                        .stream()
                        .map(book ->
                                mapper.map(book, GetBooksDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(booksDto);
    }

    @GetMapping("/authors/id/{authorId}")
    public ResponseEntity<List<GetBooksDto>> getBooksByAuthor(@PathVariable("authorId") String authorId) {
        Author author = authorService.findAuthor(authorId);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        List<GetBooksDto> booksDto =
                bookService
                        .findBooksByAuthor(author)
                        .stream()
                        .map(book ->
                                mapper.map(book, GetBooksDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(booksDto);
    }

    @GetMapping("/keyword")
    public ResponseEntity<List<GetBooksDto>> getBooksByKeyword(@RequestBody BooksKeywordRequest request) {
        List<GetBooksDto> booksDto =
                bookService
                        .findBooksByKeyword(request.getLimit(), request.getSkip(), request.getKeyword())
                        .stream()
                        .map(book ->
                                mapper.map(book, GetBooksDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(booksDto);
    }

    @GetMapping("/rating")
    public ResponseEntity<List<GetBooksDto>> findBooksByRating(@RequestBody BooksRatingRequest request) {
        List<GetBooksDto> booksDto =
                bookService
                        .findBooksByRating(request.getLimit(), request.getSkip())
                        .stream()
                        .map(book ->
                                mapper.map(book, GetBooksDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(booksDto);
    }

    @PostMapping("/authors/id/{authorId}")
    public ResponseEntity<Void> addBook(@PathVariable("authorId") String authorId,
                                        @RequestBody CreateBookDto request) {
        Author author = authorService.findAuthor(authorId);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        Book book = bookService.insertBook(mapper.map(request, Book.class));
        if (book == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
