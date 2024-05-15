package com.froi.library.entities;

import com.froi.library.enums.studentstatus.StudentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "student", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Student {
    @Id
    @Column
    private String id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column
    private Integer degree;
    
    @Column(name = "birt_date")
    private String birtDate;
    
    @Column
    @Enumerated(EnumType.STRING)
    private StudentStatus status;
}
