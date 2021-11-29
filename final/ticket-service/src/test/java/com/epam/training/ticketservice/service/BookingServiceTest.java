package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.model.config.ScreeningId;
import com.epam.training.ticketservice.model.config.Seat;
import com.epam.training.ticketservice.repositry.BookingRepository;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import com.epam.training.ticketservice.service.exception.NoSuchSeatException;
import com.epam.training.ticketservice.service.exception.SeatsTakenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class BookingServiceTest {

    private final static int basePrice = 1500;
    private final static String dateTimePattern = "yyyy-MM-dd HH:mm";

    private final static Room room = new Room("Room1", 10, 10);
    private final static Movie movie = new Movie("Movie1", "action", 100);
    private final static String startingAt = "2000-12-13 10:10";
    private final static LocalDateTime formattedStartingAt = LocalDateTime.parse(startingAt, DateTimeFormatter.ofPattern(dateTimePattern));
    private final static Screening screening = new Screening(new ScreeningId(movie, room, formattedStartingAt));
    private final static String seats = "1,2 3,4";
    private final static String notExistingSeats1 = "11,10 4,3";
    private final static String notExistingSeats2 = "-10,10 4,3";
    private final static List<Seat> seatsList = List.of(new Seat("1,2"), new Seat("3,4"));
    private final static List<Seat> notExistingSeatList1 = List.of(new Seat("11,10"), new Seat("4,3"));
    private final static List<Seat> notExistingSeatList2 = List.of(new Seat("-10,10"), new Seat("4,3"));
    private final static Account user = new Account("user", "user", false);
    private final static Long price = (long) seatsList.size() * basePrice;
    private final static Booking booking1 = new Booking(screening, seatsList, user, price);

    private BookingService underTest;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ScreeningService screeningService;

    @Mock
    private RoomService roomService;

    @Mock
    private PriceService priceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new BookingService(bookingRepository, screeningService, roomService, priceService);
    }

    @Test
    void testCreateScreeningShouldCallRepositorySave() {
        // Given + When
        underTest.createBooking(booking1);

        // Then
        verify(bookingRepository).save(booking1);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testCreateBookingByIdsShouldThrowNoSuchItemExceptionWhenScreeningIsNotValid() {
        // Given
        given(screeningService.getScreeningById(movie.getName(), room.getName(), startingAt)).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () ->
                underTest.createBookingByIds(movie.getName(), room.getName(), startingAt, seats, user));
    }

    @Test
    void testCreateBookingByIdsShouldThrowSeatTakenExceptionWhenOtherBookingHasSameSeatAtSameScreening() {
        // Given
        final BookingService bookingService = spy(underTest);
        given(screeningService.getScreeningById(movie.getName(), room.getName(), startingAt)).willReturn(Optional.of(screening));
        given(bookingRepository.findBookingsByScreening(any(Screening.class))).willReturn(List.of(booking1));
        given(bookingService.seatsStringParser(seats)).willReturn(seatsList);

        // When + Then
        assertThrows(SeatsTakenException.class, () ->
                bookingService.createBookingByIds(movie.getName(), room.getName(), startingAt, seats, user));
    }

    @Test
    void testCreateBookingByIdsShouldThrowNoSuchSeatExceptionWhenSeatsAreNotValidInGivenRoom() {
        // Given
        given(screeningService.getScreeningById(movie.getName(), room.getName(), startingAt)).willReturn(Optional.of(screening));
        given(bookingRepository.findBookingsByScreening(screening)).willReturn(List.of());
        given(roomService.getRoomById(room.getName())).willReturn(Optional.of(room));
        final BookingService bookingService = spy(underTest);
        doReturn(notExistingSeatList1).when(bookingService).seatsStringParser(notExistingSeats1);

        // When + Then
        assertThrows(NoSuchSeatException.class, () ->
                bookingService.createBookingByIds(movie.getName(), room.getName(), startingAt, notExistingSeats1, user));
    }

    @Test
    void testCreateBookingByIdsShouldThrowNoSuchSeatExceptionWhenSeatsAreNegative() {
        // Given
        given(screeningService.getScreeningById(movie.getName(), room.getName(), startingAt)).willReturn(Optional.of(screening));
        given(bookingRepository.findBookingsByScreening(screening)).willReturn(List.of());
        given(roomService.getRoomById(room.getName())).willReturn(Optional.of(room));
        final BookingService bookingService = spy(underTest);
        doReturn(notExistingSeatList2).when(bookingService).seatsStringParser(notExistingSeats2);

        // When + Then
        assertThrows(NoSuchSeatException.class, () ->
                bookingService.createBookingByIds(movie.getName(), room.getName(), startingAt, notExistingSeats2, user));
    }

    @Test
    void testCreateBookingByIdsShouldCallCreateBookingWhenEverythingIsFine() {
        // Given
        given(screeningService.getScreeningById(movie.getName(), room.getName(), startingAt)).willReturn(Optional.of(screening));
        given(bookingRepository.findBookingsByScreening(screening)).willReturn(List.of());
        given(roomService.getRoomById(room.getName())).willReturn(Optional.empty());
        given(priceService.calculatePriceForBooking(screening, seatsList.size())).willReturn(price);
        final BookingService bookingService = spy(underTest);
        doReturn(seatsList).when(bookingService).seatsStringParser(seats);

        // When
        bookingService.createBookingByIds(movie.getName(), room.getName(), startingAt, seats, user);

        // Then
        verify(bookingService).createBooking(any(Booking.class));
    }

    @Test
    void testFormattedBookingMessageShouldReturnExpectedValue() {
        // Given
        final String expected = "Seats booked: (1,2), (3,4); the price for this booking is " + price + " HUF";
        given(priceService.calculatePriceForBooking(movie.getName(), room.getName(), startingAt, seatsList.size())).willReturn(price);

        // When
        final String actual = underTest.formattedBookingMessage(movie.getName(), room.getName(), startingAt, seats);

        assertEquals(expected, actual);
    }

    @Test
    void testSeatsStringParserShouldReturnExpectedValue() {
        // Given + When
        final List<Seat> actual = underTest.seatsStringParser(seats);

        // Then
        assertEquals(seatsList.toString(), actual.toString());
    }
}
