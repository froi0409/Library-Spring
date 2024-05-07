package com.froi.library.entities;

import com.froi.library.enums.studentstatus.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    
    private static final String USERNAME = "201830121";
    private static final String PASSWORD = "password123";
    private static final Role ROLE = Role.STUDENT;
    private static final String STUDENT = "201830121";
    
    private static final String EXPECTED_USERNAME = "201830121";
    private static final String EXPECTED_PASSWORD = "password123";
    private static final Role EXPECTED_ROLE = Role.STUDENT;
    private static final String EXPECTED_STUDENT = "201830121";
    
    @Test
    void testUser() {
        // Arrange
        User user = new User();
        
        // Act
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRole(ROLE);
        user.setStudent(STUDENT);
        
        // Assert
        assertEquals(EXPECTED_USERNAME, user.getUsername());
        assertEquals(EXPECTED_PASSWORD, user.getPassword());
        assertEquals(EXPECTED_ROLE, user.getRole());
        assertEquals(EXPECTED_STUDENT, user.getStudent());
    }
    
}
