package com.reservation.online_reservation_system.controllers;

import com.reservation.online_reservation_system.models.Reservation;
import com.reservation.online_reservation_system.models.User;
import com.reservation.online_reservation_system.services.ReservationService;
import com.reservation.online_reservation_system.services.UserService;
import com.reservation.online_reservation_system.services.dto.ReservationRequestDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    // Book a reservation
    @PostMapping("/book")
    public ResponseEntity<Reservation> bookReservation(@RequestBody Reservation reservation) {
        Reservation savedReservation = reservationService.bookReservation(reservation);
        return ResponseEntity.ok(savedReservation);
    }

    // Book a reservation from the form
    @PostMapping("form/book")
    public ResponseEntity<Reservation> bookReservation(@RequestBody ReservationRequestDTO reservationDTO) {
        Reservation savedReservation = reservationService.bookReservation(reservationDTO);
        return ResponseEntity.ok(savedReservation);
    }

    // Cancel a reservation
    @PutMapping("/cancel/{pnrNumber}")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long pnrNumber) {
        Reservation cancelledReservation = reservationService.cancelReservation(pnrNumber);
        return ResponseEntity.ok(cancelledReservation);
    }

    // Get all reservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    // Get reservations by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return ResponseEntity.ok(reservationService.getReservationsByUser(user));
    }

    // ReservationController
    @GetMapping("/user/me")
    public ResponseEntity<List<Reservation>> myReservations(@AuthenticationPrincipal UserDetails principal) {
        var user = userService.findByUsername(principal.getUsername())
                            .orElseThrow(() -> new UsernameNotFoundException(principal.getUsername()));

        List<Reservation> reservations = reservationService.getReservationsByUser(user);
        return ResponseEntity.ok(reservations);
    }


    // Get reservation by PNR
    @GetMapping("/{pnrNumber}")
    public ResponseEntity<Reservation> getReservationByPnr(@PathVariable Long pnrNumber) {
        return reservationService.getReservationByPnr(pnrNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

