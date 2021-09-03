package com.chuansen.system.controller;

import com.chuansen.system.config.MqttPushClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/9/2 14:54
 */

@RestController
@RequestMapping("/mqtt")
@Slf4j
public class MqttController {
    @Autowired
    private MqttPushClient mqttPushClient;

    @GetMapping(value = "/publishTopic")
    public ResponseEntity publishTopic() {
        String topicString = "testTopic";
        mqttPushClient.publish(0, false, topicString, "测试一下发布消息");
        return ResponseEntity.ok(HttpStatus.OK);
    }
    // 发送自定义消息内容（使用默认主题）
    @RequestMapping("/publishTopic")
    public ResponseEntity test1(String data) {
        String topicString = "testTopic";
        mqttPushClient.publish(0,false,topicString, data);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // 发送自定义消息内容，且指定主题
    @GetMapping("/publishTopic2")
    public ResponseEntity test2(String topic,String data) {
        mqttPushClient.publish(0,false,topic, data);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/connectMqtt")
    public ResponseEntity connectMqtt(String host,String clientId){
        mqttPushClient.connect(host,clientId);
        log.info("clientId:{}",mqttPushClient.getClient().getClientId());
        return  ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("/subscribeTopic")
    public ResponseEntity subscribeTopic(String topic){
        mqttPushClient.subscribe(topic,0);
        log.info("clientId:{}",mqttPushClient.getClient().getClientId());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
