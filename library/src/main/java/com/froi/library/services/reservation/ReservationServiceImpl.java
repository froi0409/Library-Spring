package com.froi.library.services.reservation;

import com.froi.library.dto.bookloan.CreateBookLoanDTO;
import com.froi.library.dto.reservation.CreateReservationDTO;
import com.froi.library.entities.Book;
import com.froi.library.entities.BookLoan;
import com.froi.library.entities.Reservation;
import com.froi.library.entities.Student;
import com.froi.library.enums.bookstatus.BookReservationStatus;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.ReservationRepository;
import com.froi.library.services.book.BookService;
import com.froi.library.services.bookloan.BookLoanService;
import com.froi.library.services.student.StudentService;
import com.froi.library.services.tools.ToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService{

    private ReservationRepository reservationRepository;
    private BookLoanService bookLoanService;
    private StudentService studentService;
    private BookService bookService;
    private ToolsService toolsService;
    
    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, BookLoanService bookLoanService, StudentService studentService, BookService bookService, ToolsService toolsService) {
        this.reservationRepository = reservationRepository;
        this.bookLoanService = bookLoanService;
        this.studentService = studentService;
        this.bookService = bookService;
        this.toolsService = toolsService;
    }
    
    
    @Override
    public boolean createReservation(CreateReservationDTO newReservation) throws EntityNotFoundException, DenegatedActionException {
        checkEntities(newReservation);
        if (bookLoanService.checkAvailability(newReservation.getBookCode()) > 0) {
            throw new DenegatedActionException("BOOK_IS_AVAILABLE");
        }
        Reservation reservation = new Reservation();
        reservation.setBook(newReservation.getBookCode());
        reservation.setStudent(newReservation.getSudentId());
        reservation.setReservationDate(Date.valueOf(LocalDate.now()));
        reservation.setStatus(BookReservationStatus.NO_SERVED);
        
        reservationRepository.save(reservation);
        
        return false;
    }
    
    @Override
    public boolean checkReservation(String bookId, Date date) {
        Optional<Reservation> checkReservation = reservationRepository.findValidReservation(bookId, date);
        if (checkReservation.isEmpty()) {
            return false;
        } else {
            Reservation reservation = checkReservation.get();
            
            reservation.setReservationValidated(date);
            reservationRepository.save(reservation);
            return true;
        }
    }
    
    @Override
    public boolean doReservation(CreateReservationDTO newReservation) throws EntityNotFoundException, DenegatedActionException, EntitySyntaxException {
        checkEntities(newReservation);
        Reservation reservation = reservationRepository
                .findReservationByStudent_AndBookAndStatus(newReservation.getSudentId(), newReservation.getBookCode(), BookReservationStatus.NO_SERVED)
                .orElseThrow(() -> new EntityNotFoundException("RESERVATION_NOT_FOUND"));
        
        if (!isInTime(reservation.getReservationValidated())) {
            throw new DenegatedActionException("RESERVATION_EXPIRED");
        }
        
        List<String> bookCode = new ArrayList<>();
        bookCode.add(newReservation.getBookCode());
        CreateBookLoanDTO newLoan = new CreateBookLoanDTO(bookCode, newReservation.getSudentId(), LocalDate.now().toString());
        bookLoanService.createLoan(newLoan);
        return true;
    }
    
    private boolean isInTime(Date fecha) {
        Date actualDate = Date.valueOf(LocalDate.now());
        long difference = actualDate.getTime() - fecha.getTime();
        long days = difference / (24 * 60 * 60 * 1000);
        
        return days <= 1;
    }
    
    public boolean checkEntities(CreateReservationDTO newReservation) throws EntityNotFoundException {
        Student student = studentService.getStudentById(newReservation.getSudentId())
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("STUDENT_NOT_FOUND"));
        Book book = bookService.getBookByCode(newReservation.getBookCode())
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("BOOK_NOT_FOUND"));
        
        return true;
    }
    
    
    
}
