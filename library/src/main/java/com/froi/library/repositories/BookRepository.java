package com.froi.library.repositories;

import com.froi.library.dto.book.ReadBookResponseDTO;
import com.froi.library.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    
    @Query(value = "SELECT b.code, b.title, b.author, b.publisher, COALESCE(bl.loan_count, 0) AS loan_count " +
            "FROM Book b " +
            "LEFT JOIN ( " +
            "    SELECT book, COUNT(*) AS loan_count " +
            "    FROM book_loan " +
            "    WHERE status IN ('IN_TIME', 'OUT_OF_TIME') " +
            "    GROUP BY book " +
            ") bl ON b.code = bl.book " +
            "WHERE b.code LIKE CONCAT('%', :searchTerm, '%') " +
            "   OR b.title LIKE CONCAT('%', :searchTerm, '%') " +
            "   OR b.author LIKE CONCAT('%', :searchTerm, '%') " +
            "   OR b.publisher LIKE CONCAT('%', :searchTerm, '%')", nativeQuery = true)
    List<Map<String, Object>> findAllBooksAndLoanCounts(@Param("searchTerm") String searchTerm);
    
}
