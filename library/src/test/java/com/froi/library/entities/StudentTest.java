package com.froi.library.entities;

import com.froi.library.enums.studentstatus.StudentStatus;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentTest {
    private static final String ID = "201830121";
    private static final String FIRST_NAME = "Fernando";
    private static final String LAST_NAME = "Ocaña";
    private static final Integer DEGREE = 1;
    private static final String BIRTH_DATE = "2000/04/04";
    private static final StudentStatus STATUS = StudentStatus.ACTIVE;
    
    private static final String EXPECTED_ID = "201830121";
    private static final String EXPECTED_FIRST_NAME = "Fernando";
    private static final String EXPECTED_LAST_NAME = "Ocaña";
    private static final Integer EXPECTED_DEGREE = 1;
    private static final String EXPECTED_BIRTH_DATE = "2000/04/04";
    private static final StudentStatus EXPECTED_STATUS = StudentStatus.ACTIVE;
    
    @Test
    void testStudent() {
        // Arrange
        Student student = new Student();
        
        // Act
        student.setId(ID);
        student.setFirstName(FIRST_NAME);
        student.setLastName(LAST_NAME);
        student.setDegree(DEGREE);
        student.setBirtDate(BIRTH_DATE);
        student.setStatus(STATUS);
        
        // Assert
        assertEquals(EXPECTED_ID, student.getId());
        assertEquals(EXPECTED_FIRST_NAME, student.getFirstName());
        assertEquals(EXPECTED_LAST_NAME, student.getLastName());
        assertEquals(EXPECTED_DEGREE, student.getDegree());
        assertEquals(EXPECTED_BIRTH_DATE, student.getBirtDate());
        assertEquals(EXPECTED_STATUS, student.getStatus());
    }
}
