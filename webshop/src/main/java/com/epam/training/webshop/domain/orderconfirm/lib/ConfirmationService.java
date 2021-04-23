package com.epam.training.webshop.domain.orderconfirm.lib;

import java.util.List;

import com.epam.training.webshop.domain.order.Product;

public interface ConfirmationService {
    void sendConfirmationMessageAbout(List<Product> products);
}
