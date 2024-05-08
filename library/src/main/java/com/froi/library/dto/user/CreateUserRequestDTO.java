package com.froi.library.dto.user;

import com.froi.library.enums.studentstatus.Role;
import lombok.Value;

@Value
public class CreateUserRequestDTO {
    String username;
    String password;
    String student;
}
