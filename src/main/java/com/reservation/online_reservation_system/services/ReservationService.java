package com.reservation.online_reservation_system.services;

import com.reservation.online_reservation_system.models.Reservation;
import com.reservation.online_reservation_system.models.Train;
import com.reservation.online_reservation_system.models.User;
import com.reservation.online_reservation_system.repositories.ReservationRepository;
import com.reservation.online_reservation_system.repositories.TrainRepository;
import com.reservation.online_reservation_system.utilities.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TrainRepository trainRepository;

    // Book a new reservation
    public Reservation bookReservation(Reservation reservation) {
        // check seat availability before booking
        Train train = reservation.getTrain();
        if (train.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available for this train.");
        }

        // Decrease available seats
        train.setAvailableSeats(train.getAvailableSeats() - 1);
        trainRepository.save(train);

        // Save reservation
        return reservationRepository.save(reservation);
    }

    // Cancel a reservation by PNR
    public Reservation cancelReservation(Long pnrNumber) {
        Reservation reservation = reservationRepository.findById(pnrNumber)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new RuntimeException("Reservation already cancelled");
        }

        // Update status
        reservation.setStatus(ReservationStatus.CANCELLED);

        // Increase train available seats
        Train train = reservation.getTrain();
        train.setAvailableSeats(train.getAvailableSeats() + 1);
        trainRepository.save(train);

        return reservationRepository.save(reservation);
    }

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Get reservations for a specific user
    public List<Reservation> getReservationsByUser(User user) {
        return reservationRepository.findByUser(user);
    }

    // Get reservation by PNR
    public Optional<Reservation> getReservationByPnr(Long pnrNumber) {
        return reservationRepository.findById(pnrNumber);
    }
}

