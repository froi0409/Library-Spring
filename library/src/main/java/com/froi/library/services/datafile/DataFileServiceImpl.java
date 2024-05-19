package com.froi.library.services.datafile;

import com.froi.library.dto.datafile.DataFileResponseDTO;
import com.froi.library.exceptions.UploadDataFileException;
import com.froi.library.services.book.BookService;
import com.froi.library.services.bookloan.BookLoanService;
import com.froi.library.services.degree.DegreeService;
import com.froi.library.services.student.StudentService;
import com.froi.library.services.tools.ToolsService;
import com.froi.library.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public DataFileResponseDTO handleDataFile(MultipartFile file) throws UploadDataFileException {
        return null;
    }
    
    @Override
    public List<String> verifySystemData() {
        return List.of();
    }
}
