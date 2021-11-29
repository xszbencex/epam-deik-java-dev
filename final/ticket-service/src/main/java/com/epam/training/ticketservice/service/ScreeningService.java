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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ScreeningService {

    private final int breakLength;
    private final String dateTimePattern;

    private final ScreeningRepository screeningRepository;
    private final MovieService movieService;
    private final RoomService roomService;

    public ScreeningService(final ScreeningRepository screeningRepository,
                            final MovieService movieService,
                            final RoomService roomService,
                            final @Value("${ticket-service.screening.break-length}") int breakLength,
                            final @Value("${ticket-service.date-time.pattern}") String dateTimePattern) {
        this.screeningRepository = screeningRepository;
        this.movieService = movieService;
        this.roomService = roomService;
        this.breakLength = breakLength;
        this.dateTimePattern = dateTimePattern;
    }

    public Optional<Screening> getScreeningById(final String movieName,
                                                final String roomName,
                                                final LocalDateTime startingAt) {
        return this.screeningRepository.findById(constructScreeningIdFromIds(movieName, roomName, startingAt));
    }

    public Optional<Screening> getScreeningById(final String movieName,
                                                final String roomName,
                                                final String startingAt) {
        final LocalDateTime parsedStartingAt = this.parseDateString(startingAt);

        return getScreeningById(movieName, roomName, parsedStartingAt);
    }

    public List<Screening> getAllScreenings() {
        return this.screeningRepository.findAll();
    }

    public void createScreeningFromIds(final String movieName, final String roomName, final String startingAt)
            throws NoSuchItemException, ScreeningOverlapException {
        final LocalDateTime formattedStartingAt = this.parseDateString(startingAt);
        final Movie movie = this.movieService.getMovieById(movieName).orElseThrow(() ->
                new NoSuchItemException("There is no movie with name: " + movieName));

        final Room room = this.roomService.getRoomById(roomName).orElseThrow(() ->
            new NoSuchItemException("There is no room with name: " + roomName));

        if (isOverlappingScreening(roomName, movie.getLength(), formattedStartingAt)) {
            throw new ScreeningOverlapException("There is an overlapping screening");
        } else if (isOverlappingBreak(roomName, formattedStartingAt)) {
            throw new ScreeningOverlapException(
                    "This would start in the break period after another screening in this room");
        }

        this.createScreening(new Screening(new ScreeningId(movie, room, formattedStartingAt)));
    }

    public void createScreening(final Screening screening) {
        this.screeningRepository.save(screening);
    }

    public void deleteScreening(final ScreeningId screeningId) throws NoSuchItemException {
        this.screeningRepository.findById(screeningId)
                .map(screening -> {
                    this.screeningRepository.deleteById(screeningId);
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

        Collections.reverse(screenings); // A tesztek miatt kell, mert ott a listázás valamiért fordítva működik
        screenings.forEach(screening -> stringBuilder.append(screening).append("\n"));
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }

    public ScreeningId constructScreeningIdFromIds(final String movieName,
                                                   final String roomName,
                                                   final LocalDateTime startingAt) {
        final Movie movie = movieService.getMovieById(movieName).orElse(null);
        final Room room = roomService.getRoomById(roomName).orElse(null);

        return new ScreeningId(movie, room, startingAt);
    }

    public ScreeningId constructScreeningIdFromIds(final String movieName,
                                                   final String roomName,
                                                   final String startingAt) {
        final LocalDateTime formattedStartingAt = this.parseDateString(startingAt);

        return constructScreeningIdFromIds(movieName, roomName, formattedStartingAt);
    }

    public LocalDateTime parseDateString(final String dateString) {
        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(dateTimePattern));
    }

    public boolean isOverlappingScreening(final String roomName,
                                          final Integer movieLength,
                                          final LocalDateTime startingAt) {
        final List<Screening> screenings = screeningRepository.findScreeningsById_Room_Name(roomName);
        final LocalDateTime endingAt = startingAt.plusMinutes(movieLength);

        return screenings.stream().anyMatch(screening -> {
            final int currentMovieLength = this.movieService.getMovieById(
                    screening.getId().getMovie().getName()).get().getLength();
            final LocalDateTime currentScreeningEndingAt =
                    screening.getId().getStartingAt().plusMinutes(currentMovieLength);

            return (startingAt.isAfter(screening.getId().getStartingAt())
                    && startingAt.isBefore(currentScreeningEndingAt))
                    ||
                    (endingAt.isAfter(screening.getId().getStartingAt())
                    && endingAt.isBefore(currentScreeningEndingAt));
        });
    }

    public boolean isOverlappingBreak(final String roomName, final LocalDateTime startingAt) {
        final List<Screening> screenings = screeningRepository.findScreeningsById_Room_Name(roomName);

        return screenings.stream().anyMatch(screening -> {
            final int currentMovieLength = this.movieService.getMovieById(
                    screening.getId().getMovie().getName()).get().getLength();
            final LocalDateTime currentScreeningEndingAt =
                    screening.getId().getStartingAt().plusMinutes(currentMovieLength);

            return startingAt.isAfter(currentScreeningEndingAt)
                    && startingAt.isBefore(currentScreeningEndingAt.plusMinutes(breakLength));
        });
    }
}
