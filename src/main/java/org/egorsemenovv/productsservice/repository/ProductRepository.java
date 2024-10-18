package org.egorsemenovv.productsservice.repository;

import org.egorsemenovv.productsservice.model.Product;
import org.egorsemenovv.productsservice.model.Sku;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
            SELECT p FROM Product p
            WHERE p.active = :active AND
            p.startDate >= :startDate AND
            p.loaded = FALSE""")
    @EntityGraph(attributePaths = {"skus"})
    Page<Product> findUnloadedProductByFilter(Boolean active, LocalDate startDate, Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.loaded = :loaded WHERE p.id IN :productIds")
    void updateLoadedStatusForProduct(List<Long> productIds, Boolean loaded);
}
