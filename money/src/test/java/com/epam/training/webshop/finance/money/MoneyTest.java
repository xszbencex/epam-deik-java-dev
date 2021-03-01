package com.epam.training.webshop.finance.money;

import com.epam.training.webshop.finance.bank.Bank;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Currency;
import java.util.Optional;

public class MoneyTest {

    private static final Currency HUF_CURRENCY = Currency.getInstance("HUF");
    private static final Currency USD_CURRENCY = Currency.getInstance("USD");
    private static final Currency GBP_CURRENCY = Currency.getInstance("GBP");

    @Test
    public void testAddShouldReturnsExpectedResultWhenDifferentCurrencyIsUsed() {
        // Given
        Money underTest = new Money(120, USD_CURRENCY);
        Money moneyToAdd = new Money(1, HUF_CURRENCY);
        Money expected = new Money(369.3, USD_CURRENCY);
        Bank mockBank = Mockito.mock(Bank.class);
        Mockito.when(mockBank.getExchangeRate(HUF_CURRENCY, USD_CURRENCY)).thenReturn(Optional.of(249.3));

        // When
        Money actual = underTest.add(moneyToAdd, mockBank);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(mockBank).getExchangeRate(HUF_CURRENCY, USD_CURRENCY);
        Mockito.verifyNoMoreInteractions(mockBank);
    }

    @Test
    public void testAddShouldThrowExceptionWhenExchangeRateDoesNotExist() {
        // Given
        Money money = new Money(1, HUF_CURRENCY);
        Money moneyToAdd = new Money(1, GBP_CURRENCY);
        Bank mockBank = Mockito.mock(Bank.class);
        Mockito.when(mockBank.getExchangeRate(GBP_CURRENCY, HUF_CURRENCY)).thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(UnsupportedOperationException.class, () -> money.add(moneyToAdd, mockBank));

        // Then
        Mockito.verify(mockBank).getExchangeRate(GBP_CURRENCY, HUF_CURRENCY);
        Mockito.verifyNoMoreInteractions(mockBank);
    }

    @Test
    public void testAddShouldThrowNullPointerExceptionWhenBankParameterIsNull() {
        // Given
        Money money = new Money(1, HUF_CURRENCY);
        Money moneyToAdd = new Money(1, USD_CURRENCY);

        // When
        Assertions.assertThrows(NullPointerException.class, () -> money.add(moneyToAdd, null));
    }

    @Test
    public void testAddShouldThrowNullPointerExceptionWhenMoneyToAddParameterIsNull() {
        // Given
        Money money = new Money(1, HUF_CURRENCY);
        Money moneyToAdd = new Money(1, USD_CURRENCY);
        Bank mockBank = Mockito.mock(Bank.class);

        // When
        Assertions.assertThrows(NullPointerException.class, () -> money.add(null, mockBank));

        // Then
        Mockito.verifyNoMoreInteractions(mockBank);
    }
}
