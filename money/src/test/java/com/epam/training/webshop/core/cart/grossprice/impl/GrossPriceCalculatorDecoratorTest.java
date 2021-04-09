package com.epam.training.webshop.core.cart.grossprice.impl;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grossprice.GrossPriceCalculator;
import com.epam.training.webshop.core.finance.money.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GrossPriceCalculatorDecoratorTest {

    private GrossPriceCalculatorDecorator underTest;

    @Test
    public void testGetAggregatedGrossPriceShouldInvokeGrossPriceCalculator() {
        // Given
        GrossPriceCalculator grossPriceCalculator = Mockito.mock(GrossPriceCalculator.class);
        Cart cart = Mockito.mock(Cart.class);
        Money expected = Mockito.mock(Money.class);
        Mockito.when(grossPriceCalculator.getAggregatedGrossPrice(cart)).thenReturn(expected);
        underTest = new GrossPriceCalculatorDecorator(grossPriceCalculator);


        // When
        Money actual = underTest.getAggregatedGrossPrice(cart);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(grossPriceCalculator).getAggregatedGrossPrice(cart);
        Mockito.verifyNoMoreInteractions(grossPriceCalculator, cart, expected);
    }

}
