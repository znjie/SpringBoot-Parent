package com.chuansen.system.controller;

import com.chuansen.system.util.RedisUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/15 10:18
 * 缓存雪崩
 * 例如：我们设置缓存时采用了相同的过期时间，在同一时刻出现大面积的缓存过期
 * 所有原本应该访问缓存的请求都去查询数据库了，而对数据库CPU和内存造成巨大压力，严重的会造成数据库宕机。
 * 从而形成一系列连锁反应，造成整个系统崩溃。
 */
@RequestMapping("/avalanche")
@RestController
@Slf4j
public class AvalancheController {

    private Lock lock = new ReentrantLock();

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 雪崩解决 增加随机过期时间，减少缓存同时消失的概率
     *
     * @param key 缓存键
     * @return
     */
    @RequestMapping("/cacheBreakdown_1")
    public ResponseEntity cacheBreakdown_1(String key) {
        Object value = redisUtil.get(key);
        if (value != null) {
            return ResponseEntity.ok(value.toString());
        }
        String dataBaseValue = "数据库中的值";
        Random random = new Random(100);
        System.out.println("随机值 -- 获取数据库中的值");
        redisUtil.set(key, dataBaseValue, 60 + random.nextInt(6));
        return ResponseEntity.ok(dataBaseValue);
    }

    /**
     * 雪崩解决 hystrix 降级操作
     *
     * @param key
     * @return
     */
    @RequestMapping("/cacheBreakdown_2")
    @HystrixCommand(fallbackMethod = "hystrixString")
    public ResponseEntity cacheBreakdown_2(String key) throws Exception {
        Object value = redisUtil.get(key);
        if (value != null) {
            return ResponseEntity.ok(value.toString());
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
     * 雪崩解决， 加线程锁
     *
     * @param key 缓存键
     */
    @RequestMapping("/cacheBreakdown_3")
    public ResponseEntity cacheBreakdown_3(String key) throws InterruptedException {
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
        return cacheBreakdown_3(key);
    }

    /**
     * 雪崩解决 ，分布式锁，
     * 保证同一时刻只能有同一种key值能够进行 db数据的获取
     *
     * @param key 缓存键
     */
    @RequestMapping("/cacheBreakdown_4")
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
