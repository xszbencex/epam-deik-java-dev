package com.epam.training.webshop.core.cart;

import com.epam.training.webshop.core.finance.bank.Bank;
import com.epam.training.webshop.core.finance.bank.staticbank.impl.StaticBank;
import com.epam.training.webshop.core.finance.bank.staticbank.model.StaticExchangeRates;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Currency;

public class CartTest {

    private static final Currency HUF_CURRENCY = Currency.getInstance("HUF");
    private static final Currency USD_CURRENCY = Currency.getInstance("USD");

    private static final Product TV = new Product.Builder()
            .withName("TV")
            .withNetPrice(new Money(100_000D, Currency.getInstance("HUF")))
            .build();
    private static final Product MOBIL = new Product.Builder()
            .withName("Mobil")
            .withNetPrice(new Money(300_000D, Currency.getInstance("HUF")))
            .build();

    private Bank bank = StaticBank.of(() -> new StaticExchangeRates.Builder()
            .addRate(HUF_CURRENCY, USD_CURRENCY, 0.0034, 249.3)
            .build());

    // get net price of the current content of the cart for different currencies
    // add several items to the cart from the same product
    // getAggregatedGrossPrice for hungarian point of sale (27% AFA)
    // getAggregatedGrossPrice for any other point of sale (X% AFA)
    // add fix price coupon (HUF or USD) to the cart
    // get the final price based on the content (products and coupons) of the cart
    // add percentage based coupons
    // Throw exception when final price is less then the 10% of the full price

    @Test
    public void testAddShouldAddAProductToTheCart() {
        // Given
        Cart underTest = new Cart(bank);
        Cart expected = Cart.of(bank, TV);

        // When
        underTest.add(TV);

        // Then
        Assertions.assertEquals(expected, underTest);
    }

    @Test
    public void testGetAggregatedNetPriceShouldReturnACorrectNetPriceWhenOneItemInTheCart() {
        // Given
        Cart underTest = Cart.of(bank, TV);
        Money expected = new Money(100_000D, Currency.getInstance("HUF"));

        // When
        Money actual = underTest.getAggregatedNetPrice();

        // Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetAggregatedNetPriceShouldReturnACorrectNetPriceWhenTwoItemsInTheCart() {
        // Given
        Cart underTest = Cart.of(bank, TV, MOBIL);
        Money expected = new Money(400_000D, Currency.getInstance("HUF"));

        // When
        Money actual = underTest.getAggregatedNetPrice();

        // Then
        Assertions.assertEquals(expected, actual);
    }

}
