package com.chuansen.system.service;

import com.chuansen.system.dao.SystemLogRepository;
import com.chuansen.system.entity.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/28 15:12
 */
@Service
public class SystemLogService extends BaseService<SystemLog,java.lang.String>{

    @Resource
    private SystemLogRepository systemLogRepository;

    @Override
    public JpaRepository<SystemLog, String> getJpaRepository() {
        return systemLogRepository;
    }
}
