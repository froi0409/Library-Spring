package com.froi.library.entities;

import com.froi.library.enums.studentstatus.Role;
import com.froi.library.enums.studentstatus.StudentStatus;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {
    
    private static final String USERNAME = "201830121";
    private static final String PASSWORD = "password123";
    private static final Role ROLE = Role.STUDENT;
    private static final String STUDENT_ID = "201830121";
    private static final String STUDENT_FIRST_NAME = "Fernando Rubén";
    private static final String STUDENT_LAST_NAME = "Ocaña Ixcot";
    private static final String DEGREE_ID = "1";
    private static final String STUDENT_BIRTH_DATE = "2000-09-04";
    private static final StudentStatus STUDENT_STATUS = StudentStatus.ACTIVE;
    
    private static final String EXPECTED_USERNAME = "201830121";
    private static final String EXPECTED_PASSWORD = "password123";
    private static final Role EXPECTED_ROLE = Role.STUDENT;
    private static final String EXPECTED_STUDENT = "201830121";
    private static final String EXPECTED_STUDENT_ID = "201830121";
    private static final String EXPECTED_STUDENT_FIRST_NAME = "Fernando Rubén";
    private static final String EXPECTED_STUDENT_LAST_NAME = "Ocaña Ixcot";
    private static final String EXPECTED_DEGREE_ID = "1";
    private static final String EXPECTED_STUDENT_BIRTH_DATE = "2000-09-04";
    private static final StudentStatus EXPECTED_STUDENT_STATUS = StudentStatus.ACTIVE;
    
    @Test
    void testUser() {
        // Arrange
        User user = new User();
        Student student = new Student();
        student.setId(STUDENT_ID);
        student.setFirstName(STUDENT_FIRST_NAME);
        student.setLastName(STUDENT_LAST_NAME);
        student.setDegree(Integer.valueOf(DEGREE_ID));
        student.setBirthDate(Date.valueOf(STUDENT_BIRTH_DATE));
        student.setStatus(STUDENT_STATUS);
        
        // Act
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRole(ROLE);
        user.setStudent(student);
        
        // Assert
        assertEquals(EXPECTED_USERNAME, user.getUsername());
        assertEquals(EXPECTED_PASSWORD, user.getPassword());
        assertEquals(EXPECTED_ROLE, user.getRole());
        assertNotNull(user.getStudent());
        assertEquals(EXPECTED_STUDENT_ID, user.getStudent().getId());
        assertEquals(EXPECTED_STUDENT_FIRST_NAME, user.getStudent().getFirstName());
        assertEquals(EXPECTED_STUDENT_LAST_NAME, user.getStudent().getLastName());
        assertEquals(Integer.valueOf(EXPECTED_DEGREE_ID), user.getStudent().getDegree());
        assertNotNull(user.getStudent().getBirthDate());
        assertEquals(EXPECTED_STUDENT_STATUS, user.getStudent().getStatus());
    }
    
    
}
