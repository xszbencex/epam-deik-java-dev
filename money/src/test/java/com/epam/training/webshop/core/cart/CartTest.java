package com.epam.training.webshop.core.cart;

import com.epam.training.webshop.core.checkout.model.OrderDto;
import com.epam.training.webshop.core.finance.bank.Bank;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.Currency;
import java.util.Optional;

import static org.mockito.Mockito.times;

public class CartTest {

    private static final Currency HUF = Currency.getInstance("HUF");
    private static final ProductDto TV = new ProductDto.Builder()
            .withName("TV")
            .withNetPrice(new Money(100_000D, HUF))
            .build();
    private static final ProductDto MOBIL = new ProductDto.Builder()
            .withName("Mobil")
            .withNetPrice(new Money(300_000D, HUF))
            .build();

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
        Bank bank = Mockito.mock(Bank.class);
        Cart underTest = new Cart(bank);
        Cart expected = Cart.of(bank, TV);

        // When
        underTest.add(TV);

        // Then
        Assertions.assertEquals(expected, underTest);
        Mockito.verifyNoMoreInteractions(bank);
    }

    @Test
    public void testAddShouldThrowANullPointerExceptionWhenProductIsNull() {
        // Given
        Bank bank = Mockito.mock(Bank.class);
        Cart underTest = new Cart(bank);
        Cart expected = Cart.of(bank);

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.add(null));

        // Then
        Assertions.assertEquals(expected, underTest);
        Mockito.verifyNoMoreInteractions(bank);
    }

    @Test
    public void testGetAggregatedNetPriceShouldReturnACorrectNetPriceWhenTheCartIsEmpty() {
        // Given
        Bank bank = Mockito.mock(Bank.class);
        Cart underTest = Cart.of(bank);
        Money expected = new Money(0D, HUF);

        // When
        Money actual = underTest.getAggregatedNetPrice();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verifyNoMoreInteractions(bank);
    }

    @Test
    public void testGetAggregatedNetPriceShouldReturnACorrectNetPriceWhenOneItemIsInTheCart() {
        // Given
        Bank bank = Mockito.mock(Bank.class);
        Mockito.when(bank.getExchangeRate(HUF, HUF)).thenReturn(Optional.of(1D));
        Cart underTest = Cart.of(bank, TV);
        Money expected = new Money(100_000D, HUF);

        // When
        Money actual = underTest.getAggregatedNetPrice();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(bank).getExchangeRate(HUF, HUF);
        Mockito.verifyNoMoreInteractions(bank);
    }

    @Test
    public void testGetAggregatedNetPriceShouldReturnACorrectNetPriceWhenTwoItemsInTheCart() {
        // Given
        Bank bank = Mockito.mock(Bank.class);
        Mockito.when(bank.getExchangeRate(HUF, HUF)).thenReturn(Optional.of(1D));
        Cart underTest = Cart.of(bank, TV, MOBIL);
        Money expected = new Money(400_000D, HUF);

        // When
        Money actual = underTest.getAggregatedNetPrice();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(bank, times(2)).getExchangeRate(HUF, HUF);
        Mockito.verifyNoMoreInteractions(bank);
    }

    @Test
    public void testGetProductListShouldReturnAnEmptyListWhenTheCartIsEmpty() {
        // Given
        Bank bank = Mockito.mock(Bank.class);
        Cart underTest = Cart.of(bank);
        List<ProductDto> expected = List.of();

        // When
        List<ProductDto> actual = underTest.getProductList();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verifyNoMoreInteractions(bank);
    }

    @Test
    public void testGetProductListShouldReturnTheProductListWhenTheCartIsNotEmpty() {
        // Given
        Bank bank = Mockito.mock(Bank.class);
        Cart underTest = Cart.of(bank, TV, MOBIL);
        List<ProductDto> expected = List.of(TV, MOBIL);

        // When
        List<ProductDto> actual = underTest.getProductList();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verifyNoMoreInteractions(bank);
    }

    @Test
    public void testGetProductListShouldReturnAnUnmodifiableListWhenTheCartIsNotEmpty() {
        // Given
        Bank bank = Mockito.mock(Bank.class);
        Cart underTest = Cart.of(bank, TV, MOBIL);
        List<ProductDto> expected = List.of(TV, MOBIL);

        // When
        List<ProductDto> productListFromTheCart = underTest.getProductList();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> productListFromTheCart.add(TV));
        List<ProductDto> actual = underTest.getProductList();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verifyNoMoreInteractions(bank);
    }

    @Test
    public void testHandleOrderShouldReturnEmptyTheProductListWhenItIsCalled() {
        // Given
        OrderDto orderDto = Mockito.mock(OrderDto.class);
        Bank bank = Mockito.mock(Bank.class);
        Cart underTest = Cart.of(bank, TV, MOBIL);
        Cart expected = Cart.of(bank);

        // When
        underTest.handleOrder(orderDto);

        // Then
        Assertions.assertEquals(expected, underTest);
        Mockito.verifyNoMoreInteractions(bank, orderDto);
    }

}
