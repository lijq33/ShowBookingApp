package com.project.booking.service;

import com.project.booking.model.Seat;
import com.project.booking.model.Show;
import com.project.booking.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShowService {

    private Map<Long, Show> shows = new HashMap<>();
    private long ticketCounter = 1;

    @Autowired
    TicketService ticketService;


    public Show setupShow(Show show) {
        shows.put(show.getId(), show);
        return show;
    }

    public Show getShowById(Long showId) {
        return shows.get(showId);
    }

    public Ticket bookTicket(Show show, String phone, List<Seat> seats) {
        Ticket ticket = new Ticket(ticketCounter++, phone, seats, new Date(), show);
        show.getTickets().add(ticket);
        return ticketService.addTicket(ticket);
    }

    public Ticket cancelBooking(Show show, long ticketNumber) {
        Ticket ticket = ticketService.getTicketById(ticketNumber);
        show.getTickets().remove(ticket);
        ticket.getSeats().forEach(s -> s.setOccupied(false));

        return ticketService.removeTicket(ticketNumber);
    }
}
