package com.froi.library.controllers.bookloan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.froi.library.controllers.AbstractMvcTest;
import com.froi.library.controllers.exceptionhandler.GlobalExceptionHandler;
import com.froi.library.dto.bookloan.CreateBookLoanDTO;
import com.froi.library.services.bookloan.BookLoanService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {BookLoanController.class, BookLoanService.class, GlobalExceptionHandler.class})
class BookLoanControllerTest extends AbstractMvcTest {
    
    private static final String BOOK_CODE_1 = "code_1";
    private static final String STUDENT_ID = "201830121";
    private static final String LOAN_DATE = "2024-05-02";
    private static final String BOOK_ID = "123456";
    private static final Integer AVAILABILITY = 5;
    
    
    @MockBean
    private BookLoanService bookLoanService;
    
//    @Test
//    @WithMockUser(username = "librarian1", roles={"LIBRARIAN"})
//    void testCreateBookLoan() throws Exception {
//        // Arrange
//        ObjectMapper objectMapper = new ObjectMapper();
//        ArrayList<String> listBooks = new ArrayList<>();
//        listBooks.add(BOOK_CODE_1);
//        CreateBookLoanDTO newLoanRequest = new CreateBookLoanDTO(listBooks, STUDENT_ID, LOAN_DATE);
//
//        // Act & Assert
//        mockMvc.perform(post("/v1/bookloan")
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(newLoanRequest))
//                )
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
//    void testGetBookAvailability() throws Exception {
//        // Arrange
//        when(bookLoanService.checkAvailability(BOOK_ID)).thenReturn(AVAILABILITY);
//
//        // Act & Assert
//        mockMvc.perform(get("/v1/bookloan/availability/{bookId}", BOOK_ID)
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
}
