package com.epam.training.webshop.core.checkout.impl;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grossprice.GrossPriceCalculator;
import com.epam.training.webshop.core.checkout.model.Order;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

public class CheckoutServiceImplTest {

    private CheckoutServiceImpl underTest;

    @Test
    public void testCheckoutShouldReturnWithAnOrderWhenCartIsNotNull() {
        // Given
        GrossPriceCalculator grossPriceCalculator = Mockito.mock(GrossPriceCalculator.class);
        CheckoutObservable checkoutObservable = Mockito.mock(CheckoutObservable.class);
        underTest = new CheckoutServiceImpl(grossPriceCalculator, checkoutObservable);
        Cart cart = Mockito.mock(Cart.class);
        List<Product> productList = Mockito.mock(List.class);
        Money netPrice = Mockito.mock(Money.class);
        Money grossPrice = Mockito.mock(Money.class);
        Mockito.when(cart.getProductList()).thenReturn(productList);
        Mockito.when(cart.getAggregatedNetPrice()).thenReturn(netPrice);
        Mockito.when(grossPriceCalculator.getAggregatedGrossPrice(cart)).thenReturn(grossPrice);
        Order expected = new Order(productList, netPrice, grossPrice);

        // When
        Order actual = underTest.checkout(cart);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(cart).getProductList();
        Mockito.verify(cart).getAggregatedNetPrice();
        Mockito.verify(grossPriceCalculator).getAggregatedGrossPrice(cart);
        Mockito.verify(checkoutObservable).broadcastOrder(expected);
        Mockito.verifyNoMoreInteractions(grossPriceCalculator, cart, productList, netPrice, grossPrice, checkoutObservable);
    }

    @Test
    public void testCheckoutShouldThrowANullPointerExceptionWhenCartIsNull() {
        // Given
        GrossPriceCalculator grossPriceCalculator = Mockito.mock(GrossPriceCalculator.class);
        CheckoutObservable checkoutObservable = Mockito.mock(CheckoutObservable.class);
        underTest = new CheckoutServiceImpl(grossPriceCalculator, checkoutObservable);

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.checkout(null));

        // Then
        Mockito.verifyNoMoreInteractions(grossPriceCalculator, checkoutObservable);
    }

}
