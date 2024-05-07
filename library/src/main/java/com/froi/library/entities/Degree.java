package com.froi.library.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "degree")
@Getter
@Setter
@NoArgsConstructor
public class Degree {
    @Id
    @Column
    private Integer id;
    
    @Column
    private String name;
}
