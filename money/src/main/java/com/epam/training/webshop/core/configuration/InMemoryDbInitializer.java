package com.epam.training.webshop.core.configuration;

import com.epam.training.webshop.core.product.persistence.entity.Product;
import com.epam.training.webshop.core.product.persistence.repository.ProductRepository;
import com.epam.training.webshop.core.user.persistence.entity.User;
import com.epam.training.webshop.core.user.persistence.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Profile("! prod")
public class InMemoryDbInitializer {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public InMemoryDbInitializer(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Product tv = new Product(null, "TV", 100_000D, "HUF");
        Product mobil = new Product(null, "Mobil", 300_000D, "HUF");
        productRepository.saveAll(List.of(tv, mobil));

        User admin = new User(null, "admin", "admin", User.Role.ADMIN);
        userRepository.save(admin);
    }

}
