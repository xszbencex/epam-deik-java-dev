package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.repositry.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getBookingsByUsername(String username) {
        return this.bookingRepository.findBookingsByAccount_Username(username);
    }

    public Booking createBooking(Booking booking) {
        return this.bookingRepository.save(booking);
    }


}
