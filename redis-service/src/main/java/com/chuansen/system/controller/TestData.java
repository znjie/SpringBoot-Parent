package com.chuansen.system.controller;

import com.chuansen.system.entity.SystemLog;
import com.chuansen.system.service.SystemLogService;
import com.chuansen.system.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/8/2 17:08
 */
@RequestMapping("/data")
@RestController
public class TestData {

    @Resource
    private SystemLogService systemLogService;

    @Autowired
    private RedisUtil redisUtil;


    @RequestMapping("/avalanche_1")
    public ResponseEntity avalanche_1() {
        List<SystemLog> systemLogs = systemLogService.findAll();
        redisUtil.lSet("avalanche_list_value", systemLogs, 72000);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/findAll_1")
    public ResponseEntity findAll_1() {
        List<Object> logList = redisUtil.lGet("avalanche_list_value", 0, -1);
        return ResponseEntity.ok(logList);
    }

    @RequestMapping("/get")
    public ResponseEntity get(String id) {
        SystemLog systemLog = systemLogService.get(id).orElse(null);
        redisUtil.lSet("avalanche_get", systemLog, 72000);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @RequestMapping("/test")
    public ResponseEntity test() {
        List<SystemLog> systemLogList = new ArrayList();
        for (int i = 1; i < 20000; i++) {
            SystemLog systemLog = new SystemLog();
            systemLog.setId(UUID.randomUUID().toString());
            systemLog.setCreated(new Date());
            systemLog.setUpdated(new Date());
            systemLog.setUsername("占" + i);
            systemLog.setHandleType(0);
            systemLog.setBeforeContent("XXX");
            systemLog.setAfterContent("YYY");
            systemLog.setModelCnName("ZZZ");
            systemLogList.add(systemLog);
        }
        return ResponseEntity.ok(systemLogService.saveAll(systemLogList));
    }


}
