package com.froi.library.security;

import com.froi.library.entities.User;
import com.froi.library.enums.studentstatus.Role;
import com.froi.library.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {
    
    public static final String INVALID_USERNAME = "205030121";
    public static final String USERNAME = "201830121";
    public static final String PASSWORD = "encrypted_password";
    public static final Role ROLE = Role.LIBRARIAN;
    
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserDetailsServiceImpl serviceToTest;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testNotFoundUser() {
        // Arrange
        when(userRepository.findById(INVALID_USERNAME))
                .thenReturn(Optional.empty());
        
        // Assert
        assertThrows(UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername(INVALID_USERNAME));
    }
    
    @Test
    void testValidUsername() {
        // Arrange
        User userOptional = new User();
        userOptional.setUsername(USERNAME);
        userOptional.setPassword(PASSWORD);
        userOptional.setRole(ROLE);
        when(userRepository.findById(USERNAME))
                .thenReturn(Optional.of(userOptional));
        
        // Act
        UserDetails user = serviceToTest.loadUserByUsername(USERNAME);
        
        // Assert
        assertNotNull(user);
    }
    
}