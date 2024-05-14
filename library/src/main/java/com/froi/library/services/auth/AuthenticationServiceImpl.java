package com.froi.library.services.auth;

import com.froi.library.dto.auth.AuthenticationRequestDTO;
import com.froi.library.dto.auth.JwtResponseDTO;
import com.froi.library.entities.User;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.repositories.UserRepository;
import com.froi.library.services.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UserRepository userRepository;
    
    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, DataSourcePoolMetadataProvider hikariPoolDataSourceMetadataProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    
    @Override
    public JwtResponseDTO authenticateAndGetToken(AuthenticationRequestDTO authDTO) throws EntityNotFoundException {
        User userToAuth = userRepository.findById(authDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("USERAME_NOT_FOUND"));
        
        UsernamePasswordAuthenticationToken authData
                = new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword());
        
        try {
            Authentication authentication = authenticationManager.authenticate(authData);
            if (authentication.isAuthenticated()) {
                jwtService.updateTokenExpiration(authDTO.getUsername());
                return new JwtResponseDTO(jwtService.generateToken(authDTO.getUsername(), userToAuth.getRole()), userToAuth.getRole());
            }
        } catch (AuthenticationException ex) {
            log.error("Error Authenticating " + ex);
        }
        throw new UsernameNotFoundException("USERNAME_NOT_FOUND");
    }
}
