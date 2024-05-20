package com.froi.library.services.bookloan;

import com.froi.library.dto.bookloan.CreateBookLoanDTO;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;

public interface BookLoanService {
    boolean createLoan(CreateBookLoanDTO newLoan) throws EntityNotFoundException, DenegatedActionException, EntitySyntaxException;
    
    Integer checkAvailability(String bookId);
}
