package com.froi.library.controllers.user;

import com.froi.library.dto.user.CreateUserRequestDTO;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin
public class UserController {
    
    private UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequestDTO newUser) throws DuplicatedEntityException, EntitySyntaxException, EntityNotFoundException {
        userService.createUser(newUser);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
    
}
