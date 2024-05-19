package com.froi.library.services.student;

import com.froi.library.dto.user.StudentDTO;
import com.froi.library.entities.Student;
import com.froi.library.entities.User;
import com.froi.library.enums.studentstatus.StudentStatus;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.StudentRepository;
import com.froi.library.services.tools.ToolsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {
    
    private static final String VALID_ID = "201830121";
    private static final String VALID_FIRST_NAME = "Fernando";
    private static final String VALID_LAST_NAME = "Ocaña";
    private static final String VALID_BIRTH_DATE = "2000-09-04";
    private static final String VALID_DEGREE = "1";
    private static final String VALID_STATUS = "ACTIVE";
    private static final String VALID_EMAIL = "fernandoocana201830121@cunoc.edu.gt";
    private static final String INVALID_DATE = "20-01-2000";
    private static final String INVALID_STATUS = "Inactive";
    private static final String INVALID_NAME_WITH_NUMBER = "John2";
    private static final String INVALID_NAME_WITH_SPECIAL_CHAR = "John@Doe";
    private static final String INVALID_EMAIL = "asdfadasf";
    private static final boolean TRUE_ASSERTION = true;
    private static final boolean FALSE_ASSERTION = false;
    
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ToolsService toolsService;
    
    @InjectMocks
    private StudentServiceImpl serviceToTest;
    
    @Test
    void testDuplicatedStudent() {
        // Arrange
        StudentDTO newStudent = new StudentDTO(VALID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, VALID_DEGREE, VALID_BIRTH_DATE, VALID_STATUS, VALID_EMAIL);
        when(studentRepository.findById(newStudent.getId()))
                .thenReturn(Optional.of(new Student()));
        
        // Act & Assert
        assertThrows(DuplicatedEntityException.class,
                () -> serviceToTest.createStudent(newStudent));
    }
    
    @Test
    void testInvalidEmailFormat() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO(VALID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, INVALID_DATE, VALID_DEGREE, StudentStatus.ACTIVE.name(), INVALID_EMAIL);
        when(studentRepository.findById(studentDTO.getId()))
                .thenReturn(Optional.empty());
        when(toolsService.isValidEmail(studentDTO.getEmail()))
                .thenReturn(FALSE_ASSERTION);
    
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.createStudent(studentDTO));
    }
    
    @Test
    void testInvalidDateFormat() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO(VALID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, INVALID_DATE, VALID_DEGREE, StudentStatus.ACTIVE.name(), VALID_EMAIL);
        when(studentRepository.findById(studentDTO.getId()))
                .thenReturn(Optional.empty());
        when(toolsService.isValidEmail(studentDTO.getEmail()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(studentDTO.getBirthDate())).thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.createStudent(studentDTO));
    }
    
    @Test
    void testInvalidStatus() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO(VALID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, VALID_BIRTH_DATE, VALID_DEGREE, INVALID_STATUS, VALID_EMAIL);
        when(studentRepository.findById(studentDTO.getId()))
                .thenReturn(Optional.empty());
        when(toolsService.isValidEmail(studentDTO.getEmail()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(studentDTO.getBirthDate()))
                .thenReturn(TRUE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.createStudent(studentDTO));
    }
    
    @Test
    void testInvalidFirstName() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO(VALID_ID, INVALID_NAME_WITH_NUMBER, VALID_LAST_NAME, VALID_BIRTH_DATE, VALID_DEGREE, StudentStatus.ACTIVE.name(), VALID_EMAIL);
        when(studentRepository.findById(studentDTO.getId()))
                .thenReturn(Optional.empty());
        when(toolsService.isValidEmail(studentDTO.getEmail()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(studentDTO.getBirthDate()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isAlphabetic(studentDTO.getFirstName()))
                .thenReturn(FALSE_ASSERTION);
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.createStudent(studentDTO));
    }
    
    @Test
    void testInvalidLastName() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO(VALID_ID, VALID_FIRST_NAME, INVALID_NAME_WITH_SPECIAL_CHAR, VALID_BIRTH_DATE, VALID_DEGREE, StudentStatus.ACTIVE.name(), VALID_EMAIL);
        when(studentRepository.findById(studentDTO.getId()))
                .thenReturn(Optional.empty());
        when(toolsService.isValidEmail(studentDTO.getEmail()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(studentDTO.getBirthDate()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isAlphabetic(studentDTO.getFirstName()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isAlphabetic(studentDTO.getLastName()))
                .thenReturn(FALSE_ASSERTION);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> serviceToTest.createStudent(studentDTO));
    }
    
    @Test
    void testValidStudent() throws DuplicatedEntityException, EntitySyntaxException {
        // Arrange
        StudentDTO studentDTO = new StudentDTO(VALID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, VALID_DEGREE, VALID_BIRTH_DATE, StudentStatus.ACTIVE.name(), VALID_EMAIL);
        when(studentRepository.findById(studentDTO.getId()))
                .thenReturn(Optional.empty());
        when(toolsService.isValidEmail(studentDTO.getEmail()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isValidDateFormat(studentDTO.getBirthDate()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isAlphabetic(studentDTO.getFirstName()))
                .thenReturn(TRUE_ASSERTION);
        when(toolsService.isAlphabetic(studentDTO.getLastName()))
                .thenReturn(TRUE_ASSERTION);
        
        // Act
        Student result = serviceToTest.createStudent(studentDTO);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    void testGetValidUserById() {
        // Arrange
        when(studentRepository.findById(VALID_ID))
                .thenReturn(Optional.of(new Student()));
        
        // Act
        Optional<Student> student = serviceToTest.getStudentById(VALID_ID);
        
        // Assert
        assertTrue(student.isPresent());
    }
    
    @Test
    void testGetUserNotFound() {
        // Arrange
        when(studentRepository.findById(VALID_ID))
                .thenReturn(Optional.empty());
        
        // Act
        Optional<Student> student = serviceToTest.getStudentById(VALID_ID);
        
        // Act & Assert
        assertTrue(student.isEmpty());
    }
}
