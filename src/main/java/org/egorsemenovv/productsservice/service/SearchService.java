package org.egorsemenovv.productsservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egorsemenovv.productsservice.dto.ProductSkuDocument;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final ElasticsearchClient esClient;

    public List<ProductSkuDocument> searchDocuments(String keyword) {
        String indexName = "products_skus_index";
        List<ProductSkuDocument> results = new ArrayList<>();
        try {
            MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(fn -> fn
                    .fields(List.of("name^3", "description^2", "color", "code"))
                    .query(keyword)
                    .type(TextQueryType.BestFields));
            SearchRequest searchRequest = SearchRequest.of(fn -> fn
                    .index(indexName)
                    .query(q -> q.multiMatch(multiMatchQuery)));
            SearchResponse<ProductSkuDocument> response = esClient.search(searchRequest, ProductSkuDocument.class);
            for (Hit<ProductSkuDocument> hit : response.hits().hits()) {
                results.add(hit.source());
            }
        }
        catch (IOException e) {
            log.error("failed to search products in index '{}' for keyword '{}'", indexName, keyword);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return results;
    }

}
