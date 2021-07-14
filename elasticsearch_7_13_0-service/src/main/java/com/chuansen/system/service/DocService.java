package com.chuansen.system.service;

import com.chuansen.system.model.SystemLog;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/13 14:41
 */
public interface DocService {
    String created(String indexName);

    String update(String indexName);

    SystemLog get(String indexName);

    String delete(String indexName);

    String saveAll(String indexName);

    String deleteAll(String indexName);
}
