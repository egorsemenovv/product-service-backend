package org.egorsemenovv.productsservice.repository;

import org.egorsemenovv.productsservice.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuRepository extends JpaRepository<Sku, Long> {
}
