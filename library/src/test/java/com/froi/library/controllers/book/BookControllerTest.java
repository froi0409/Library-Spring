package com.froi.library.controllers.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.froi.library.controllers.AbstractMvcTest;
import com.froi.library.controllers.book.BookController;
import com.froi.library.controllers.exceptionhandler.GlobalExceptionHandler;
import com.froi.library.dto.book.CreateBookRequestDTO;
import com.froi.library.entities.Book;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.services.book.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {BookController.class, BookService.class, GlobalExceptionHandler.class})
class BookControllerTest extends AbstractMvcTest {
    
    @MockBean
    private BookService bookService;
    
    @Test
    @WithMockUser(username = "librarian1", roles={"LIBRARIAN"})
    void testCreateBook() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        CreateBookRequestDTO newBookRequest = new CreateBookRequestDTO("123456", "Author", "Publisher", "Title", "2024-05-18", "10.99", "50");
        Book createdBook = new Book(); // Create a book object to return
        when(bookService.createBook(any())).thenReturn(createdBook); // Mock the bookService to return the created book
        
        // Act & Assert
        mockMvc.perform(post("/v1/book")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookRequest))
                )
                .andExpect(status().isCreated());
    }
}
