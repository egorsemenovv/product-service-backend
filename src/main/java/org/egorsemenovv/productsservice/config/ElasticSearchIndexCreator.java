package org.egorsemenovv.productsservice.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchIndexCreator {

    private final ElasticsearchClient esClient;

    @PostConstruct
    public void createIndex() {
        String indexName = "products_skus_index";

        try {
            boolean exists = indexExists(indexName);
            if (!exists) {
                CreateIndexRequest request = new CreateIndexRequest.Builder()
                        .index(indexName)
                        .settings(new IndexSettings.Builder()
                                .numberOfShards("1")
                                .numberOfReplicas("1")
                                .build())
                        .mappings(TypeMapping.of(fn -> fn
                                .properties("name", name -> name.text(text -> text))
                                .properties("description", desc -> desc.text(text -> text))
                                .properties("price", price -> price.double_(number -> number))
                                .properties("active", active -> active.boolean_(bool -> bool))
                                .properties("startDate", stDate -> stDate.date(date -> date))
                                .properties("skuCode", code -> code.text(text -> text))
                                .properties("stock", stock -> stock.long_(number -> number))
                                .properties("color", color -> color.text(text -> text))
                        ))
                        .build();

                CreateIndexResponse response = esClient.indices().create(request);

                if (response.acknowledged()) {
                    log.info("Index '{}' created successfully", indexName);
                } else {
                    throw new RuntimeException("Index creation failed for '" + indexName + "'");
                }
            }
        } catch (IOException e) {
            log.error("Failed to create index '{}'", indexName);
            throw new RuntimeException("Index creation failed for '" + indexName + "'");
        }

    }

    private boolean indexExists(String indexName) throws IOException {
        return esClient.indices().exists(e -> e.index(indexName)).value();
    }
}
