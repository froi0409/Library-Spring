package com.froi.library.services.user;

import com.froi.library.dto.user.CreateUserRequestDTO;
import com.froi.library.entities.Student;
import com.froi.library.entities.User;
import com.froi.library.enums.studentstatus.Role;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.UserRepository;
import com.froi.library.services.student.StudentService;
import com.froi.library.services.student.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    
    private final UserRepository userRepository;
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private StudentService studentService;
    
    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, StudentService studentService, UserRepository userRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.studentService = studentService;
        this.userRepository = userRepository;
    }
    
    
    @Override
    public User createUser(CreateUserRequestDTO newUser) throws DuplicatedEntityException, EntitySyntaxException, EntityNotFoundException {
        if (newUser.getUsername().length() < 4) {
            throw new EntitySyntaxException("USERNAME_SYNTAX");
        }
        if (newUser.getPassword().length() < 4) {
            throw new EntitySyntaxException("PASSWORD_SYNTAX");
        }
        
        Optional<User> verifyUser = repository.findById(newUser.getUsername());
        if (verifyUser.isPresent()) {
            throw new DuplicatedEntityException("USER_DUPLICATED");
        }
   
        User userEntity = new User();
        userEntity.setUsername(newUser.getUsername());
        userEntity.setPassword(passwordEncoder.encode(newUser.getPassword()));
        if (newUser.getStudent() != null) {
            Student checkStudent = studentService.getStudentById(newUser.getStudent().getId())
                    .orElseThrow(() -> new EntityNotFoundException("STUDENT_NOT_FOUND"));
            List<User> checkDuplicatedStudent = userRepository.findUsersByStudent_Id(checkStudent.getId());
            if (!checkDuplicatedStudent.isEmpty()) {
                throw new DuplicatedEntityException("STUDENT_USER_DUPLICATED");
            }
            userEntity.setRole(Role.STUDENT);
            userEntity.setStudent(userEntity.getStudent());
        } else {
            userEntity.setRole(Role.LIBRARIAN);
        }
        
        repository.save(userEntity);
        return userEntity;
    }
}
