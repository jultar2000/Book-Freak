package com.example.Bookmodule.book.controller;


import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.author.service.AuthorService;
import com.example.Bookmodule.book.dto.*;
import com.example.Bookmodule.book.entity.Book;
import com.example.Bookmodule.book.entity.Comment;
import com.example.Bookmodule.book.service.BookService;
import com.example.Bookmodule.book.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


//TODO> Change all objects to optional and use other structure to check for existence
//TODO> Change all return of  db inserting to boolean
@RestController
@RequestMapping("api/v1/books")
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;
    private final AuthorService authorService;
    private final ModelMapper mapper;

    @Autowired
    public BookController(BookService bookService, CommentService commentService, AuthorService authorService, ModelMapper mapper) {
        this.bookService = bookService;
        this.commentService = commentService;
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

    @GetMapping("/keyword")
    public ResponseEntity<List<GetBooksDto>> getBooksByKeyword(@RequestBody BooksStringParameterRequest request) {
        List<GetBooksDto> booksDto =
                bookService
                        .findBooksByKeyword(request.getLimit(), request.getSkip(), request.getParameter())
                        .stream()
                        .map(book ->
                                mapper.map(book, GetBooksDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(booksDto);
    }

    @GetMapping("/genre")
    public ResponseEntity<List<GetBooksDto>> getBooksByGenre(@RequestBody BooksStringParameterRequest request) {
        List<GetBooksDto> booksDto =
                bookService
                        .findBooksByGenre(request.getLimit(), request.getSkip(), request.getParameter())
                        .stream()
                        .map(book ->
                                mapper.map(book, GetBooksDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(booksDto);
    }

    @GetMapping("/rating")
    public ResponseEntity<List<GetBooksDto>> getBooksByRating(@RequestBody BooksRatingRequest request) {
        List<GetBooksDto> booksDto =
                bookService
                        .findBooksByRating(request.getLimit(), request.getSkip())
                        .stream()
                        .map(book ->
                                mapper.map(book, GetBooksDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(booksDto);
    }

    @DeleteMapping("/id/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable("bookId") String bookId) {
        if (!bookService.deleteBook(bookId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/authors/id/{authorId}")
    public ResponseEntity<List<GetBooksDto>> getBooksByAuthor(@PathVariable("authorId") String authorId,
                                                              @RequestBody BooksRatingRequest request) {
        Author author = authorService.findAuthor(authorId);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        List<GetBooksDto> booksDto =
                bookService
                        .findBooksByAuthor(request.getLimit(), request.getSkip(), author)
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
        Book book = bookService.insertBook(mapper.map(request, Book.class), author);
        if (book == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/id/{bookId}/comments")
    public ResponseEntity<Void> addComment(@PathVariable("bookId") String bookId,
                                           @RequestBody String text) {
        Book book = bookService.findBook(bookId);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        Comment comment = commentService.insertComment(text);
        if (comment == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/id/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") String commentId) {
        if (!commentService.deleteComment(commentId)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/comments/id/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") String commentId,
                                              @RequestBody String text) {
        if (!commentService.updateComment(commentId, text)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}