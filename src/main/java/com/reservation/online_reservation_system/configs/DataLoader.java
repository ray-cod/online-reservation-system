package com.reservation.online_reservation_system.configs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.reservation.online_reservation_system.models.Reservation;
import com.reservation.online_reservation_system.models.Train;
import com.reservation.online_reservation_system.models.User;
import com.reservation.online_reservation_system.repositories.ReservationRepository;
import com.reservation.online_reservation_system.repositories.TrainRepository;
import com.reservation.online_reservation_system.repositories.UserRepository;
import com.reservation.online_reservation_system.utilities.ReservationStatus;
import com.reservation.online_reservation_system.utilities.UserRole;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DataLoader {
    
    @Bean
    public ApplicationRunner initData(
            UserRepository userRepository, 
            TrainRepository trainRepository,  ReservationRepository reservationRepository,
            PasswordEncoder passwordEncoder
        ) {
        return args -> {

            // --- Create 10 trains ---
            List<Train> trains = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                Train t = new Train();
                t.setTrainNumber(String.format("T%04d", 1000 + i)); // e.g. T1001
                t.setTrainName("Express " + i);
                t.setSourceStation("City " + ((i % 5) + 1));
                t.setDestinationStation("City " + (((i + 1) % 5) + 1));
                t.setAvailableSeats(100); // initial seats
                t.setClassType(i % 2 == 0 ? "AC" : "SLEEPER");
                t.setDepartureTime(LocalDateTime.now().plusDays(i).withHour(6).withMinute(0));
                t.setArrivalTime(t.getDepartureTime().plusHours(6));
                trains.add(t);
            }
            trainRepository.saveAll(trains);
            log.info("Seeded {} trains", trains.size());

            // --- Create 2 users ---
            User user1 = new User();
            user1.setUsername("alice");
            user1.setEmail("alice@example.com");
            user1.setRole(UserRole.CUSTOMER);
            user1.setPassword(passwordEncoder.encode("password1"));

            User user2 = new User();
            user2.setUsername("bob");
            user2.setEmail("bob@example.com");
            user2.setRole(UserRole.CUSTOMER);
            user2.setPassword(passwordEncoder.encode("password2"));

            userRepository.save(user1);
            userRepository.save(user2);
            log.info("Seeded users: {}, {}", user1.getUsername(), user2.getUsername());

            // --- Create 3 reservations for each user ---
            List<Train> savedTrains = trainRepository.findAll();
            if (savedTrains.size() < 6) {
                log.warn("Not enough trains to create reservations; expected 10, found {}", savedTrains.size());
            }

            // helper to create and save reservation and decrement seats
            java.util.function.BiConsumer<User, Integer> createThreeForUser = (user, startIndex) -> {
                for (int j = 0; j < 3; j++) {
                    int trainIndex = (startIndex + j) % savedTrains.size();
                    Train train = savedTrains.get(trainIndex);

                    Reservation r = new Reservation();
                    r.setUser(user);
                    r.setTrain(train);
                    r.setPassengerName(user.getUsername() + " Passenger " + (j + 1));
                    r.setClassType(train.getClassType());
                    r.setJourneyDate(LocalDate.now().plusDays(3 + j)); // different dates
                    r.setFromStation(train.getSourceStation());
                    r.setToStation(train.getDestinationStation());
                    r.setBookingDate(LocalDateTime.now());

                    try {
                        r.setStatus(ReservationStatus.BOOKED);
                    } catch (Exception ex) {
                        log.error("Error setting reservation status", ex);
                    }

                    // decrement available seats and persist both reservation and train
                    if (train.getAvailableSeats() > 0) {
                        train.setAvailableSeats(train.getAvailableSeats() - 1);
                    }
                    reservationRepository.save(r);
                    trainRepository.save(train);
                }
            };

            // create for user1 using trains starting at index 0, user2 starting at index 3
            createThreeForUser.accept(user1, 0);
            createThreeForUser.accept(user2, 3);

            log.info("Seeded reservations for users");
        };
    }
}
