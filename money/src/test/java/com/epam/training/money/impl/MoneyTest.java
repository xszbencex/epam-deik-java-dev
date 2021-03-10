package com.epam.training.money.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.util.Currency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MoneyTest {

    public static final Currency CURRENCY = Currency.getInstance("HUF");
    public static final int VALUE = 123;
    public static final Currency CURRENCY_OF_MONEY_TO_ADD = Currency.getInstance("USD");
    @Mock
    private CurrencyConversionService currencyConversionService;

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @CsvSource({"0, 0, 0", "0, 1, 1", "-1, 0, -1", "0, -1, -1", "1, -1, 0"})
    public void testAddShouldReturnMoneyWithTheSumOfValuesWhenConversionSucceeds(double valueOfUnderTest,
                                                                                 double valueOfMoneyToAdd,
                                                                                 double expectedSum) {
        // Given
        Money underTest = new Money(valueOfUnderTest, CURRENCY);
        Money moneyToAdd = new Money(valueOfMoneyToAdd, CURRENCY_OF_MONEY_TO_ADD);
        given(currencyConversionService.convert(moneyToAdd, CURRENCY)).willReturn(moneyToAdd);

        // When
        Money result = underTest.add(moneyToAdd, currencyConversionService);

        // Then
        assertThat(result.getValue(), equalTo(expectedSum));
    }

    @Test
    public void testAddShouldReturnMoneyWithTheCorrectCurrencyWhenConversionSucceeds() {
        // Given
        Money underTest = new Money(VALUE, CURRENCY);
        Money moneyToAdd = new Money(VALUE, CURRENCY_OF_MONEY_TO_ADD);
        given(currencyConversionService.convert(moneyToAdd, CURRENCY)).willReturn(new Money(VALUE, CURRENCY));

        // When
        Money result = underTest.add(moneyToAdd, currencyConversionService);

        // Then
        assertThat(result.getCurrency(), equalTo(CURRENCY));
    }

    @Test
    public void testAddShouldThrowExceptionWhenConversionFailsWithUnsupportedOperationException() {
        // Given
        Money underTest = new Money(VALUE, CURRENCY);
        Money moneyToAdd = new Money(VALUE, CURRENCY_OF_MONEY_TO_ADD);
        given(currencyConversionService.convert(moneyToAdd, CURRENCY)).willThrow(new UnsupportedOperationException());

        // When - Then
        assertThrows(UnsupportedOperationException.class, () -> underTest.add(moneyToAdd, currencyConversionService));
    }

    @ParameterizedTest
    @CsvSource({"0, 0, 0", "0, 1, -1", "-1, 0, -1", "1, 0, 1", "0, -1, 1"})
    public void testCompareToShouldReturnExpectedValueWhenConversionSucceeds(double valueOfUnderTest,
                                                                             double valueOfMoneyToCompare,
                                                                             Integer expectedResult) {
        // Given
        Money underTest = new Money(valueOfUnderTest, CURRENCY);
        Money moneyToAdd = new Money(valueOfMoneyToCompare, CURRENCY_OF_MONEY_TO_ADD);
        given(currencyConversionService.convert(moneyToAdd, CURRENCY)).willReturn(moneyToAdd);

        // When
        Integer result = underTest.compareTo(moneyToAdd, currencyConversionService);

        // Then
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void testCompareToShouldThrowExceptionWhenConversionFailsWithUnsupportedOperationException() {
        // Given
        Money underTest = new Money(VALUE, CURRENCY);
        Money moneyToAdd = new Money(VALUE, CURRENCY_OF_MONEY_TO_ADD);
        given(currencyConversionService.convert(moneyToAdd, CURRENCY)).willThrow(new UnsupportedOperationException());

        // When - Then
        assertThrows(UnsupportedOperationException.class, () -> underTest.compareTo(moneyToAdd, currencyConversionService));
    }

}