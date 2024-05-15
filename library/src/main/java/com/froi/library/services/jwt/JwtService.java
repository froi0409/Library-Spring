package com.froi.library.services.jwt;

import com.froi.library.enums.studentstatus.Role;

public interface JwtService {
    
    String generateToken(String username, Role role);
    
    String getUsername(String token);
    
    boolean isValid(String token);
    
    void updateTokenExpiration(String username);
    
    public boolean isTokenExpired(String username);
    
    String getPayload(String token);
}
