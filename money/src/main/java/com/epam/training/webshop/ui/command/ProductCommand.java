package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.model.Product;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.*;

@ShellComponent
public class ProductCommand {

    private final ProductService productService;

    public ProductCommand(ProductService productService) {
        this.productService = productService;
    }

    @ShellMethod(value = "Product List", key = "user product list")
    public List<Product> listAvailableProducts() {
        return productService.getProductList();
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(value = "Admin create Product", key = "admin product create")
    public Product createProduct(String name, double netPriceAmount, String netPriceCurrency) {
        Product product = new Product.Builder()
                .withName(name)
                .withNetPrice(new Money(netPriceAmount, Currency.getInstance(netPriceCurrency)))
                .build();
        productService.createProduct(product);
        return product;
    }

    private Availability isAvailable() {
        // return Availability.unavailable("You are not an admin user");
        return Availability.available();
    }

}
