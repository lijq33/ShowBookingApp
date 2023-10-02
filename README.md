# Show Booking App

# About this application
This is a simple Springboot CLI application that allows admin to setup shows and buyers to make bookings.

## Assumptions made
1. Buyer cannot book seats that are already occupied
2. Cancelling a ticket will free the booked seats for other buyers
3. Buyer cannot make bookings for shows that don't exist
4. Phone numbers are not strictly enforce (can contain anything)
5. Booking of seats in a single command will have the same ticket number

## Sample commands:

**Admin**
1. Setup shows: `Setup 1 5 10 2`
2. View shows and bookings: `View 1`

**Buyer**
1. View available seats for a shows: `Availability 1`
2. Book seats for a show: `Book 1 1234567890 A1,A2,A3`
3. Cancel booking for a show: `Cancel 1 1234567890`

## How to use this application

1. Run `ShowBookingApplication`
2. Interact with the CLI with your command inputs
