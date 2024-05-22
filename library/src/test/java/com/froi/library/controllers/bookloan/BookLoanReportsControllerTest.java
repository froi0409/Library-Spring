package com.froi.library.controllers.bookloan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.froi.library.controllers.AbstractMvcTest;
import com.froi.library.controllers.exceptionhandler.GlobalExceptionHandler;
import com.froi.library.dto.bookloan.LoansByDegreeResponseDTO;
import com.froi.library.dto.bookloan.ReturnLoanDTO;
import com.froi.library.dto.bookloan.RevenueResponseDTO;
import com.froi.library.entities.BookLoan;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.services.bookloan.BookLoanReportsService;
import com.froi.library.services.bookloan.BookLoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@ContextConfiguration(classes = {BookLoanReportsController.class, GlobalExceptionHandler.class})
public class BookLoanReportsControllerTest extends AbstractMvcTest {
    
    private static final String STUDENT_ID = "12345";
    private static final String DATE = "2024-05-22";
    private static final String LOAN_ID = "1";
    private static final String RETURN_DATE = "2024-05-22";
    private static final String START_DATE = "2024-01-01";
    private static final String END_DATE = "2024-12-31";
    private static final String DEGREE_NAME_MAP_ID = "degreeName";
    private static final String DEGREE_NAME = "Ingeniería en Ciencias y Sistemas";
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private BookLoanService bookLoanService;
    
    @MockBean
    private BookLoanReportsService bookLoanReportsService;
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindBookLoansByStudent() throws Exception {
        // Arrange
        BookLoan bookLoan = new BookLoan();
        when(bookLoanService.findNoReturnedByStudent(any(), any())).thenReturn(Collections.singletonList(bookLoan));
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/byStudent/{studentId}/{date}", STUDENT_ID, DATE))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindBookLoansByStudentNotFound() throws Exception {
        // Arrange
        when(bookLoanService.findNoReturnedByStudent(any(), any())).thenThrow(new EntityNotFoundException("Not found"));
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/byStudent/{studentId}/{date}", STUDENT_ID, DATE))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindBLoanBookById() throws Exception {
        // Arrange
        BookLoan bookLoan = new BookLoan();
        when(bookLoanService.findById(any(), any())).thenReturn(bookLoan);
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/{loanId}/{returnDate}", LOAN_ID, RETURN_DATE))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindBLoanBookByIdNotFound() throws Exception {
        // Arrange
        when(bookLoanService.findById(any(), any())).thenThrow(new EntityNotFoundException("Not found"));
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/{loanId}/{returnDate}", LOAN_ID, RETURN_DATE))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindBLoanBookByIdInvalidDate() throws Exception {
        // Arrange
        when(bookLoanService.findById(any(), any())).thenThrow(new EntitySyntaxException("Invalid date"));
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/{loanId}/{returnDate}", LOAN_ID, RETURN_DATE))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testReturnBookLoan() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        ReturnLoanDTO returnLoanDTO = new ReturnLoanDTO(LOAN_ID, RETURN_DATE);
        when(bookLoanService.returnLoan(any(ReturnLoanDTO.class))).thenReturn(true);
        
        // Act & Assert
        mockMvc.perform(post("/v1/bookloan/return")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(returnLoanDTO)))
                .andExpect(status().isOk());
    }
    
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testReturnBookLoanNotFound() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        ReturnLoanDTO returnLoanDTO = new ReturnLoanDTO(LOAN_ID, RETURN_DATE);
        when(bookLoanService.returnLoan(any())).thenThrow(new EntityNotFoundException("Not found"));
        
        // Act & Assert
        mockMvc.perform(post("/v1/bookloan/return")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(returnLoanDTO)))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testTodayLoans() throws Exception {
        // Arrange
        when(bookLoanReportsService.findBookLoansDueToday(any())).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/today/{date}", DATE))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testTodayLoansInvalidDate() throws Exception {
        // Arrange
        when(bookLoanReportsService.findBookLoansDueToday(any())).thenThrow(new EntitySyntaxException("Invalid date"));
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/today/{date}", DATE))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindOverdueBookLoans() throws Exception {
        // Arrange
        when(bookLoanReportsService.findOverdueBookLoans(any())).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/overdueLoans/{date}", DATE))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindOverdueBookLoansInvalidDate() throws Exception {
        // Arrange
        when(bookLoanReportsService.findOverdueBookLoans(any())).thenThrow(new EntitySyntaxException("Invalid date"));
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/overdueLoans/{date}", DATE))
                .andExpect(status().isBadRequest());
    }
    
    private static final String TOTAL_REVENUE_MAP_ID = "totalRevenue";
    private static final Double TOTAL_REVENUE = 15800.00;
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindTotalRevenueBetweenDates() throws Exception {
        // Arrange
        RevenueResponseDTO revenueResponse = new RevenueResponseDTO(Map.of(TOTAL_REVENUE_MAP_ID, TOTAL_REVENUE), Collections.emptyList());
        when(bookLoanReportsService.findTotalRevenueBetweenDates(any(), any())).thenReturn(revenueResponse);
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/revenue/{startDate}/{endDate}", START_DATE, END_DATE))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindTotalRevenueBetweenDatesInvalidDate() throws Exception {
        // Arrange
        when(bookLoanReportsService.findTotalRevenueBetweenDates(any(), any())).thenThrow(new EntitySyntaxException("Invalid date"));
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/revenue/{startDate}/{endDate}", START_DATE, END_DATE))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindTotalRevenue() throws Exception {
        // Arrange
        RevenueResponseDTO revenueResponse = new RevenueResponseDTO(Map.of(TOTAL_REVENUE_MAP_ID, TOTAL_REVENUE), Collections.emptyList());
        when(bookLoanReportsService.findTotalRevenue()).thenReturn(revenueResponse);
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/revenue"))
                .andExpect(status().isOk());
    }
    
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindDegreeWithMostLoans() throws Exception {
        // Arrange
        LoansByDegreeResponseDTO loansByDegreeResponse = new LoansByDegreeResponseDTO(Map.of(DEGREE_NAME_MAP_ID, DEGREE_NAME), Collections.emptyList());
        when(bookLoanReportsService.findDegreeWithMostLoans()).thenReturn(loansByDegreeResponse);
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/degreeTopLoans"))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindDegreeWithMostLoansNotFound() throws Exception {
        // Arrange
        when(bookLoanReportsService.findDegreeWithMostLoans()).thenThrow(new EntityNotFoundException("Not found"));
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/degreeTopLoans"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindDegreeWithMostLoansBetweenDates() throws Exception {
        // Arrange
        LoansByDegreeResponseDTO loansByDegreeResponse = new LoansByDegreeResponseDTO(Map.of(DEGREE_NAME_MAP_ID, DEGREE_NAME), Collections.emptyList());
        when(bookLoanReportsService.findDegreeWithMostLoansBetweenDate(any(), any())).thenReturn(loansByDegreeResponse);
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/degreeTopLoans/{startDate}/{endDate}", START_DATE, END_DATE))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindDegreeWithMostLoansBetweenDatesNotFound() throws Exception {
        // Arrange
        when(bookLoanReportsService.findDegreeWithMostLoansBetweenDate(any(), any())).thenThrow(new EntityNotFoundException("Not found"));
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/degreeTopLoans/{startDate}/{endDate}", START_DATE, END_DATE))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindOverduePaymentByStudentBetweenDates() throws Exception {
        // Arrange
        when(bookLoanReportsService.findOverduePaymentByStudentBetweenDates(any(), any(), any())).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/overduePaymentByStudent/{studentId}/{startDate}/{endDate}", STUDENT_ID, START_DATE, END_DATE))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindOverduePaymentByStudentBetweenDatesInvalidDate() throws Exception {
        // Arrange
        when(bookLoanReportsService.findOverduePaymentByStudentBetweenDates(any(), any(), any())).thenThrow(new EntitySyntaxException("Invalid date"));
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/overduePaymentByStudent/{studentId}/{startDate}/{endDate}", STUDENT_ID, START_DATE, END_DATE))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testFindOverduePaymentByStudent() throws Exception {
        // Arrange
        when(bookLoanReportsService.findOverduePaymentByStudent(any())).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/v1/bookloan/overduePaymentByStudent/{studentId}", STUDENT_ID))
                .andExpect(status().isOk());
    }
}
