package com.froi.library.services.datafile;

import com.froi.library.dto.book.CreateBookRequestDTO;
import com.froi.library.dto.bookloan.CreateBookLoanDTO;
import com.froi.library.dto.datafile.DataFileError;
import com.froi.library.dto.datafile.DataFileResponseDTO;
import com.froi.library.dto.degree.CreateDegreeRequestDTO;
import com.froi.library.dto.user.CreateUserRequestDTO;
import com.froi.library.dto.user.StudentDTO;
import com.froi.library.entities.Student;
import com.froi.library.enums.datafile.DataFileErrorType;
import com.froi.library.exceptions.*;
import com.froi.library.services.book.BookService;
import com.froi.library.services.bookloan.BookLoanService;
import com.froi.library.services.degree.DegreeService;
import com.froi.library.services.student.StudentService;
import com.froi.library.services.tools.ToolsService;
import com.froi.library.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataFileServiceImplTest {
    
    private static final String VALID_CARRERA_LINE = "CARRERA,Ingeniería,Software";
    private static final String VALID_ESTUDIANTE_LINE = "ESTUDIANTE,201830121,Fernando Rubén,Ocaña Ixcot,58,2000-09-04,fernandoocana201830121@cunoc.edu.gt";
    private static final String VALID_USUARIO_LINE = "USUARIO,201830121,password,STUDENT";
    private static final String VALID_BIBLIOTECARIO_LINE = "BIBLIOTECARIO,LIB_01,password";
    private static final String VALID_LIBRO_LINE = "LIBRO,978-0262033848,Thomas H. Cormen,MIT Press,Introduction to Algorithms,2009-02-01,25.22,4";
    private static final String VALID_PRESTAMO_LINE = "PRESTAMO,001,201830121,2023-05-17";
    private static final String INVALID_EMPTY = "";
    private static final String INVALID_CARRERA_LINE = "CARRERA,Ingeniería,Software,valor";
    private static final String INVALID_ESTUDIANTE_LINE = "ESTUDIANTE,201830121,Fernando Rubén,Ocaña Ixcot,58,2000-09-04,fernandoocana201830121@cunoc.edu.gt,valor";
    private static final String INVALID_USUARIO_LINE = "USUARIO,201830121,password,STUDENT,valor";
    private static final String INVALID_BIBLIOTECARIO_LINE = "BIBLIOTECARIO,LIB_01,password,valor";
    private static final String INVALID_LIBRO_LINE = "LIBRO,978-0262033848,Thomas H. Cormen,MIT Press,Introduction to Algorithms,2009-02-01,25.22,4,valor";
    private static final String INVALID_PRESTAMO_LINE = "PRESTAMO,001,201830121,2023-05-17,valor,valor,valor";
    private static final String INVALID_ENTITY_PARAMETERS = "ENTIDAD,VALOR,VALOR";
    private static final int EXPECTED_RECORDS_COUNT = 6;
    private static final int LINE_NUMBER = 1;
    private static final String[] INVALID_COLUMNS_SYNTAX = {"CARRERA", "Ingeniería"};
    private static final String[] INVALID_COLUMNS_DUPLICATE = {"ESTUDIANTE", "201830121", "Fernando Rubén", "Ocaña Ixcot", "1", "2000-09-04", "fernandoocana201830121@cunoc.edu.gt"};
    private static final String[] INVALID_COLUMNS_NOT_FOUND = {"PRESTAMO", "bookCode1", "userId", "loanDate"};
    private static final String[] INVALID_COLUMNS_DENIED_ACTION = {"PRESTAMO", "001", "201830121", "2023-05-17"};
    
    
    @Mock
    private BookService bookService;
    @Mock
    private StudentService studentService;
    @Mock
    private UserService userService;
    @Mock
    private DegreeService degreeService;
    @Mock
    private BookLoanService bookLoanService;
    @Mock
    private ToolsService toolsService;
    @Mock
    private MultipartFile multipartFile;
    
    @InjectMocks
    private DataFileServiceImpl dataFileService;
    
    private BufferedReader reader;
    
    @BeforeEach
    void setUp() throws Exception {
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(
                (VALID_CARRERA_LINE + "\n" + VALID_ESTUDIANTE_LINE + "\n" + VALID_USUARIO_LINE + "\n" +
                        VALID_BIBLIOTECARIO_LINE + "\n" + VALID_LIBRO_LINE + "\n" + VALID_PRESTAMO_LINE).getBytes()));
        reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
    }
    
    @Test
    void testHandleDataFile() throws Exception {
        // Arrange
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(
                (VALID_CARRERA_LINE + "\n" + VALID_ESTUDIANTE_LINE + "\n" + VALID_USUARIO_LINE + "\n" +
                        VALID_BIBLIOTECARIO_LINE + "\n" + VALID_LIBRO_LINE + "\n" + VALID_PRESTAMO_LINE).getBytes()));
        
        // Act
        DataFileResponseDTO response = dataFileService.handleDataFile(multipartFile);
        
        // Assert
        assertNotNull(response);
        assertEquals(EXPECTED_RECORDS_COUNT, response.getRecords());
    }
    
    @Test
    void testInsertEntityDegree() throws Exception {
        // Arrange
        String[] columns = VALID_CARRERA_LINE.split(",");
        List<DataFileError> errorsList = new ArrayList<>();
        
        // Act
        boolean result = dataFileService.insertEntity(errorsList, columns, 1);
        
        // Assert
        assertTrue(result);
        verify(degreeService).createDegree(any(CreateDegreeRequestDTO.class));
        assertTrue(errorsList.isEmpty());
    }
    
    @Test
    void testInsertEntityStudent() throws Exception {
        // Arrange
        String[] columns = VALID_ESTUDIANTE_LINE.split(",");
        List<DataFileError> errorsList = new ArrayList<>();
        
        // Act
        boolean result = dataFileService.insertEntity(errorsList, columns, 2);
        
        // Assert
        assertTrue(result);
        verify(studentService).createStudent(any(StudentDTO.class));
        assertTrue(errorsList.isEmpty());
    }
    
    @Test
    void testInsertEntityUser() throws Exception {
        // Arrange
        String[] columns = VALID_USUARIO_LINE.split(",");
        List<DataFileError> errorsList = new ArrayList<>();
        
        // Act
        boolean result = dataFileService.insertEntity(errorsList, columns, 3);
        
        // Assert
        assertTrue(result);
        verify(userService).createUser(any(CreateUserRequestDTO.class));
        assertTrue(errorsList.isEmpty());
    }
    
    @Test
    void testInsertEntityLibrarian() throws Exception {
        // Arrange
        String[] columns = VALID_BIBLIOTECARIO_LINE.split(",");
        List<DataFileError> errorsList = new ArrayList<>();
        
        // Act
        boolean result = dataFileService.insertEntity(errorsList, columns, 4);
        
        // Assert
        assertTrue(result);
        verify(userService).createUser(any(CreateUserRequestDTO.class));
        assertTrue(errorsList.isEmpty());
    }
    
    @Test
    void testInsertEntityBook() throws Exception {
        // Arrange
        String[] columns = VALID_LIBRO_LINE.split(",");
        List<DataFileError> errorsList = new ArrayList<>();
        
        // Act
        boolean result = dataFileService.insertEntity(errorsList, columns, 5);
        
        // Assert
        assertTrue(result);
        verify(bookService).createBook(any(CreateBookRequestDTO.class));
        assertTrue(errorsList.isEmpty());
    }
    
    @Test
    void testInsertEntityLoan() throws Exception {
        // Arrange
        String[] columns = VALID_PRESTAMO_LINE.split(",");
        List<DataFileError> errorsList = new ArrayList<>();
        
        // Act
        boolean result = dataFileService.insertEntity(errorsList, columns, 6);
        
        // Assert
        assertTrue(result);
        verify(bookLoanService).createLoan(any(CreateBookLoanDTO.class));
        assertTrue(errorsList.isEmpty());
    }
    
    @Test
    void testInsertEntityInvalidEntity() {
        // Arrange
        String invalidLine = "INVALID,1";
        String[] columns = invalidLine.split(",");
        List<DataFileError> errorsList = new ArrayList<>();
        
        // Act
        boolean result = dataFileService.insertEntity(errorsList, columns, 7);
        
        // Assert
        assertFalse(result);
        assertFalse(errorsList.isEmpty());
        assertEquals(DataFileErrorType.SYNTAX.name(), errorsList.get(0).getType());
    }
    
    @Test
    void testVerifyParametersValid() throws EntitySyntaxException {
        // Arrange
        int expectedParameters = 3;
        int actualParameters = 3;
        String columnName = "CARRERA";
        
        // Act & Assert
        assertTrue(dataFileService.verifyParameters(expectedParameters, actualParameters, columnName));
    }
    
    @Test
    void testVerifyParametersInvalid() {
        // Arrange
        int expectedParameters = 3;
        int actualParameters = 2;
        String columnName = "CARRERA";
        
        // Act & Assert
        EntitySyntaxException exception = assertThrows(EntitySyntaxException.class, () ->
                dataFileService.verifyParameters(expectedParameters, actualParameters, columnName));
        assertEquals("CARRERA_PARAMETERS_DO_NOT_MATCH", exception.getMessage());
    }
    
    @Test
    void testInsertEntityCatchesEntitySyntaxException() throws Exception {
        // Arrange
        List<DataFileError> errorsList = new ArrayList<>();
        lenient().doThrow(new EntitySyntaxException("")).when(degreeService).createDegree(any());
        
        // Act
        boolean result = dataFileService.insertEntity(errorsList, INVALID_COLUMNS_SYNTAX, LINE_NUMBER);
        
        // Assert
        assertFalse(result);
        assertFalse(errorsList.isEmpty());
        assertEquals(DataFileErrorType.SYNTAX.name(), errorsList.get(0).getType());
    }
    
    
    @Test
    void testInsertEntityCatchesDuplicatedEntityException() throws Exception {
        // Arrange
        List<DataFileError> errorsList = new ArrayList<>();
        doThrow(new DuplicatedEntityException("")).when(studentService).createStudent(any(StudentDTO.class));
        
        // Act
        boolean result = dataFileService.insertEntity(errorsList, INVALID_COLUMNS_DUPLICATE, LINE_NUMBER);
        
        // Assert
        assertFalse(result);
        assertFalse(errorsList.isEmpty());
        assertEquals(DataFileErrorType.DUPLICATED_ENTITY.name(), errorsList.get(0).getType());
    }
    
    
    @Test
    void testInsertEntityCatchesEntityNotFoundException() throws Exception {
        // Arrange
        List<DataFileError> errorsList = new ArrayList<>();
        doThrow(new EntityNotFoundException("Entity Not Found")).when(bookLoanService).createLoan(any());
        
        // Act
        boolean result = dataFileService.insertEntity(errorsList, INVALID_COLUMNS_NOT_FOUND, LINE_NUMBER);
        
        // Assert
        assertFalse(result);
        assertFalse(errorsList.isEmpty());
        assertEquals(DataFileErrorType.ENTITY_NOT_FOUND.name(), errorsList.get(0).getType());
    }
    
    @Test
    void testInsertEntityCatchesDenegatedActionException() throws Exception {
        // Arrange
        List<DataFileError> errorsList = new ArrayList<>();
        doThrow(new DenegatedActionException("")).when(bookLoanService).createLoan(any());
        
        // Act
        boolean result = dataFileService.insertEntity(errorsList, INVALID_COLUMNS_DENIED_ACTION, LINE_NUMBER);
        
        // Assert
        assertFalse(result);
        assertFalse(errorsList.isEmpty());
        assertEquals(DataFileErrorType.ERROR.name(), errorsList.get(0).getType());
    }
    
    @Test
    void testEntityNotFound() {
        //Arrange
        List<DataFileError> errorsList = new ArrayList<>();
        String[] columns = INVALID_ENTITY_PARAMETERS.split(",");
        
        // Act
        boolean result = dataFileService.insertEntity(errorsList, columns, LINE_NUMBER);
        
        // Assert
        assertFalse(result);
        
    }
    
    @Test
    void testDegreeWrongParameters() {
        // Arrange
        List<DataFileError> errorList = new ArrayList<>();
        String[] columns = INVALID_CARRERA_LINE.split(",");
        
        // Act
        boolean result = dataFileService.insertEntity(errorList, columns, LINE_NUMBER);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testStudentWrongParameters() {
        // Arrange
        List<DataFileError> errorList = new ArrayList<>();
        String[] columns = INVALID_ESTUDIANTE_LINE.split(",");
        
        // Act
        boolean result = dataFileService.insertEntity(errorList, columns, LINE_NUMBER);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testUserWrongParameters() {
        // Arrange
        List<DataFileError> errorList = new ArrayList<>();
        String[] columns = INVALID_USUARIO_LINE.split(",");
        
        // Act
        boolean result = dataFileService.insertEntity(errorList, columns, LINE_NUMBER);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testLibrarianWrongParameters() {
        // Arrange
        List<DataFileError> errorList = new ArrayList<>();
        String[] columns = INVALID_BIBLIOTECARIO_LINE.split(",");
        
        // Act
        boolean result = dataFileService.insertEntity(errorList, columns, LINE_NUMBER);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testBookWrongParameters() {
        // Arrange
        List<DataFileError> errorList = new ArrayList<>();
        String[] columns = INVALID_LIBRO_LINE.split(",");
        
        // Act
        boolean result = dataFileService.insertEntity(errorList, columns, LINE_NUMBER);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testLoanWrongParameters() {
        // Arrange
        List<DataFileError> errorList = new ArrayList<>();
        String[] columns = INVALID_PRESTAMO_LINE.split(",");
        
        // Act
        boolean result = dataFileService.insertEntity(errorList, columns, LINE_NUMBER);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testEmptyWrongParameters() {
        // Arrange
        List<DataFileError> errorList = new ArrayList<>();
        String[] columns = INVALID_EMPTY.split(",");
        
        // Act
        boolean result = dataFileService.insertEntity(errorList, columns, LINE_NUMBER);
        
        // Assert
        assertFalse(result);
    }
}


