package com.froi.library.controllers.bookloan;

import com.froi.library.dto.bookloan.LoansByDegreeResponseDTO;
import com.froi.library.dto.bookloan.ReturnLoanDTO;
import com.froi.library.dto.bookloan.RevenueResponseDTO;
import com.froi.library.entities.BookLoan;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.services.bookloan.BookLoanReportsService;
import com.froi.library.services.bookloan.BookLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/bookloan")
@PreAuthorize("hasRole('LIBRARIAN')")
@CrossOrigin
public class BookLoanReportsController {
    private BookLoanService bookLoanService;
    private BookLoanReportsService bookLoanReportsService;
    
    @Autowired
    public BookLoanReportsController(BookLoanService bookLoanService, BookLoanReportsService bookLoanReportsService) {
        this.bookLoanService = bookLoanService;
        this.bookLoanReportsService = bookLoanReportsService;
    }
    
    @GetMapping(path = "/byStudent/{studentId}/{date}")
    public ResponseEntity<List<BookLoan>> findBookLoansByStudent(@PathVariable String studentId, @PathVariable String date) throws EntityNotFoundException {
        return ResponseEntity
                .ok(bookLoanService.findNoReturnedByStudent(studentId, date));
    }
    
    @GetMapping(path = "/{loanId}/{returnDate}")
    public ResponseEntity<BookLoan> findBLoanBookById(@PathVariable String loanId, @PathVariable String returnDate) throws EntitySyntaxException, EntityNotFoundException {
        return ResponseEntity
                .ok(bookLoanService.findById(loanId, returnDate));
    }
    
    @PostMapping(path = "/return")
    public ResponseEntity<Boolean> returnBookLoan(@RequestBody ReturnLoanDTO returnLoan) throws EntityNotFoundException, EntitySyntaxException {
        return ResponseEntity
                .ok(bookLoanService.returnLoan(returnLoan));
    }
    
    @GetMapping(path = "/today/{date}")
    public ResponseEntity<List<Map<String, Object>>> todayLoans(@PathVariable String date) throws EntitySyntaxException {
        return ResponseEntity
                .ok(bookLoanReportsService.findBookLoansDueToday(date));
    }
    
    @GetMapping(path = "/overdueLoans/{date}")
    public ResponseEntity<List<Map<String, Object>>> findOverdueBookLoans(@PathVariable String date) throws EntitySyntaxException {
        return ResponseEntity
                .ok(bookLoanReportsService.findOverdueBookLoans(date));
    }
    
    @GetMapping(path = "/revenue/{startDate}/{endDate}")
    public ResponseEntity<RevenueResponseDTO> findTotalRevenueBetweenDates(@PathVariable String startDate, @PathVariable String endDate) throws EntitySyntaxException {
        return ResponseEntity
                .ok(bookLoanReportsService.findTotalRevenueBetweenDates(startDate, endDate));
    }
    
    @GetMapping(path = "/revenue")
    public ResponseEntity<RevenueResponseDTO> findTotalRevenue() {
        return ResponseEntity
                .ok(bookLoanReportsService.findTotalRevenue());
    }
    
    @GetMapping(path = "/degreeTopLoans")
    public ResponseEntity<LoansByDegreeResponseDTO> findDegreeWithMostLoans() throws EntityNotFoundException {
        return ResponseEntity
                .ok(bookLoanReportsService.findDegreeWithMostLoans());
    }
    
    @GetMapping(path = "/degreeTopLoans/{startDate}/{endDate}")
    public ResponseEntity<LoansByDegreeResponseDTO> findDegreeWithMostLoansBetweenDates(@PathVariable String startDate, @PathVariable String endDate) throws EntityNotFoundException, EntitySyntaxException {
        return ResponseEntity
                .ok(bookLoanReportsService.findDegreeWithMostLoansBetweenDate(startDate, endDate));
    }
    
    @GetMapping(path = "/overduePaymentByStudent/{studentId}")
    public ResponseEntity<List<BookLoan>> findOverduePaymentByStudentBetweenDates(@PathVariable String studentId) {
        return ResponseEntity
                .ok(bookLoanReportsService.findOverduePaymentByStudent(studentId));
    }
    
    @GetMapping(path = "/overduePaymentByStudent/{studentId}/{startDate}/{endDate}")
    public ResponseEntity<List<BookLoan>> findOverduePaymentByStudentBetweenDates(@PathVariable String studentId, @PathVariable String startDate, @PathVariable String endDate) throws EntitySyntaxException {
        return ResponseEntity
                .ok(bookLoanReportsService.findOverduePaymentByStudentBetweenDates(studentId, startDate, endDate));
    }
}
