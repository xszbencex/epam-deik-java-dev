package com.epam.training.webshop.core.product.impl;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import com.epam.training.webshop.core.product.persistence.entity.Product;
import com.epam.training.webshop.core.product.persistence.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

public class ProductServiceImplTest {

    private static final String TV = "TV";
    private static final String HUF = "HUF";
    private static final Product TV_ENTITY = new Product(null, TV, 100_000D, HUF);
    private static final Product MOBIL_ENTITY = new Product(null, "Mobil", 300_000D, HUF);
    private static final ProductDto TV_DTO = new ProductDto.Builder()
            .withName(TV)
            .withNetPrice(new Money(100_000D, Currency.getInstance(HUF)))
            .build();
    private static final ProductDto MOBIL_DTO = new ProductDto.Builder()
            .withName("Mobil")
            .withNetPrice(new Money(300_000D, Currency.getInstance(HUF)))
            .build();


    private ProductServiceImpl underTest;

    private ProductRepository productRepository;

    @BeforeEach
    public void init() {
        productRepository = Mockito.mock(ProductRepository.class);
        underTest = new ProductServiceImpl(productRepository);
    }

    @Test
    public void testGetProductListShouldCallProductRepositoryAndReturnADtoList() {
        // Given
        Mockito.when(productRepository.findAll()).thenReturn(List.of(TV_ENTITY, MOBIL_ENTITY));
        List<ProductDto> expected = List.of(TV_DTO, MOBIL_DTO);

        // When
        List<ProductDto> actual = underTest.getProductList();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(productRepository).findAll();
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void testGetProductByNameShouldReturnATvDtoWhenTheProductExists() {
        // Given
        Mockito.when(productRepository.findByName(TV)).thenReturn(Optional.of(TV_ENTITY));
        Optional<ProductDto> expected = Optional.of(TV_DTO);

        // When
        Optional<ProductDto> actual = underTest.getProductByName(TV);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(productRepository).findByName(TV);
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void testGetProductByNameShouldReturnOptionalEmptyWhenInputProductNameDoesNotExist() {
        // Given
        Mockito.when(productRepository.findByName(TV)).thenReturn(Optional.empty());
        Optional<ProductDto> expected = Optional.empty();

        // When
        Optional<ProductDto> actual = underTest.getProductByName(TV);

        // Then
        Assertions.assertTrue(actual.isEmpty());
        Assertions.assertEquals(expected, actual);
        Mockito.verify(productRepository).findByName(TV);
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void testGetProductByNameShouldReturnOptionalEmptyWhenInputProductNameIsNull() {
        // Given
        Mockito.when(productRepository.findByName(null)).thenReturn(Optional.empty());
        Optional<ProductDto> expected = Optional.empty();

        // When
        Optional<ProductDto> actual = underTest.getProductByName(null);

        // Then
        Assertions.assertTrue(actual.isEmpty());
        Assertions.assertEquals(expected, actual);
        Mockito.verify(productRepository).findByName(null);
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void testCreateProductShouldCallProductRepositoryWhenTheInputProductIsValid() {
        // Given
        Mockito.when(productRepository.save(TV_ENTITY)).thenReturn(TV_ENTITY);

        // When
        underTest.createProduct(TV_DTO);

        // Then
        Mockito.verify(productRepository).save(TV_ENTITY);
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void testCreateProductShouldThrowNullPointerExceptionWhenProductIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createProduct(null));

        // Then
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void testCreateProductShouldThrowNullPointerExceptionWhenProductNameIsNull() {
        // Given
        ProductDto product = new ProductDto.Builder()
                .withName(null)
                .withNetPrice(new Money(230_000D, Currency.getInstance(HUF)))
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createProduct(product));

        // Then
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void testCreateProductShouldThrowNullPointerExceptionWhenNetPriceIsNull() {
        // Given
        ProductDto product = new ProductDto.Builder()
                .withName("LG Monitor")
                .withNetPrice(null)
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createProduct(product));

        // Then
        Mockito.verifyNoMoreInteractions(productRepository);
    }

}
