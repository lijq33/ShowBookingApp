package com.project.booking;

import com.project.booking.service.AdminService;
import com.project.booking.service.BuyerService;
import com.project.booking.model.Show;
import com.project.booking.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = "com.project.booking")
public class ShowBookingApplication implements CommandLineRunner {

	@Autowired
	BuyerService buyerService;
	@Autowired
	AdminService adminService;

	public static void main(String[] args) {
		SpringApplication.run(ShowBookingApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Welcome to Show Booking App");
		System.out.println("Please enter your command!");

		// Initialize the scanner for user input
		Scanner scanner = new Scanner(System.in);
		do {
			try {
				String[] commandline = scanner.nextLine().split(" ");
				switch (commandline[0].toLowerCase()) {
					case "setup": {
						if (commandline.length != 5) {
							System.out.println("Invalid params for Setup command");
							break;
						}
						Show show = adminService.setup(Long.parseLong(commandline[1]),
								Integer.parseInt(commandline[2]), Integer.parseInt(commandline[3]),
								Integer.parseInt(commandline[4]));
						System.out.println("Show " + show.getId() + " set up successfully");
						break;
					}
					case "view": {
						if (commandline.length != 2) {
							System.out.println("Invalid params for View command");
							break;
						}
						adminService.view(Long.parseLong(commandline[1]));
						break;
					}
					case "availability": {
						if (commandline.length != 2) {
							System.out.println("Invalid params for Availability command");
							break;
						}
						buyerService.listAvailability(Long.parseLong(commandline[1]));
						break;
					}
					case "book": {
						if (commandline.length != 4) {
							System.out.println("Invalid params for Book command");
							break;
						}
						Ticket ticket = buyerService.book(Long.parseLong(commandline[1]), commandline[2],
								commandline[3]);
						System.out.println("Your ticket number is: " + ticket.getTicketNumber());
						break;
					}
					case "cancel": {
						if (commandline.length != 3) {
							System.out.println("Invalid params for Cancel command");
							break;
						}
						Ticket cancelledTicket = buyerService.cancel(Long.parseLong(commandline[1]),
								commandline[2]);
						System.out.println("Booking cancelled for ticket number: " + cancelledTicket.getTicketNumber());
						break;
					}
					case "exit": {
						System.exit(0);
						break;
					}
					default:
						System.out.println("Unknown command");
						break;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} while (scanner.hasNextLine());
		scanner.close();
	}
}
