package com.reservation.online_reservation_system.repositories;

import com.reservation.online_reservation_system.models.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {

    Optional<Train> findByTrainNumber(String trainNumber);

    Optional<Train> findByClassType(String classType);
}

