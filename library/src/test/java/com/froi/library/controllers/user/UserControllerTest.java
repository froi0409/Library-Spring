package com.froi.library.controllers.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.froi.library.controllers.AbstractMvcTest;
import com.froi.library.controllers.exceptionhandler.GlobalExceptionHandler;
import com.froi.library.dto.user.CreateUserRequestDTO;
import com.froi.library.enums.studentstatus.Role;
import com.froi.library.services.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {UserController.class, UserService.class, GlobalExceptionHandler.class})
public class UserControllerTest extends AbstractMvcTest {
    
    public static final String INVALID_USERNAME = "us";
    public static final String INVALID_PASSWORD = "xd";
    public static final String NULL_STUDENT = null;
    public static final String EXPECTED_STUDENT_USERNAME = "201830121";
    public static final String EXPECTED_LIBRARIAN_USERNAME = "LIB_01";
    public static final String EXPECTED_PASSWORD = "admin123";
    public static final String EXPECTED_STUDENT = "201830121";
    public static final String EXPECTED_ENCRYPTED_PASSWORD = "$2a$04$y8FbOSsXx/9HsV/Zenl7OeaUbpeeHaB1XWGCMmyztrJr4.edjVbbu";
    public static final Role STUDENT_ROLE = Role.STUDENT;
    public static final Role LIBRARIAN_ROLE = Role.LIBRARIAN;
    
    @MockBean
    private UserService userService;
    
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    public void testCreateUser() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(EXPECTED_STUDENT_USERNAME, EXPECTED_PASSWORD, EXPECTED_STUDENT);
        
        // Act & Assert
        mockMvc.perform(post("/v1/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser))
                )
                .andExpect(status().isCreated());
    }
    
    
}
