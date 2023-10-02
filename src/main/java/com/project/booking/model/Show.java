package com.project.booking.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Show {

    public Show(Long id, List<Seat> seats, int cancellationWindowMinutes) {
        this.id = id;
        this.seats = seats;
        this.cancellationWindowMinutes = cancellationWindowMinutes;
        tickets = new ArrayList<>();
    }

    @Setter
    private Long id;
    @Setter
    private List<Seat> seats;
    @Setter
    private int cancellationWindowMinutes;
    @Getter
    @Setter
    private List<Ticket> tickets;
}

