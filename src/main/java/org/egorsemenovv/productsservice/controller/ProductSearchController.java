package org.egorsemenovv.productsservice.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.egorsemenovv.productsservice.dto.ProductSkuDocument;
import org.egorsemenovv.productsservice.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/search")
public class ProductSearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<Object> findByKeyword(@PathParam("keyword") String keyword){
        List<ProductSkuDocument> productsWithSku = searchService.searchDocuments(keyword);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("found products", productsWithSku));
    }
}
