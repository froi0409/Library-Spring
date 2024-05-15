package com.froi.library.services.bookloan;

import com.froi.library.entities.BookLoan;
import com.froi.library.repositories.BookLoanRepository;
import com.froi.library.services.book.BookService;
import com.froi.library.services.student.StudentService;
import com.froi.library.services.tools.ToolsService;
import com.froi.library.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookLoanServiceImpl implements BookLoanService {

    private BookLoanRepository bookLoanRepository;
    private ToolsService toolsService;
    private BookService bookService;
    private StudentService studentService;
    
    @Autowired
    public BookLoanServiceImpl(BookLoanRepository bookLoanRepository, ToolsService toolsService, BookService bookService, StudentService studentService) {
        this.bookLoanRepository = bookLoanRepository;
        this.toolsService = toolsService;
        this.bookService = bookService;
        this.studentService = studentService;
    }
    
    public BookLoan doLoan() {
        return null;
    }
    
}
