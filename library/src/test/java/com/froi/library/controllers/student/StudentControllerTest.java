package com.froi.library.controllers.student;

import com.froi.library.controllers.AbstractMvcTest;
import com.froi.library.controllers.exceptionhandler.GlobalExceptionHandler;
import com.froi.library.entities.Student;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.services.student.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ContextConfiguration(classes = {StudentController.class, StudentService.class, GlobalExceptionHandler.class})
class StudentControllerTest extends AbstractMvcTest {
    
    private static final String STUDENT_ID = "123456";
    private static final String STUDENT_FIRST_NAME = "First";
    private static final String STUDENT_LAST_NAME = "Last";
    private static final int LOAN_COUNT = 3;
    
    @MockBean
    private StudentService studentService;
    
    @Test
    @WithMockUser(username = "user1")
    void testGetStudentById() throws Exception {
        // Arrange
        Student student = new Student();
        student.setId(STUDENT_ID);
        student.setFirstName(STUDENT_FIRST_NAME);
        student.setLastName(STUDENT_LAST_NAME);
        
        when(studentService.getOneStudentById(STUDENT_ID)).thenReturn(student);
        
        // Act & Assert
        mockMvc.perform(get("/v1/student/{id}", STUDENT_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID))
                .andExpect(jsonPath("$.firstName").value(STUDENT_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(STUDENT_LAST_NAME));
    }
    
    @Test
    @WithMockUser(username = "user1")
    void testGetStudentLoansCount() throws Exception {
        // Arrange
        when(studentService.getStudentLoansCount(STUDENT_ID)).thenReturn(LOAN_COUNT);
        
        // Act & Assert
        mockMvc.perform(get("/v1/student/loanCount/{studentId}", STUDENT_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
