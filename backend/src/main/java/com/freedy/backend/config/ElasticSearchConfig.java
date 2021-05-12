package com.freedy.backend.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @author Freedy
 * @date 2021/5/1 11:15
 */
@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    @Value("${easy.elastic-search.addr}")
    private String addr;
    @Value("${easy.elastic-search.port}")
    private String port;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(addr+":"+port)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
