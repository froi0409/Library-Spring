package com.froi.library.entities;

import com.froi.library.enums.studentstatus.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @Column
    private String username;
    
    @Column
    private String password;
    
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @Column
    private String student;
    
    @Column(name = "token_expiration")
    private LocalDateTime tokenExpiration;
}
