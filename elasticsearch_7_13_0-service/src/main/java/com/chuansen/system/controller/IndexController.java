package com.chuansen.system.controller;

import com.chuansen.system.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/7 16:47
 */
@Slf4j
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Value("${elasticsearch.index.name}")
    private String indexName;

    @RequestMapping("/created")
    public ResponseEntity created(){
       log.info(indexName);
       boolean flag= indexService.created(indexName);
       return ResponseEntity.ok(flag);
    }

    @RequestMapping("/delete")
    public ResponseEntity delete(){
        log.info(indexName);
        boolean flag= indexService.delete(indexName);
        return ResponseEntity.ok(flag);
    }




}
