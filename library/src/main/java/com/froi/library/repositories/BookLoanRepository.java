package com.froi.library.repositories;

import com.froi.library.entities.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Integer> {
    @Query(value = "SELECT COUNT(*) FROM public.book_loan WHERE student = :student AND status IN ('IN_TIME', 'OUT_OF_TIME')", nativeQuery = true)
    Long countByStudentAndStatus(@Param("student") String student);
    
    @Query(value = "SELECT b.stock - " +
            "(SELECT COUNT(*) FROM public.book_loan bl WHERE bl.book = :bookCode AND bl.status IN ('IN_TIME', 'OUT_OF_TIME')) - " +
            "(SELECT COUNT(*) FROM public.reservation r WHERE r.book = :bookCode AND r.reservation_validated >= :specificDate - INTERVAL '1 day') " +
            "FROM public.book b WHERE b.code = :bookCode", nativeQuery = true)
    Integer countAvailableCopies(@Param("bookCode") String bookCode, @Param("specificDate") Date specificDate);
    
}
