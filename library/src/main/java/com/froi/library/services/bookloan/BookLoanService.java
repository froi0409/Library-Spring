package com.froi.library.services.bookloan;

import com.froi.library.dto.bookloan.CreateBookLoanDTO;
import com.froi.library.dto.bookloan.ReturnLoanDTO;
import com.froi.library.entities.BookLoan;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;

public interface BookLoanService {
    boolean createLoan(CreateBookLoanDTO newLoan) throws EntityNotFoundException, DenegatedActionException, EntitySyntaxException;
    
    Integer checkAvailability(String bookId);
    
    boolean returnLoan(ReturnLoanDTO returnLoan) throws EntityNotFoundException, EntitySyntaxException;
    
    Page<BookLoan> findAll(Pageable pageable) throws EntityNotFoundException;
    
    List<BookLoan> findNoReturnedByStudent(String studentId, String dateProvided) throws EntityNotFoundException;
    
    BookLoan findById(String loanId, String returnDate) throws EntitySyntaxException, EntityNotFoundException;
}
