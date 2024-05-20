package com.froi.library.repositories;

import com.froi.library.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    @Query(value = "SELECT COUNT(*) FROM public.book_loan " +
            "WHERE student = :studentId " +
            "AND status IN ('IN_TIME', 'OUT_OF_TIME')",
            nativeQuery = true)
    int countBookLoansByStudentWithStatus(@Param("studentId") String studentId);
}
