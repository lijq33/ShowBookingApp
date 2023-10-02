package com.project.booking.service;

import com.project.booking.model.Seat;
import com.project.booking.model.Show;
import com.project.booking.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdminService {
    @Autowired
    private ShowService showService;

    public Show setup(Long showNumber, int numOfRows, int numOfSeats, int cancellationWindow) throws Exception {
        // Show number validation
        if (showService.getShowById(showNumber) != null) {
            throw new Exception("Show " + showNumber + " already exists.");
        }
        // Seat validation
        if (numOfSeats > 10) {
            throw new Exception("Number of seats must be 10 or less.");
        }
        if (numOfRows > 26) {
            throw new Exception("Number of rows must be 26 or less.");
        }

        ArrayList<Seat> seats = new ArrayList<>();
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 1; j <= numOfSeats; j++){
                String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                seats.add(new Seat(alphabets.charAt(i) + Integer.toString(j), false));
            }
        }
        Show show = new Show(showNumber, seats, cancellationWindow);

        return showService.setupShow(show);
    }

    public void view(Long showNumber){
        Show show = showService.getShowById(showNumber);

        if (show == null) {
            System.out.println("Show " + showNumber + " does not exist!");
            return;
        }

        List<Ticket> tickets = show.getTickets();
        System.out.println("Show Number: " + show.getId());

        if (tickets.isEmpty()) {
            System.out.println("No Ticket has been booked for this show");
            return;
        }

        for (Ticket ticket : tickets) {
            System.out.println("Ticket Number: " + ticket.getTicketNumber());
            System.out.println("Buyer Phone Number: " + ticket.getPhoneNumber());

            System.out.println("Seats booked: ");

            StringBuilder result = new StringBuilder();
            for (Seat seat : ticket.getSeats()) {
                result.append(seat.getSeatNumber()).append(", ");
            }
            if (result.length() > 0) {
                result.setLength(result.length() - 2);
            }
            System.out.println(result);
        }
    }

}
