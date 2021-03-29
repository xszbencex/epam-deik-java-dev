package com.epam.training.webshop.core.finance.bank.staticbank.impl;

import com.epam.training.webshop.core.finance.bank.staticbank.model.StaticExchangeRates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Currency;
import java.util.Optional;

public class StaticBankTest {

    private static final Currency HUF_CURRENCY = Currency.getInstance("HUF");

    @Test
    public void testGetExchangeRateShouldReturnOneWhenTheCurrenciesAreTheSame() {
        // When
        StaticExchangeRates staticExchangeRates = Mockito.mock(StaticExchangeRates.class);
        StaticBank underTest = new StaticBank(staticExchangeRates);
        Optional<Double> expected = Optional.of(1D);

        // When
        Optional<Double> actual = underTest.getExchangeRate(HUF_CURRENCY, HUF_CURRENCY);

        // Then
        Assertions.assertEquals(expected, actual);
    }
}
