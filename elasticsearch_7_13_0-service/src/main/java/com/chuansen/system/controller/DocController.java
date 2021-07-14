package com.chuansen.system.controller;

import com.chuansen.system.model.SystemLog;
import com.chuansen.system.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/13 14:39
 */
@RestController
@RequestMapping("/doc")
public class DocController {

    @Value("${elasticsearch.index.name}")
    private String indexName;

    @Autowired
    private DocService docService;

    @RequestMapping("/created")
    public ResponseEntity created(){
        String resultId= docService.created(indexName);
        return ResponseEntity.ok(resultId);
    }

    @RequestMapping("/update")
    public ResponseEntity update(){
        String resultId= docService.update(indexName);
        return ResponseEntity.ok(resultId);
    }

    @RequestMapping("/get")
    public ResponseEntity get(){
        SystemLog systemLog= docService.get(indexName);
        return ResponseEntity.ok(systemLog);
    }

    @RequestMapping("/delete")
    public ResponseEntity delete(){
        String result= docService.delete(indexName);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/saveAll")
    public ResponseEntity saveAll(){
        String result= docService.saveAll(indexName);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/deleteAll")
    public ResponseEntity deleteAll(){
        String result= docService.deleteAll(indexName);
        return ResponseEntity.ok(result);
    }




}
