package com.chuansen.system.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/9/2 14:40
 * 消息处理类(回调)
 */
@Slf4j
@Component
public class PushCallback implements MqttCallback {

    public String TOPIC = "testTopic";
    private MqttClient client;
    private MqttConnectOptions options;
    private ScheduledExecutorService scheduler;

    public PushCallback() {
    }

    public PushCallback(MqttClient client, MqttConnectOptions options, String TOPIC) {
        super();
        this.TOPIC = TOPIC;
        this.client = client;
        this.options = options;
    }



    @Override
    public void connectionLost(Throwable throwable) {
        while (true) {
            try {
                System.out.println("====连接失败重连");
                startReconnect();
                // 发布相关的订阅 String[] topic = {"test1"}; int[] qos = {1};
                // client.subscribe(topic, qos);
                break;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("====连接失败重连失败");
            }
        }
    }



    private void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!client.isConnected()) {
                    try {
                        client.connect(options);
                    } catch (MqttSecurityException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        },0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }


    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        log.info("=====接收消息主题 : " + topic);
        log.info("=====接收消息内容 : " + new String(mqttMessage.getPayload()));

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("=====deliveryComplete---------" + iMqttDeliveryToken.isComplete());
    }
}
