package com.froi.library.repositories;

import com.froi.library.entities.BookLoan;
import com.froi.library.enums.bookstatus.BookLoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Integer> {
    @Query(value = "SELECT COUNT(*) FROM public.book_loan WHERE student = :student AND status IN ('IN_TIME', 'OUT_OF_TIME')", nativeQuery = true)
    Long countByStudentAndStatus(@Param("student") String student);
    
    @Query(value = "SELECT b.stock - " +
            "(SELECT COUNT(*) FROM public.book_loan bl WHERE bl.book = :bookCode AND bl.status IN ('IN_TIME', 'OUT_OF_TIME')) - " +
            "(SELECT COUNT(*) FROM public.reservation r WHERE r.book = :bookCode AND r.reservation_validated >= (CAST(:specificDate AS timestamp) - INTERVAL '1 day')) " +
            "FROM public.book b WHERE b.code = :bookCode", nativeQuery = true)
    Integer countAvailableCopies(@Param("bookCode") String bookCode, @Param("specificDate") Date specificDate);
    
    List<BookLoan> findAllByStudentAndStatus(String student, BookLoanStatus status);
    
    @Query(value = "SELECT bl.*, b.title " +
            "FROM book_loan bl " +
            "JOIN book b ON bl.book = b.code " +
            "WHERE bl.returned_date IS NULL " +
            "AND (bl.loan_date + 3) = :date", nativeQuery = true)
    List<Map<String, Object>> findBookLoansDueTodayWithBookTitle(@Param("date") Date date);
    
    @Query(value = "SELECT bl.*, b.title FROM book_loan bl " +
            "JOIN book b ON bl.book = b.code " +
            "WHERE returned_date IS NULL AND (loan_date + 3) < :date", nativeQuery = true)
    List<Map<String, Object>> findOverdueBookLoans(@Param("date") Date date);
    
    
    @Query(value = "SELECT SUM(loan_total) AS totalLoan, SUM(delay_total) AS totalDelay, (SUM(loan_total) + SUM(delay_total)) AS totalRevenue " +
            "FROM book_loan WHERE loan_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    Map<String, Double> findTotalRevenueBetweenDates(Date startDate, Date endDate);
    
    @Query(value = "SELECT SUM(loan_total) AS totalLoan, SUM(delay_total) AS totalDelay, (SUM(loan_total) + SUM(delay_total)) AS totalRevenue " +
            "FROM book_loan", nativeQuery = true)
    Map<String, Double> findTotalRevenue();
    
    @Query(value = "SELECT * FROM book_loan WHERE loan_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<BookLoan> findBookLoansBetweenDates(Date startDate, Date endDate);
    
    @Query(value = "SELECT d.id AS degree_id, d.name AS degree_name, COUNT(bl.id) AS total_loans " +
            "FROM book_loan bl " +
            "JOIN student s ON bl.student = s.id " +
            "JOIN degree d ON s.degree = d.id " +
            "WHERE bl.loan_date BETWEEN :startDate AND :endDate " +
            "GROUP BY d.name " +
            "ORDER BY total_loans DESC " +
            "LIMIT 1", nativeQuery = true)
    Map<String, Object> findDegreeWithMostLoansBetweenDates(Date startDate, Date endDate);
    
    @Query(value = "SELECT d.name AS degree_name, COUNT(bl.id) AS total_loans " +
            "FROM book_loan bl " +
            "JOIN student s ON bl.student = s.id " +
            "JOIN degree d ON s.degree = d.id " +
            "GROUP BY d.name " +
            "ORDER BY total_loans DESC " +
            "LIMIT 1", nativeQuery = true)
    Map<String, Object> findDegreeWithMostLoans();
    
    @Query(value = "SELECT * FROM book_loan bl " +
            "JOIN student s ON bl.student = s.id " +
            "WHERE s.degree = :degreeId AND bl.loan_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<BookLoan> findLoansByDegreeBetweenDates(Integer degreeId, Date startDate, Date endDate);
    
    @Query(value = "SELECT * FROM book_loan bl " +
            "JOIN student s ON bl.student = s.id " +
            "WHERE s.degree = :degreeId", nativeQuery = true)
    List<BookLoan> findLoansByDegree(Integer degreeId);
    
}
