package com.project.booking;

import com.project.booking.model.Seat;
import com.project.booking.model.Show;
import com.project.booking.model.Ticket;
import com.project.booking.service.BuyerService;
import com.project.booking.service.ShowService;
import com.project.booking.service.TicketService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BuyerServiceTest {

    @InjectMocks
    private BuyerService buyerService;

    @Mock
    private ShowService showService;

    @Mock
    private TicketService ticketService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAvailableSeats() throws Exception {
        // Create a sample show
        long showNumber = 123;
        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat("A1", false));
        seats.add(new Seat("A2", true));
        Show show = new Show(showNumber, seats, 30);

        // Mock the behavior of showService
        when(showService.getShowById(showNumber)).thenReturn(show);

        // Call the method being tested
        Map<String, Seat> availableSeats = buyerService.getAvailableSeats(showNumber);

        // Verify that only the available seat "A1" is in the result
        assertEquals(1, availableSeats.size());
        assertTrue(availableSeats.containsKey("A1"));
        assertFalse(availableSeats.get("A1").isOccupied());
    }

    @Test
    public void testListAvailability()  {
        long showNumber = 456;
        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat("B1", false));
        seats.add(new Seat("B2", false));
        Show show = new Show(showNumber, seats, 30);

        when(showService.getShowById(showNumber)).thenReturn(show);
        String capturedOutput = getOutput(() -> {
            try {
                buyerService.listAvailability(showNumber);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        assertEquals("These seats are available:\nB1, B2", capturedOutput.toString());
    }

    @Test
    public void testBook() throws Exception {
        long showNumber = 789;
        String phoneNumber = "12345678";

        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat("C1", false));
        Show show = new Show(showNumber, seats, 30);
        when(showService.getShowById(showNumber)).thenReturn(show);
        Ticket bookedTicket = new Ticket(12345L, phoneNumber, seats, new Date(), show);
        when(showService.bookTicket(show, phoneNumber, seats)).thenReturn(bookedTicket);

        Ticket ticket = buyerService.book(showNumber, phoneNumber, "C1");
        assertEquals(bookedTicket, ticket);
    }

    @Test
    public void testCancel() throws Exception {
        long ticketNumber = 98765L;
        String phoneNumber = "12345678";
        Ticket ticket = new Ticket(ticketNumber, phoneNumber, null, new Date(), null);
        when(ticketService.getTicketById(ticketNumber)).thenReturn(ticket);
        Show show = new Show(789l, null, 30);
        ticket.setShow(show);
        when(showService.cancelBooking(show, ticketNumber)).thenReturn(ticket);
        Ticket canceledTicket = buyerService.cancel(ticketNumber, phoneNumber);
        assertEquals(ticket, canceledTicket);
    }

    @Test(expected = Exception.class)
    public void testCancelAfterCancellationWindow() throws Exception {
        Show mockShow = new Show(1L, new ArrayList<>(), 60);
        Ticket mockTicket = new Ticket(123L, "1234567890", new ArrayList<>(), new Date(System.currentTimeMillis() - 61 * 60 * 1000), mockShow);
        when(ticketService.getTicketById(123L)).thenReturn(mockTicket);
        buyerService.cancel(123L, "1234567890");
    }

    private String getOutput(Runnable runnable) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        runnable.run();
        System.setOut(originalOut);
        return outContent.toString().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n").trim();
    }
}
