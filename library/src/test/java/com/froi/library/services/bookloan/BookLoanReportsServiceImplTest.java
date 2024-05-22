package com.froi.library.services.bookloan;

import com.froi.library.dto.bookloan.LoansByDegreeResponseDTO;
import com.froi.library.dto.bookloan.RevenueResponseDTO;
import com.froi.library.entities.BookLoan;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.BookLoanRepository;
import com.froi.library.services.tools.ToolsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookLoanReportsServiceImplTest {
    
    private static final String VALID_DATE = "2024-05-22";
    private static final String INVALID_DATE = "22/05/2024";
    private static final String STUDENT_ID = "12345";
    private static final String START_DATE = "2024-01-01";
    private static final String END_DATE = "2024-12-31";
    private static final int DEGREE_ID = 1;
    
    private static final Map<String, Double> REVENUE_INFORMATION = Map.of("totalRevenue", 1000.0);
    private static final Map<String, Object> DEGREE_INFORMATION = Map.of("degree_id", DEGREE_ID, "count", 10);
    private static final List<BookLoan> BOOK_LOAN_LIST = Collections.singletonList(new BookLoan());
    
    private static final boolean TRUE_ASSERTION = true;
    private static final boolean FALSE_ASSERTION = false;
    
    @Mock
    private BookLoanRepository bookLoanRepository;
    
    @Mock
    private ToolsService toolsService;
    
    @InjectMocks
    private BookLoanReportsServiceImpl serviceToTest;
    
    @Test
    void testFindBookLoansDueTodayValidDate() throws EntitySyntaxException {
        // Arrange
        when(toolsService.isValidDateFormat(VALID_DATE)).thenReturn(TRUE_ASSERTION);
        when(bookLoanRepository.findBookLoansDueTodayWithBookTitle(Date.valueOf(VALID_DATE)))
                .thenReturn(Collections.emptyList());
        
        // Act
        List<Map<String, Object>> result = serviceToTest.findBookLoansDueToday(VALID_DATE);
        
        // Assert
        assertNotNull(result);
        verify(bookLoanRepository).findBookLoansDueTodayWithBookTitle(Date.valueOf(VALID_DATE));
    }
    
    @Test
    void testFindBookLoansDueTodayInvalidDate() {
        // Arrange
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.findBookLoansDueToday(INVALID_DATE));
    }
    
    @Test
    void testFindOverdueBookLoansValidDate() throws EntitySyntaxException {
        // Arrange
        when(toolsService.isValidDateFormat(VALID_DATE)).thenReturn(TRUE_ASSERTION);
        when(bookLoanRepository.findOverdueBookLoans(Date.valueOf(VALID_DATE)))
                .thenReturn(Collections.emptyList());
        
        // Act
        List<Map<String, Object>> result = serviceToTest.findOverdueBookLoans(VALID_DATE);
        
        // Assert
        assertNotNull(result);
        verify(bookLoanRepository).findOverdueBookLoans(Date.valueOf(VALID_DATE));
    }
    
    @Test
    void testFindOverdueBookLoansInvalidDate() {
        // Arrange
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.findOverdueBookLoans(INVALID_DATE));
    }
    
    @Test
    void testFindTotalRevenueBetweenDatesValid() throws EntitySyntaxException {
        // Arrange
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(END_DATE)).thenReturn(TRUE_ASSERTION);
        when(bookLoanRepository.findTotalRevenueBetweenDates(Date.valueOf(START_DATE), Date.valueOf(END_DATE)))
                .thenReturn(REVENUE_INFORMATION);
        when(bookLoanRepository.findBookLoansBetweenDates(Date.valueOf(START_DATE), Date.valueOf(END_DATE)))
                .thenReturn(BOOK_LOAN_LIST);
        
        // Act
        RevenueResponseDTO result = serviceToTest.findTotalRevenueBetweenDates(START_DATE, END_DATE);
        
        // Assert
        assertNotNull(result);
        assertEquals(REVENUE_INFORMATION, result.getRevenueInformation());
        assertEquals(BOOK_LOAN_LIST, result.getBookLoanList());
    }
    
    @Test
    void testFindTotalRevenueBetweenDatesInvalidStartDate() {
        // Arrange
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.findTotalRevenueBetweenDates(INVALID_DATE, END_DATE));
    }
    
    @Test
    void testFindTotalRevenueBetweenDatesInvalidEndDate() {
        // Arrange
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.findTotalRevenueBetweenDates(START_DATE, INVALID_DATE));
    }
    
    @Test
    void testFindTotalRevenueBetweenDatesStartDateAfterEndDate() {
        // Arrange
        when(toolsService.isValidDateFormat(END_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.findTotalRevenueBetweenDates(END_DATE, START_DATE));
    }
    
    @Test
    void testFindTotalRevenue() {
        // Arrange
        when(bookLoanRepository.findTotalRevenue()).thenReturn(REVENUE_INFORMATION);
        when(bookLoanRepository.findAll()).thenReturn(BOOK_LOAN_LIST);
        
        // Act
        RevenueResponseDTO result = serviceToTest.findTotalRevenue();
        
        // Assert
        assertNotNull(result);
        assertEquals(REVENUE_INFORMATION, result.getRevenueInformation());
        assertEquals(BOOK_LOAN_LIST, result.getBookLoanList());
    }
    
    @Test
    void testFindDegreeWithMostLoansBetweenDatesValid() throws EntitySyntaxException, EntityNotFoundException {
        // Arrange
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(END_DATE)).thenReturn(TRUE_ASSERTION);
        when(bookLoanRepository.findDegreeWithMostLoansBetweenDates(Date.valueOf(START_DATE), Date.valueOf(END_DATE)))
                .thenReturn(DEGREE_INFORMATION);
        when(bookLoanRepository.findLoansByDegreeBetweenDates(DEGREE_ID, Date.valueOf(START_DATE), Date.valueOf(END_DATE)))
                .thenReturn(BOOK_LOAN_LIST);
        
        // Act
        LoansByDegreeResponseDTO result = serviceToTest.findDegreeWithMostLoansBetweenDate(START_DATE, END_DATE);
        
        // Assert
        assertNotNull(result);
        assertEquals(DEGREE_INFORMATION, result.getDegreeInformation());
        assertEquals(BOOK_LOAN_LIST, result.getBookLoanList());
    }
    
    @Test
    void testFindDegreeWithMostLoansBetweenDatesInvalidStartDate() {
        // Arrange
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.findDegreeWithMostLoansBetweenDate(INVALID_DATE, END_DATE));
    }
    
    @Test
    void testFindDegreeWithMostLoansBetweenDatesInvalidEndDate() {
        // Arrange
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.findDegreeWithMostLoansBetweenDate(START_DATE, INVALID_DATE));
    }
    
    @Test
    void testFindDegreeWithMostLoansBetweenDatesStartDateAfterEndDate() {
        // Arrange
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(END_DATE)).thenReturn(TRUE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.findDegreeWithMostLoansBetweenDate(END_DATE, START_DATE));
    }
    
    @Test
    void testFindDegreeWithMostLoansBetweenDatesDegreeNotFound() {
        // Arrange
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(END_DATE)).thenReturn(TRUE_ASSERTION);
        when(bookLoanRepository.findDegreeWithMostLoansBetweenDates(Date.valueOf(START_DATE), Date.valueOf(END_DATE)))
                .thenReturn(null);
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> serviceToTest.findDegreeWithMostLoansBetweenDate(START_DATE, END_DATE));
    }
    
    @Test
    void testFindDegreeWithMostLoans() throws EntityNotFoundException {
        // Arrange
        when(bookLoanRepository.findDegreeWithMostLoans()).thenReturn(DEGREE_INFORMATION);
        when(bookLoanRepository.findLoansByDegree(DEGREE_ID)).thenReturn(BOOK_LOAN_LIST);
        
        // Act
        LoansByDegreeResponseDTO result = serviceToTest.findDegreeWithMostLoans();
        
        // Assert
        assertNotNull(result);
        assertEquals(DEGREE_INFORMATION, result.getDegreeInformation());
        assertEquals(BOOK_LOAN_LIST, result.getBookLoanList());
    }
    
    @Test
    void testFindDegreeWithMostLoansDegreeNotFound() {
        // Arrange
        when(bookLoanRepository.findDegreeWithMostLoans()).thenReturn(null);
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> serviceToTest.findDegreeWithMostLoans());
    }
    
    @Test
    void testFindOverduePaymentByStudentBetweenDatesValid() throws EntitySyntaxException {
        // Arrange
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(END_DATE)).thenReturn(TRUE_ASSERTION);
        when(bookLoanRepository.findOverduePaymentsByStudentBetweenDates(STUDENT_ID, Date.valueOf(START_DATE), Date.valueOf(END_DATE)))
                .thenReturn(BOOK_LOAN_LIST);
        
        // Act
        List<BookLoan> result = serviceToTest.findOverduePaymentByStudentBetweenDates(STUDENT_ID, START_DATE, END_DATE);
        
        // Assert
        assertNotNull(result);
        assertEquals(BOOK_LOAN_LIST, result);
    }
    
    @Test
    void testFindOverduePaymentByStudentBetweenDatesInvalidStartDate() {
        // Arrange
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.findOverduePaymentByStudentBetweenDates(STUDENT_ID, INVALID_DATE, END_DATE));
    }
    
    @Test
    void testFindOverduePaymentByStudentBetweenDatesInvalidEndDate() {
        // Arrange
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.findOverduePaymentByStudentBetweenDates(STUDENT_ID, START_DATE, INVALID_DATE));
    }
    
    @Test
    void testFindOverduePaymentByStudentBetweenDatesStartDateAfterEndDate() {
        // Arrange
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(END_DATE)).thenReturn(TRUE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.findOverduePaymentByStudentBetweenDates(STUDENT_ID, END_DATE, START_DATE));
    }
    
    @Test
    void testFindOverduePaymentByStudent() {
        // Arrange
        when(bookLoanRepository.findOverduePaymentsByStudent(STUDENT_ID)).thenReturn(BOOK_LOAN_LIST);
        
        // Act
        List<BookLoan> result = serviceToTest.findOverduePaymentByStudent(STUDENT_ID);
        
        // Assert
        assertNotNull(result);
        assertEquals(BOOK_LOAN_LIST, result);
    }
    
    @Test
    void testCheckDatesValid() throws EntitySyntaxException {
        // Arrange
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(END_DATE)).thenReturn(TRUE_ASSERTION);
        
        // Act
        boolean result = serviceToTest.checkDates(START_DATE, END_DATE);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void testCheckDatesInvalidStartDate() {
        // Arrange
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.checkDates(INVALID_DATE, END_DATE));
    }
    
    @Test
    void testCheckDatesInvalidEndDate() {
        // Arrange
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.checkDates(START_DATE, INVALID_DATE));
    }
    
    @Test
    void testCheckDatesStartDateAfterEndDate() {
        // Arrange
        when(toolsService.isValidDateFormat(START_DATE)).thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(END_DATE)).thenReturn(TRUE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.checkDates(END_DATE, START_DATE));
    }
}
