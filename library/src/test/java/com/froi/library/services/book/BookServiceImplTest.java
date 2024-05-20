package com.froi.library.services.book;

import com.froi.library.dto.book.CreateBookRequestDTO;
import com.froi.library.entities.Book;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.BookRepository;
import com.froi.library.services.tools.ToolsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    private static final String CODE = "0-19-852663-6";
    private static final String TITLE = "Calculo, 8va Edición";
    private static final String AUTHOR = "James Stewart";
    private static final String PUBLISHER = "CENGAGE LEARNING";
    private static final String COST = "125.25";
    private static final String PUBLISH_DATE = "2000-12-12";
    private static final String INVALID_DATE = "1000/0001/1223";
    private static final String STOCK = "25";
    private static final String INVALID_STOCK = "12.12";
    private static final boolean TRUE_ASSERTION = true;
    private static final boolean FALSE_ASSERTION = false;
    
    @Mock
    BookRepository bookRepository;
    @Mock
    ToolsService toolsService;
    
    @InjectMocks
    BookServiceImpl serviceToTest;
    
    @Test
    void testDuplicatedBook() {
        // Arrange
        CreateBookRequestDTO newBook = new CreateBookRequestDTO(CODE, AUTHOR, PUBLISHER, TITLE, PUBLISH_DATE, COST, STOCK);
        when(bookRepository.findById(newBook.getCode()))
                .thenReturn(Optional.of(new Book()));
        // Act & Assert
        assertThrows(DuplicatedEntityException.class,
                () -> serviceToTest.createBook(newBook));
        
    }
    
    @Test
    void testInvalidUSBN() {
        // Arrange
        CreateBookRequestDTO newBook = new CreateBookRequestDTO(CODE, AUTHOR, PUBLISHER, TITLE, PUBLISH_DATE, COST, STOCK);
        when(bookRepository.findById(newBook.getCode()))
                .thenReturn(Optional.empty());
        when(toolsService.isValidISBN(newBook.getCode()))
                .thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class,
                () -> serviceToTest.createBook(newBook));
    }

    @Test
    void testInvalidDateFormat() {
        // Arrange
        CreateBookRequestDTO newBook = new CreateBookRequestDTO(CODE, AUTHOR, PUBLISHER, TITLE, INVALID_DATE, INVALID_STOCK, STOCK);
        when(bookRepository.findById(newBook.getCode()))
                .thenReturn(Optional.empty());
        when(toolsService.isValidISBN(newBook.getCode()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(newBook.getPublishDate()))
                .thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class,
                () -> serviceToTest.createBook(newBook));
    }
    
    @Test
    void testInvalidStockFormat() {
        // Arrange
        CreateBookRequestDTO newBook = new CreateBookRequestDTO(CODE, AUTHOR, PUBLISHER, TITLE, PUBLISH_DATE, COST, INVALID_STOCK);
        when(bookRepository.findById(newBook.getCode()))
                .thenReturn(Optional.empty());
        when(toolsService.isValidISBN(newBook.getCode()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(newBook.getPublishDate()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isPositiveInteger(newBook.getStock()))
                .thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class,
                () -> serviceToTest.createBook(newBook));
    }
    
    @Test
    void testValidStockFormat() throws DuplicatedEntityException, EntitySyntaxException {
        // Arrange
        CreateBookRequestDTO newBook = new CreateBookRequestDTO(CODE, AUTHOR, PUBLISHER, TITLE, PUBLISH_DATE, COST, STOCK);
        when(bookRepository.findById(newBook.getCode()))
                .thenReturn(Optional.empty());
        when(toolsService.isValidISBN(newBook.getCode()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(newBook.getPublishDate()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isPositiveInteger(newBook.getStock()))
                .thenReturn(TRUE_ASSERTION);
        
        
        // Act
        Book result = serviceToTest.createBook(newBook);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    void testGetBookByCode() {
        // Arrange
        Book book = new Book();
        book.setCode(CODE);
        when(bookRepository.findById(CODE)).thenReturn(Optional.of(book));
        
        // Act
        Optional<Book> result = serviceToTest.getBookByCode(CODE);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(CODE, result.get().getCode());
    }
    
    @Test
    void testGetBookByCodeNotFound() {
        // Arrange
        when(bookRepository.findById(CODE)).thenReturn(Optional.empty());
        
        // Act
        Optional<Book> result = serviceToTest.getBookByCode(CODE);
        
        // Assert
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testGetOneBookByCode() throws EntityNotFoundException {
        // Arrange
        Book book = new Book();
        book.setCode(CODE);
        when(bookRepository.findById(CODE))
                .thenReturn(Optional.of(book));
        
        // Act
        Book bookTest = serviceToTest.getOneBookByCode(CODE);
        
        // Assert
        assertNotNull(bookTest);
        assertEquals(book.getCode(), CODE);
        
    }
    
    @Test
    void testGetOneBookByCodeNotFound() {
        // Arrange
        when(bookRepository.findById(CODE))
                .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> serviceToTest.getOneBookByCode(CODE));
    }
    
}
