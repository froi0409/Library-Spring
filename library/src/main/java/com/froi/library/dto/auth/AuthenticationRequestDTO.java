package com.froi.library.dto.auth;

import lombok.Value;

@Value
public class AuthenticationRequestDTO {
    String username;
    String password;
}
