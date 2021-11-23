package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.repositry.RoomRepository;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Optional<Room> getRoomById(String name) {
        return this.roomRepository.findById(name);
    }

    public List<Room> getAllRooms() {
        return this.roomRepository.findAll();
    }

    public void createRoom(String name, Integer rowCount, Integer columnCount) {
        this.roomRepository.save(new Room(name, rowCount, columnCount));
    }

    public void updateRoom(String name, Integer rowCount, Integer columnCount) throws NoSuchItemException {
        this.roomRepository.findById(name)
                .map(room -> this.roomRepository.save(new Room(name, rowCount, columnCount)))
                .orElseThrow(() -> new NoSuchItemException("There is no room with name: " + name));
    }

    public void deleteRoom(String name) throws NoSuchItemException {
        this.roomRepository.findById(name)
                .map(room -> {
                    this.roomRepository.deleteById(name);
                    return room;
                })
                .orElseThrow(() -> new NoSuchItemException("There is no room with name: " + name));
    }
}
