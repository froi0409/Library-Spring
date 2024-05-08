package com.froi.library.services.user;

import com.froi.library.dto.user.CreateUserRequestDTO;
import com.froi.library.entities.Student;
import com.froi.library.entities.User;
import com.froi.library.enums.studentstatus.Role;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }
    
    
    @Override
    public User createUser(CreateUserRequestDTO newUser) throws DuplicatedEntityException, EntitySyntaxException {
        if (newUser.getUsername().length() < 4) {
            throw new EntitySyntaxException("USERNAME_SYNTAX");
        }
        if (newUser.getPassword().length() < 4) {
            throw new EntitySyntaxException("PASSWORD_SYNTAX");
        }
        
        Optional<User> verifyUser = repository.findById(newUser.getUsername());
        if (verifyUser.isPresent()) {
            throw new DuplicatedEntityException("DUPLICATED");
        }
        if (newUser.getStudent() != null) {
            // validate if student exists
        }
   
        User userEntity = new User();
        userEntity.setUsername(newUser.getUsername());
        userEntity.setPassword(passwordEncoder.encode(newUser.getPassword()));
        if (newUser.getStudent() != null) {
            userEntity.setRole(Role.STUDENT);
            userEntity.setStudent(userEntity.getStudent());
        } else {
            userEntity.setRole(Role.LIBRARIAN);
        }
        
        repository.save(userEntity);
        return userEntity;
    }
}
