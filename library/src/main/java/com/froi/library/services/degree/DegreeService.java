package com.froi.library.services.degree;

import com.froi.library.dto.degree.CreateDegreeRequestDTO;
import com.froi.library.entities.Degree;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntitySyntaxException;

import java.util.Optional;

public interface DegreeService {
    Degree createDegree(CreateDegreeRequestDTO newDegree) throws EntitySyntaxException, DuplicatedEntityException;
    
    Optional<Degree> getDegreeById(Integer id);
}
