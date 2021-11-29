package com.epam.training.ticketservice.model.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeatListConverterTest {

    private final static List<Seat> seatList = List.of(new Seat("1,2"), new Seat("3,4"));
    private final static String seats = "1,2 3,4";

    private SeatListConverter underTest;

    @BeforeEach
    void setup() {
        underTest = new SeatListConverter();
    }

    @Test
    void testConvertToDatabaseColumnShouldReturnEmptyStringWhenArgIsNull() {
        // Given + When
        final String actual = underTest.convertToDatabaseColumn(null);

        // Then
        assertEquals("", actual);
    }

    @Test
    void testConvertToDatabaseColumnShouldReturnExpectedValue() {
        // Given + When
        final String actual = underTest.convertToDatabaseColumn(seatList);

        // Then
        assertEquals(seats, actual);
    }

    @Test
    void testConvertToEntityAttributeShouldReturnEmptyListWhenArgIsNull() {
        // Given + When
        final List<Seat> actual = underTest.convertToEntityAttribute(null);

        // Then
        assertEquals(List.of(), actual);
    }

    @Test
    void testConvertToEntityAttributeShouldReturnExpectedValue() {
        // Given + When
        final List<Seat> actual = underTest.convertToEntityAttribute(seats);

        // Then
        assertEquals(seatList.toString(), actual.toString());
    }
}
