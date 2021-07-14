package com.chuansen.system.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/13 14:32
 */
@Data
public class SystemLog {
    private String id;
    private Date created;
    private Date updated;
    /*
     * 用户名
     */
    private String username;
    /*
     * 操作类型
     */
    private Integer handleType;
    /*
     * 操作前内容
     */
    private String beforeContent;
    /*
     * 操作后内容
     */
    private String afterContent;
    /*
     * 模块中文名
     */
    private String modelCnName;

    public SystemLog() {
    }

    public SystemLog(String id, Date created, Date updated, String username, Integer handleType, String beforeContent, String afterContent, String modelCnName) {
        this.id = id;
        this.created = created;
        this.updated = updated;
        this.username = username;
        this.handleType = handleType;
        this.beforeContent = beforeContent;
        this.afterContent = afterContent;
        this.modelCnName = modelCnName;
    }
}
