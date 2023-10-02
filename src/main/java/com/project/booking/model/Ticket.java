package com.project.booking.model;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
public class Ticket {

    public Ticket(Long ticketNumber, String phoneNumber, List<Seat> seats, Date bookingTime, Show show) {
        this.ticketNumber = ticketNumber;
        this.phoneNumber = phoneNumber;
        this.seats = seats;
        this.show = show;
        this.bookingTime = bookingTime;
    }

    private Long ticketNumber;
    @Setter
    private String phoneNumber;
    @Setter
    private List<Seat> seats;
    @Setter
    private Show show;
    @Setter
    private Date bookingTime;

}