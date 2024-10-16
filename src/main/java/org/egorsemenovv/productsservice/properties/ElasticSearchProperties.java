package org.egorsemenovv.productsservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchProperties {

    private String url;

}
