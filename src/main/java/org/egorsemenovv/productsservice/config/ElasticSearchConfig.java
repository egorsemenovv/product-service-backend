package org.egorsemenovv.productsservice.config;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.egorsemenovv.productsservice.properties.ElasticSearchProperties;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class ElasticSearchConfig {

    private final ElasticSearchProperties esProperties;

    @Bean
    public ElasticsearchClient esClient() {
        RestClient restClient = RestClient
                .builder(HttpHost.create(esProperties.getUrl()))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }

}
