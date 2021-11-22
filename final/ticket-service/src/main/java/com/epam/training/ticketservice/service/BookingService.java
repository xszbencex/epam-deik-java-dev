package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.repositry.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
}
