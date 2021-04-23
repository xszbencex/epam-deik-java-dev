package com.epam.training.webshop.dataaccess.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.training.webshop.dataaccess.projection.OrderProjection;

public interface OrderDao extends JpaRepository<OrderProjection, UUID> {
}
