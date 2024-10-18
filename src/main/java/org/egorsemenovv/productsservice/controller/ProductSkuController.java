package org.egorsemenovv.productsservice.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.egorsemenovv.productsservice.dto.ProductCreateEditDto;
import org.egorsemenovv.productsservice.dto.SkuCreateEditDto;
import org.egorsemenovv.productsservice.service.ProductSkuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController("/api/v1")
@RequiredArgsConstructor
@Validated
public class ProductSkuController {


    private final ProductSkuService productSkuService;

    @PostMapping("/product")
    public ResponseEntity<Object> createProduct(@RequestBody @Validated ProductCreateEditDto productCreateEditDto){
        Long productId = productSkuService.createProduct(productCreateEditDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", "product was created successfully",
                            "id", productId));
    }

    @PostMapping("/sku/{id}")
    public ResponseEntity<Object> createProductSku(@PathVariable Long id, @Validated SkuCreateEditDto skuCreateEditDto) {
        String skuCode = productSkuService.createSku(skuCreateEditDto, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("skuCode", skuCode,
                            "message", "sku was created successfully"));
    }

    @PostMapping("/load")
    public ResponseEntity<Object> loadProductsFromDb(@PathParam("active") Boolean active, @PathParam("startDate")LocalDate startDate){
        int numberOfProducts = productSkuService.loadProductsSkuToElastic();
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", "products was successfully loaded from db",
                        "products load number", numberOfProducts));
    }


}
