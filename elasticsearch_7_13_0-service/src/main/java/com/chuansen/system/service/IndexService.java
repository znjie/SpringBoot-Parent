package com.chuansen.system.service;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/7 16:21
 */

public interface IndexService {
    boolean created(String indexName);

    boolean delete(String indexName);
}
