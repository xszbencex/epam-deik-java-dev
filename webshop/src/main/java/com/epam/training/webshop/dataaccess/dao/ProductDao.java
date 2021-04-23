package com.epam.training.webshop.dataaccess.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.training.webshop.dataaccess.projection.ProductProjection;

public interface ProductDao extends JpaRepository<ProductProjection, UUID> {
    Optional<ProductProjection> findByName(String name);
}
