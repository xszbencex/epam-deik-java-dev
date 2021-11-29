package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repositry.PriceRepository;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PriceService {

    private int basePrice;

    private final PriceRepository priceRepository;
    private final RoomService roomService;
    private final MovieService movieService;
    private final ScreeningService screeningService;

    public PriceService(final PriceRepository priceRepository,
                        final RoomService roomService,
                        final MovieService movieService,
                        final ScreeningService screeningService,
                        final @Value("${ticket-service.price.base}") int basePrice) {
        this.priceRepository = priceRepository;
        this.roomService = roomService;
        this.movieService = movieService;
        this.screeningService = screeningService;
        this.basePrice = basePrice;
    }

    public void setBasePrice(final int basePrice) {
        this.basePrice = basePrice;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public Optional<PriceComponent> getPriceComponentById(final String priceComponentName) {
        return this.priceRepository.findById(priceComponentName);
    }

    public void createPriceComponent(final PriceComponent priceComponent) {
        this.priceRepository.save(priceComponent);
    }

    public void attachPriceComponentToRoom(final String priceComponentName, final String roomName)
            throws NoSuchItemException {
        PriceComponent priceComponent = this.getPriceComponentById(priceComponentName).orElseThrow(() ->
                new NoSuchItemException("There is no price component with name: " + priceComponentName));
        final Room room = this.roomService.getRoomById(roomName).orElseThrow(() ->
                new NoSuchItemException("There is no room with name: " + roomName));
        priceComponent.setRoom(room);

        this.updateExistingPriceComponent(priceComponent);
    }

    public void attachPriceComponentToMovie(final String priceComponentName, final String movieName)
            throws NoSuchItemException {
        PriceComponent priceComponent = this.getPriceComponentById(priceComponentName).orElseThrow(() ->
                new NoSuchItemException("There is no price component with name: " + priceComponentName));
        final Movie movie = this.movieService.getMovieById(movieName).orElseThrow(() ->
                new NoSuchItemException("There is no movie with name: " + movieName));
        priceComponent.setMovie(movie);

        this.updateExistingPriceComponent(priceComponent);
    }

    public void attachPriceComponentToScreening(final String priceComponentName,
                                                final String movieName,
                                                final String roomName,
                                                final String startingAt) throws NoSuchItemException {
        PriceComponent priceComponent = this.getPriceComponentById(priceComponentName).orElseThrow(() ->
                new NoSuchItemException("There is no price component with name: " + priceComponentName));
        final Screening screening = this.screeningService.getScreeningById(movieName, roomName, startingAt)
                .orElseThrow(() -> new NoSuchItemException("There is no screening with the given ids"));
        priceComponent.setScreening(screening);

        this.updateExistingPriceComponent(priceComponent);
    }

    public Long calculatePriceForBooking(final String movieName,
                                         final String roomName,
                                         final String startingAt,
                                         final Integer numberOfSeats) {
        final Screening screening = this.screeningService.getScreeningById(movieName, roomName, startingAt).get();

        return this.calculatePriceForBooking(screening, numberOfSeats);
    }

    public Long calculatePriceForBooking(final Screening screening, final Integer numberOfSeats) {
        List<PriceComponent> priceComponents = new ArrayList<>();

        priceComponents.addAll(this.priceRepository.findPriceComponentsByRoom_Name(
                screening.getId().getRoom().getName()));
        priceComponents.addAll(this.priceRepository.findPriceComponentsByMovie_Name(
                screening.getId().getMovie().getName()));
        priceComponents.addAll(this.priceRepository.findPriceComponentsByScreening(
                screening));

        int modifiedPrice = this.getBasePrice();
        modifiedPrice += priceComponents.stream().mapToInt(PriceComponent::getAmount).sum();

        return (long) numberOfSeats * modifiedPrice;
    }

    public void updateExistingPriceComponent(final PriceComponent priceComponent) {
        this.priceRepository.save(priceComponent);
    }
}
