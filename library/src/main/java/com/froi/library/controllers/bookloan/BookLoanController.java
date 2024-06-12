package com.froi.library.controllers.bookloan;

import com.froi.library.dto.bookloan.CreateBookLoanDTO;
import com.froi.library.dto.bookloan.ReturnLoanDTO;
import com.froi.library.dto.bookloan.RevenueResponseDTO;
import com.froi.library.entities.BookLoan;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.services.bookloan.BookLoanReportsService;
import com.froi.library.services.bookloan.BookLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/bookloan")
@CrossOrigin
public class BookLoanController {

    private BookLoanService bookLoanService;
    private BookLoanReportsService bookLoanReportsService;
    
    @Autowired
    public BookLoanController(BookLoanService bookLoanService, BookLoanReportsService bookLoanReportsService) {
        this.bookLoanService = bookLoanService;
        this.bookLoanReportsService = bookLoanReportsService;
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
