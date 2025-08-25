package com.reservation.online_reservation_system.controllers;

import com.reservation.online_reservation_system.models.Train;
import com.reservation.online_reservation_system.services.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;

    // Add a new train
    @PostMapping
    public ResponseEntity<Train> addTrain(@RequestBody Train train) {
        Train savedTrain = trainService.addTrain(train);
        return ResponseEntity.ok(savedTrain);
    }

    // Get train by ID
    @GetMapping("/{id}")
    public ResponseEntity<Train> getTrainById(@PathVariable Long id) {
        return trainService.getTrainById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get train by train number
    @GetMapping("/number/{trainNumber}")
    public ResponseEntity<Train> getTrainByNumber(@PathVariable String trainNumber) {
        return trainService.getTrainByNumber(trainNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all trains
    @GetMapping
    public ResponseEntity<List<Train>> getAllTrains() {
        List<Train> trains = trainService.getAllTrains();
        return ResponseEntity.ok(trains);
    }

    // Update train details
    @PutMapping("/{id}")
    public ResponseEntity<Train> updateTrain(@PathVariable Long id, @RequestBody Train train) {
        return trainService.getTrainById(id).map(existingTrain -> {
            train.setTrainId(id);
            Train updatedTrain = trainService.updateTrain(train);
            return ResponseEntity.ok(updatedTrain);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete a train
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        if (trainService.getTrainById(id).isPresent()) {
            trainService.deleteTrain(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

