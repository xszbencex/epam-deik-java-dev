package com.epam.training.ticketservice.model.config;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class SeatListConverter implements AttributeConverter<List<Seat>, String> {
    private static final String SPLIT_CHAR = " ";

    @Override
    public String convertToDatabaseColumn(List<Seat> seats) {
        if (seats != null) {
            StringBuilder stringBuilder = new StringBuilder();
            seats.forEach(seat -> stringBuilder.append(seat.toString()).append(" "));
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    @Override
    public List<Seat> convertToEntityAttribute(String string) {
        return string != null
                ? Arrays.stream(string.split(SPLIT_CHAR)).map(Seat::new).collect(Collectors.toList())
                : List.of();
    }
}
