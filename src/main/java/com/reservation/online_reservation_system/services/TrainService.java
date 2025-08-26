package com.reservation.online_reservation_system.services;

import com.reservation.online_reservation_system.models.Train;
import com.reservation.online_reservation_system.repositories.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepository;

    // Add a new train
    public Train addTrain(Train train) {
        return trainRepository.save(train);
    }

    // Get train by ID
    public Optional<Train> getTrainById(Long id) {
        return trainRepository.findById(id);
    }

    // Get train by train number
    public Optional<Train> getTrainByNumber(String trainNumber) {
        return trainRepository.findByTrainNumber(trainNumber);
    }

    // Get train by class type
    public Optional<Train> getTrainByClassType(String classType) {
        return trainRepository.findByClassType(classType);
    }

    // Get all trains
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    // Update train details
    public Train updateTrain(Train train) {
        return trainRepository.save(train);
    }

    // Delete a train
    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }
}

