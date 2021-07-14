package com.chuansen.system.service.impl;

import com.chuansen.system.config.ElasticSearchClientConfig;
import com.chuansen.system.service.IndexService;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/7 16:21
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private ElasticSearchClientConfig searchClientConfig;

    public boolean created(String indexName) {
        // 创建索引 - 请求对象
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        // 发送请求，获取响应
        CreateIndexResponse response = null;
        try {
            response = searchClientConfig.restHighLevelClient().indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.isAcknowledged();
    }

    public boolean delete(String indexName) {
        // 删除索引 - 请求对象
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
         // 发送请求，获取响应
        AcknowledgedResponse response = null;
        try {
            response = searchClientConfig.restHighLevelClient().indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.isAcknowledged();
    }
}
