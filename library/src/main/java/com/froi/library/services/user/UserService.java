package com.froi.library.services.user;

import com.froi.library.dto.user.CreateUserRequestDTO;
import com.froi.library.entities.Student;
import com.froi.library.entities.User;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;

import java.util.Optional;

public interface UserService {

    User createUser(CreateUserRequestDTO newUser) throws DuplicatedEntityException, EntitySyntaxException, EntityNotFoundException;
    
}
