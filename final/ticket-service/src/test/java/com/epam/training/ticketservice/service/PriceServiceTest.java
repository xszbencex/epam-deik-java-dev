package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.model.config.ScreeningId;
import com.epam.training.ticketservice.repositry.PriceRepository;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class PriceServiceTest {

    private final static int basePrice = 1500;
    private final static String dateTimePattern = "yyyy-MM-dd HH:mm";

    private final static Room room = new Room("Room1", 10, 10);
    private final static Movie movie = new Movie("Movie1", "action", 100);
    private final static String startingAt = "2000-12-13 10:10";
    private final static LocalDateTime formattedStartingAt = LocalDateTime.parse(startingAt, DateTimeFormatter.ofPattern(dateTimePattern));
    private final static Screening screening = new Screening(new ScreeningId(movie, room, formattedStartingAt));
    private final static PriceComponent priceComponent = new PriceComponent("discount", -200);
    private final static int numberOfSeats = 2;

    private PriceService underTest;

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private RoomService roomService;

    @Mock
    private MovieService movieService;

    @Mock
    private ScreeningService screeningService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new PriceService(priceRepository, roomService, movieService, screeningService, basePrice);
    }

    @Test
    void testGetPriceComponentByIdShouldCallRepositoryFindById() {
        // Given + When
        underTest.getPriceComponentById(priceComponent.getName());

        // Then
        verify(priceRepository).findById(priceComponent.getName());
        verifyNoMoreInteractions(priceRepository);
    }

    @Test
    void testCreatePriceComponentShouldCallRepositorySave() {
        // Given + When
        underTest.createPriceComponent(priceComponent);

        // Then
        verify(priceRepository).save(priceComponent);
        verifyNoMoreInteractions(priceRepository);
    }

    @Test
    void testAttachPriceComponentToRoomShouldThrowExceptionWhenPriceComponentDoesNotExit() {
        // Given
        final PriceService priceService = spy(underTest);
        given(priceService.getPriceComponentById(priceComponent.getName())).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () -> priceService.attachPriceComponentToRoom(priceComponent.getName(), room.getName()));
    }

    @Test
    void testAttachPriceComponentToRoomShouldThrowExceptionWhenRoomDoesNotExit() {
        // Given
        final PriceService priceService = spy(underTest);
        given(priceService.getPriceComponentById(priceComponent.getName())).willReturn(Optional.of(priceComponent));
        given(roomService.getRoomById(room.getName())).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () -> priceService.attachPriceComponentToRoom(priceComponent.getName(), room.getName()));
    }

    @Test
    void testAttachPriceComponentToRoomShouldCallUpdateWhenEverythingIsOk() {
        // Given
        final PriceService priceService = spy(underTest);
        given(priceService.getPriceComponentById(priceComponent.getName())).willReturn(Optional.of(priceComponent));
        given(roomService.getRoomById(room.getName())).willReturn(Optional.of(room));

        // When
        priceService.attachPriceComponentToRoom(priceComponent.getName(), room.getName());

        // Then
        verify(priceService).updateExistingPriceComponent(priceComponent);
        assertEquals(room, priceComponent.getRoom());
    }

    @Test
    void testAttachPriceComponentToMovieShouldThrowExceptionWhenPriceComponentDoesNotExit() {
        // Given
        final PriceService priceService = spy(underTest);
        given(priceService.getPriceComponentById(priceComponent.getName())).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () -> priceService.attachPriceComponentToMovie(priceComponent.getName(), movie.getName()));
    }

    @Test
    void testAttachPriceComponentToMovieShouldThrowExceptionWhenMovieDoesNotExit() {
        // Given
        final PriceService priceService = spy(underTest);
        given(priceService.getPriceComponentById(priceComponent.getName())).willReturn(Optional.of(priceComponent));
        given(movieService.getMovieById(movie.getName())).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () -> priceService.attachPriceComponentToMovie(priceComponent.getName(), movie.getName()));
    }

    @Test
    void testAttachPriceComponentToMovieShouldCallUpdateWhenEverythingIsOk() {
        // Given
        final PriceService priceService = spy(underTest);
        given(priceService.getPriceComponentById(priceComponent.getName())).willReturn(Optional.of(priceComponent));
        given(movieService.getMovieById(movie.getName())).willReturn(Optional.of(movie));

        // When
        priceService.attachPriceComponentToMovie(priceComponent.getName(), movie.getName());

        // Then
        verify(priceService).updateExistingPriceComponent(priceComponent);
        assertEquals(movie, priceComponent.getMovie());
    }

    @Test
    void testAttachPriceComponentToScreeningShouldThrowExceptionWhenPriceComponentDoesNotExit() {
        // Given
        final PriceService priceService = spy(underTest);
        given(priceService.getPriceComponentById(priceComponent.getName())).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () ->
                priceService.attachPriceComponentToScreening(priceComponent.getName(), movie.getName(), room.getName(), startingAt));
    }

    @Test
    void testAttachPriceComponentToScreeningShouldThrowExceptionWhenScreeningDoesNotExit() {
        // Given
        final PriceService priceService = spy(underTest);
        given(priceService.getPriceComponentById(priceComponent.getName())).willReturn(Optional.of(priceComponent));
        given(screeningService.getScreeningById(movie.getName(), room.getName(), startingAt)).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () ->
                priceService.attachPriceComponentToScreening(priceComponent.getName(), movie.getName(), room.getName(), startingAt));
    }

    @Test
    void testAttachPriceComponentToScreeningShouldCallUpdateWhenEverythingIsOk() {
        // Given
        final PriceService priceService = spy(underTest);
        given(priceService.getPriceComponentById(priceComponent.getName())).willReturn(Optional.of(priceComponent));
        given(screeningService.getScreeningById(movie.getName(), room.getName(), startingAt)).willReturn(Optional.of(screening));

        // When
        priceService.attachPriceComponentToScreening(priceComponent.getName(), movie.getName(), room.getName(), startingAt);

        // Then
        verify(priceService).updateExistingPriceComponent(priceComponent);
        assertEquals(screening, priceComponent.getScreening());
    }

    @Test
    void testCalculatePriceForBookingShouldCallOtherWhenIdsAreGiven() {
        // Given
        final PriceService priceService = spy(underTest);
        given(screeningService.getScreeningById(movie.getName(), room.getName(), startingAt)).willReturn(Optional.of(screening));

        // When
        priceService.calculatePriceForBooking(movie.getName(), room.getName(), startingAt, numberOfSeats);

        // Then
        verify(priceService).calculatePriceForBooking(screening, numberOfSeats);
    }

    @Test
    void testCalculatePriceForBookingShouldReturnModifiedPriceWhenRoomHasPriceComponent() {
        // Given
        final Long expected = (long) (basePrice + priceComponent.getAmount()) * numberOfSeats;
        given(priceRepository.findPriceComponentsByRoom_Name(room.getName())).willReturn(List.of(priceComponent));

        // When
        final Long actual = underTest.calculatePriceForBooking(screening, numberOfSeats);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testCalculatePriceForBookingShouldReturnModifiedPriceWhenMovieHasPriceComponent() {
        // Given
        final Long expected = (long) (basePrice + priceComponent.getAmount()) * numberOfSeats;
        given(priceRepository.findPriceComponentsByMovie_Name(movie.getName())).willReturn(List.of(priceComponent));

        // When
        final Long actual = underTest.calculatePriceForBooking(screening, numberOfSeats);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testCalculatePriceForBookingShouldReturnModifiedPriceWhenScreeningHasPriceComponent() {
        // Given
        final Long expected = (long) (basePrice + priceComponent.getAmount()) * numberOfSeats;
        given(priceRepository.findPriceComponentsByScreening(screening)).willReturn(List.of(priceComponent));

        // When
        final Long actual = underTest.calculatePriceForBooking(screening, numberOfSeats);

        // Then
        assertEquals(expected, actual);
    }
}
