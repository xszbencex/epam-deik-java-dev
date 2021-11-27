package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.model.config.ScreeningId;
import com.epam.training.ticketservice.repositry.ScreeningRepository;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import com.epam.training.ticketservice.service.exception.ScreeningOverlapException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ScreeningService {

    @Value("${ticket-service.screening.break-length}")
    private int breakLength;

    @Value("${ticket-service.date-time.pattern}")
    private String dateTimePattern;

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
        return this.screeningRepository.findById(constructScreeningIdFromIds(movieName, roomName, startingAt));
    }

    public List<Screening> getAllScreenings() {
        return this.screeningRepository.findAll();
    }

    public void createScreeningFromIds(String movieName, String roomName, LocalDateTime startingAt)
            throws NoSuchItemException, ScreeningOverlapException {
        final Movie movie = this.movieService.getMovieById(movieName).orElseThrow(() ->
                new NoSuchItemException("There is no movie with name: " + movieName));
        final Room room = this.roomService.getRoomById(roomName).orElseThrow(() ->
            new NoSuchItemException("There is no room with name: " + roomName));
        if (isOverlappingScreening(roomName, movie.getLength(), startingAt)) {
            throw new ScreeningOverlapException("There is an overlapping screening");
        } else if (isOverlappingBreak(roomName, startingAt)) {
            throw new ScreeningOverlapException(
                    "This would start in the break period after another screening in this room");
        }
        this.createScreening(new Screening(constructScreeningIdFromIds(movieName, roomName, startingAt)));
    }

    public void createScreening(Screening screening) {
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
                                screeningId.getMovie().getName(),
                                screeningId.getRoom().getName(),
                                screeningId.getStartingAt().format(DateTimeFormatter.ofPattern(dateTimePattern)))));
    }

    public String formattedScreeningList(List<Screening> screenings) {
        StringBuilder stringBuilder = new StringBuilder();
        screenings.forEach(screening -> stringBuilder.append(screening).append("\n"));
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public ScreeningId constructScreeningIdFromIds(String movieName, String roomName, LocalDateTime startingAt) {
        final Movie movie = movieService.getMovieById(movieName).orElse(null);
        final Room room = roomService.getRoomById(roomName).orElse(null);
        return new ScreeningId(movie, room, startingAt);
    }

    private boolean isOverlappingScreening(String roomName, Integer movieLength, LocalDateTime startingAt) {
        final List<Screening> screenings = screeningRepository.findScreeningsById_Room_Name(roomName);
        System.out.println(screenings);
        final LocalDateTime endingAt = startingAt.plusMinutes(movieLength);
        return screenings.stream().anyMatch(screening -> {
            LocalDateTime currentScreeningEndingAt = screening.getId().getStartingAt().plusMinutes(
                    this.movieService.getMovieById(screening.getId().getMovie().getName()).get().getLength()
            );
            return (startingAt.isAfter(screening.getId().getStartingAt())
                    && startingAt.isBefore(currentScreeningEndingAt))
                    ||
                    (endingAt.isAfter(screening.getId().getStartingAt())
                    && endingAt.isBefore(currentScreeningEndingAt));
        });
    }

    private boolean isOverlappingBreak(String roomName, LocalDateTime startingAt) {
        final List<Screening> screenings = screeningRepository.findScreeningsById_Room_Name(roomName);
        return screenings.stream().anyMatch(screening -> {
            LocalDateTime currentScreeningEndingAt = screening.getId().getStartingAt().plusMinutes(
                    this.movieService.getMovieById(screening.getId().getMovie().getName()).get().getLength()
            );
            return startingAt.isAfter(currentScreeningEndingAt)
                    && startingAt.isBefore(currentScreeningEndingAt.plusMinutes(breakLength));
        });
    }
}
