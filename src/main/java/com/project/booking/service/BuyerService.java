package com.project.booking.service;

import com.project.booking.model.Seat;
import com.project.booking.model.Show;
import com.project.booking.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

// BuyerService.java
@Service
public class BuyerService {

    @Autowired
    private ShowService showService;
    @Autowired
    private TicketService ticketService;

    public Map<String , Seat> getAvailableSeats(long showNumber) throws Exception {
        Show show = showService.getShowById(showNumber);
        if (show == null) {
            throw new Exception("no such show");
        }

        return show.getSeats()
                .stream()
                .filter(s -> !s.isOccupied())
                .collect(Collectors.toMap(Seat::getSeatNumber, seat -> seat));
    }

    public void listAvailability(long showNumber) throws Exception {
        Map<String, Seat> availableSeats = getAvailableSeats(showNumber);

        if (availableSeats.isEmpty()) {
            System.out.println("There are no available seats");
        } else {
            System.out.println("These seats are available:");

            StringBuilder result = new StringBuilder();
            for (Seat seat : availableSeats.values()) {
                result.append(seat.getSeatNumber()).append(", ");
            }
            if (result.length() > 0) {
                result.setLength(result.length() - 2);
            }

            System.out.println(result);
        }
    }

    public Ticket book(long showNumber, String phoneNumber, String seats) throws Exception {
        Show show = showService.getShowById(showNumber);

        if (show == null) {
            throw new Exception("no such show");
        }

        String[] seatArray = seats.split(",");
        Map<String, Seat> availableSeats = getAvailableSeats(showNumber);
        List<Seat> seatsToBook = new ArrayList<>();

        for (String value : seatArray) {
            if (!availableSeats.containsKey(value)) {
                throw new Exception("Seat: " + value + " is not available.");
            }
            seatsToBook.add(availableSeats.get(value));
        }

        seatsToBook.forEach(s -> s.setOccupied(true));

        return showService.bookTicket(show, phoneNumber, seatsToBook);
    }

    public Ticket cancel(Long ticketNumber, String phoneNumber) throws Exception {
        Ticket ticket = ticketService.getTicketById(ticketNumber);

        if (ticket == null) {
            throw new Exception("no such ticket");
        }
        if (!ticket.getPhoneNumber().equals(phoneNumber)) {
            throw new Exception("Wrong phone number booking.");
        }

        long minutesDiff = (new Date().getTime() - ticket.getBookingTime().getTime()) / (60 * 1000);

        Show show = ticket.getShow();

        if (minutesDiff > show.getCancellationWindowMinutes()) {
            throw new Exception("You cannot cancel the booking as you have exceeded the cancellation window.");
        }

        return showService.cancelBooking(show, ticketNumber);
    }

}
