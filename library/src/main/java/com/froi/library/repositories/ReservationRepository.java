package com.froi.library.repositories;

import com.froi.library.entities.Reservation;
import com.froi.library.enums.bookstatus.BookReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Optional<Reservation> findReservationByStudent_AndBookAndStatus(String student, String book, BookReservationStatus status);
    
    @Query (value = "SELECT * FROM reservation " +
            "WHERE book=:book AND status='NO_SERVED' AND reservation_validated<=:date " +
            "ORDER BY reservation_date ASC LIMIT 1", nativeQuery = true)
    Optional<Reservation> findValidReservation(String book, Date date);
}
