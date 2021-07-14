package com.chuansen.system.service.impl;

import com.chuansen.system.config.ElasticSearchClientConfig;
import com.chuansen.system.service.SearchRequestService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/13 17:09
 */

@Service
public class SearchRequestServiceImpl implements SearchRequestService {

    @Autowired
    private ElasticSearchClientConfig searchClientConfig;

    public SearchResponse searchIndexList(SearchSourceBuilder sourceBuilder,String indexName) {
        // 创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices(indexName);

        request.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = searchClientConfig.restHighLevelClient().search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
