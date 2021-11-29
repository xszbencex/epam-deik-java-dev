package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.RoomService;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.shell.Availability;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class RoomCommandHandlerTest {

    private final static Account ADMIN = new Account("admin", "admin", true);
    private final static Account USER = new Account("user", "user");
    private final static String roomName = "Room";
    private final static Integer roomRowCount = 10;
    private final static Integer roomColumnCount = 10;
    private final static Room room = new Room(roomName, roomRowCount, roomColumnCount);

    private RoomCommandHandler underTest;

    @Mock
    private AccountService accountService;

    @Mock
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new RoomCommandHandler(roomService, accountService);
    }

    @Test
    void testCreateRoomShouldCallServiceAndReturnSuccessMessage() {
        // Given
        final String expected = "Room with name 'Room' successfully created.";
        doNothing().when(roomService).createRoom(any(Room.class));

        // When
        final String actual = underTest.createRoom(roomName, roomRowCount, roomColumnCount);

        // Then
        verify(roomService).createRoom(any(Room.class));
        assertEquals(expected, actual);
    }

    @Test
    void testUpdateRoomShouldReturnErrorMessageWhenExceptionOccurs() {
        // Given
        final String expected = "Error";
        doThrow(new NoSuchItemException(expected)).when(roomService).updateRoom(any(Room.class));

        // When
        final String actual = underTest.updateRoom(roomName, roomRowCount, roomColumnCount);

        // Then
        verify(roomService).updateRoom(any(Room.class));
        assertEquals(expected, actual);
    }

    @Test
    void testUpdateRoomShouldCallServiceReturnSuccessMessage() {
        // Given
        final String expected = "Room with name 'Room' successfully updated.";
        doNothing().when(roomService).updateRoom(any(Room.class));

        // When
        final String actual = underTest.updateRoom(roomName, roomRowCount, roomColumnCount);

        // Then
        verify(roomService).updateRoom(any(Room.class));
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteRoomShouldReturnErrorMessageWhenExceptionOccurs() {
        // Given
        final String expected = "Error";
        doThrow(new NoSuchItemException(expected)).when(roomService).deleteRoom(roomName);

        // When
        final String actual = underTest.deleteRoom(roomName);

        // Then
        verify(roomService).deleteRoom(roomName);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteRoomShouldCallServiceReturnSuccessMessage() {
        // Given
        final String expected = "Room with name 'Room' successfully deleted.";
        doNothing().when(roomService).deleteRoom(roomName);

        // When
        final String actual = underTest.deleteRoom(roomName);

        // Then
        verify(roomService).deleteRoom(roomName);
        assertEquals(expected, actual);
    }

    @Test
    void testListRoomsShouldReturnStringWhenNoRoomFound() {
        // Given
        final String expected = "There are no rooms at the moment";
        given(roomService.getAllRooms()).willReturn(List.of());

        // When
        final String actual = underTest.listRooms();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testListRoomsShouldReturnFormattedStringWhenRoomsFound() {
        // Given
        final String expected = "Formatted message";
        given(roomService.getAllRooms()).willReturn(List.of(room));
        given(roomService.formattedRoomList(anyList())).willReturn(expected);

        // When
        final String actual = underTest.listRooms();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testCheckAdminAvailabilityShouldReturnUnavailableWhenNoOneIsSignedIn() {
        // Given
        final Availability expected = Availability.unavailable("this command requires admin privileges.");
        given(accountService.getLoggedInAccount()).willReturn(Optional.empty());

        // When
        final Availability actual = underTest.checkAdminAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }


    @Test
    void testCheckAdminAvailabilityShouldReturnUnavailableWhenDefaultUserIsSignedIn() {
        // Given
        final Availability expected = Availability.unavailable("this command requires admin privileges.");
        given(accountService.getLoggedInAccount()).willReturn(Optional.of(USER));

        // When
        final Availability actual = underTest.checkAdminAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }

    @Test
    void testCheckAdminAvailabilityShouldReturnAvailableWhenAdminIsSignedIn() {
        // Given
        final Availability expected = Availability.available();
        given(accountService.getLoggedInAccount()).willReturn(Optional.of(ADMIN));

        // When
        final Availability actual = underTest.checkAdminAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }
}
