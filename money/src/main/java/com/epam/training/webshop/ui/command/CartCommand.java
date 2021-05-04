package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.checkout.CheckoutService;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.model.ProductDto;
import com.epam.training.webshop.core.user.LoginService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
public class CartCommand extends AbstractAuthenticatedCommand{

    private final ProductService productService;
    private final CheckoutService checkoutService;
    private final Cart cart;

    public CartCommand(ProductService productService, CheckoutService checkoutService, LoginService loginService, Cart cart) {
        super(loginService);
        this.productService = productService;
        this.checkoutService = checkoutService;
        this.cart = cart;
    }

    @ShellMethod(value = "Add Product to Cart", key = "user cart addProduct")
    public String addProductToCart(String productName) {
        String returnString = productName + " is added to your cart";
        Optional<ProductDto> optionalProduct = productService.getProductByName(productName);

        if(returnString.isEmpty()) {
            returnString = productName + " is not found as a Product";
        } else {
            cart.add(optionalProduct.get());
        }

        return returnString;
    }

    @ShellMethodAvailability("loggedIn")
    @ShellMethod(value = "Checkout", key = "user cart checkout")
    public String checkout() {
        return "Your Order: " + checkoutService.checkout(cart);
    }

}
