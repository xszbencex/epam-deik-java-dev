package com.epam.training.money.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.util.Currency;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class FixedCurrencyConversionServiceTest {

    private static final Currency HUF_CURRENCY = Currency.getInstance("HUF");
    public static final double VALUE = 1234.0;
    private static final Currency USD_CURRENCY = Currency.getInstance("USD");
    private static final Currency GBP_CURRENCY = Currency.getInstance("GBP");

    @Test
    public void testConvertShouldReturnOriginalCurrencyWhenTargetCurrencyMatchesOriginal() {
        // Given
        FixedCurrencyConversionService underTest = new FixedCurrencyConversionService();
        Money moneyToConvert = Mockito.mock(Money.class);
        given(moneyToConvert.getValue()).willReturn(VALUE);
        given(moneyToConvert.getCurrency()).willReturn(HUF_CURRENCY);

        // When
        Money actualResult = underTest.convert(moneyToConvert, HUF_CURRENCY);

        // Then
        assertThat(actualResult.getValue(), equalTo(VALUE));
        assertThat(actualResult.getCurrency(), equalTo(HUF_CURRENCY));
    }

    @Test
    public void testConvertShouldReturnConvertedCurrencyWhenCurrenciesDifferent() {
        //Given
        FixedCurrencyConversionService underTest = new FixedCurrencyConversionService();
        double expectedValue = 4.1956;
        Money moneyToConvert = Mockito.mock(Money.class);
        given(moneyToConvert.getValue()).willReturn(VALUE);
        given(moneyToConvert.getCurrency()).willReturn(HUF_CURRENCY);

        //When
        Money result = underTest.convert(moneyToConvert, USD_CURRENCY);

        //Then
        assertThat(result.getValue(), equalTo(expectedValue));
        assertThat(result.getCurrency(), equalTo(USD_CURRENCY));
    }

    @ParameterizedTest
    @MethodSource("nonConvertibleCurrenciesDataSource")
    public void testConvertShouldThrowExceptionWhenCurrencyConversionRateIsNotKnown(Currency moneyToConvertCurrency, Currency targetCurrency) {
        // Given
        FixedCurrencyConversionService underTest = new FixedCurrencyConversionService();
        Money moneyToConvert = Mockito.mock(Money.class);
        given(moneyToConvert.getValue()).willReturn(VALUE);
        given(moneyToConvert.getCurrency()).willReturn(moneyToConvertCurrency);

        // When - Then
        assertThrows(UnsupportedOperationException.class, () -> underTest.convert(moneyToConvert, targetCurrency));
    }

    private static Stream<Arguments> nonConvertibleCurrenciesDataSource() {
        return Stream.of(
                Arguments.arguments(GBP_CURRENCY, HUF_CURRENCY),
                Arguments.arguments(HUF_CURRENCY, GBP_CURRENCY)
        );
    }

}