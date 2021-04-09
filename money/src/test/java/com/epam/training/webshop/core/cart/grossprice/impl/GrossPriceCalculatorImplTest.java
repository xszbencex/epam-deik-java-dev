package com.epam.training.webshop.core.cart.grossprice.impl;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.finance.money.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GrossPriceCalculatorImplTest {

    private GrossPriceCalculatorImpl underTest;

    @Test
    public void testGetAggregatedGrossPriceShouldReturnTheAggregatedNetPriceWhenCartIsNotNull() {
        // Given
        Cart cart = Mockito.mock(Cart.class);
        Money expected = Mockito.mock(Money.class);
        Mockito.when(cart.getAggregatedNetPrice()).thenReturn(expected);
        underTest = new GrossPriceCalculatorImpl();

        // When
        Money actual = underTest.getAggregatedGrossPrice(cart);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(cart).getAggregatedNetPrice();
        Mockito.verifyNoMoreInteractions(cart, expected);
    }

    @Test
    public void testGetAggregatedGrossPriceShouldThrowNullPointerExceptionWhenCartIsNull() {
        // Given
        underTest = new GrossPriceCalculatorImpl();

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.getAggregatedGrossPrice(null));

        // Then
    }

}
