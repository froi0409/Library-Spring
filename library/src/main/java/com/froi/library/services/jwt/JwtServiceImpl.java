package com.froi.library.services.jwt;

import com.froi.library.entities.User;
import com.froi.library.enums.studentstatus.Role;
import com.froi.library.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtServiceImpl implements JwtService {

    public static final String SECRET_PHRASE = "9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9";
    
    private UserRepository userRepository;
    
    @Autowired
    public JwtServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    
    @Override
    public String generateToken(String username, Role role) {
        return Jwts.builder()
                .claims(Collections.singletonMap("role", role))
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + 1800000))
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSecretKey())
                .compact();
        
    }
    
    @Override
    public String getUsername(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }
    
    @Override
    public String getPayload(String token) {
        Claims claims = extractClaims(token);
        return claims.toString();
    }
    
    @Override
    public boolean isValid(String token) {
        Claims claims = extractClaims(token);
        System.out.println("ROLE - " + claims.get("role"));
        Date expirationDate = claims.getExpiration();
        return new Date().before(expirationDate);
    }
    
    @Override
    public void updateTokenExpiration(String username) {
        Optional<User> userOptional = userRepository.findById(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setTokenExpiration(LocalDateTime.now().plusMinutes(30));
            userRepository.save(user);
        }
    }
    
    @Override
    public boolean isTokenExpired(String username) {
        Optional<User> userOptional = userRepository.findById(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getTokenExpiration() == null
                    || LocalDateTime.now().isAfter(user.getTokenExpiration());
        }
        return true;
    }

    public SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_PHRASE);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    
    
    
}
