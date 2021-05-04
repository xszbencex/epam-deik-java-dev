package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.model.ProductDto;
import com.epam.training.webshop.core.user.LoginService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.*;

@ShellComponent
public class ProductCommand extends AbstractAuthenticatedCommand {

    private final ProductService productService;

    public ProductCommand(ProductService productService, LoginService loginService) {
        super(loginService);
        this.productService = productService;
    }

    @ShellMethod(value = "Product List", key = "user product list")
    public List<ProductDto> listAvailableProducts() {
        return productService.getProductList();
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Admin create Product", key = "admin product create")
    public ProductDto createProduct(String name, double netPriceAmount, String netPriceCurrency) {
        ProductDto product = new ProductDto.Builder()
                .withName(name)
                .withNetPrice(new Money(netPriceAmount, Currency.getInstance(netPriceCurrency)))
                .build();
        productService.createProduct(product);
        return product;
    }

}
