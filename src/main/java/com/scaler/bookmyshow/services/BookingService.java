package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.exceptions.InvalidShowException;
import com.scaler.bookmyshow.exceptions.ShowSeatNotAvailableException;
import com.scaler.bookmyshow.exceptions.InvalidUserException;
import com.scaler.bookmyshow.models.*;
import com.scaler.bookmyshow.repositories.ShowRepository;
import com.scaler.bookmyshow.repositories.ShowSeatRepository;
import com.scaler.bookmyshow.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private UserRepository userRepository;
    private ShowRepository showRepository;
    private ShowSeatRepository showSeatRepository;
    private TransactionTemplate transactionTemplate;
    private PriceCalculatorService priceCalculatorService;

    public Booking bookMovie(Long userId, Long showId, List<Long> showSeatId) throws InvalidUserException, InvalidShowException, ShowSeatNotAvailableException {
         /*
        STEPS to book movie tickets :-

        1. Get the User from userId from DB.
        2. Get the Show from showId from DB.
        3. Get the list of showSeats from showSeatIds from DB.
        4. Check if all the seats are available or not.
        5. If not, throw an exception. -> Code
        6. If yes, check if all the seats are available or not once again.
        -------ACQUIRE THE LOCK-------
        7. If yes, Mark the status as BLOCKED.
        8. Save the status of seats in the DB as well.
        -------RELEASE THE LOCK-------
        9. Create the BOOKING object.
        10. Save the booking to DB.
        11. Return the Booking.
         */
//        1. Get the User from userId from DB.
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new InvalidUserException("User not found");
        }
        User user = userOptional.get();

//        2. Get the Show from showId from DB.
        Optional<Show> showOptional = showRepository.findById(showId);
        if(showOptional.isEmpty()){
            throw new InvalidShowException("Show not found");
        }
        Show show = showOptional.get();

//        3. Get the list of showSeats from showSeatIds from DB.
        List<ShowSeat> showSeats = showSeatRepository.findAllById(showSeatId);

//        4. Check if all the seats are available or not.
        for (ShowSeat showSeat : showSeats) {
            if (showSeat.getStatus() != ShowSeatStatus.AVAILABLE) {
//                5. If not, throw an exception. ->Code
                throw new ShowSeatNotAvailableException("Show seat with id " + showSeat.getId() + " is not available");
            }
        }
//        6. If yes, check if all the seats are available or not once again.
//        -------ACQUIRE THE LOCK-------

        // Use transactionTemplate to limit lock scope
        List<ShowSeat> updatedShowSeats = transactionTemplate.execute(status -> {
            // This code runs in a transaction with lock
            List<ShowSeat> showSeatsWithLock = showSeatRepository.findByIdWithLock(showSeatId);

            for (ShowSeat showSeat : showSeatsWithLock) {
                if (showSeat.getStatus() != ShowSeatStatus.AVAILABLE) {
                    try {
                        throw new ShowSeatNotAvailableException("Show seat with id " + showSeat.getId() + " is not available");
                    } catch (ShowSeatNotAvailableException e) {
                        throw new RuntimeException(e);
                    }
                }
                showSeat.setStatus(ShowSeatStatus.BLOCKED);
            }

            return showSeatRepository.saveAll(showSeatsWithLock);
        });


//        List<ShowSeat> showSeatsWithLock = showSeatRepository.findByIdWithLock(showSeatId);
//        for (ShowSeat showSeat : showSeatsWithLock) {
//            if (showSeat.getStatus() != ShowSeatStatus.AVAILABLE) {
//                7. If not, throw an exception. ->Code
//                throw new ShowSeatNotAvailableException("Show seat with id " + showSeat.getId() + " is not available");
//            }
//        }
//        8. If yes, Mark the status as BLOCKED.
//        for (ShowSeat showSeat : showSeatsWithLock) {
//            showSeat.setStatus(ShowSeatStatus.BLOCKED);
//        }
//        9. Save the status of seats in the DB as well.
//        showSeatRepository.saveAll(showSeatsWithLock);
//        -------RELEASE THE LOCK-------

//        10. Create the BOOKING object.
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setShowSeats(updatedShowSeats);
        booking.setCreatedAt(new Date());
        booking.setUpdatedAt(booking.getCreatedAt());
        booking.setBookedDate(booking.getCreatedAt());
        booking.setAmount(priceCalculatorService.calculateTotalAmount(updatedShowSeats,show));

        return booking;
    }
}
