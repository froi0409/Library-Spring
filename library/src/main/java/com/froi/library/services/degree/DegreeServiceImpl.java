package com.froi.library.services.degree;

import com.froi.library.dto.degree.CreateDegreeRequestDTO;
import com.froi.library.entities.Degree;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.DegreeRepository;
import com.froi.library.services.tools.ToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DegreeServiceImpl implements DegreeService {

    private ToolsService toolsService;
    private DegreeRepository degreeRepository;
    
    @Autowired
    public DegreeServiceImpl(ToolsService toolsService, DegreeRepository degreeRepository) {
        this.toolsService = toolsService;
        this.degreeRepository = degreeRepository;
    }
    
    @Override
    public Degree createDegree(CreateDegreeRequestDTO newDegree) throws EntitySyntaxException, DuplicatedEntityException {
        if (!toolsService.isPositiveInteger(newDegree.getId())) {
            throw new EntitySyntaxException("INVALID_ID");
        }
        
        Optional<Degree> checkDegree = degreeRepository.findById(Integer.valueOf(newDegree.getId()));
        if (checkDegree.isPresent()) {
            throw new DuplicatedEntityException("DUPLICATED_DEGREE");
        }
        
        Degree degreeEntity = new Degree();
        degreeEntity.setId(Integer.valueOf(newDegree.getId()));
        degreeEntity.setName(newDegree.getName());
        
        degreeEntity = degreeRepository.save(degreeEntity);
        
        return degreeEntity;
    }
    
    @Override
    public Optional<Degree> getDegreeById(Integer id) {
        return degreeRepository.findById(id);
    }
    
}
