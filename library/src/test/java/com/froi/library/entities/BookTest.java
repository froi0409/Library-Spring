package com.froi.library.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

public class BookTest {

    private static final String CODE = "978-9706860699";
    private static final String TITLE = "Cálculo de una Variable";
    private static final String PUBLISH_DATE = "2020/12/12";
    private static final String PUBLISHER = "CENGAGE Learning";
    private static final String AUTHOR = "James Stewart";
    private static final Double COST = 450.00;
    private static final Integer STOCK = 12;
    
    private static final String EXPECTED_CODE = "978-9706860699";
    private static final String EXPECTED_TITLE = "Cálculo de una Variable";
    private static final String EXPECTED_PUBLISH_DATE = "2020/12/12";
    private static final String EXPECTED_PUBLISHER = "CENGAGE Learning";
    private static final String EXPECTED_AUTHOR = "James Stewart";
    private static final Double EXPECTED_COST = 450.00;
    private static final Integer EXPECTED_STOCK = 12;
    
    @Test
    void testBook() {
        // Arrange
        Book book = new Book();
        
        //Act
        book.setCode(CODE);
        book.setTitle(TITLE);
        book.setPublishDate(PUBLISH_DATE);
        book.setPublisher(PUBLISHER);
        book.setAuthor(AUTHOR);
        book.setCost(COST);
        book.setStock(STOCK);
        
        // Assert
        assertEquals(EXPECTED_CODE, book.getCode());
        assertEquals(EXPECTED_TITLE, book.getTitle());
        assertEquals(EXPECTED_PUBLISH_DATE, book.getPublishDate());
        assertEquals(EXPECTED_PUBLISHER, book.getPublisher());
        assertEquals(EXPECTED_AUTHOR, book.getAuthor());
        assertEquals(EXPECTED_COST, book.getCost());
        assertEquals(EXPECTED_STOCK, book.getStock());
        
    }

}
