package com.project.booking;

import com.project.booking.model.Ticket;
import com.project.booking.service.TicketService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private Map<Long, Ticket> tickets;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTicketById() {
        Ticket ticket = new Ticket(12345l, "John Doe", null, new Date(), null);
        when(tickets.get(12345L)).thenReturn(ticket);
        Ticket retrievedTicket = ticketService.getTicketById(12345);
        assertEquals(ticket, retrievedTicket);
    }

    @Test
    public void testRemoveTicket() {
        Ticket ticket = new Ticket(54321l, "Jane Smith", null, new Date(), null);
        when(tickets.remove(54321L)).thenReturn(ticket);
        Ticket removedTicket = ticketService.removeTicket(54321);
        assertEquals(ticket, removedTicket);
    }

    @Test
    public void testAddTicket() {
        Ticket ticket = new Ticket(98765l, "Alice Johnson", null, new Date(), null);
        Ticket addedTicket = ticketService.addTicket(ticket);
        verify(tickets).put(98765L, ticket);
        assertEquals(ticket, addedTicket);
    }
}