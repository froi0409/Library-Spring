package com.froi.library.entities;

import com.froi.library.enums.bookstatus.BookReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "reservation", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    
    @Column
    private String student;
    
    @Column
    private String book;
    
    @Column(name = "reservation_date")
    private Date reservationDate;
    
    @Column(name = "reservation_validated")
    private Date reservationValidated;
    
    @Column
    @Enumerated(EnumType.STRING)
    private BookReservationStatus status;
}
