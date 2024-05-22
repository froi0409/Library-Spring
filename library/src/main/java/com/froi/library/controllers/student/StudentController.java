package com.froi.library.controllers.student;

import com.froi.library.dto.EnableStudentDTO;
import com.froi.library.entities.Student;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.services.student.StudentService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/student")
@PreAuthorize("hasRole('LIBRARIAN')")
@CrossOrigin
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
    
    @GetMapping(path = "/loanCount/{studentId}")
    public ResponseEntity<Integer> getStudentLoansCount(@PathVariable String studentId) throws EntityNotFoundException {
        return ResponseEntity
                .ok(studentService.getStudentLoansCount(studentId));
    }
    
    @PostMapping("/enableStudent")
    public ResponseEntity<Boolean> enableStudent(@RequestBody EnableStudentDTO enableStudent) throws DenegatedActionException, EntityNotFoundException, EntitySyntaxException {
        return ResponseEntity
                .ok(studentService.enableStudent(enableStudent));
    }
    
    @GetMapping(path = "/allInactive")
    public ResponseEntity<List<Student>> getAllInactive() {
        return ResponseEntity
                .ok(studentService.findAllInactiveStudents());
    }
}
