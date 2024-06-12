package com.froi.library.services.student;

import com.froi.library.dto.EnableStudentDTO;
import com.froi.library.dto.user.StudentDTO;
import com.froi.library.entities.Student;
import com.froi.library.entities.User;
import com.froi.library.enums.studentstatus.StudentStatus;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.StudentRepository;
import com.froi.library.services.tools.ToolsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
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
    private static final String NULL_STATUS = null;
    private static final String INVALID_NAME_WITH_NUMBER = "John2";
    private static final String INVALID_NAME_WITH_SPECIAL_CHAR = "John@Doe";
    private static final String INVALID_EMAIL = "asdfadasf";
    private static final boolean TRUE_ASSERTION = true;
    private static final boolean FALSE_ASSERTION = false;
    private static final Integer STUDENT_LOANS_COUNT = 5;
    private static final Integer EXPECTED_STUDENT_LOANS_COUNT = 5;
    private static final String STUDENT_ID = "student123";
    private static final String INVALID_STUDENT_ID = "invalidStudent123";
    private static final String ENABLE_DATE = "2024-05-01";
    private static final Integer INVALID_LOANS = 0;
    
    
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
    void testNullStatus() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO(VALID_ID, VALID_FIRST_NAME, VALID_LAST_NAME, VALID_BIRTH_DATE, VALID_DEGREE, NULL_STATUS, VALID_EMAIL);
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
    
    @Test
    void testGetOneStudentById() throws EntityNotFoundException {
        // Arrange
        Student student = new Student();
        student.setId(VALID_ID);
        when(studentRepository.findById(VALID_ID))
                .thenReturn(Optional.of(student));
        
        // Act
        Student studentToFind = serviceToTest.getOneStudentById(VALID_ID);
    
        // Assert
        assertNotNull(studentToFind);
    }
    
    @Test
    void testGetOneStudentByIdNotFound() {
        // Arrange
        Student student = new Student();
        student.setId(VALID_ID);
        when(studentRepository.findById(VALID_ID))
                .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> serviceToTest.getOneStudentById(VALID_ID));
    }
    
    @Test
    void testGetStudentLoansCount() throws EntityNotFoundException {
        // Arrange
        Student student = new Student();
        student.setId(VALID_ID);
        when(studentRepository.findById(VALID_ID))
                .thenReturn(Optional.of(student));
        when(studentRepository.countBookLoansByStudentWithStatus(student.getId()))
                .thenReturn(STUDENT_LOANS_COUNT);
        
        // Act
        Integer studentLoansCount = serviceToTest.getStudentLoansCount(VALID_ID);
    
        // Assert
        assertEquals(studentLoansCount, EXPECTED_STUDENT_LOANS_COUNT);
    }
    
    @Test
    void testGetStudentLoansCountNotFount() {
        // Arrange
        Student student = new Student();
        student.setId(VALID_ID);
        when(studentRepository.findById(VALID_ID))
                .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> serviceToTest.getStudentLoansCount(VALID_ID));
    }
    
    private Student student;
    
    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(STUDENT_ID);
        student.setStatus(StudentStatus.ACTIVE);
    }
    
    @Test
    void testSanctionStudentSuccess() throws EntityNotFoundException {
        // Arrange
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        
        // Act
        boolean result = serviceToTest.sanctionStudent(STUDENT_ID);
        
        // Assert
        assertTrue(result);
        assertEquals(StudentStatus.INACTIVE, student.getStatus());
    }
    
    @Test
    void testSanctionStudentNotFound() {
        // Arrange
        when(studentRepository.findById(INVALID_STUDENT_ID)).thenReturn(Optional.empty());
        
        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            serviceToTest.sanctionStudent(INVALID_STUDENT_ID);
        });
        assertEquals("STUDENT_NOT_FOUND", exception.getMessage());
    }
    
    @Test
    void testEnableStudentSuccess() throws EntityNotFoundException, EntitySyntaxException, DenegatedActionException {
        // Arrange
        EnableStudentDTO enableStudentDTO = new EnableStudentDTO(STUDENT_ID, ENABLE_DATE);
        when(toolsService.isValidDateFormat(ENABLE_DATE)).thenReturn(true);
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentRepository.countInvalidLoans(STUDENT_ID, Date.valueOf(ENABLE_DATE))).thenReturn(INVALID_LOANS);
        
        // Act
        boolean result = serviceToTest.enableStudent(enableStudentDTO);
        
        // Assert
        assertTrue(result);
        assertEquals(StudentStatus.ACTIVE, student.getStatus());
    }
    
    @Test
    void testEnableStudentInvalidDate() {
        // Arrange
        EnableStudentDTO enableStudentDTO = new EnableStudentDTO(STUDENT_ID, INVALID_DATE);
        when(toolsService.isValidDateFormat(INVALID_DATE)).thenReturn(false);
        
        // Act & Assert
        EntitySyntaxException exception = assertThrows(EntitySyntaxException.class, () -> {
            serviceToTest.enableStudent(enableStudentDTO);
        });
        assertEquals("INVALID_DATE", exception.getMessage());
    }
    
    @Test
    void testEnableStudentNotFound() {
        // Arrange
        EnableStudentDTO enableStudentDTO = new EnableStudentDTO(INVALID_STUDENT_ID, ENABLE_DATE);
        when(toolsService.isValidDateFormat(ENABLE_DATE)).thenReturn(true);
        when(studentRepository.findById(INVALID_STUDENT_ID)).thenReturn(Optional.empty());
        
        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            serviceToTest.enableStudent(enableStudentDTO);
        });
        assertEquals("STUDENT_NOT_FOUND", exception.getMessage());
    }
    
    @Test
    void testEnableStudentWithInvalidLoans() {
        // Arrange
        EnableStudentDTO enableStudentDTO = new EnableStudentDTO(STUDENT_ID, ENABLE_DATE);
        when(toolsService.isValidDateFormat(ENABLE_DATE)).thenReturn(true);
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentRepository.countInvalidLoans(STUDENT_ID, Date.valueOf(ENABLE_DATE))).thenReturn(1);
        
        // Act & Assert
        DenegatedActionException exception = assertThrows(DenegatedActionException.class, () -> {
            serviceToTest.enableStudent(enableStudentDTO);
        });
        assertEquals("STUDENT_HAS_INVALID_LOANS", exception.getMessage());
    }
    
    @Test
    void testFindAllInactiveStudents() {
        // Arrange
        Student inactiveStudent1 = new Student();
        inactiveStudent1.setStatus(StudentStatus.INACTIVE);
        Student inactiveStudent2 = new Student();
        inactiveStudent2.setStatus(StudentStatus.INACTIVE);
        List<Student> inactiveStudents = Arrays.asList(inactiveStudent1, inactiveStudent2);
        when(studentRepository.findAllByStatus(StudentStatus.INACTIVE)).thenReturn(inactiveStudents);
        
        // Act
        List<Student> result = serviceToTest.findAllInactiveStudents();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(inactiveStudent1));
        assertTrue(result.contains(inactiveStudent2));
    }
}

