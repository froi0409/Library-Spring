package com.froi.library.services.datafile;

import com.froi.library.dto.book.CreateBookRequestDTO;
import com.froi.library.dto.bookloan.CreateBookLoanDTO;
import com.froi.library.dto.datafile.DataFileError;
import com.froi.library.dto.datafile.DataFileResponseDTO;
import com.froi.library.dto.degree.CreateDegreeRequestDTO;
import com.froi.library.dto.user.CreateUserRequestDTO;
import com.froi.library.dto.user.StudentDTO;
import com.froi.library.enums.datafile.DataFileErrorType;
import com.froi.library.exceptions.*;
import com.froi.library.services.book.BookService;
import com.froi.library.services.bookloan.BookLoanService;
import com.froi.library.services.degree.DegreeService;
import com.froi.library.services.student.StudentService;
import com.froi.library.services.tools.ToolsService;
import com.froi.library.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataFileServiceImpl implements DataFileService {
    
    private BookService bookService;
    private StudentService studentService;
    private UserService userService;
    private DegreeService degreeService;
    private BookLoanService bookLoanService;
    private ToolsService toolsService;
    
    @Autowired
    public DataFileServiceImpl(BookService bookService, StudentService studentService, UserService userService, DegreeService degreeService, BookLoanService bookLoanService, ToolsService toolsService) {
        this.bookService = bookService;
        this.studentService = studentService;
        this.userService = userService;
        this.degreeService = degreeService;
        this.bookLoanService = bookLoanService;
        this.toolsService = toolsService;
    }
    
    @Override
    public List<String> verifySystemData() {
        return List.of();
    }
    
    @Override
    public DataFileResponseDTO handleDataFile(MultipartFile file) throws UploadDataFileException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<DataFileError> errorsList = new ArrayList<>();
            int linesCount = 0;
            int recordsCount = 0;
            String line;
            
            while ((line = reader.readLine()) != null) {
                linesCount++;
                String[] columns = line.split(",");
                if (insertEntity(errorsList, columns, linesCount)) {
                    recordsCount++;
                }
            }
            return new DataFileResponseDTO(recordsCount, errorsList);
        } catch (IOException ex) {
            throw new UploadDataFileException("ERROR_UPLOADING_DATA_FILE");
        }
    }
    
    @Override
    public boolean insertEntity(List<DataFileError> errorsList, String[] columns, int line) {
        try {
            if (columns[0].equalsIgnoreCase("CARRERA")) {
                verifyParameters(3, columns.length, "CARRERA");
                CreateDegreeRequestDTO newDegree = new CreateDegreeRequestDTO(columns[1], columns[2]);
                degreeService.createDegree(newDegree);
            } else if (columns[0].equalsIgnoreCase("ESTUDIANTE")) {
                verifyParameters(7, columns.length, "ESTUDIANTE");
                StudentDTO newStudent = new StudentDTO(columns[1], columns[2], columns[3], columns[4], columns[5], null, columns[6]);
                studentService.createStudent(newStudent);
            } else if (columns[0].equalsIgnoreCase("USUARIO")) {
                verifyParameters(4, columns.length, "USUARIO");
                CreateUserRequestDTO newUser = new CreateUserRequestDTO(columns[1], columns[2], columns[3]);
                userService.createUser(newUser);
            } else if (columns[0].equalsIgnoreCase("BIBLIOTECARIO")) {
                verifyParameters(3, columns.length, "BIBLIOTECARIO");
                CreateUserRequestDTO newUser = new CreateUserRequestDTO(columns[1], columns[2], null);
                userService.createUser(newUser);
            } else if (columns[0].equalsIgnoreCase("LIBRO")) {
                verifyParameters(8, columns.length, "LIBRO");
                CreateBookRequestDTO newBook = new CreateBookRequestDTO(columns[1], columns[2], columns[3], columns[4], columns[5], columns[6], columns[7]);
                bookService.createBook(newBook);
            } else if (columns[0].equalsIgnoreCase("PRESTAMO")) {
                verifyParameters(4, columns.length, "PRESTAMO");
                List<String> bookCodes = new ArrayList<>();
                bookCodes.add(columns[1]);
                CreateBookLoanDTO newLoan = new CreateBookLoanDTO(bookCodes, columns[2], columns[3]);
                bookLoanService.createLoan(newLoan);
            } else {
                if (!columns[0].trim().isEmpty()) {
                    errorsList.add(new DataFileError(line, DataFileErrorType.SYNTAX.name(), "UNKNOWN_ENTITY"));
                }
                return false;
            }
            
            return true;
        } catch (EntitySyntaxException ex) {
            errorsList.add(new DataFileError(line, DataFileErrorType.SYNTAX.name(), ex.getMessage()));
        } catch (DuplicatedEntityException ex) {
            errorsList.add(new DataFileError(line, DataFileErrorType.DUPLICATED_ENTITY.name(), ex.getMessage()));
        } catch (EntityNotFoundException ex) {
            errorsList.add(new DataFileError(line, DataFileErrorType.ENTITY_NOT_FOUND.name(), ex.getMessage()));
        } catch (DenegatedActionException ex) {
            errorsList.add(new DataFileError(line, DataFileErrorType.ERROR.name(), ex.getMessage()));
        }
        return false;
    }
    
    public boolean verifyParameters(int parametersRequired, int parameters, String columnName) throws EntitySyntaxException {
        if (parametersRequired != parameters) {
            throw new EntitySyntaxException(String.format("%s_PARAMETERS_DO_NOT_MATCH", columnName));
        }
        return true;
    }
}
