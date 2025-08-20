package com.reservation.online_reservation_system.repositories;

import com.reservation.online_reservation_system.models.Reservation;
import com.reservation.online_reservation_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser(User user);

}

