package com.froi.library.services.student;

import com.froi.library.dto.EnableStudentDTO;
import com.froi.library.dto.user.StudentDTO;
import com.froi.library.entities.Student;
import com.froi.library.enums.studentstatus.StudentStatus;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.StudentRepository;
import com.froi.library.services.tools.ToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    
    private StudentRepository studentRepository;
    private ToolsService toolsService;
    
    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, ToolsService toolsService) {
        this.studentRepository = studentRepository;
        this.toolsService = toolsService;
    }

    @Override
    public Student createStudent(StudentDTO student) throws DuplicatedEntityException, EntitySyntaxException {
        Optional<Student> checkStudent = studentRepository.findById(student.getId());
        if (checkStudent.isPresent()) {
            throw new DuplicatedEntityException("DUPLICATED_STUDENT");
        }
        
        if (!toolsService.isValidEmail(student.getEmail())) {
            throw new EntitySyntaxException("EMAIL_SYNTAX");
        }
        if (!toolsService.isValidDateFormat(student.getBirthDate())) {
            throw new EntitySyntaxException("STUDENT_BIRTH_DATE_SYNTAX");
        }
        if (student.getStatus() != null && !student.getStatus().equals(StudentStatus.ACTIVE.name())) {
            throw new EntitySyntaxException("STUDENT_STATUS_SYNTAX");
        }
        if (!toolsService.isAlphabetic(student.getFirstName())) {
            throw new EntitySyntaxException("STUDENT_FIRST_NAME_SYNTAX");
        }
        if (!toolsService.isAlphabetic(student.getLastName())) {
            throw new EntitySyntaxException("STUDENT_LAST_NAME_SYNTAX");
        }
        
        Student studentEntity = new Student();
        studentEntity.setId(student.getId());
        studentEntity.setFirstName(student.getFirstName());
        studentEntity.setLastName(student.getLastName());
        studentEntity.setBirthDate(Date.valueOf(student.getBirthDate()));
        studentEntity.setDegree(Integer.valueOf(student.getDegree()));
        studentEntity.setEmail(student.getEmail());
        studentEntity.setStatus(StudentStatus.ACTIVE);
        
        studentRepository.save(studentEntity);
        
        return studentEntity;
    }
    
    @Override
    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(id);
    }
    
    @Override
    public Student getOneStudentById(String id) throws EntityNotFoundException {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("STUDENT_NOT_FOUND"));
    }
    
    @Override
    public Integer getStudentLoansCount(String studentId) throws EntityNotFoundException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("STUDENT_NOT_FOUND"));
        return studentRepository.countBookLoansByStudentWithStatus(student.getId());
    }
    
    @Override
    public boolean sanctionStudent(String studentId) throws EntityNotFoundException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("STUDENT_NOT_FOUND"));
        student.setStatus(StudentStatus.INACTIVE);
        studentRepository.save(student);
        return true;
    }
    
    @Override
    public boolean enableStudent(EnableStudentDTO enableStudent) throws EntityNotFoundException, EntitySyntaxException, DenegatedActionException {
        if (!toolsService.isValidDateFormat(enableStudent.getEnableDate())) {
            throw new EntitySyntaxException("INVALID_DATE");
        }
        Student student = studentRepository.findById(enableStudent.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("STUDENT_NOT_FOUND"));
        
        Integer invalidLoans = studentRepository.countInvalidLoans(student.getId(), Date.valueOf(enableStudent.getEnableDate()));
        if (invalidLoans == 0) {
            student.setStatus(StudentStatus.ACTIVE);
            studentRepository.save(student);
        } else {
            throw new DenegatedActionException("STUDENT_HAS_INVALID_LOANS");
        }
        return true;
    }
    
    @Override
    public List<Student> findAllInactiveStudents() {
        return studentRepository.findAllByStatus(StudentStatus.INACTIVE);
    }
}
