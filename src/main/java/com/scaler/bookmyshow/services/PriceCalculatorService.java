package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.models.*;
import com.scaler.bookmyshow.repositories.ShowSeatTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PriceCalculatorService {
    private final ShowSeatTypeRepository showSeatTypeRepository;
    public double calculateTotalAmount(List<ShowSeat> showSeats, Show show) {
        double totalAmount=0;
        List<ShowSeatType> showSeatTypes = showSeatTypeRepository.findAllByShow(show);
        for(ShowSeat showSeat:showSeats){
            for(ShowSeatType showSeatType:showSeatTypes){
                if(showSeat.getSeat().getSeatType() == showSeatType.getSeatType()){
                    totalAmount += showSeatType.getPrice();
                    break;
                }
            }
        }
        return  totalAmount;
    }
}
