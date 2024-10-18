package org.egorsemenovv.productsservice.repository;

import org.egorsemenovv.productsservice.model.Sku;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SkuRepository extends JpaRepository<Sku, Long> {

    @Query("""
            SELECT s FROM Sku s
            JOIN FETCH s.product p
            WHERE p.active = :active AND
            p.startDate >= :startDate AND
            s.loaded = FALSE""")
    Page<Sku> findUnloadedSkuByFilter(Boolean active, LocalDate startDate, Pageable pageable);

    @Modifying
    @Query("UPDATE Sku s SET s.loaded = :loaded WHERE s.id IN :skuIds")
    void updateLoadedStatusForSku(List<Long> skuIds, Boolean loaded);
}
