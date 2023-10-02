package com.project.booking.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Seat {
    public Seat(String seatNumber, boolean isOccupied) {
        this.seatNumber = seatNumber;
        this.isOccupied = isOccupied;
    }

    @Setter
    private String seatNumber;
    @Setter
    private boolean isOccupied;


}