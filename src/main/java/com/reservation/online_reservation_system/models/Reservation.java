package com.reservation.online_reservation_system.models;

import com.reservation.online_reservation_system.utilities.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pnrNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // links to User entity

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;  // links to Train entity

    @Column(nullable = false)
    private String passengerName;

    @Column(nullable = false)
    private String classType;

    @Column(nullable = false)
    private LocalDate journeyDate;

    @Column(nullable = false)
    private String fromStation;

    @Column(nullable = false)
    private String toStation;

    @Column(nullable = false)
    private LocalDateTime bookingDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.BOOKED;
}

