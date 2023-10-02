package com.project.booking;

import com.project.booking.model.Seat;
import com.project.booking.model.Show;
import com.project.booking.model.Ticket;
import com.project.booking.service.ShowService;
import com.project.booking.service.TicketService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class ShowServiceTest {

    @InjectMocks
    private ShowService showService;

    @Mock
    private TicketService ticketService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetupShow() {
        Show show = new Show(1L, new ArrayList<>(), 60);
        Show savedShow = showService.setupShow(show);
        assertEquals(show, savedShow);
    }

    @Test
    public void testGetShowById() {
        Show show = new Show(1L, new ArrayList<>(), 60);
        showService.setupShow(show);
        Show retrievedShow = showService.getShowById(1L);
        assertEquals(show, retrievedShow);
    }

    @Test
    public void testBookTicket() {
        Show show = new Show(1L, new ArrayList<>(), 60);
        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat("A1", false));
        String phoneNumber = "1234567890";

        when(ticketService.addTicket(any())).thenReturn(new Ticket(1L, phoneNumber, seats, new Date(), show));

        Ticket bookedTicket = showService.bookTicket(show, phoneNumber, seats);

        assertEquals(phoneNumber, bookedTicket.getPhoneNumber());
        assertEquals(seats, bookedTicket.getSeats());
        assertEquals(show, bookedTicket.getShow());
    }

    @Test
    public void testCancelBooking() {
        long showId = 1L;
        long ticketNumber = 1L;
        String phoneNumber = "1234567890";

        Show show = new Show(showId, new ArrayList<>(), 10);
        Seat seat = new Seat("A1", true);
        List<Seat> seats = new ArrayList<>();
        seats.add(seat);
        Ticket ticket = new Ticket(ticketNumber, phoneNumber, seats, new Date(), show);
        show.getTickets().add(ticket);

        when(ticketService.getTicketById(ticketNumber)).thenReturn(ticket);
        when(ticketService.removeTicket(ticketNumber)).thenReturn(ticket);

        Ticket canceledTicket = showService.cancelBooking(show, ticketNumber);

        assertEquals(ticket, canceledTicket);
        assertEquals(0, show.getTickets().size());
        assertFalse(seat.isOccupied());
        verify(ticketService, times(1)).removeTicket(ticketNumber);
    }
}
