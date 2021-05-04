package com.epam.training.webshop.core.checkout.impl;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grossprice.GrossPriceCalculator;
import com.epam.training.webshop.core.checkout.model.OrderDto;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
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
        ProductDto productDto = new ProductDto.Builder()
                .withName("TV")
                .withNetPrice(new Money(100, Currency.getInstance("HUF")))
                .build();
        List<ProductDto> productList = List.of(productDto);
        Money netPrice = Mockito.mock(Money.class);
        Money grossPrice = Mockito.mock(Money.class);
        Mockito.when(cart.getProductList()).thenReturn(productList);
        Mockito.when(cart.getAggregatedNetPrice()).thenReturn(netPrice);
        Mockito.when(grossPriceCalculator.getAggregatedGrossPrice(cart)).thenReturn(grossPrice);
        OrderDto expected = new OrderDto(productList, netPrice, grossPrice);

        // When
        OrderDto actual = underTest.checkout(cart);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(cart).getProductList();
        Mockito.verify(cart).getAggregatedNetPrice();
        Mockito.verify(grossPriceCalculator).getAggregatedGrossPrice(cart);
        Mockito.verify(checkoutObservable).broadcastOrder(expected);
        Mockito.verifyNoMoreInteractions(grossPriceCalculator, cart, netPrice, grossPrice, checkoutObservable);
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
