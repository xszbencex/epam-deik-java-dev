package com.epam.training.webshop.core.checkout.persistence.repository;

import java.util.*;
import com.epam.training.webshop.core.checkout.persistence.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserUsername(String username);

}
