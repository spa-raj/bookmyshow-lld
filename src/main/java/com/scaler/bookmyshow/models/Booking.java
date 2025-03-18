package com.scaler.bookmyshow.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Booking extends BaseModel{
    @ManyToOne
    private User user;

    @ManyToMany
    private List<ShowSeat> showSeats; //If ticket gets cancelled then the same show seat can be booked by someone else

    @OneToMany
    private List<Payment> payments;

    private double amount;

    @Enumerated(EnumType.ORDINAL)
    private BookingStatus bookingStatus;

    private Date bookedDate;
}
