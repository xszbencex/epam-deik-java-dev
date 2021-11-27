package com.epam.training.ticketservice.repositry.init;

import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<PriceComponent, String> {

    List<PriceComponent> findPriceComponentsByRoom_Name(String roomName);

    List<PriceComponent> findPriceComponentsByMovie_Name(String movieName);

    List<PriceComponent> findPriceComponentsByScreening(Screening screening);
}
