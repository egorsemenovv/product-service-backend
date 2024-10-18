package org.egorsemenovv.productsservice.repository;

import org.egorsemenovv.productsservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
