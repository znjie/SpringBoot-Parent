package com.chuansen.system.controller;

import com.alibaba.fastjson.JSON;
import com.chuansen.system.config.RedisAsync;
import com.chuansen.system.util.RedisUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/15 10:20
 * 服务击穿
 * 缓存中没有，数据库中有，（一般为缓存时间到期）
 */
@RequestMapping("/breakdown")
@RestController
@Slf4j
public class BreakdownController {

    private Lock lock = new ReentrantLock();

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisAsync redisAsync;

    /**
     * 缓存击穿   加线程锁
     *
     * @param key 缓存键
     */
    public ResponseEntity cacheBreakdown_1(String key) throws InterruptedException {
        Object value = redisUtil.get(key);
        if (value != null) {
            return ResponseEntity.ok(value.toString());
        }
        //获得锁
        if (lock.tryLock()) {
            String dataBaseValue;
            try {
                //数据库获取，并存在redis 中
                dataBaseValue = "数据库查询结果";
                System.out.println("互斥锁 -- 获取数据库中的值");
                redisUtil.set(key, dataBaseValue, 60);
            } finally {
                lock.unlock();
            }
            return ResponseEntity.ok(dataBaseValue);
        }
        TimeUnit.SECONDS.sleep(5);
        return cacheBreakdown_1(key);
    }

    /**
     * 缓存击穿
     * 热点数据永不过期 设置永久性，并在value中添加过期时间，用于过期更新key值操作
     * 给每个缓存增加是否失效标记，失效后马上刷新缓存 或者预先更新
     *
     * @param key 缓存键
     * @return
     */
    public ResponseEntity cacheBreakdown_2(String key) {
        Object value = redisUtil.get(key);
        if (value != null) {
            Map<String, String> map = JSON.parseObject(value.toString(), HashMap.class);
            LocalDateTime ldt = LocalDateTime.parse(map.get("expire"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (ldt.isBefore(LocalDateTime.now())) {
                redisAsync.refreshAsync(key, map);
            }
            return ResponseEntity.ok(map.get("value"));
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("value", "数据库中的值");
            map.put("expire", "2019-04-17 11:30:00");
            System.out.println("永久性 -- 获取数据库中的值");
            String dataBaseValue = JSON.toJSONString(map);
            redisUtil.set(key, dataBaseValue);
            return ResponseEntity.ok(dataBaseValue);
        }
    }

    /**
     * 缓存击穿 hystrix 降级操作
     *
     * @param key
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixString")
    public String cacheBreakdown_3(String key) throws Exception {
        Object value = redisUtil.get(key);
        if (value != null) {
            return value.toString();
        }
        String dataBaseValue = "数据库中的值";
        System.out.println("hystrix -- 获取数据库中的值");
        redisUtil.set(key, dataBaseValue, 60);
        throw new Exception("adfs");
    }

    public String hystrixString(String key) {
        return "服务器繁忙！";
    }


    /**
     * 缓存击穿,分布式锁，
     * 保证同一时刻只能有同一种key值能够进行 db数据的获取
     *
     * @param key 缓存键
     */
    public ResponseEntity cacheBreakdown_4(String key) {
        System.out.println("线程开始");
        Object value = redisUtil.get(key);
        if (value != null) {
            return ResponseEntity.ok(value.toString());
        }
        try {
            Boolean isSuccess = redisUtil.setIfAbsent("LOCAL:" + key, "easy");
            redisUtil.expire("LOCAL" + key, 10);
            log.info(Thread.currentThread().getName() + "isSuccess : " + isSuccess);
            if (isSuccess) {
                //数据库获取信息，并放进缓存中
                String dataBaseValue = "数据库查询结果";
                System.out.println("分布式锁 -- 获取数据库中的值");
                redisUtil.set(key, dataBaseValue, 60);
                return ResponseEntity.ok(dataBaseValue + "\t 数据库");
            }
            return cacheBreakdown_4(key);
        } catch (Exception e) {
            log.error("cacheBreakdown_2 is error , key : {} ", key, e);
        } finally {
            redisUtil.del("LOCAL");
        }
        return null;
    }
}
