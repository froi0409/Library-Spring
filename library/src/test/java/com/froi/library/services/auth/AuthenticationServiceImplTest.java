package com.froi.library.services.auth;

import com.froi.library.dto.auth.AuthenticationRequestDTO;
import com.froi.library.dto.auth.JwtResponseDTO;
import com.froi.library.entities.User;
import com.froi.library.enums.studentstatus.Role;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.repositories.UserRepository;
import com.froi.library.services.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {
    
    private static final String USERNAME = "201830121";
    private static final String PASSWORD = "example_encoded_password";
    private static final String TOKEN = "generated_token";
    private static final String EXPECTED_TOKEN = "generated_token";
    
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtService jwtService;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    AuthenticationServiceImpl serviceToTest;
    
    @Test
    void testAuthenticateAndGetTokenUserNotFound() throws EntityNotFoundException {
        // Arrange
        AuthenticationRequestDTO authDTO = new AuthenticationRequestDTO(USERNAME, PASSWORD);
        when(userRepository.findById(USERNAME)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> serviceToTest.authenticateAndGetToken(authDTO));
    }
    
    @Test
    void testAuthenticateAndGetTokenAuthenticationSuccess() throws EntityNotFoundException {
        // Arrange
        AuthenticationRequestDTO authDTO = new AuthenticationRequestDTO(USERNAME, PASSWORD);
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRole(Role.STUDENT);
        when(userRepository.findById(USERNAME)).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD, Collections.emptyList()));
        when(jwtService.generateToken(USERNAME, user.getRole())).thenReturn(TOKEN);
        
        // Act
        JwtResponseDTO responseDTO = serviceToTest.authenticateAndGetToken(authDTO);
        
        // Assert
        assertNotNull(responseDTO);
        assertEquals(EXPECTED_TOKEN, responseDTO.getToken());
        assertEquals(Role.STUDENT, responseDTO.getRole());
    }
    
    
    @Test
    void testAuthenticateAndGetTokenBadCredentialsException() throws EntityNotFoundException {
        // Arrange
        AuthenticationRequestDTO authDTO = new AuthenticationRequestDTO(USERNAME, PASSWORD);
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        when(userRepository.findById(USERNAME)).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));
        
        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> serviceToTest.authenticateAndGetToken(authDTO));
    }
    
    @Test
    void testAuthenticateAndGetTokenAuthenticationFailure() throws EntityNotFoundException {
        // Arrange
        AuthenticationRequestDTO authDTO = new AuthenticationRequestDTO(USERNAME, PASSWORD);
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        when(userRepository.findById(USERNAME)).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD));
        
        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> serviceToTest.authenticateAndGetToken(authDTO));
    }
}
