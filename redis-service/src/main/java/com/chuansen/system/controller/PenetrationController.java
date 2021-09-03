package com.chuansen.system.controller;

import com.chuansen.system.util.RedisConstant;
import com.chuansen.system.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/15 10:19
 * 服务穿透
 * 请求数据库不存在的值，redis失去作用
 */
@RequestMapping("/penetration")
@RestController
@Slf4j
public class PenetrationController {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 缓存穿透 设置空对象
     *
     * @param key 缓存键
     * @return
     */
    public ResponseEntity cachePenetrate_1(String key) {
        Object value = redisUtil.get(key);
        if ("".equals(value)) {
            return null;
        }
        if (value != null) {
            return ResponseEntity.ok(value.toString());
        }
        String dataBaseValue = "数据库中的值";
        System.out.println("空对象 -- 获取数据库中的值");
        if (dataBaseValue == null) {
            redisUtil.set(key, "", 60);
        } else {
            redisUtil.set(key, dataBaseValue, 60);
        }
        return ResponseEntity.ok(dataBaseValue);
    }

    /**
     * 缓存穿透 布隆过滤器
     *
     * @param key 缓存键
     * @return
     */
    public ResponseEntity cachePenetrate_2(String key) {
        if (RedisConstant.bloomFilter.mightContain(key)) {
            Object value = redisUtil.get(key);
            if (value != null) {
                return ResponseEntity.ok(value.toString());
            }
            String dataBaseValue = "数据库中的值";
            System.out.println("布隆过滤器 -- 获取数据库中的值");
            redisUtil.set(key, dataBaseValue, 60);
            return ResponseEntity.ok(dataBaseValue);
        } else {
            System.out.println("该key不存在");
            return null;
        }
    }
}
