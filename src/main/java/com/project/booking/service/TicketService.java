package com.project.booking.service;

import com.project.booking.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TicketService {
    private Map<Long, Ticket> tickets = new HashMap<>();

    public Ticket getTicketById(long ticketNumber) {
        return tickets.get(ticketNumber);
    }

    public Ticket removeTicket(long ticketNumber) {
        return tickets.remove(ticketNumber);
    }

    public Ticket addTicket (Ticket ticket) {
        tickets.put(ticket.getTicketNumber(), ticket);
        return ticket;
    }

}
