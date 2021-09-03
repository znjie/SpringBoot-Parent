package com.chuansen.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/9/2 14:59
 */
@SpringBootApplication(scanBasePackages = "com.chuansen.system")
public class SpringBootMqttApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootMqttApplication.class,args);
    }
}
