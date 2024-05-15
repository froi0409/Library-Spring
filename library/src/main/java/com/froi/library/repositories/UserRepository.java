package com.froi.library.repositories;

import com.froi.library.entities.Student;
import com.froi.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findUsersByStudent_Id(String studentId);
}
