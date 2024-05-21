package com.froi.library.services.bookloan;

import com.froi.library.dto.bookloan.CreateBookLoanDTO;
import com.froi.library.dto.bookloan.ReturnLoanDTO;
import com.froi.library.entities.Book;
import com.froi.library.entities.BookLoan;
import com.froi.library.entities.Reservation;
import com.froi.library.entities.Student;
import com.froi.library.enums.bookstatus.BookLoanStatus;
import com.froi.library.enums.studentstatus.StudentStatus;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.BookLoanRepository;
import com.froi.library.repositories.ReservationRepository;
import com.froi.library.services.book.BookService;
import com.froi.library.services.student.StudentService;
import com.froi.library.services.tools.ToolsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

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
    private static final String DATE_MORE_THAN_THREE_DAYS = "2024-05-05";
    private static final String DATE_WITHIN_THREE_DAYS = "2024-05-03";
    private static final String DATE_MORE_THAN_THIRTY_DAYS = "2024-06-20";
    private static final String DATE_EXACTLY_THIRTY_DAYS = "2024-06-15";
    private static final long TWO_DAYS_IN_MILLIS = 2 * 24 * 60 * 60 * 1000L;
    private static final String NULL_DATE = null;
    private static final Integer AVAILABLE_BOOKS = 5;
    private static final Integer EXPECTED_AVAILABLE_BOOKS = 5;
    private static final String LOAN_ID = "1";
    private static final String RETURN_DATE = "2024-05-10";
    private static final Double BOOK_COST = 225.0;
    
    @Mock
    private BookLoanRepository bookLoanRepository;
    
    @Mock
    private ToolsService toolsService;
    
    @Mock
    private BookService bookService;
    
    @Mock
    private StudentService studentService;
    
    @Mock
    private ReservationRepository reservationRepository;
    
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
        student.setStatus(StudentStatus.ACTIVE);
        Book book = new Book();
        book.setCode(BOOK_CODE);
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
        Date pastDateMoreThanThreeDays = new Date(System.currentTimeMillis() - (3 * TWO_DAYS_IN_MILLIS));
        
        // Act & Assert
        assertTrue(bookLoanService.isMoreThanThreeDays(pastDateMoreThanThreeDays));
    }
    
    @Test
    void testIsMoreThanThreeDaysFalse() {
        // Arrange
        Date pastDateLessThanThreeDays = new Date(System.currentTimeMillis() - TWO_DAYS_IN_MILLIS);
        
        // Act & Assert
        assertFalse(bookLoanService.isMoreThanThreeDays(pastDateLessThanThreeDays));
    }
    
    @Test
    void testReturnLoanInvalidLoanId() {
        // Arrange
        ReturnLoanDTO returnLoan = new ReturnLoanDTO("invalid-loan-id", RETURN_DATE);
        when(toolsService.isPositiveInteger("invalid-loan-id")).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> bookLoanService.returnLoan(returnLoan));
    }
    
    @Test
    void testReturnLoanInvalidDateFormat() {
        // Arrange
        ReturnLoanDTO returnLoan = new ReturnLoanDTO(LOAN_ID, INVALID_DATE);
        when(toolsService.isPositiveInteger(LOAN_ID)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> bookLoanService.returnLoan(returnLoan));
    }
    
    @Test
    void testReturnLoanNotFound() {
        // Arrange
        ReturnLoanDTO returnLoan = new ReturnLoanDTO(LOAN_ID, RETURN_DATE);
        when(toolsService.isPositiveInteger(LOAN_ID)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(RETURN_DATE)).thenReturn(TRUE_ASSERTION);
        when(bookLoanRepository.findById(1)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookLoanService.returnLoan(returnLoan));
    }
    
    @Test
    void testReturnLoanSuccessReturnedInTime() throws EntityNotFoundException, EntitySyntaxException {
        // Arrange
        ReturnLoanDTO returnLoan = new ReturnLoanDTO(LOAN_ID, RETURN_DATE);
        BookLoan bookLoan = new BookLoan();
        bookLoan.setDelayTotal(0.0);
        when(toolsService.isPositiveInteger(LOAN_ID)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(RETURN_DATE)).thenReturn(TRUE_ASSERTION);
        when(bookLoanRepository.findById(1)).thenReturn(Optional.of(bookLoan));
        
        // Act
        boolean result = bookLoanService.returnLoan(returnLoan);
        
        // Assert
        assertTrue(result);
        assertEquals(BookLoanStatus.RETURNED_IN_TIME, bookLoan.getStatus());
        verify(bookLoanRepository, times(1)).save(bookLoan);
    }
    
    @Test
    void testReturnLoanSuccessReturnedOutOfTime() throws EntityNotFoundException, EntitySyntaxException {
        // Arrange
        ReturnLoanDTO returnLoan = new ReturnLoanDTO(LOAN_ID, RETURN_DATE);
        BookLoan bookLoan = new BookLoan();
        bookLoan.setDelayTotal(10.0);
        when(toolsService.isPositiveInteger(LOAN_ID)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(RETURN_DATE)).thenReturn(TRUE_ASSERTION);
        when(bookLoanRepository.findById(1)).thenReturn(Optional.of(bookLoan));
        
        // Act
        boolean result = bookLoanService.returnLoan(returnLoan);
        
        // Assert
        assertTrue(result);
        assertEquals(BookLoanStatus.RETURNED_OUT_OF_TIME, bookLoan.getStatus());
        verify(bookLoanRepository, times(1)).save(bookLoan);
    }
    
    @Test
    void testFindAll() throws EntityNotFoundException {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        BookLoan bookLoan = new BookLoan();
        bookLoan.setLoanDate(Date.valueOf(LOAN_DATE));
        List<BookLoan> allLoans = Arrays.asList(bookLoan);
        Page<BookLoan> page = mock(Page.class);
        when(bookLoanRepository.findAll()).thenReturn(allLoans);
        when(bookLoanRepository.findAll(pageable)).thenReturn(page);
        
        // Act
        Page<BookLoan> result = bookLoanService.findAll(pageable);
        
        // Assert
        assertEquals(page, result);
        verify(bookLoanRepository, times(1)).findAll(pageable);
    }
    
    @Test
    void testFindNoReturnedByStudentInvalidDateFormat() {
        // Arrange
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookLoanService.findNoReturnedByStudent(STUDENT_ID, INVALID_DATE));
    }
    
    @Test
    void testFindNoReturnedByStudent() throws EntityNotFoundException {
        // Arrange
        Date date = Date.valueOf(LOAN_DATE);
        BookLoan bookLoan = new BookLoan();
        bookLoan.setLoanDate(Date.valueOf(LOAN_DATE));
        List<BookLoan> booksList = Arrays.asList(bookLoan);
        when(toolsService.isValidDateFormat(LOAN_DATE)).thenReturn(TRUE_ASSERTION);
        when(bookLoanRepository.findAllByStudentAndStatus(STUDENT_ID, BookLoanStatus.IN_TIME)).thenReturn(booksList);
        
        // Act
        List<BookLoan> result = bookLoanService.findNoReturnedByStudent(STUDENT_ID, LOAN_DATE);
        
        // Assert
        assertEquals(booksList, result);
        verify(bookLoanRepository, times(1)).findAllByStudentAndStatus(STUDENT_ID, BookLoanStatus.IN_TIME);
    }
    
    @Test
    void testFindByIdInvalidDateFormat() {
        // Arrange
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> bookLoanService.findById(LOAN_ID, INVALID_DATE));
    }
    
    @Test
    void testFindByIdInvalidLoanId() {
        // Arrange
        when(toolsService.isValidDateFormat(RETURN_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isPositiveInteger("invalid-loan-id")).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> bookLoanService.findById("invalid-loan-id", RETURN_DATE));
    }
    
    @Test
    void testFindByIdLoanNotFound() {
        // Arrange
        when(toolsService.isValidDateFormat(RETURN_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isPositiveInteger(LOAN_ID)).thenReturn(TRUE_ASSERTION);
        when(bookLoanRepository.findById(1)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookLoanService.findById(LOAN_ID, RETURN_DATE));
    }
    
    @Test
    void testFindByIdSuccess() throws EntityNotFoundException, EntitySyntaxException {
        // Arrange
        BookLoan bookLoan = new BookLoan();
        bookLoan.setLoanDate(Date.valueOf(LOAN_DATE));
        when(toolsService.isValidDateFormat(RETURN_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isPositiveInteger(LOAN_ID)).thenReturn(TRUE_ASSERTION);
        when(bookLoanRepository.findById(1)).thenReturn(Optional.of(bookLoan));
        
        // Act
        BookLoan result = bookLoanService.findById(LOAN_ID, RETURN_DATE);
        
        // Assert
        assertEquals(bookLoan, result);
        verify(bookLoanRepository, times(1)).findById(1);
    }
    
    @Test
    void testCheckDelayNoDelay() throws EntityNotFoundException {
        // Arrange
        BookLoan bookLoan = new BookLoan();
        bookLoan.setLoanDate(Date.valueOf(LOAN_DATE));
        Date returnDate = Date.valueOf(LOAN_DATE);
        
        // Act
        boolean result = bookLoanService.checkDelay(bookLoan, returnDate);
        
        // Assert
        assertTrue(result);
        assertEquals(0.0, bookLoan.getLoanTotal());
        assertEquals(0.0, bookLoan.getDelayTotal());
    }
    
    @Test
    void testCheckDelayWithinThreeDays() throws EntityNotFoundException {
        // Arrange
        BookLoan bookLoan = new BookLoan();
        bookLoan.setLoanDate(Date.valueOf(LOAN_DATE));
        Date returnDate = Date.valueOf(DATE_WITHIN_THREE_DAYS);
        
        // Act
        boolean result = bookLoanService.checkDelay(bookLoan, returnDate);
        
        // Assert
        assertTrue(result);
        assertEquals(10.0, bookLoan.getLoanTotal());
        assertEquals(0.0, bookLoan.getDelayTotal());
    }
    
    @Test
    void testCheckDelayMoreThanThreeDays() throws EntityNotFoundException {
        // Arrange
        BookLoan bookLoan = new BookLoan();
        bookLoan.setLoanDate(Date.valueOf(LOAN_DATE));
        Date returnDate = Date.valueOf(DATE_MORE_THAN_THREE_DAYS);
        
        // Act
        boolean result = bookLoanService.checkDelay(bookLoan, returnDate);
        
        // Assert
        assertTrue(result);
        assertEquals(20.0, bookLoan.getLoanTotal());
    }
    
    @Test
    void testCheckDelayMoreThanThirtyDays() throws EntityNotFoundException {
        // Arrange
        BookLoan bookLoan = new BookLoan();
        bookLoan.setLoanDate(Date.valueOf(LOAN_DATE));
        Date returnDate = Date.valueOf(DATE_MORE_THAN_THIRTY_DAYS);
        Book book = new Book();
        book.setCost(100.0);
        when(bookService.getOneBookByCode(bookLoan.getBook())).thenReturn(book);
        
        // Act
        boolean result = bookLoanService.checkDelay(bookLoan, returnDate);
        
        // Assert
        assertTrue(result);
        assertEquals(250.0, bookLoan.getLoanTotal());
        assertEquals(1105.0, bookLoan.getDelayTotal());
        verify(studentService, times(1)).sanctionStudent(bookLoan.getStudent());
    }
    
    @Test
    void testCheckDelayExactlyThirtyDays() throws EntityNotFoundException {
        // Arrange
        BookLoan bookLoan = new BookLoan();
        Book book = new Book();
        book.setCode(BOOK_CODE);
        book.setCost(BOOK_COST);
        bookLoan.setBook(BOOK_CODE);
        bookLoan.setLoanDate(Date.valueOf(LOAN_DATE));
        Date returnDate = Date.valueOf(DATE_EXACTLY_THIRTY_DAYS);
        when(bookService.getOneBookByCode(BOOK_CODE))
                .thenReturn(book);
        
        // Act
        boolean result = bookLoanService.checkDelay(bookLoan, returnDate);
        
        // Assert
        assertTrue(result);
        assertEquals(225.0, bookLoan.getLoanTotal());
        assertEquals(1080.0, bookLoan.getDelayTotal());
    }
    
}
