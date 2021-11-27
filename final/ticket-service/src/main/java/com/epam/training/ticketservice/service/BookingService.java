package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.model.config.Seat;
import com.epam.training.ticketservice.model.config.SeatListConverter;
import com.epam.training.ticketservice.repositry.BookingRepository;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import com.epam.training.ticketservice.service.exception.NoSuchSeatException;
import com.epam.training.ticketservice.service.exception.SeatsTakenException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ScreeningService screeningService;
    private final RoomService roomService;

    public BookingService(BookingRepository bookingRepository,
                          ScreeningService screeningService,
                          RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.screeningService = screeningService;
        this.roomService = roomService;
    }

    public List<Booking> getBookingsByUsername(String username) {
        return this.bookingRepository.findBookingsByAccount_Username(username);
    }

    public void createBookingByIds(String movieName,
                                   String roomName,
                                   LocalDateTime startingAt,
                                   String seats,
                                   Account account) {
        final Screening screening = this.screeningService.getScreeningById(movieName, roomName, startingAt)
                .orElseThrow(() -> new NoSuchItemException("There is no screening with the given ids"));

        final List<Seat> seatList = seatsStringParser(seats);
        this.bookingRepository.findBookingsByScreening(screening).forEach(booking ->
                seatList.forEach(currentSeat ->
                        booking.getSeats().forEach(bookingSeat -> {
                            if (bookingSeat.equals(currentSeat)) {
                                throw new SeatsTakenException(String.format("Seat (%s) is already taken", currentSeat));
                            }
                        })
                )
        );

        this.roomService.getRoomById(roomName).ifPresent(room ->
                seatList.forEach(seat -> {
                    if ((seat.getColumnNum() > room.getColumnCount() || seat.getRowNum() > room.getRowCount())
                            || (seat.getColumnNum() < 0 || seat.getRowNum() < 0)) {
                        throw new NoSuchSeatException(String.format("Seat (%s) does not exist in this room", seat));
                    }
                })
        );

        this.createBooking(new Booking(screening, seatList, account, 1500L * seatList.size()));
    }

    public void createBooking(Booking booking) {
        this.bookingRepository.save(booking);
    }

    public String formattedBookingMessage(String seats) {
        final List<Seat> seatList = seatsStringParser(seats);
        StringBuilder stringBuilder = new StringBuilder();
        seatList.forEach(seat -> stringBuilder.append("(").append(seat).append("), "));
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        return String.format("Seats booked: %s; the price for this booking is %s HUF",
                stringBuilder, seatList.size() * 1500L);
    }

    private List<Seat> seatsStringParser(String seats) {
        SeatListConverter seatListConverter = new SeatListConverter();
        return seatListConverter.convertToEntityAttribute(seats);
    }
}
