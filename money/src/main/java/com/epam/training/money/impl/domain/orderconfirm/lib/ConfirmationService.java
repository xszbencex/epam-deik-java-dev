package com.epam.training.money.impl.domain.orderconfirm.lib;

import java.util.List;

import com.epam.training.money.impl.domain.order.Product;

public interface ConfirmationService {
    void sendConfirmationMessageAbout(List<Product> products);
}
