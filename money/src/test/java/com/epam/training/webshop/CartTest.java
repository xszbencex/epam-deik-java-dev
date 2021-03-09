package com.epam.training.webshop;

import com.epam.training.webshop.cart.Cart;
import com.epam.training.webshop.finance.bank.Bank;
import com.epam.training.webshop.finance.bank.staticbank.impl.StaticBank;
import com.epam.training.webshop.finance.bank.staticbank.model.StaticExchangeRates;
import com.epam.training.webshop.finance.money.Money;
import com.epam.training.webshop.product.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Currency;

public class CartTest {

    private static final Currency HUF_CURRENCY = Currency.getInstance("HUF");
    private static final Currency USD_CURRENCY = Currency.getInstance("USD");

    private Bank bank = StaticBank.of(() -> new StaticExchangeRates.Builder()
            .addRate(HUF_CURRENCY, USD_CURRENCY, 0.0034, 249.3)
            .build());

    // get net price of the current content of the cart for different currencies
    // add several items to the cart from the same product


    @Test
    public void testAddShouldAddAProductToTheCart() {
        // Given
        Cart underTest = new Cart();
        Cart expected = Cart.of(new Product("TV", new Money(100_000D, Currency.getInstance("HUF"))));
        // When
        underTest.add(new Product("TV", new Money(100_000D, Currency.getInstance("HUF"))));

        // Then
        Assertions.assertEquals(expected, underTest);
    }

    @Test
    public void testGetAggregatedNetPriceShouldReturnACorrectNetPriceWhenOneItemInTheCart() {
        // Given
        Cart underTest = Cart.of(new Product("TV", new Money(100_000D, Currency.getInstance("HUF"))));
        Money expected = new Money(100_000D, Currency.getInstance("HUF"));

        // When
        Money actual = underTest.getAggregatedNetPrice(bank);

        // Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetAggregatedNetPriceShouldReturnACorrectNetPriceWhenTwoItemsInTheCart() {
        // Given
        Cart underTest = Cart.of(new Product("TV", new Money(100_000D, Currency.getInstance("HUF"))),
                new Product("Mobil", new Money(300_000D, Currency.getInstance("HUF"))));
        Money expected = new Money(400_000D, Currency.getInstance("HUF"));

        // When
        Money actual = underTest.getAggregatedNetPrice(bank);

        // Then
        Assertions.assertEquals(expected, actual);
    }

}
