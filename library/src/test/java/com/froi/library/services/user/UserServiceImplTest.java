package com.froi.library.services.user;

import com.froi.library.dto.user.CreateUserRequestDTO;
import com.froi.library.entities.User;
import com.froi.library.enums.studentstatus.Role;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    public static final String INVALID_USERNAME = "us";
    public static final String INVALID_PASSWORD = "xd";
    public static final String NULL_STUDENT = null;
    public static final String EXPECTED_STUDENT_USERNAME = "201830121";
    public static final String EXPECTED_LIBRARIAN_USERNAME = "LIB_01";
    public static final String EXPECTED_PASSWORD = "admin123";
    public static final String EXPECTED_STUDENT = "201830121";
    public static final String EXPECTED_ENCRYPTED_PASSWORD = "$2a$04$y8FbOSsXx/9HsV/Zenl7OeaUbpeeHaB1XWGCMmyztrJr4.edjVbbu";
    public static final Role STUDENT_ROLE = Role.STUDENT;
    public static final Role LIBRARIAN_ROLE = Role.LIBRARIAN;
    
    @Mock
    UserRepository repository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl serviceToTest;
    
    @Test
    void testInvalidUsername() {
        // Arrange
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(INVALID_USERNAME, EXPECTED_PASSWORD, EXPECTED_STUDENT);
        
        // Assert
        Assertions.assertThrows(EntitySyntaxException.class,
                () -> serviceToTest.createUser(newUser));
    }

    @Test
    void testInvalidPassword() {
        // Arrange
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(EXPECTED_STUDENT_USERNAME, INVALID_PASSWORD, EXPECTED_STUDENT);
        
        // Assert
        Assertions.assertThrows(EntitySyntaxException.class,
                () -> serviceToTest.createUser(newUser));
    }
    
    @Test
    void testDuplicatedUser() {
        // Arrange
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(EXPECTED_STUDENT_USERNAME, EXPECTED_PASSWORD, EXPECTED_STUDENT);
        when(repository.findById(newUser.getUsername()))
                .thenReturn(Optional.of(new User()));
        
        // Assert
        Assertions.assertThrows(DuplicatedEntityException.class,
                () -> serviceToTest.createUser(newUser));
        
    }
    
    @Test
    void testValidStudent() throws DuplicatedEntityException, EntitySyntaxException {
        // Arrange
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(EXPECTED_STUDENT_USERNAME, EXPECTED_PASSWORD, EXPECTED_STUDENT);
        when(repository.findById((newUser.getUsername())))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(EXPECTED_PASSWORD))
                .thenReturn(EXPECTED_ENCRYPTED_PASSWORD);
        
        // Act
        User createdUser = serviceToTest.createUser(newUser);
        Role userRole = createdUser.getRole();
        
        // Asserts
        assertNotNull(createdUser);
        assertEquals(STUDENT_ROLE, userRole);
    }
    
    @Test
    void testValidLibrarian() throws DuplicatedEntityException, EntitySyntaxException {
        // Arrange
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(EXPECTED_LIBRARIAN_USERNAME, EXPECTED_PASSWORD, NULL_STUDENT);
        when(repository.findById((newUser.getUsername())))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(EXPECTED_PASSWORD))
                .thenReturn(EXPECTED_ENCRYPTED_PASSWORD);
        
        // Act
        User createdUser = serviceToTest.createUser(newUser);
        Role userRole = createdUser.getRole();
        
        // Asserts
        assertNotNull(createdUser);
        assertEquals(LIBRARIAN_ROLE, userRole);
    }
    
}
