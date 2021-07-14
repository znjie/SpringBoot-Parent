package com.chuansen.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/7 16:40
 */
@SpringBootApplication(scanBasePackages = "com.chuansen.system")
public class StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

}
