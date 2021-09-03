package com.chuansen.system.config;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * @Author:chuansen.zhan
 * @Date: 2021/8/4 19:23
 */

@Component
public class RedisAsync {

    @Autowired
    private RedisTemplate redisTemplate;

    @Async
    public void refreshAsync(String key, Map<String,String> map){
        LocalDateTime ldt= LocalDateTime.now();
        ldt=ldt.plusHours(1);
        map.put("value","数据库中的值");
        map.put("expire", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(ldt));
        String dataBaseValue = JSON.toJSONString(map);
        System.out.println("异步 -- 获取数据库中的值");
        redisTemplate.opsForValue().set(key,dataBaseValue);
    }
}
