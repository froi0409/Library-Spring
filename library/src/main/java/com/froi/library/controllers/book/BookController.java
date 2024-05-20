package com.froi.library.controllers.book;

import com.froi.library.dto.book.CreateBookRequestDTO;
import com.froi.library.entities.Book;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.services.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/book")
public class BookController {
    private BookService bookService;
    
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Book> createBook(@RequestBody CreateBookRequestDTO newBook) throws DuplicatedEntityException, EntitySyntaxException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.createBook(newBook));
    }
    
    @GetMapping(path = "/{bookCode}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Book> findBookByCode(@PathVariable String bookCode) throws EntityNotFoundException {
        return ResponseEntity
                .ok(bookService.getOneBookByCode(bookCode));
    }
    
}
