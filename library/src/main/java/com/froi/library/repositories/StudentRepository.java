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
            "WHERE student_id = :studentId " +
            "AND status IN ('IN_TIME', 'OUT_OF_TIME') " +
            "AND DATEDIFF(:checkDate, loan_date) <= 30", nativeQuery = true)
    Integer countInvalidLoans(@Param("studentId") String studentId, @Param("checkDate") Date checkDate);

    List<Student> findAllByStatus(StudentStatus status);
}
