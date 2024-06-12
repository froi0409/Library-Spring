package com.froi.library.services.tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToolsServiceTest {
    
    private static final String VALID_10_ISBN = "0-19-852663-6";
    private static final String VALID_13_ISBN = "978-1-56619-909-4";
    private static final String INVALID_ISBN_LENGTH = "9999999990";
    private static final String INVALID_ISBN = "isbn";
    private static final String INVALID_13_ISBN_CHAR = "ASDFGHSDFDSAS";
    private static final String INVALID_13_ISBN_NUMBERS = "1234567891234";
    private static final String VALID_DATE_FORMAT = "2022-05-20";
    private static final String INVALID_DATE_FORMAT = "20-05-2022";
    private static final Double VALID_DOUBLE = 12.34;
    private static final Double NEGATIVE_DOUBLE = -45.12;
    private static final Double NULL_DOUBLE = null;
    private static final String VALID_DOUBLE_STRING = "12.34";
    private static final String VALID_POSITIVE_INTEGER_STRING = "123";
    private static final String INVALID_POSITIVE_INTEGER_STRING = "-123";
    private static final String INVALID_FORMAT_NUMBER = "AS123AS";
    private static final String INVALID_MONEY_STRING = "12.345";
    private static final String INVALID_MONEY_NEGATIVE = "-12.25";
    private static final String INVALID_MONEY_NUMBER_FORMAT = "12A.21";
    private static final String VALID_STRING = "Hello World";
    private static final String INVALID_NUMBER_STRING = "Hello123";
    private static final String INVALID_SPECIAL_CHAR_STRING = "Hello!";
    private static final String NULL_STRING = null;
    private static final String EMPTY_STRING = "";
    private static final String VALID_EMAIL = "fernandoocana201830121@cunoc.edu.gt";
    private static final String INVALID_EMAIL = "correo.invalido";
    
    @InjectMocks
    private ToolsServiceImpl serviceToTest;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testInvalidISBN() {
        // Arrange & Act
        boolean isValid = serviceToTest.isValidISBN(INVALID_ISBN);
        boolean isValid13 = serviceToTest.isValidISBN(INVALID_13_ISBN_CHAR);
        boolean isValid13Numbers = serviceToTest.isValidISBN(INVALID_13_ISBN_NUMBERS);
        boolean isValidLength = serviceToTest.isValidISBN(INVALID_ISBN_LENGTH);
        // Assert
        assertFalse(isValid);
        assertFalse(isValid13);
        assertFalse(isValid13Numbers);
        assertFalse(isValidLength);
    }
    
    @Test
    void testValid10ISBN() {
        // Arrange & Act
        boolean isValid = serviceToTest.isValidISBN(VALID_10_ISBN);
        
        // Assert
        assertTrue(isValid);
    }
    
    @Test
    void testValid13ISBN() {
        // Arrange
        boolean isValid = serviceToTest.isValidISBN(VALID_13_ISBN);
        
        // Act & Assert
        assertTrue(isValid);
    }
    
    @Test
    void testValidDateFormat() {
        // Arrange & Act
        boolean isValid = serviceToTest.isValidDateFormat(VALID_DATE_FORMAT);
        
        // Assert
        assertTrue(isValid);
    }
    
    @Test
    void testInvalidDateFormat() {
        // Arrange
        boolean isValid = serviceToTest.isValidDateFormat(INVALID_DATE_FORMAT);
        
        // Assert
        assertFalse(isValid);
    }
    
    @Test
    void testIsValidDouble() {
        // Arrange & Act
        boolean isValid = serviceToTest.isValidDouble(VALID_DOUBLE);
        
        // Assert
        assertTrue(isValid);
    }
    
    @Test
    void testInvalidDouble() {
        // Arrangr & Act
        boolean isValidNullDouble = serviceToTest.isValidDouble(NULL_DOUBLE);
        boolean isValidNegativeDouble = serviceToTest.isValidDouble(NEGATIVE_DOUBLE);
        
        // Assert
        assertFalse(isValidNullDouble);
        assertFalse(isValidNullDouble);
    }
    
    @Test
    void testIsMoney() {
        // Arrange & Assert
        boolean isValid = serviceToTest.isMoney(VALID_DOUBLE_STRING);
        
        // Assert
        assertTrue(isValid);
    }
    
    @Test
    void testInvalidMoney() {
        // Arrange & Act
        boolean isValidCents = serviceToTest.isMoney(INVALID_MONEY_STRING);
        boolean isValidNegative = serviceToTest.isMoney(INVALID_MONEY_NEGATIVE);
        boolean isValidFormat = serviceToTest.isMoney(INVALID_MONEY_NUMBER_FORMAT);
        
        // Assert
        assertFalse(isValidCents);
        assertFalse(isValidNegative);
        assertFalse(isValidFormat);
    }
    
    @Test
    void testIsPositiveInteger() {
        // Arrange & aCT
        boolean isValid = serviceToTest.isPositiveInteger(VALID_POSITIVE_INTEGER_STRING);
        boolean isInvalid = serviceToTest.isPositiveInteger(INVALID_POSITIVE_INTEGER_STRING);
        boolean isInvalidFormat = serviceToTest.isPositiveInteger(INVALID_FORMAT_NUMBER);
        
        // Assert
        assertTrue(isValid);
        assertFalse(isInvalid);
        assertFalse(isInvalidFormat);
    }
    
    @Test
    void testIsAlphabetic_ValidString_ReturnsTrue() {
        // Arrange
        String validString = VALID_STRING;
        
        // Act
        boolean result = serviceToTest.isAlphabetic(validString);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void testIsAlphabetic_InvalidStringWithNumber_ReturnsFalse() {
        // Arrange
        String invalidString = INVALID_NUMBER_STRING;
        
        // Act
        boolean result = serviceToTest.isAlphabetic(invalidString);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testIsAlphabetic_InvalidStringWithSpecialCharacter_ReturnsFalse() {
        // Arrange
        String invalidString = INVALID_SPECIAL_CHAR_STRING;
        
        // Act
        boolean result = serviceToTest.isAlphabetic(invalidString);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testIsAlphabetic_NullString_ReturnsFalse() {
        // Arrange
        String nullString = NULL_STRING;
        
        // Act
        boolean result = serviceToTest.isAlphabetic(nullString);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testIsAlphabetic_EmptyString_ReturnsTrue() {
        // Arrange
        String emptyString = EMPTY_STRING;
        
        // Act
        boolean result = serviceToTest.isAlphabetic(emptyString);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void isValidEmail_ValidEmail_ReturnsTrue() {
        // Arrange
        String email = VALID_EMAIL;
        
        // Act
        boolean result = serviceToTest.isValidEmail(email);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void isValidEmail_InvalidEmail_ReturnsFalse() {
        // Arrange
        String email = INVALID_EMAIL;
        
        // Act
        boolean result = serviceToTest.isValidEmail(email);
        
        // Assert
        assertFalse(result);
    }
    
}
