package com.froi.library.services.student;

import com.froi.library.dto.EnableStudentDTO;
import com.froi.library.dto.user.StudentDTO;
import com.froi.library.entities.Student;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;

import java.util.Optional;

public interface StudentService {
    Student createStudent(StudentDTO student) throws DuplicatedEntityException, EntitySyntaxException;
    
    Optional<Student> getStudentById(String id) throws EntityNotFoundException;
    
    Student getOneStudentById(String id) throws EntityNotFoundException;
    
    Integer getStudentLoansCount(String studentId) throws EntityNotFoundException;
    
    boolean sanctionStudent(String studentId) throws EntityNotFoundException;
    
    boolean enableStudent(EnableStudentDTO enableStudent);
}
