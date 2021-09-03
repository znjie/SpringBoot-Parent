package com.chuansen.system.dao;

import com.chuansen.system.entity.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/8/2 18:03
 */
@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, java.lang.String> {
}
