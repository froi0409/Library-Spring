package com.froi.library.entities;

import com.froi.library.enums.bookstatus.BookReservationStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.sql.Date;

public class ReservationTest {
    
    private static final Integer ID = 1;
    private static final String STUDENT = "201830121";
    private static final String BOOK = "978-9706860699";
    private static final Date RESERVATION_DATE = java.sql.Date.valueOf(LocalDate.of(2024, Month.MAY, 5));
    private static final Date RESERVATION_VALIDATED_DATE = java.sql.Date.valueOf("2024-05-18");
    private static final BookReservationStatus STATUS = BookReservationStatus.SERVED;
    
    private static final Integer EXPECTED_ID = 1;
    private static final String EXPECTED_STUDENT = "201830121";
    private static final String EXPECTED_BOOK = "978-9706860699";
    private static final Date EXPECTED_RESEVATION_VALIDATED_DATE = java.sql.Date.valueOf("2024-05-18");
    private static final Date EXPECTED_RESERVATION_DATE = java.sql.Date.valueOf(LocalDate.of(2024, Month.MAY, 5));
    private static final BookReservationStatus EXPECTED_STATUS = BookReservationStatus.SERVED;
    
    @Test
    void testReservation() {
        // Arrange
        Reservation reservation = new Reservation();
        
        // Act
        reservation.setId(ID);
        reservation.setStudent(STUDENT);
        reservation.setBook(BOOK);
        reservation.setReservationDate(RESERVATION_DATE);
        reservation.setStatus(STATUS);
        reservation.setReservationValidated(RESERVATION_VALIDATED_DATE);
        
        // Assert
        assertEquals(EXPECTED_ID, reservation.getId());
        assertEquals(EXPECTED_STUDENT, reservation.getStudent());
        assertEquals(EXPECTED_BOOK, reservation.getBook());
        assertEquals(EXPECTED_RESEVATION_VALIDATED_DATE, reservation.getReservationValidated());
        assertEquals(EXPECTED_RESERVATION_DATE, reservation.getReservationDate());
        assertEquals(EXPECTED_STATUS, reservation.getStatus());
    }
    
}
