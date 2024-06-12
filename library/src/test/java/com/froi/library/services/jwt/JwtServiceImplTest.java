package com.froi.library.services.jwt;

import com.froi.library.entities.User;
import com.froi.library.enums.studentstatus.Role;
import com.froi.library.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtServiceImplTest {
    
    public static final String SECRET_PHRASE = "9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9";
    public static final String USERNAME = "username";
    public static final String INVALID_TOKEN = "invalid_token";
    public static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyMDE4MzAxMjEiLCJyb2xlIjoiU1RVREVOVCIsIm5hbWUiOiJKb2huIERvZSIsImlhdCI6MTY0Mzc5ODQwMCwiZXhwIjoxNjc1MzM0NDAwfQ.LE8tMxTKmzu-DJEgFXx3kFn_zIF0EXHeIh3YxZF2XEc";
    public static final Role ROLE = Role.STUDENT;
    private static final String TOKEN = "eyJhbGciOiJIUzM4NCJ9.eyJyb2xlIjoiTElCUkFSSUFOIiwic3ViIjoibGlicmFyaWFuMSIsImV4cCI6MTcxNTkzNTAwNCwiaWF0IjoxNzE1OTMzMjA0fQ.oEE-aJevlrwFJiPiaR9tluf3mrcFKWHOJ0He8l4Sa14DwQ2fNDA5pGEz0SNQHssu";
    private static final String CLAIMS_STRING = "sampleClaimsString";
    
    @Mock
    UserRepository userRepository;
    
    @Mock
    private Claims claims;
    
    @InjectMocks
    JwtServiceImpl serviceToTest;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testGenerateToken() {
        // Arrange & Act
        String token = serviceToTest.generateToken(USERNAME, ROLE);
        
        // Assert
        assertNotNull(token);
    }
    
    @Test
    void testGetUsernameValidToken() {
        // Arrange
        String token = serviceToTest.generateToken(USERNAME, ROLE);
        
        // Act
        String username = serviceToTest.getUsername(token);
        
        // Assert
        assertEquals(USERNAME, username);
    }
    
    @Test
    void testGetUsernameInvalidToken() {
        // Assert
        assertThrows(MalformedJwtException.class, () -> serviceToTest.getUsername(INVALID_TOKEN));
    }
    
    @Test
    void testIsValidValidToken() {
        // Arrange
        String token = serviceToTest.generateToken(USERNAME, ROLE);
        
        // Act
        boolean isValid = serviceToTest.isValid(token);
        
        // Assert
        assertTrue(isValid);
    }
    
    @Test
    void testIsValidExpiredToken() {
        // Assert
        assertFalse(serviceToTest.isValid(EXPIRED_TOKEN));
    }
    
    @Test
    void testUpdateTokenExpirationUserExists() {
        // Arrange
        User user = new User();
        user.setUsername(USERNAME);
        user.setTokenExpiration(LocalDateTime.now());
        
        when(userRepository.findById(USERNAME)).thenReturn(Optional.of(user));
        
        // Act
        serviceToTest.updateTokenExpiration(USERNAME);
        
        // Assert
        assertTrue(user.getTokenExpiration().isAfter(LocalDateTime.now()));
    }
    
    @Test
    void testUpdateTokenExpirationUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(USERNAME)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertDoesNotThrow(() -> serviceToTest.updateTokenExpiration(USERNAME));
    }
    
    @Test
    void testIsTokenExpiredUserExistsAndTokenNotExpired() {
        // Arrange
        User user = new User();
        user.setUsername(USERNAME);
        user.setTokenExpiration(LocalDateTime.now().plusMinutes(30));
        
        when(userRepository.findById(USERNAME)).thenReturn(Optional.of(user));
        
        // Act
        boolean isExpired = serviceToTest.isTokenExpired(USERNAME);
        
        // Assert
        assertFalse(isExpired);
    }
    
    @Test
    void testIsTokenExpiredUserExistsAndTokenNotExpiredButNull() {
        // Arrange
        User user = new User();
        user.setUsername(USERNAME);
        user.setTokenExpiration(null);
        
        when(userRepository.findById(USERNAME)).thenReturn(Optional.of(user));
        
        // Act
        boolean isExpired = serviceToTest.isTokenExpired(USERNAME);
        
        // Assert
        assertTrue(isExpired);
    }
    
    
    @Test
    void testIsTokenExpiredUserExistsAndTokenExpired() {
        // Arrange
        User user = new User();
        user.setUsername(USERNAME);
        user.setTokenExpiration(LocalDateTime.now().minusMinutes(30));
        
        when(userRepository.findById(USERNAME)).thenReturn(Optional.of(user));
        
        // Act
        boolean isExpired = serviceToTest.isTokenExpired(USERNAME);
        
        // Assert
        assertTrue(isExpired);
    }
    
    @Test
    void testsIsTokenExpiredUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(USERNAME)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertTrue(serviceToTest.isTokenExpired(USERNAME));
    }
    
    @Test
    void testGetSecretKey() {
        // Act
        SecretKey secretKey = serviceToTest.getSecretKey();
        
        // Assert
        assertNotNull(secretKey);
    }
    
    @Test
    void testExtractClaimsValidToken() {
        // Arrange
        String token = serviceToTest.generateToken(USERNAME, ROLE);
        
        // Act
        Claims claims = serviceToTest.extractClaims(token);
        
        // Assert
        assertEquals(USERNAME, claims.getSubject());
        assertEquals(ROLE.toString(), claims.get("role"));
    }
    
}
