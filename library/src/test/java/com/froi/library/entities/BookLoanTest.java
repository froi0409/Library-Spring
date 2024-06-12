package com.froi.library.entities;

import com.froi.library.enums.bookstatus.BookLoanStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.sql.Date;

public class BookLoanTest {
    
    private static final Integer ID = 1;
    private static final String BOOK = "978-9706860699";
    private static final String STUDENT = "201830121";
    private static final Date LOAN_DATE = java.sql.Date.valueOf(LocalDate.of(2024, Month.MAY, 5));
    private static final Double LOAN_TOTAL = 15.00;
    private static final Double DELAY_TOTAL = 0.00;
    private static final Date RETURNED_DATE = java.sql.Date.valueOf(LocalDate.of(2024, Month.MAY, 6));
    private static final BookLoanStatus STATUS = BookLoanStatus.IN_TIME;
    
    private static final Integer EXPECTED_ID = 1;
    private static final String EXPECTED_BOOK = "978-9706860699";
    private static final String EXPECTED_STUDENT = "201830121";
    private static final Date EXPECTED_LOAN_DATE = java.sql.Date.valueOf(LocalDate.of(2024, Month.MAY, 5));
    private static final Double EXPECTED_LOAN_TOTAL = 15.00;
    private static final Double EXPECTED_DELAY_TOTAL = 0.00;
    private static final Date EXPECTED_RETURNED_DATE = java.sql.Date.valueOf(LocalDate.of(2024, Month.MAY, 6));
    private static final BookLoanStatus EXPECTED_STATUS = BookLoanStatus.IN_TIME;
    
    @Test
    void testBookLoan() {
        // Arrange
        BookLoan bookLoan = new BookLoan();
        
        // Act
        bookLoan.setId(ID);
        bookLoan.setBook(BOOK);
        bookLoan.setStudent(STUDENT);
        bookLoan.setLoanDate(LOAN_DATE);
        bookLoan.setLoanTotal(LOAN_TOTAL);
        bookLoan.setDelayTotal(DELAY_TOTAL);
        bookLoan.setReturnedDate(RETURNED_DATE);
        bookLoan.setStatus(STATUS);
        
        // Assert
        assertEquals(EXPECTED_ID, bookLoan.getId());
        assertEquals(EXPECTED_BOOK, bookLoan.getBook());
        assertEquals(EXPECTED_STUDENT, bookLoan.getStudent());
        assertEquals(EXPECTED_LOAN_DATE, bookLoan.getLoanDate());
        assertEquals(EXPECTED_LOAN_TOTAL, bookLoan.getLoanTotal());
        assertEquals(EXPECTED_DELAY_TOTAL, bookLoan.getDelayTotal());
        assertEquals(EXPECTED_RETURNED_DATE, bookLoan.getReturnedDate());
        assertEquals(EXPECTED_STATUS, bookLoan.getStatus());
    }
    
}
