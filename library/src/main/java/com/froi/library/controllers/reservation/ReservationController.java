package com.froi.library.controllers.reservation;

import com.froi.library.dto.reservation.CreateReservationDTO;
import com.froi.library.exceptions.DenegatedActionException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.services.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reservation")
public class ReservationController {

    private ReservationService reservationService;
    
    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Boolean> createReservation(@RequestBody CreateReservationDTO newReservation) throws DenegatedActionException, EntityNotFoundException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservationService.createReservation(newReservation));
    }
    
    @PostMapping(path = "/doReservation")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Boolean> doReservation(@RequestBody CreateReservationDTO doReservation) throws DenegatedActionException, EntityNotFoundException, EntitySyntaxException {
        return ResponseEntity
                .ok(reservationService.doReservation(doReservation));
    }
}
