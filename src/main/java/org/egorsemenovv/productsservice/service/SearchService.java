package org.egorsemenovv.productsservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import org.egorsemenovv.productsservice.dto.ProductSkuReadDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchClient esClient;

    public List<ProductSkuReadDto> findByUserDesc(String description){
        // todo: findInElasticSearchIndex;
        return null;
    }

}
