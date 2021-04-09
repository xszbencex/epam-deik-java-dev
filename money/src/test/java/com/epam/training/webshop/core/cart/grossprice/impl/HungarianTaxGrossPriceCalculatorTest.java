package com.epam.training.webshop.core.cart.grossprice.impl;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grossprice.GrossPriceCalculator;
import com.epam.training.webshop.core.finance.money.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class HungarianTaxGrossPriceCalculatorTest {

    private HungarianTaxGrossPriceCalculator underTest;

    @Test
    public void testGetAggregatedGrossPriceShouldReturnTheAggregatedGrossPriceWhenCartIsNotNull() {
        // Given
        GrossPriceCalculator grossPriceCalculator = Mockito.mock(GrossPriceCalculator.class);
        Cart cart = Mockito.mock(Cart.class);
        Money netPrice = Mockito.mock(Money.class);
        Mockito.when(grossPriceCalculator.getAggregatedGrossPrice(cart)).thenReturn(netPrice);
        Money expected = Mockito.mock(Money.class);
        Mockito.when(netPrice.multiply(1.27D)).thenReturn(expected);
        underTest = new HungarianTaxGrossPriceCalculator(grossPriceCalculator);

        // When
        Money actual = underTest.getAggregatedGrossPrice(cart);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(grossPriceCalculator).getAggregatedGrossPrice(cart);
        Mockito.verify(netPrice).multiply(1.27D);
        Mockito.verifyNoMoreInteractions(grossPriceCalculator, cart, netPrice, expected);
    }

}
