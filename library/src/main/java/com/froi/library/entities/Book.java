package com.froi.library.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "book", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @Column
    private String code;
    
    @Column
    private String title;
    
    @Column(name = "publish_date")
    private Date publishDate;
    
    @Column
    private String publisher;
    
    @Column
    private String author;
    
    @Column
    private Double cost;
    
    @Column
    private Integer stock;
}
