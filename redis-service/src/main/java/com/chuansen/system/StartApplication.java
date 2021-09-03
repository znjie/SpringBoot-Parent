package com.chuansen.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/7 16:40
 */
@SpringBootApplication
@EntityScan("com.chuansen.system")
public class StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

}




