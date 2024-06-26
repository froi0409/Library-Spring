package com.froi.library.entities;

import com.froi.library.enums.bookstatus.BookLoanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "book_loan", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class BookLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    
    @Column
    private String book;
    
    @Column
    private String student;
    
    @Column(name = "loan_date")
    private Date loanDate;
    
    @Column(name = "loan_total")
    private Double loanTotal;
    
    @Column(name = "delay_total")
    private Double delayTotal;
    
    @Column(name = "returned_date")
    private Date returnedDate;
    
    @Column
    @Enumerated(EnumType.STRING)
    private BookLoanStatus status;
}
