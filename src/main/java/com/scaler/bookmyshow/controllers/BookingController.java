package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.dtos.BookMovieRequestDto;
import com.scaler.bookmyshow.dtos.BookMovieResponseDto;
import com.scaler.bookmyshow.dtos.BookingResponseStatus;
import com.scaler.bookmyshow.models.Booking;
import com.scaler.bookmyshow.services.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class BookingController {
    BookingService bookingService;
    public BookMovieResponseDto bookMovie(BookMovieRequestDto bookMovieRequestDto) {
        BookMovieResponseDto bookMovieResponseDto = new BookMovieResponseDto();

        try {
            Booking booking = bookingService.bookMovie(
                    bookMovieRequestDto.getUserId(),
                    bookMovieRequestDto.getShowId(),
                    bookMovieRequestDto.getShowSeatIds()
            );

            bookMovieResponseDto.setBookingId(booking.getId());
            bookMovieResponseDto.setTotalAmount(booking.getAmount());
            bookMovieResponseDto.setBookingStatus(BookingResponseStatus.SUCCESS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            bookMovieResponseDto.setBookingStatus(BookingResponseStatus.FAILED);

        }
        return bookMovieResponseDto;
    }
}
