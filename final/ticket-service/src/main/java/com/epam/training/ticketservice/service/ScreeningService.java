package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.repositry.ScreeningRepository;
import org.springframework.stereotype.Service;

@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;

    public ScreeningService(ScreeningRepository screeningRepository) {
        this.screeningRepository = screeningRepository;
    }
}
