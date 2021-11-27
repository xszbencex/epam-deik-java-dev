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

    public RoomService(final RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Optional<Room> getRoomById(final String name) {
        return this.roomRepository.findById(name);
    }

    public List<Room> getAllRooms() {
        return this.roomRepository.findAll();
    }

    public void createRoom(final Room room) {
        this.roomRepository.save(room);
    }

    public void updateRoom(final Room room) throws NoSuchItemException {
        this.roomRepository.findById(room.getName())
                .map(currentRoom -> this.roomRepository.save(room))
                .orElseThrow(() -> new NoSuchItemException("There is no room with name: " + room.getName()));
    }

    public void deleteRoom(final String name) throws NoSuchItemException {
        this.roomRepository.findById(name)
                .map(room -> {
                    this.roomRepository.deleteById(name);
                    return room;
                })
                .orElseThrow(() -> new NoSuchItemException("There is no room with name: " + name));
    }

    public String formattedRoomList(final List<Room> rooms) {
        StringBuilder stringBuilder = new StringBuilder();

        rooms.forEach(room -> stringBuilder.append(room.toString()).append('\n'));
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }
}
