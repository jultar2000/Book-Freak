package com.example.Ordermodule.book.controller;

import com.example.Ordermodule.book.dto.CreateBookDto;
import com.example.Ordermodule.book.entity.Book;
import com.example.Ordermodule.book.service.BookService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("insert/{oid}")
    public ResponseEntity<Void> addBook(@PathVariable("oid") ObjectId oid) {
        if (!bookService.insertBoot(oid)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") String id) {
        if (!bookService.deleteBook(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }
}
