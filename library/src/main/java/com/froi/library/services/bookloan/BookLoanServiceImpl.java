package com.froi.library.services.bookloan;

import com.froi.library.dto.bookloan.CreateBookLoanDTO;
import com.froi.library.dto.bookloan.ReturnLoanDTO;
import com.froi.library.entities.Book;
import com.froi.library.entities.BookLoan;
import com.froi.library.entities.Student;
import com.froi.library.enums.bookstatus.BookLoanStatus;
import com.froi.library.enums.studentstatus.StudentStatus;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.BookLoanRepository;
import com.froi.library.repositories.ReservationRepository;
import com.froi.library.services.book.BookService;
import com.froi.library.services.student.StudentService;
import com.froi.library.services.tools.ToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class BookLoanServiceImpl implements BookLoanService {

    private BookLoanRepository bookLoanRepository;
    private ToolsService toolsService;
    private BookService bookService;
    private StudentService studentService;
    private ReservationRepository reservationRepository;
    
    @Autowired
    public BookLoanServiceImpl(BookLoanRepository bookLoanRepository, ToolsService toolsService, BookService bookService, StudentService studentService, ReservationRepository reservationRepository) {
        this.bookLoanRepository = bookLoanRepository;
        this.toolsService = toolsService;
        this.bookService = bookService;
        this.studentService = studentService;
        this.reservationRepository = reservationRepository;
    }
    
    @Override
    public boolean createLoan(CreateBookLoanDTO newLoan) throws EntityNotFoundException, DenegatedActionException, EntitySyntaxException {
        if (newLoan.getLoanDate() != null && !toolsService.isValidDateFormat(newLoan.getLoanDate())) {
            throw new EntitySyntaxException("INVALID_DATE");
        } else if (newLoan.getLoanDate() == null) {
            newLoan = new CreateBookLoanDTO(newLoan.getBookCodes(), newLoan.getStudentId(), LocalDate.now().toString());
        }
        Student student = studentService.getStudentById(newLoan.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("STUDENT_NOT_FOUND"));
        if (student.getStatus() != StudentStatus.ACTIVE) {
            throw new DenegatedActionException("STUDENT_IS_INACTIVE");
        }
        
        List<Book> booksList = new ArrayList<>();
        for (String bookCode: newLoan.getBookCodes()) {
           Book book = bookService.getBookByCode(bookCode)
                   .orElseThrow(() -> new EntityNotFoundException(bookCode + "BOOK_NOT_FOUND"));
           booksList.add(book);
        }
        
        Long studentLoans = bookLoanRepository.countByStudentAndStatus(student.getId());
        if (studentLoans >= 3) {
            throw new DenegatedActionException("STUDENT_HAS_3_OR_MORE_LOANS");
        }
        String unavaliableCopies = "";
        for (Book book: booksList) {
            Integer availableCopies = bookLoanRepository.countAvailableCopies(book.getCode(), Date.valueOf(newLoan.getLoanDate()));
            if (availableCopies <= 0) {
                unavaliableCopies += book.getCode() + " ";
            }
        }
        if (!unavaliableCopies.isEmpty()) {
            throw new EntityNotFoundException(unavaliableCopies + "NOT_FOUND");
        }
        
        for (Book book: booksList) {
            BookLoan bookLoanEntity = new BookLoan();
            bookLoanEntity.setLoanDate(Date.valueOf(newLoan.getLoanDate()));
            bookLoanEntity.setStudent(student.getId());
            bookLoanEntity.setBook(book.getCode());
            bookLoanEntity.setStatus(BookLoanStatus.IN_TIME);
            
            bookLoanRepository.save(bookLoanEntity);
        }
        
        return true;
    }
    
    public boolean isMoreThanThreeDays(Date dateString) {
        // Get today's date
        java.util.Date today = new java.util.Date();
        Date sqlToday = new java.sql.Date(today.getTime());
        
        // Calculate the difference in milliseconds
        long diffInMillies = sqlToday.getTime() - dateString.getTime();
        // Convert the difference from milliseconds to days
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        
        // Return true if the difference is greater than 3 days
        return diffInDays > 3;
    }
    
    @Override
    public Integer checkAvailability(String bookId) {
        System.out.println("disponibilidad");
        Date today = Date.valueOf(LocalDate.now());
        return bookLoanRepository.countAvailableCopies(bookId, today);
    }
    
    @Override
    public boolean returnLoan(ReturnLoanDTO returnLoan) throws EntityNotFoundException, EntitySyntaxException {
        if (!toolsService.isPositiveInteger(returnLoan.getLoanId())) {
            throw new EntitySyntaxException("INVALID_LOAN_ID");
        }
        if (!toolsService.isValidDateFormat(returnLoan.getReturnDate())) {
            throw new EntitySyntaxException("INVALID_DATE");
        }
        
        BookLoan bookLoan = bookLoanRepository.findById(Integer.valueOf(returnLoan.getLoanId()))
                .orElseThrow(() -> new EntityNotFoundException("LOAN_NOT_FOUND"));
        bookLoan.setReturnedDate(Date.valueOf(returnLoan.getReturnDate()));
        if (bookLoan.getDelayTotal() > 0) {
            bookLoan.setStatus(BookLoanStatus.RETURNED_OUT_OF_TIME);
        } else {
            bookLoan.setStatus(BookLoanStatus.RETURNED_IN_TIME);
        }
        bookLoanRepository.save(bookLoan);
        
        // Check if book is reserved
        reservationRepository.findValidReservation(bookLoan.getBook(), Date.valueOf(returnLoan.getReturnDate()))
                .ifPresent(reservation -> reservation.setReservationValidated(Date.valueOf(returnLoan.getReturnDate())));
        return true;
    }
    
    @Override
    public Page<BookLoan> findAll(Pageable pageable) throws EntityNotFoundException {
        List<BookLoan> allLoans = bookLoanRepository.findAll();
        for (BookLoan bookLoan : allLoans) {
            checkDelay(bookLoan, Date.valueOf(LocalDate.now()));
            bookLoanRepository.save(bookLoan);
        }
        
        return bookLoanRepository.findAll(pageable);
    }
    
    @Override
    public List<BookLoan> findNoReturnedByStudent(String studentId, String dateProvided) throws EntityNotFoundException {
        if (!toolsService.isValidDateFormat(dateProvided)) {
            throw new EntityNotFoundException("INVALID_DATE");
        }
        Date date = Date.valueOf(dateProvided);
        List<BookLoan> booksList = bookLoanRepository.findAllByStudentAndStatus(studentId, BookLoanStatus.IN_TIME);
        checkDelayBooks(booksList, date);
        return booksList;
    }
    
    @Override
    public BookLoan findById(String loanId, String returnDate) throws EntitySyntaxException, EntityNotFoundException {
        if (!toolsService.isValidDateFormat(returnDate)) {
            throw new EntitySyntaxException("INVALID_DATE");
        }
        if (!toolsService.isPositiveInteger(loanId)) {
            throw new EntitySyntaxException("INVALID_LOAN_ID");
        }
        BookLoan bookLoan =  bookLoanRepository.findById(Integer.valueOf(loanId))
                .orElseThrow(() -> new EntityNotFoundException("LOAN_NOT_FOUND"));
        checkDelay(bookLoan, Date.valueOf(returnDate));
        
        return bookLoan;
    }
    
    public boolean checkDelayBooks(List<BookLoan> booksList, Date date) throws EntityNotFoundException {
        for (BookLoan bookLoan: booksList) {
            checkDelay(bookLoan, date);
        }
        return true;
    }
    
    public boolean checkDelay(BookLoan bookLoan, Date returnDate) throws EntityNotFoundException {
        Date loanDate = bookLoan.getLoanDate();
        
        long daysBetween = ChronoUnit.DAYS.between(loanDate.toLocalDate(), returnDate.toLocalDate());
        
        double loanTotal = 0.0;
        double delayTotal = 0.0;
        
        if (daysBetween <= 0) {
            bookLoan.setLoanTotal(loanTotal);
            bookLoan.setDelayTotal(delayTotal);
            return true;
        }
        
        loanTotal = daysBetween * 5.0;
        
        if (daysBetween > 30) {
            Book book = bookService.getOneBookByCode(bookLoan.getBook());
            delayTotal = ((daysBetween - 3) * 15.0) + ((daysBetween - 30) * 15.0) + book.getCost();
            studentService.sanctionStudent(bookLoan.getStudent());
        } else if (daysBetween > 3) {
            delayTotal = (daysBetween - 3) * 15.0;
        }
        
        bookLoan.setLoanTotal(loanTotal);
        bookLoan.setDelayTotal(delayTotal);
        bookLoanRepository.save(bookLoan);
        return true;
    }
    
}
