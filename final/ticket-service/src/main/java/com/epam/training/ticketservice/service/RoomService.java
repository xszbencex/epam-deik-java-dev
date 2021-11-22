package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.repositry.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
}
