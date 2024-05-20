package com.froi.library.controllers.student;

import com.froi.library.entities.Student;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.services.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/student")
public class StudentController {
    
    private StudentService studentService;
    
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    
    @GetMapping(path = "/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) throws EntityNotFoundException {
        return ResponseEntity
                .ok(studentService.getOneStudentById(id));
    }
}
