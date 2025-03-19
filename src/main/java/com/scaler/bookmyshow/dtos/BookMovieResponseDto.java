package com.scaler.bookmyshow.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookMovieResponseDto {
    private Long bookingId;
    private BookingResponseStatus bookingStatus;
    private double totalAmount;
}
