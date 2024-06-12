package com.froi.library.dto.auth;

import com.froi.library.enums.studentstatus.Role;
import lombok.Value;

@Value
public class JwtResponseDTO {
    String token;
    Role role;
}
