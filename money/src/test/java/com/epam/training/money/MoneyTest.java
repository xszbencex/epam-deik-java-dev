package com.epam.training.money;

import static java.lang.Integer.signum;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Currency;
import java.util.Optional;

import com.epam.training.money.impl.Bank;
import com.epam.training.money.impl.MapBank;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.epam.training.money.impl.Money;
import org.mockito.Mockito;

public class MoneyTest {

    private static final Currency HUF_CURRENCY = Currency.getInstance("HUF");
    private static final Currency USD_CURRENCY = Currency.getInstance("USD");
    private static final Currency GBP_CURRENCY = Currency.getInstance("GBP");

    private Bank bank = new MapBank();

    @Test
    public void testAddShouldReturnsExpectedResultWhenDifferentCurrencyIsUsed() {
        // Given
        Money underTest = new Money(120, HUF_CURRENCY);
        Money moneyToAdd = new Money(1, USD_CURRENCY);
        Bank mockBank = Mockito.mock(Bank.class);
        Mockito.when(mockBank.getExchangeRate(HUF_CURRENCY, USD_CURRENCY)).thenReturn(Optional.of(249.3));

        // When
        Money result = underTest.add(moneyToAdd, mockBank);

        // Then
        assertThat(result.getAmount(), equalTo(369.3));
        assertThat(result.getCurrency(), equalTo(HUF_CURRENCY));
    }

    @Test
    public void testAddReturnsExpectedResultWhenMatchingCurrencyIsUsed() {
        // Given
        Money underTest = new Money(120, HUF_CURRENCY);
        Money moneyToAdd = new Money(1, HUF_CURRENCY);

        // When
        Money result = underTest.add(moneyToAdd, bank);

        // Then
        assertThat(result.getAmount(), equalTo(121.0));
        assertThat(result.getCurrency(), equalTo(HUF_CURRENCY));
    }

    @Test
    public void testAddReturnsNullWhenCurrencyWithUnknownRateIsUsed() {
        // Given
        Money underTest = new Money(120, HUF_CURRENCY);
        Money moneyToAdd = new Money(1, GBP_CURRENCY);

        // When - Then
        Assertions.assertThrows(UnsupportedOperationException.class, () -> underTest.add(moneyToAdd, bank));
    }


    @ParameterizedTest
    @CsvSource({"249, 1, -1", "249.3, 1, 0", "250, 0, 1"})
    public void testCompareToReturnsExpectedResultWhenDifferentCurrencyIsUsed(double firstValue, double secondValue, int expectedSignum) {
        // Given
        Money underTest = new Money(firstValue, HUF_CURRENCY);
        Money moneyToCompareWith = new Money(secondValue, USD_CURRENCY);

        // When
        Integer result = underTest.compareTo(moneyToCompareWith, bank);

        // Then
        assertThat(signum(result), equalTo(expectedSignum));
    }

    @ParameterizedTest
    @CsvSource({"0, 100, -1", "100, 100, 0", "100, 0, 1"})
    public void testCompareToReturnsExpectedResultWhenMatchingCurrencyIsUsed(double firstValue, double secondValue, int expectedSignum) {
        // Given
        Money underTest = new Money(firstValue, HUF_CURRENCY);
        Money moneyToCompareWith = new Money(secondValue, HUF_CURRENCY);

        // When
        Integer result = underTest.compareTo(moneyToCompareWith, bank);

        // Then
        assertThat(signum(result), equalTo(expectedSignum));
    }

    @Test
    public void testCompareToReturnsNullWhenCurrencyWithUnknownRateIsUsed() {
        // Given
        Money underTest = new Money(120, HUF_CURRENCY);
        Money moneyToCompareWith = new Money(1, GBP_CURRENCY);

        // When - Then
        Assertions.assertThrows(UnsupportedOperationException.class, () -> underTest.compareTo(moneyToCompareWith, bank));
    }

}
