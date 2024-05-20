package com.froi.library.controllers.bookloan;

import com.froi.library.dto.bookloan.CreateBookLoanDTO;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.services.bookloan.BookLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/bookloan")
public class BookLoanController {

    private BookLoanService bookLoanService;
    
    @Autowired
    public BookLoanController(BookLoanService bookLoanService) {
        this.bookLoanService = bookLoanService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Boolean> createBookLoan(@RequestBody CreateBookLoanDTO newLoan) throws DenegatedActionException, EntityNotFoundException, EntitySyntaxException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookLoanService.createLoan(newLoan));
    }

    @GetMapping(path = "/availability/{bookId}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Integer> getBookAvailability(@PathVariable String bookId) {
        return ResponseEntity
                .ok(bookLoanService.checkAvailability(bookId));
    }
}
