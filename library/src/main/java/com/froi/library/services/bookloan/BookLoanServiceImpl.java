package com.froi.library.services.bookloan;

import com.froi.library.dto.bookloan.CreateBookLoanDTO;
import com.froi.library.entities.Book;
import com.froi.library.entities.BookLoan;
import com.froi.library.entities.Student;
import com.froi.library.enums.bookstatus.BookLoanStatus;
import com.froi.library.enums.studentstatus.StudentStatus;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.BookLoanRepository;
import com.froi.library.services.book.BookService;
import com.froi.library.services.student.StudentService;
import com.froi.library.services.tools.ToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BookLoanServiceImpl implements BookLoanService {

    private BookLoanRepository bookLoanRepository;
    private ToolsService toolsService;
    private BookService bookService;
    private StudentService studentService;
    
    @Autowired
    public BookLoanServiceImpl(BookLoanRepository bookLoanRepository, ToolsService toolsService, BookService bookService, StudentService studentService) {
        this.bookLoanRepository = bookLoanRepository;
        this.toolsService = toolsService;
        this.bookService = bookService;
        this.studentService = studentService;
    }
    
    @Override
    public boolean createLoan(CreateBookLoanDTO newLoan) throws EntityNotFoundException, DenegatedActionException, EntitySyntaxException {
        if (newLoan.getLoanDate() != null && !toolsService.isValidDateFormat(newLoan.getLoanDate())) {
            throw new EntitySyntaxException("INVALID_DATE");
        } else if (newLoan.getLoanDate() == null) {
            newLoan = new CreateBookLoanDTO(newLoan.getBookCodes(), newLoan.getStudentId(), LocalDate.now().toString());
        }
        Student student = studentService.getStudentById(newLoan.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("STUDENT_NOT_FOUND"));
        if (student.getStatus() != StudentStatus.ACTIVE) {
            throw new DenegatedActionException("STUDENT_IS_INACTIVE");
        }
        
        List<Book> booksList = new ArrayList<>();
        for (String bookCode: newLoan.getBookCodes()) {
           Book book = bookService.getBookByCode(bookCode)
                   .orElseThrow(() -> new EntityNotFoundException(bookCode + "BOOK_NOT_FOUND"));
           booksList.add(book);
        }
        
        Long studentLoans = bookLoanRepository.countByStudentAndStatus(student.getId());
        if (studentLoans >= 3) {
            throw new DenegatedActionException("STUDENT_HAS_3_OR_MORE_LOANS");
        }
        String unavaliableCopies = "";
        for (Book book: booksList) {
            Integer availableCopies = bookLoanRepository.countAvailableCopies(book.getCode(), Date.valueOf(newLoan.getLoanDate()));
            if (availableCopies <= 0) {
                unavaliableCopies += book.getCode() + " ";
            }
        }
        if (!unavaliableCopies.isEmpty()) {
            throw new EntityNotFoundException(unavaliableCopies + "NOT_FOUND");
        }
        
        for (Book book: booksList) {
            BookLoan bookLoanEntity = new BookLoan();
            bookLoanEntity.setLoanDate(Date.valueOf(newLoan.getLoanDate()));
            bookLoanEntity.setStudent(student.getId());
            bookLoanEntity.setBook(book.getCode());
            bookLoanEntity.setStatus(BookLoanStatus.IN_TIME);
            
            bookLoanRepository.save(bookLoanEntity);
        }
        
        return true;
    }
    
    public boolean isMoreThanThreeDays(Date dateString) {
        // Get today's date
        java.util.Date today = new java.util.Date();
        Date sqlToday = new java.sql.Date(today.getTime());
        
        // Calculate the difference in milliseconds
        long diffInMillies = sqlToday.getTime() - dateString.getTime();
        // Convert the difference from milliseconds to days
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        
        // Return true if the difference is greater than 3 days
        return diffInDays > 3;
    }
    
    @Override
    public Integer checkAvailability(String bookId) {
        System.out.println("disponibilidad");
        Date today = Date.valueOf(LocalDate.now());
        return bookLoanRepository.countAvailableCopies(bookId, today);
    }
    
}
