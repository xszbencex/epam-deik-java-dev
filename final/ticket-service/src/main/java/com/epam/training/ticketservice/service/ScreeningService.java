package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.model.config.ScreeningId;
import com.epam.training.ticketservice.repositry.ScreeningRepository;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import com.epam.training.ticketservice.service.exception.ScreeningOverlapException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScreeningService {

    @Value("${ticket-service.screening.break-length}")
    private int breakLength;

    private final ScreeningRepository screeningRepository;
    private final MovieService movieService;
    private final RoomService roomService;

    public ScreeningService(ScreeningRepository screeningRepository,
                            MovieService movieService,
                            RoomService roomService) {
        this.screeningRepository = screeningRepository;
        this.movieService = movieService;
        this.roomService = roomService;
    }

    public Optional<Screening> getScreeningById(String movieName, String roomName, LocalDateTime startingAt) {
        return this.screeningRepository.findById(new ScreeningId(movieName, roomName, startingAt));
    }

    public List<Screening> getAllScreenings() {
        return this.screeningRepository.findAll();
    }

    public void createScreening(Screening screening) throws NoSuchItemException, ScreeningOverlapException {
        final Movie movie = this.movieService.getMovieById(screening.getMovieName()).orElseThrow(() ->
                new NoSuchItemException("There is no movie with name: " + screening.getMovieName()));
        if (this.roomService.getRoomById(screening.getRoomName()).isEmpty()) {
            throw new NoSuchItemException("There is no room with name: " + screening.getRoomName());
        } else if (isOverlappingScreening(screening.getRoomName(), movie.getLength(), screening.getStartingAt())) {
            throw new ScreeningOverlapException("There is an overlapping screening");
        } else if (isOverlappingBreak(screening.getRoomName(), screening.getStartingAt())) {
            throw new ScreeningOverlapException(
                    "This would start in the break period after another screening in this room");
        }
        this.screeningRepository.save(screening);
    }

    public void deleteScreening(ScreeningId screeningId) throws NoSuchItemException {
        this.screeningRepository.findById(screeningId)
                .map(screening -> {
                    this.screeningRepository.delete(screening);
                    return screening;
                })
                .orElseThrow(() -> new NoSuchItemException(
                        String.format("There is no screening with %s movie name, %s room name, starting at %s",
                                screeningId.getMovieName(),
                                screeningId.getRoomName(),
                                screeningId.getStartingAt().toString())));
    }

    private boolean isOverlappingScreening(String roomName, Integer movieLength, LocalDateTime startingAt) {
        final List<Screening> screenings = screeningRepository.findScreeningsByRoomName(roomName);
        final LocalDateTime endingAt = startingAt.plusMinutes(movieLength);
        return screenings.stream().anyMatch(screening -> {
            LocalDateTime currentScreeningEndingAt = screening.getStartingAt().plusMinutes(
                    this.movieService.getMovieById(screening.getMovieName()).get().getLength()
            );
            return (startingAt.isAfter(screening.getStartingAt()) && startingAt.isBefore(currentScreeningEndingAt))
                    || (endingAt.isAfter(screening.getStartingAt()) && endingAt.isBefore(currentScreeningEndingAt));
        });
    }

    private boolean isOverlappingBreak(String roomName, LocalDateTime startingAt) {
        final List<Screening> screenings = screeningRepository.findScreeningsByRoomName(roomName);
        return screenings.stream().anyMatch(screening -> {
            LocalDateTime currentScreeningEndingAt = screening.getStartingAt().plusMinutes(
                    this.movieService.getMovieById(screening.getMovieName()).get().getLength()
            );
            return startingAt.isAfter(currentScreeningEndingAt)
                    && startingAt.isBefore(currentScreeningEndingAt.plusMinutes(breakLength));
        });
    }
}
