package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.model.config.ScreeningId;
import com.epam.training.ticketservice.model.config.Seat;
import com.epam.training.ticketservice.repositry.init.PriceRepository;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PriceService {

    @Value("${ticket-service.price.base}")
    private int basePrice;

    private final PriceRepository priceRepository;
    private final RoomService roomService;
    private final MovieService movieService;
    private final ScreeningService screeningService;

    public PriceService(PriceRepository priceRepository,
                        RoomService roomService,
                        MovieService movieService,
                        ScreeningService screeningService) {
        this.priceRepository = priceRepository;
        this.roomService = roomService;
        this.movieService = movieService;
        this.screeningService = screeningService;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public Optional<PriceComponent> getPriceComponentById(String priceComponentName) {
        return this.priceRepository.findById(priceComponentName);
    }

    public void createPriceComponent(PriceComponent priceComponent) {
        this.priceRepository.save(priceComponent);
    }

    public void attachPriceComponentToRoom(String priceComponentName, String roomName) {
        PriceComponent priceComponent = this.getPriceComponentById(priceComponentName).orElseThrow(() ->
                new NoSuchItemException("There is no price component with name: " + priceComponentName));
        Room room = this.roomService.getRoomById(roomName).orElseThrow(() ->
                new NoSuchItemException("There is no room with name: " + roomName));
        priceComponent.setRoom(room);
        this.updateExistingPriceComponent(priceComponent);
    }

    public void attachPriceComponentToMovie(String priceComponentName, String movieName) {
        PriceComponent priceComponent = this.getPriceComponentById(priceComponentName).orElseThrow(() ->
                new NoSuchItemException("There is no price component with name: " + priceComponentName));
        Movie movie = this.movieService.getMovieById(movieName).orElseThrow(() ->
                new NoSuchItemException("There is no movie with name: " + movieName));
        priceComponent.setMovie(movie);
        this.updateExistingPriceComponent(priceComponent);
    }

    public void attachPriceComponentToScreening(String priceComponentName,
                                                String movieName,
                                                String roomName,
                                                String startingAt) {
        PriceComponent priceComponent = this.getPriceComponentById(priceComponentName).orElseThrow(() ->
                new NoSuchItemException("There is no price component with name: " + priceComponentName));
        Screening screening = this.screeningService.getScreeningById(movieName, roomName,
                LocalDateTime.parse(startingAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).orElseThrow(() ->
                new NoSuchItemException("There is no room screening with the given ids"));
        priceComponent.setScreening(screening);
        this.updateExistingPriceComponent(priceComponent);
    }

    public Long calculatePriceForBooking(String movieName, String roomName, String startingAt, Integer numberOfSeats) {
        final LocalDateTime parsedStartingAt = LocalDateTime.parse(startingAt,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return this.calculatePriceForBooking(
                this.screeningService.getScreeningById(movieName, roomName, parsedStartingAt).get(), numberOfSeats);
    }

    public Long calculatePriceForBooking(Screening screening, Integer numberOfSeats) {
        List<PriceComponent> priceComponents = new ArrayList<>();
        priceComponents.addAll(this.priceRepository.findPriceComponentsByRoom_Name(
                screening.getId().getRoom().getName()));
        priceComponents.addAll(this.priceRepository.findPriceComponentsByMovie_Name(
                screening.getId().getMovie().getName()));
        priceComponents.addAll(this.priceRepository.findPriceComponentsByScreening(screening));
        int modifiedPrice = this.getBasePrice();
        modifiedPrice += priceComponents.stream().mapToInt(PriceComponent::getAmount).sum();
        return (long) numberOfSeats * modifiedPrice;
    }

    private void updateExistingPriceComponent(PriceComponent priceComponent) {
        this.priceRepository.save(priceComponent);
    }
}
