package com.epam.training.ticketservice.repositry;

import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    public List<Booking> findBookingsByAccount_Username(String customerUsername);

    public List<Booking> findBookingsByScreening(Screening screening);
}
