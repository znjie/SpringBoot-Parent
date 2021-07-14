package com.chuansen.system.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/7 16:11
 */
@Configuration
public class ElasticSearchClientConfig {

    @Value("${elasticsearch.hostname}")
    private String hostname;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(hostname, 9200, "http")));
    }
}
