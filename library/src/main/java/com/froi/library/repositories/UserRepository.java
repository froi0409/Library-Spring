package com.froi.library.repositories;

import com.froi.library.entities.Student;
import com.froi.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findUsersByStudent_Id(String studentId);
}
