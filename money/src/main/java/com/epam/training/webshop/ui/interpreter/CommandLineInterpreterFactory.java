package com.epam.training.webshop.ui.interpreter;

import com.epam.training.webshop.core.cart.grossprice.GrossPriceCalculator;
import com.epam.training.webshop.core.cart.grossprice.impl.GrossPriceCalculatorDecorator;
import com.epam.training.webshop.core.cart.grossprice.impl.GrossPriceCalculatorImpl;
import com.epam.training.webshop.core.cart.grossprice.impl.HungarianTaxGrossPriceCalculator;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.ProductServiceImpl;
import com.epam.training.webshop.ui.command.impl.AbstractCommand;
import com.epam.training.webshop.ui.command.impl.UserAddProductToCartCommand;
import com.epam.training.webshop.ui.command.impl.UserCheckoutCartCommand;
import com.epam.training.webshop.ui.command.impl.UserProductListCommand;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

public class CommandLineInterpreterFactory {

    public static CommandLineInterpreter create(InputStream input, OutputStream output) {
        // Components creation
        ProductService productService = new ProductServiceImpl();
        GrossPriceCalculator grossPriceCalculator = new HungarianTaxGrossPriceCalculator(new GrossPriceCalculatorImpl());

        // Command creation
        Set<AbstractCommand> commands = new HashSet<>();
        commands.add(new UserProductListCommand(productService));
        commands.add(new UserAddProductToCartCommand(productService));
        commands.add(new UserCheckoutCartCommand(grossPriceCalculator));

        return new CommandLineInterpreter(input, output, commands);
    }

}
