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

    public void createRoom(Room room) {
        this.roomRepository.save(room);
    }

    public void updateRoom(Room room) throws NoSuchItemException {
        this.roomRepository.findById(room.getName())
                .map(currentRoom -> this.roomRepository.save(room))
                .orElseThrow(() -> new NoSuchItemException("There is no room with name: " + room.getName()));
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
