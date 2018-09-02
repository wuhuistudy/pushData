package io.socket.entity;

import java.io.Serializable;
import java.util.Date;

public class PushTimeRecord implements Serializable {
    // id
    private Integer id;
    // 推送时间
    private Date pushTime;
    // 备注
    private String remarks;
    // 创建时间
    private Date gmtCreate;
    // 修改时间
    private Date gmtModified;
    // 是否删除默认0，1表示软删除
    private Integer isDel;
}
