package com.froi.library.controllers.auth;

import com.froi.library.dto.auth.AuthenticationRequestDTO;
import com.froi.library.dto.auth.JwtResponseDTO;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.services.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@CrossOrigin
public class AuthenticationController {

    private AuthenticationService authenticationService;
    
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> authenticateAndGetToken(@RequestBody AuthenticationRequestDTO authDTO) throws EntityNotFoundException {
        return ResponseEntity.ok(authenticationService.authenticateAndGetToken(authDTO));
    }
}
