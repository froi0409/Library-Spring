package com.froi.library.security;

import com.froi.library.entities.User;
import com.froi.library.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private UserRepository userRepository;
    
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userOpt = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(userOpt.getUsername())
                .password(userOpt.getPassword())
                .roles(userOpt.getRole().toString())
                .build();
        
        return userDetails;
        
    }
    
}
