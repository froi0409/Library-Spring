package com.froi.library.repositories;

import com.froi.library.entities.Student;
import com.froi.library.enums.studentstatus.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    @Query(value = "SELECT 3-COUNT(*) FROM public.book_loan " +
            "WHERE student = :studentId " +
            "AND status IN ('IN_TIME', 'OUT_OF_TIME')",
            nativeQuery = true)
    int countBookLoansByStudentWithStatus(@Param("studentId") String studentId);
    
    @Query(value = "SELECT COUNT(*) FROM book_loan " +
            "WHERE student = :studentId " +
            "AND status IN ('IN_TIME', 'OUT_OF_TIME') " +
            "AND EXTRACT(DAY FROM age(:checkDate, loan_date)) >= 30", nativeQuery = true)
    Integer countInvalidLoans(@Param("studentId") String studentId, @Param("checkDate") Date checkDate);
    
    @Query(value = "SELECT * FROM Student s " +
            "WHERE LOWER(s.id) LIKE CONCAT('%', LOWER(:searchTerm), '%') " +
            "   OR LOWER(s.first_name) LIKE CONCAT('%', LOWER(:searchTerm), '%') " +
            "   OR LOWER(s.last_name) LIKE CONCAT('%', LOWER(:searchTerm), '%') ", nativeQuery = true)
    List<Student> findAllStudentsIgnoreCase(@Param("searchTerm") String searchTerm);
    
    
    List<Student> findAllByStatus(StudentStatus status);
}
