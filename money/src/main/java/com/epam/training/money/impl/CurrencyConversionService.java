package com.epam.training.money.impl;

import java.util.Currency;

public interface CurrencyConversionService {
    Money convert(Money moneyToConvert, Currency targetCurrency);
}
