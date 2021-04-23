package com.epam.training.webshop.dataaccess.init;

import javax.annotation.PostConstruct;

import java.util.List;

import org.springframework.stereotype.Component;

import com.epam.training.webshop.dataaccess.dao.ProductDao;
import com.epam.training.webshop.dataaccess.projection.ProductProjection;
import com.epam.training.webshop.domain.order.SimpleProduct;

@Component
public class ProductDatabaseInitializer {

    private ProductDao productDao;

    public ProductDatabaseInitializer(ProductDao productDao) {
        this.productDao = productDao;
    }

    @PostConstruct
    public void initDatabase() {
        List<ProductProjection> products = List.of(
                new ProductProjection("Alma", 42),
                new ProductProjection("Pálinka", 560),
                new ProductProjection("Táncmulatság", 220));
        products.forEach(productDao::save);
    }
}
