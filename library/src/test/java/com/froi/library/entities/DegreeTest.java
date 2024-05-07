package com.froi.library.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DegreeTest {
    private static final Integer ID = 1;
    private static final String NAME = "Ingeniería en Sistemas";
    
    private static final Integer EXPECTED_ID = 1;
    private static final String EXPECTED_NAME = "Ingeniería en Sistemas";
    
    @Test
    void testDegree() {
        // Arrange
        Degree degree = new Degree();
        
        // Act
        degree.setId(ID);
        degree.setName(NAME);
        
        // Assert
        assertEquals(EXPECTED_ID, degree.getId());
        assertEquals(EXPECTED_NAME, degree.getName());
    }
}
