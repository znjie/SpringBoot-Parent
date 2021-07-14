package com.chuansen.system.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/13 17:08
 */
public interface SearchRequestService {
    SearchResponse searchIndexList(SearchSourceBuilder sourceBuilder, String indexName);
}
