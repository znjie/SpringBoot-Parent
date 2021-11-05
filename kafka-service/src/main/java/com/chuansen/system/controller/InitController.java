package com.chuansen.system.controller;

import com.chuansen.system.service.LogProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class InitController {

    @Autowired
    private LogProducerService logProducerService;

    @RequestMapping("/init")
    public ResponseEntity init() {
        for (int i = 0; i < 10; i++) {
            //调用消息发送类中的消息发送方法
            logProducerService.sendlog(String.valueOf(i));
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
