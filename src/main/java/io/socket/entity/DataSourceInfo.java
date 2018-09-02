package io.socket.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DataSourceInfo implements Serializable {

    // id
    private Integer id;
    // 数据库编号
    private String databaseCode;
    // 数据库说明
    private String databaseDescription;
    // 账户
    private String account;
    // 密码
    private String password;
    // 数据量
    private Integer dataQuantity;
    // 表结构说明
    private String tableDtructureDescription;
    // 备注
    private String remarks;
    // 创建时间
    private Date gmtCreate;
    // 修改时间
    private Date gmtModified;
    // 是否删除默认0，1表示软删除
    private Integer isDel;
}
