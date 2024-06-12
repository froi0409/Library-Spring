package com.froi.library.security.jwtfilter;

import com.froi.library.services.jwt.JwtService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {
    
    private static final String BEARER = "Bearer ";
    private static final String VALID_TOKEN = "valid_token";
    private static final String INVALID_TOKEN = "invalid_token";
    private static final String USERNAME = "user";
    private static final String NULL_HEADER = null;
    private static final String NULL_USERNAME = null;
    
    @Mock
    private JwtService jwtService;
    
    @Mock
    private UserDetailsService userDetailsService;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;
    
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testDoFilterInternalValidTokenAuthenticated() throws Exception {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(BEARER + VALID_TOKEN);
        when(jwtService.getUsername(VALID_TOKEN)).thenReturn(USERNAME);
        UserDetails userDetails = new User(USERNAME, "", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
        when(jwtService.isTokenExpired(USERNAME)).thenReturn(false);
        
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(jwtService).updateTokenExpiration(USERNAME);
        verify(filterChain).doFilter(request, response);
    }
    
    @Test
    public void testDoFilterInternalInvalidTokenNotAuthenticated() throws Exception {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + INVALID_TOKEN);
        when(jwtService.getUsername(INVALID_TOKEN)).thenReturn(null);
        
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(jwtService, never()).updateTokenExpiration(anyString());
        verify(filterChain).doFilter(request, response);
    }
    
    @Test
    public void testDoFilterInternalExpiredTokenNotAuthenticated() throws Exception {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(BEARER + VALID_TOKEN);
        when(jwtService.getUsername(VALID_TOKEN)).thenReturn(USERNAME);
        UserDetails userDetails = new User(USERNAME, "", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
        when(jwtService.isTokenExpired(USERNAME)).thenReturn(true);
        
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(jwtService, never()).updateTokenExpiration(USERNAME);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternalNoAuthorizationHeaders() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(NULL_HEADER);
    
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
    
        // Assert
        verify(jwtService, never()).updateTokenExpiration(USERNAME);
        verify(filterChain).doFilter(request, response);
    }
    
    @Test
    public void testDoFilterInternalValidAuthorizationButNoUsername() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(BEARER + VALID_TOKEN);
        when(jwtService.getUsername(BEARER + VALID_TOKEN)).thenReturn(NULL_USERNAME);
        
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(userDetailsService, never()).loadUserByUsername(USERNAME);
        
    }
    
    @Test
    public void testDoFilterInternalValidAuthorizationButNoBearer() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(VALID_TOKEN);
        when(jwtService.getUsername(VALID_TOKEN)).thenReturn(NULL_USERNAME);
        
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        verify(userDetailsService, never()).loadUserByUsername(USERNAME);
        
    }
    
}
