package com.froi.library.entities;

import com.froi.library.enums.bookstatus.BookReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Id
    @Column
    private Integer id;
    
    @Column
    private String student;
    
    @Column
    private String book;
    
    @Column(name = "reservation_date")
    private Date reservationDate;
    
    @Column
    @Enumerated(EnumType.STRING)
    private BookReservationStatus status;
}
