package com.froi.library.services.user;

import com.froi.library.dto.user.CreateUserRequestDTO;
import com.froi.library.dto.user.StudentDTO;
import com.froi.library.entities.Student;
import com.froi.library.entities.User;
import com.froi.library.enums.studentstatus.Role;
import com.froi.library.enums.studentstatus.StudentStatus;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.UserRepository;
import com.froi.library.services.student.StudentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
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
    
    private static final String STUDENT_ID = "201830121";
    private static final String STUDENT_FIRST_NAME = "Fernando Rubén";
    private static final String STUDENT_LAST_NAME = "Ocaña Ixcot";
    private static final String DEGREE_ID = "1";
    private static final String STUDENT_BIRTH_DATE = "2000/09/04";
    private static final StudentStatus STUDENT_STATUS = StudentStatus.ACTIVE;
    private static final String VALID_EMAIL = "fernandoocana201830121@cunoc.edu.gt";
    private static final String INVALID_EMAIL = "correo.invalido";
    
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    StudentService studentService;
    @InjectMocks
    UserServiceImpl serviceToTest;
    
    @Test
    void testInvalidUsername() {
        // Arrange
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(INVALID_USERNAME, EXPECTED_PASSWORD, NULL_STUDENT);
        
        // Assert
        Assertions.assertThrows(EntitySyntaxException.class,
                () -> serviceToTest.createUser(newUser));
    }

    @Test
    void testInvalidPassword() {
        // Arrange
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(EXPECTED_STUDENT_USERNAME, INVALID_PASSWORD, NULL_STUDENT);
        
        // Assert
        Assertions.assertThrows(EntitySyntaxException.class,
                () -> serviceToTest.createUser(newUser));
    }
    
    @Test
    void testDuplicatedUser() {
        // Arrange
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(EXPECTED_STUDENT_USERNAME, EXPECTED_PASSWORD, NULL_STUDENT);
        when(userRepository.findById(newUser.getUsername()))
                .thenReturn(Optional.of(new User()));
        
        // Assert
        Assertions.assertThrows(DuplicatedEntityException.class,
                () -> serviceToTest.createUser(newUser));
        
    }
    
    @Test
    void testValidStudent() throws DuplicatedEntityException, EntitySyntaxException, EntityNotFoundException {
        // Arrange
        StudentDTO student = new StudentDTO(STUDENT_ID, STUDENT_FIRST_NAME, STUDENT_LAST_NAME, DEGREE_ID, STUDENT_BIRTH_DATE, STUDENT_STATUS.name(), VALID_EMAIL);
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(EXPECTED_STUDENT_USERNAME, EXPECTED_PASSWORD, STUDENT_ID);
        when(userRepository.findById((newUser.getUsername())))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(EXPECTED_PASSWORD))
                .thenReturn(EXPECTED_ENCRYPTED_PASSWORD);
        when(studentService.getStudentById(newUser.getStudent()))
                .thenReturn(Optional.of(new Student()));
        
        // Act
        User createdUser = serviceToTest.createUser(newUser);
        Role userRole = createdUser.getRole();
        
        // Asserts
        assertNotNull(createdUser);
        assertEquals(STUDENT_ROLE, userRole);
    }
    
    @Test
    void testDuplicatedStudent() throws EntityNotFoundException {
        // Arrange
        StudentDTO student = new StudentDTO(STUDENT_ID, STUDENT_FIRST_NAME, STUDENT_LAST_NAME, DEGREE_ID, STUDENT_BIRTH_DATE, STUDENT_STATUS.name(), VALID_EMAIL);
        Student studentEntity = new Student();
        studentEntity.setId(STUDENT_ID);
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(EXPECTED_STUDENT_USERNAME, EXPECTED_PASSWORD, STUDENT_ID);
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User());
        when(userRepository.findById((newUser.getUsername())))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(EXPECTED_PASSWORD))
                .thenReturn(EXPECTED_ENCRYPTED_PASSWORD);
        when(studentService.getStudentById(STUDENT_ID))
                .thenReturn(Optional.of(studentEntity));
        when(userRepository.findUsersByStudent_Id(STUDENT_ID))
                .thenReturn(mockUsers);
    
        // Act & Assert
        assertThrows(DuplicatedEntityException.class,
                () -> serviceToTest.createUser(newUser));
    }
    
    @Test
    void testValidLibrarian() throws DuplicatedEntityException, EntitySyntaxException, EntityNotFoundException {
        // Arrange
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(EXPECTED_LIBRARIAN_USERNAME, EXPECTED_PASSWORD, NULL_STUDENT);
        when(userRepository.findById((newUser.getUsername())))
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
