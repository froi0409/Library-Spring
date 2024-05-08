package com.froi.library.controllers.user;

import com.froi.library.dto.user.CreateUserRequestDTO;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    
    private UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequestDTO newUser) throws DuplicatedEntityException, EntitySyntaxException {
        userService.createUser(newUser);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
    
}
