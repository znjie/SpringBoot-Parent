package com.chuansen.system.service.impl;

import com.chuansen.system.config.ElasticSearchClientConfig;
import com.chuansen.system.model.SystemLog;
import com.chuansen.system.service.DocService;
import com.chuansen.system.util.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/13 14:42
 */
@Service
public class DocServiceImpl implements DocService {

    @Autowired
    private ElasticSearchClientConfig searchClientConfig;

    public String created(String indexName) {
        // 新增文档 - 请求对象
        IndexRequest request = new IndexRequest();
        // 设置索引及唯一性标识
        request.index(indexName).id("1001");
        // 创建数据对象
        SystemLog systemLog = new SystemLog(UUID.randomUUID().toString(), new Date(), null, "占传森", Constant.HANDLE_TYPE_1, "hello word", "how are you", "用户管理");
        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = null;
        try {
            productJson = objectMapper.writeValueAsString(systemLog);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 添加文档数据，数据格式为 JSON 格式
        request.source(productJson, XContentType.JSON);
        // 客户端发送请求，获取响应对象
        IndexResponse response = null;
        try {
            response = searchClientConfig.restHighLevelClient().index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.getId();
    }

    public String update(String indexName) {
        // 修改文档 - 请求对象
        UpdateRequest request = new UpdateRequest();
        // 配置修改参数
        request.index(indexName).id("1001");
        // 设置请求体，对数据进行修改
        request.doc(XContentType.JSON, "handleType", Constant.HANDLE_TYPE_2);
        UpdateResponse response = null;
        // 客户端发送请求，获取响应对象
        try {
            response = searchClientConfig.restHighLevelClient().update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.getId();
    }

    public SystemLog get(String indexName) {
        //1.创建请求对象
        GetRequest request = new GetRequest().index(indexName).id("1001");
        //2.客户端发送请求，获取响应对象
        GetResponse response = null;
        try {
            response = searchClientConfig.restHighLevelClient().get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        SystemLog systemLog = new SystemLog();
        try {
            systemLog = mapper.readValue(response.getSourceAsString(), SystemLog.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return systemLog;
    }

    public String delete(String indexName) {
        //创建请求对象
        DeleteRequest request = new DeleteRequest().index(indexName).id("1001");
        //客户端发送请求，获取响应对象
        DeleteResponse response = null;
        try {
            response = searchClientConfig.restHighLevelClient().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public String saveAll(String indexName) {
        //创建批量新增请求对象
        BulkRequest bulkRequest = new BulkRequest();
        List<SystemLog> logList=new ArrayList<SystemLog>();
        SystemLog systemLog1 = new SystemLog(UUID.randomUUID().toString(), new Date(), null, "占传森1", Constant.HANDLE_TYPE_1, "hello word", "how are you", "课程管理");
        SystemLog systemLog2 = new SystemLog(UUID.randomUUID().toString(), new Date(), null, "占传森2", Constant.HANDLE_TYPE_2, "hello word", "how are you", "视频管理");
        SystemLog systemLog3 = new SystemLog(UUID.randomUUID().toString(), new Date(), null, "占传森3", Constant.HANDLE_TYPE_1, "hello word", "how are you", "记录管理");
        SystemLog systemLog4 = new SystemLog(UUID.randomUUID().toString(), new Date(), null, "占传森4", Constant.HANDLE_TYPE_3, "hello word", "how are you", "菜单管理");
        SystemLog systemLog5 = new SystemLog(UUID.randomUUID().toString(), new Date(), null, "占传森5", Constant.HANDLE_TYPE_2, "hello word", "how are you", "角色管理");
        SystemLog systemLog6 = new SystemLog(UUID.randomUUID().toString(), new Date(), null, "占传森6", Constant.HANDLE_TYPE_1, "hello word", "how are you", "菜单管理");
        SystemLog systemLog7 = new SystemLog(UUID.randomUUID().toString(), new Date(), null, "占传森7", Constant.HANDLE_TYPE_3, "hello word", "how are you", "用户管理");
        SystemLog systemLog8 = new SystemLog(UUID.randomUUID().toString(), new Date(), null, "占传森8", Constant.HANDLE_TYPE_1, "hello word", "how are you", "课程管理");
        SystemLog systemLog9 = new SystemLog(UUID.randomUUID().toString(), new Date(), null, "占传森9", Constant.HANDLE_TYPE_3, "hello word", "how are you", "视频管理");
        logList.add(systemLog1);
        logList.add(systemLog2);
        logList.add(systemLog3);
        logList.add(systemLog4);
        logList.add(systemLog5);
        logList.add(systemLog6);
        logList.add(systemLog7);
        logList.add(systemLog8);
        logList.add(systemLog9);
        int key=1010;
        for (SystemLog systemLog:logList){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String productJson = objectMapper.writeValueAsString(systemLog);
                bulkRequest.add(new IndexRequest().index(indexName).id(String.valueOf(key)).source(productJson,XContentType.JSON));
                key++;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        /* ObjectMapper objectMapper = new ObjectMapper();
        try {
            String productJson1 = objectMapper.writeValueAsString(systemLog1);
            bulkRequest.add(new IndexRequest().index(indexName).id("1001").source(productJson1,XContentType.JSON));
            String productJson2 = objectMapper.writeValueAsString(systemLog2);
            bulkRequest.add(new IndexRequest().index(indexName).id("1002").source(productJson2,XContentType.JSON));
            String productJson3 = objectMapper.writeValueAsString(systemLog3);
            bulkRequest.add(new IndexRequest().index(indexName).id("1003").source(productJson3,XContentType.JSON));
            String productJson4 = objectMapper.writeValueAsString(systemLog4);
            bulkRequest.add(new IndexRequest().index(indexName).id("1004").source(productJson4,XContentType.JSON));
            String productJson5 = objectMapper.writeValueAsString(systemLog5);
            bulkRequest.add(new IndexRequest().index(indexName).id("1005").source(productJson5,XContentType.JSON));
            String productJson6 = objectMapper.writeValueAsString(systemLog6);
            bulkRequest.add(new IndexRequest().index(indexName).id("1006").source(productJson6,XContentType.JSON));
            String productJson7 = objectMapper.writeValueAsString(systemLog7);
            bulkRequest.add(new IndexRequest().index(indexName).id("1007").source(productJson7,XContentType.JSON));
            String productJson8 = objectMapper.writeValueAsString(systemLog8);
            bulkRequest.add(new IndexRequest().index(indexName).id("1008").source(productJson8,XContentType.JSON));
            String productJson9 = objectMapper.writeValueAsString(systemLog9);
            bulkRequest.add(new IndexRequest().index(indexName).id("1009").source(productJson9,XContentType.JSON));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
        //客户端发送请求，获取响应对象
        BulkResponse responses = null;
        try {
            responses = searchClientConfig.restHighLevelClient().bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responses.getItems().toString();
    }

    public String deleteAll(String indexName) {
        BulkRequest request = new BulkRequest();
        request.add(new DeleteRequest().index(indexName).id("1001"));
        request.add(new DeleteRequest().index(indexName).id("1002"));
        request.add(new DeleteRequest().index(indexName).id("1003"));
        BulkResponse responses = null;
        try {
            responses = searchClientConfig.restHighLevelClient().bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responses.getItems().toString();
    }


}
