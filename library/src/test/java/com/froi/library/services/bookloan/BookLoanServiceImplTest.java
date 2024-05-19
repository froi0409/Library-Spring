package com.froi.library.services.bookloan;

import com.froi.library.dto.bookloan.CreateBookLoanDTO;
import com.froi.library.entities.Book;
import com.froi.library.entities.Student;
import com.froi.library.enums.studentstatus.StudentStatus;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.BookLoanRepository;
import com.froi.library.services.book.BookService;
import com.froi.library.services.student.StudentService;
import com.froi.library.services.tools.ToolsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookLoanServiceImplTest {
    
    private static final String STUDENT_ID = "12345";
    private static final String BOOK_CODE = "978-3-16-148410-0";
    private static final String BOOK_CODE_2 = "978-1-23-456789-0";
    private static final String LOAN_DATE = "2024-05-01";
    private static final String INVALID_DATE = "invalid-date";
    private static final boolean TRUE_ASSERTION = true;
    private static final boolean FALSE_ASSERTION = false;
    private static final long STUDENT_LOANS = 2L;
    private static final long STUDENT_LOANS_MAX = 3L;
    private static final int AVAILABLE_COPIES = 1;
    private static final int NO_AVAILABLE_COPIES = 0;
    private static final String DATE_MORE_THAN_THREE_DAYS = "2024-05-01";
    private static final long TWO_DAYS_IN_MILLIS = 2 * 24 * 60 * 60 * 1000L;
    private static final String NULL_DATE = null;
    
    @Mock
    private BookLoanRepository bookLoanRepository;
    
    @Mock
    private ToolsService toolsService;
    
    @Mock
    private BookService bookService;
    
    @Mock
    private StudentService studentService;
    
    @InjectMocks
    private BookLoanServiceImpl bookLoanService;
    
    @Test
    void testCreateLoanInvalidDateFormat() {
        // Arrange
        CreateBookLoanDTO newLoan = new CreateBookLoanDTO(Arrays.asList(BOOK_CODE), STUDENT_ID, INVALID_DATE);
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> bookLoanService.createLoan(newLoan));
    }
    
    @Test
    void testCreateLoanStudentNotFound() throws EntityNotFoundException {
        // Arrange
        CreateBookLoanDTO newLoan = new CreateBookLoanDTO(Arrays.asList(BOOK_CODE), STUDENT_ID, LOAN_DATE);
        when(toolsService.isValidDateFormat(LOAN_DATE)).thenReturn(TRUE_ASSERTION);
        when(studentService.getStudentById(STUDENT_ID)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookLoanService.createLoan(newLoan));
    }
    
    @Test
    void testCreateLoanBookNotFound() throws EntityNotFoundException {
        // Arrange
        CreateBookLoanDTO newLoan = new CreateBookLoanDTO(Arrays.asList(BOOK_CODE), STUDENT_ID, LOAN_DATE);
        Student student = new Student();
        student.setId(STUDENT_ID);
        student.setStatus(StudentStatus.ACTIVE);
        when(toolsService.isValidDateFormat(LOAN_DATE)).thenReturn(TRUE_ASSERTION);
        when(studentService.getStudentById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(bookService.getBookByCode(BOOK_CODE)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookLoanService.createLoan(newLoan));
    }
    
    @Test
    void testCreateLoanStudentInactive() throws EntityNotFoundException {
        // Arrange
        CreateBookLoanDTO newLoan = new CreateBookLoanDTO(Arrays.asList(BOOK_CODE), STUDENT_ID, LOAN_DATE);
        Student student = new Student();
        student.setId(STUDENT_ID);
        student.setStatus(StudentStatus.INACTIVE);
        when(toolsService.isValidDateFormat(LOAN_DATE)).thenReturn(TRUE_ASSERTION);
        when(studentService.getStudentById(STUDENT_ID)).thenReturn(Optional.of(student));
        
        // Act & Assert
        assertThrows(DenegatedActionException.class, () -> bookLoanService.createLoan(newLoan));
    }
    
    @Test
    void testCreateLoanStudentHasTooManyLoans() throws EntityNotFoundException {
        // Arrange
        CreateBookLoanDTO newLoan = new CreateBookLoanDTO(Arrays.asList(BOOK_CODE), STUDENT_ID, LOAN_DATE);
        Student student = new Student();
        student.setId(STUDENT_ID);
        Book book = new Book();
        book.setCode(BOOK_CODE);
        student.setStatus(StudentStatus.ACTIVE);
        when(toolsService.isValidDateFormat(LOAN_DATE)).thenReturn(TRUE_ASSERTION);
        when(studentService.getStudentById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(bookService.getBookByCode(BOOK_CODE)).thenReturn(Optional.of(book));
        when(bookLoanRepository.countByStudentAndStatus(STUDENT_ID)).thenReturn(STUDENT_LOANS_MAX);
        
        // Act & Assert
        assertThrows(DenegatedActionException.class, () -> bookLoanService.createLoan(newLoan));
    }
    
    @Test
    void testCreateLoanBookUnavailable() throws EntityNotFoundException {
        // Arrange
        CreateBookLoanDTO newLoan = new CreateBookLoanDTO(Arrays.asList(BOOK_CODE), STUDENT_ID, LOAN_DATE);
        Student student = new Student();
        student.setId(STUDENT_ID);
        student.setStatus(StudentStatus.ACTIVE);
        Book book = new Book();
        book.setCode(BOOK_CODE);
        when(toolsService.isValidDateFormat(LOAN_DATE)).thenReturn(TRUE_ASSERTION);
        when(studentService.getStudentById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(bookLoanRepository.countByStudentAndStatus(STUDENT_ID)).thenReturn(STUDENT_LOANS);
        when(bookService.getBookByCode(BOOK_CODE)).thenReturn(Optional.of(book));
        when(bookLoanRepository.countAvailableCopies(BOOK_CODE, Date.valueOf(LOAN_DATE))).thenReturn(NO_AVAILABLE_COPIES);
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookLoanService.createLoan(newLoan));
    }
    
    @Test
    void testCreateLoanSuccess() throws EntityNotFoundException, DenegatedActionException, EntitySyntaxException {
        // Arrange
        CreateBookLoanDTO newLoan = new CreateBookLoanDTO(Arrays.asList(BOOK_CODE), STUDENT_ID, LOAN_DATE);
        Student student = new Student();
        student.setId(STUDENT_ID);
        student.setStatus(StudentStatus.ACTIVE);
        Book book = new Book();
        book.setCode(BOOK_CODE);
        when(toolsService.isValidDateFormat(LOAN_DATE)).thenReturn(TRUE_ASSERTION);
        when(studentService.getStudentById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(bookLoanRepository.countByStudentAndStatus(STUDENT_ID)).thenReturn(STUDENT_LOANS);
        when(bookService.getBookByCode(BOOK_CODE)).thenReturn(Optional.of(book));
        when(bookLoanRepository.countAvailableCopies(BOOK_CODE, Date.valueOf(LOAN_DATE))).thenReturn(AVAILABLE_COPIES);
        
        // Act
        boolean result = bookLoanService.createLoan(newLoan);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void testCreateLoanSuccessNoDate() throws EntityNotFoundException, DenegatedActionException, EntitySyntaxException {
        // Arrange
        CreateBookLoanDTO newLoan = new CreateBookLoanDTO(Arrays.asList(BOOK_CODE), STUDENT_ID, NULL_DATE);
        Student student = new Student();
        student.setId(STUDENT_ID);
        student.setStatus(StudentStatus.ACTIVE);
        Book book = new Book();
        book.setCode(BOOK_CODE);
        when(studentService.getStudentById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(bookLoanRepository.countByStudentAndStatus(STUDENT_ID)).thenReturn(STUDENT_LOANS);
        when(bookService.getBookByCode(BOOK_CODE)).thenReturn(Optional.of(book));
        when(bookLoanRepository.countAvailableCopies(BOOK_CODE, Date.valueOf(LocalDate.now()))).thenReturn(AVAILABLE_COPIES);
        
        // Act
        boolean result = bookLoanService.createLoan(newLoan);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void testIsMoreThanThreeDaysTrue() {
        // Arrange
        Date pastDateMoreThanThreeDays = Date.valueOf(DATE_MORE_THAN_THREE_DAYS);
        
        // Act
        boolean resultMoreThanThreeDays = bookLoanService.isMoreThanThreeDays(pastDateMoreThanThreeDays);
        
        // Assert
        assertTrue(resultMoreThanThreeDays);
    }
    
    @Test
    void testIsMoreThanThreeDaysFalse() {
        // Arrange
        Date pastDateLessThanThreeDays = new Date(System.currentTimeMillis() - TWO_DAYS_IN_MILLIS); // Two days ago
        
        // Act
        boolean resultLessThanThreeDays = bookLoanService.isMoreThanThreeDays(pastDateLessThanThreeDays);
        
        // Assert
        assertFalse(resultLessThanThreeDays);
    }
}
