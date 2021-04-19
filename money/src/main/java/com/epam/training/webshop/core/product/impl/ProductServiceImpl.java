package com.epam.training.webshop.core.product.impl;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.model.ProductDto;
import com.epam.training.webshop.core.product.persistence.entity.Product;
import com.epam.training.webshop.core.product.persistence.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDto> getProductList() {
        return productRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDto> getProductByName(String productName) {
        return convertEntityToDto(productRepository.findByName(productName));
    }

    @Override
    public void createProduct(ProductDto productDto) {
        Objects.requireNonNull(productDto, "Product cannot be null");
        Objects.requireNonNull(productDto.getName(), "Product Name cannot be null");
        Objects.requireNonNull(productDto.getNetPrice(), "Product Net Price cannot be null");
        Product product = new Product(null,
                productDto.getName(),
                productDto.getNetPrice().getAmount(),
                productDto.getNetPrice().getCurrency().getCurrencyCode());
        productRepository.save(product);
    }

    private Optional<ProductDto> convertEntityToDto(Optional<Product> product) {
        Optional<ProductDto> productDto;
        if(product.isEmpty()) {
            productDto = Optional.empty();
        } else {
            productDto = Optional.of(convertEntityToDto(product.get()));
        }
        return productDto;
    }

    private ProductDto convertEntityToDto(Product product) {
        return new ProductDto.Builder()
                .withName(product.getName())
                .withNetPrice(new Money(product.getNetPriceAmount(), Currency.getInstance(product.getNetPriceCurrencyCode())))
                .build();
    }

}
