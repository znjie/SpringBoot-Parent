package com.chuansen.system.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/9/2 14:15
 */
@Slf4j
@Configuration
public class MqttConfig {

    @Autowired
    private MqttPushClient mqttPushClient;

    @Value("${mqtt.serverUrl}")
    private String hostUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.topic}")
    private String topic;

    @Bean
    public MqttPushClient getMqttPushClient() {
        mqttPushClient.connect(hostUrl, clientId);
        // 以/#结尾表示订阅所有以test开头的主题
        mqttPushClient.subscribe(topic, 0);
        return mqttPushClient;
    }



}
