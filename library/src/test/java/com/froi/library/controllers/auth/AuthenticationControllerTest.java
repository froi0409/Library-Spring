package com.froi.library.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.froi.library.controllers.AbstractMvcTest;
import com.froi.library.controllers.exceptionhandler.GlobalExceptionHandler;
import com.froi.library.dto.auth.AuthenticationRequestDTO;
import com.froi.library.dto.auth.JwtResponseDTO;
import com.froi.library.enums.studentstatus.Role;
import com.froi.library.security.SecurityConfiguration;
import com.froi.library.services.auth.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {AuthenticationController.class, GlobalExceptionHandler.class, AuthenticationService.class})
public class AuthenticationControllerTest extends AbstractMvcTest {

    private static final String USERNAME = "user1";
    private static final String PASSWORD = "admin123";
    private static final String TOKEN = "token.example.test";
    private static final Role ROLE = Role.STUDENT;
    
    @MockBean
    private AuthenticationService authenticationService;
    
    @Test
    @WithMockUser("NO_USER")
    public void testLogin() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        AuthenticationRequestDTO authDTO = new AuthenticationRequestDTO(USERNAME, PASSWORD);
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO(TOKEN, ROLE);
        when(authenticationService.authenticateAndGetToken(authDTO))
                .thenReturn(jwtResponseDTO);
        
        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk());
    }

}
