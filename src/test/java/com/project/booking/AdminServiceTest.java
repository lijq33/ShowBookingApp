package com.project.booking;

import com.project.booking.model.Seat;
import com.project.booking.model.Show;
import com.project.booking.model.Ticket;
import com.project.booking.service.AdminService;
import com.project.booking.service.ShowService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private ShowService showService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetupShowSuccess() throws Exception {
        Long showNumber = 1L;
        int numOfRows = 5;
        int numOfSeats = 5;
        int cancellationWindow = 10;

        when(showService.getShowById(showNumber)).thenReturn(null);

        Show createdShow = adminService.setup(showNumber, numOfRows, numOfSeats, cancellationWindow);

        assertNotNull(createdShow);
        assertEquals(showNumber, createdShow.getId());
        assertEquals(numOfRows * numOfSeats, createdShow.getSeats().size());
        assertEquals(cancellationWindow, createdShow.getCancellationWindowMinutes());

        verify(showService, times(1)).setupShow(createdShow);
    }

    @Test(expected = Exception.class)
    public void testSetupShowShowAlreadyExists() throws Exception {
        Long showNumber = 1L;
        int numOfRows = 5;
        int numOfSeats = 5;
        int cancellationWindow = 10;

        when(showService.getShowById(showNumber)).thenReturn(new Show());

        // Expecting an exception to be thrown
        adminService.setup(showNumber, numOfRows, numOfSeats, cancellationWindow);
    }

    @Test(expected = Exception.class)
    public void testSetupShowInvalidNumOfSeats() throws Exception {
        Long showNumber = 1L;
        int numOfRows = 5;
        int numOfSeats = 11; // More than allowed
        int cancellationWindow = 10;

        // Expecting an exception to be thrown
        adminService.setup(showNumber, numOfRows, numOfSeats, cancellationWindow);
    }

    @Test(expected = Exception.class)
    public void testSetupShowInvalidNumOfRows() throws Exception {
        Long showNumber = 1L;
        int numOfRows = 27; // More than allowed
        int numOfSeats = 5;
        int cancellationWindow = 10;

        // Expecting an exception to be thrown
        adminService.setup(showNumber, numOfRows, numOfSeats, cancellationWindow);
    }

    @Test
    public void testViewShow() {
        Long showNumber = 1L;

        Show mockShow = new Show();
        mockShow.setId(showNumber);

        Ticket ticket1 = new Ticket();
        ticket1.setTicketNumber(1l);
        ticket1.setPhoneNumber("123456789");
        List<Seat> seats1 = new ArrayList<>();
        seats1.add(new Seat("A1", false));
        seats1.add(new Seat("B2", false));
        ticket1.setSeats(seats1);

        Ticket ticket2 = new Ticket();
        ticket2.setTicketNumber(2l);
        ticket2.setPhoneNumber("987654321");
        List<Seat> seats2 = new ArrayList<>();
        seats2.add(new Seat("C3", false));
        ticket2.setSeats(seats2);

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket1);
        tickets.add(ticket2);

        mockShow.setTickets(tickets);

        when(showService.getShowById(showNumber)).thenReturn(mockShow);

        String capturedOutput = getOutput(() -> adminService.view(showNumber));

        String expectedOutput = "Show Number: " + showNumber + "\n" +
                "Ticket Number: 1\n" +
                "Buyer Phone Number: 123456789\n" +
                "Seats booked: A1, B2\n" +
                "Ticket Number: 2\n" +
                "Buyer Phone Number: 987654321\n" +
                "Seats booked: C3";

        assertEquals(expectedOutput, capturedOutput);
    }

    @Test
    public void testViewShowShowNotFound() {
        Long showNumber = 1L;
        when(showService.getShowById(showNumber)).thenReturn(null);
        String capturedOutput = getOutput(() -> adminService.view(showNumber));
        String expectedOutput = "Show " + showNumber + " does not exist!";
        assertEquals(expectedOutput, capturedOutput);
    }

    @Test
    public void testViewShowNoTickets() {
        Long showNumber = 1L;
        Show mockShow = new Show();
        mockShow.setId(showNumber);
        when(showService.getShowById(showNumber)).thenReturn(mockShow);

        String capturedOutput = getOutput(() -> adminService.view(showNumber));
        String expectedOutput = "Show Number: " + showNumber + "\n" +
        "No Ticket has been booked for this show";
        assertEquals(expectedOutput, capturedOutput);
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
