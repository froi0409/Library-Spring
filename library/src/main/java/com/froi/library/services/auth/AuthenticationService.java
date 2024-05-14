package com.froi.library.services.auth;

import com.froi.library.dto.auth.AuthenticationRequestDTO;
import com.froi.library.dto.auth.JwtResponseDTO;
import com.froi.library.exceptions.EntityNotFoundException;

public interface AuthenticationService {
    JwtResponseDTO authenticateAndGetToken(AuthenticationRequestDTO authDTO) throws EntityNotFoundException;
}
