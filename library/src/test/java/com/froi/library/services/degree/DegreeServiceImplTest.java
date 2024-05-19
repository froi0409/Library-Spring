package com.froi.library.services.degree;

import com.froi.library.dto.degree.CreateDegreeRequestDTO;
import com.froi.library.entities.Degree;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.DegreeRepository;
import com.froi.library.services.tools.ToolsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DegreeServiceImplTest {
    
    private static final String ID = "1";
    private static final String NAME = "Computer Science";
    
    @Mock
    private ToolsService toolsService;
    
    @Mock
    private DegreeRepository degreeRepository;
    
    @InjectMocks
    private DegreeServiceImpl degreeService;
    
    @Test
    void testCreateDegreeInvalidId() {
        // Arrange
        CreateDegreeRequestDTO newDegree = new CreateDegreeRequestDTO(ID, NAME);
        when(toolsService.isPositiveInteger(ID)).thenReturn(false);
        
        // Act & Assert
        assertThrows(EntitySyntaxException.class, () -> degreeService.createDegree(newDegree));
    }
    
    @Test
    void testCreateDegreeDuplicatedEntity() {
        // Arrange
        CreateDegreeRequestDTO newDegree = new CreateDegreeRequestDTO(ID, NAME);
        when(toolsService.isPositiveInteger(ID)).thenReturn(true);
        when(degreeRepository.findById(Integer.valueOf(ID))).thenReturn(Optional.of(new Degree()));
        
        // Act & Assert
        assertThrows(DuplicatedEntityException.class, () -> degreeService.createDegree(newDegree));
    }
    
    @Test
    void testCreateDegreeSuccess() throws EntitySyntaxException, DuplicatedEntityException {
        // Arrange
        CreateDegreeRequestDTO newDegree = new CreateDegreeRequestDTO(ID, NAME);
        when(toolsService.isPositiveInteger(ID)).thenReturn(true);
        when(degreeRepository.findById(Integer.valueOf(ID))).thenReturn(Optional.empty());
        when(degreeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Degree result = degreeService.createDegree(newDegree);
        
        // Assert
        assertNotNull(result);
        assertEquals(Integer.valueOf(ID), result.getId());
        assertEquals(NAME, result.getName());
    }
    
    @Test
    void testGetDegreeById() {
        // Arrange
        Degree degree = new Degree();
        degree.setId(Integer.valueOf(ID));
        degree.setName(NAME);
        when(degreeRepository.findById(Integer.valueOf(ID))).thenReturn(Optional.of(degree));
        
        // Act
        Optional<Degree> result = degreeService.getDegreeById(Integer.valueOf(ID));
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(Integer.valueOf(ID), result.get().getId());
        assertEquals(NAME, result.get().getName());
    }
    
    @Test
    void testGetDegreeByIdNotFound() {
        // Arrange
        when(degreeRepository.findById(Integer.valueOf(ID))).thenReturn(Optional.empty());
        
        // Act
        Optional<Degree> result = degreeService.getDegreeById(Integer.valueOf(ID));
        
        // Assert
        assertTrue(result.isEmpty());
    }
}
