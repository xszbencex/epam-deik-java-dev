package com.epam.training.webshop.ui.configuration;

import com.epam.training.webshop.core.product.persistence.entity.Product;
import com.epam.training.webshop.core.product.persistence.repository.ProductRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Profile("! prod")
public class InMemoryDbInitializer {

    private ProductRepository productRepository;

    public InMemoryDbInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void init() {
        Product tv = new Product(null, "TV", 100_000D, "HUF");
        Product mobil = new Product(null, "Mobil", 300_000D, "HUF");
        productRepository.saveAll(List.of(tv, mobil));
    }

}
