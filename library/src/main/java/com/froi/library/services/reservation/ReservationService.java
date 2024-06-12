package com.froi.library.services.reservation;

import com.froi.library.dto.reservation.CreateReservationDTO;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;

import java.sql.Date;

public interface ReservationService {
    boolean createReservation(CreateReservationDTO newReservation) throws EntityNotFoundException, DenegatedActionException;
    
    boolean checkReservation(String bookId, Date date);
    
    boolean doReservation(CreateReservationDTO newReservation) throws EntityNotFoundException, DenegatedActionException, EntitySyntaxException;
}
