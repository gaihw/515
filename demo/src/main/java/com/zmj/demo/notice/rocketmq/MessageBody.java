package com.zmj.demo.notice.rocketmq;

import lombok.Data;

/**
 * 消息实体类
 *
 * @author gaihw
 * @date 2023/5/17 11:27
 */
@Data
public class MessageBody {
    // 消息id
    private String messageId;
    // body组装时间
    private long timestamp;
    // 来源 附加信息
    private String msgSource;
    // overload
    private Object data;
    public MessageBody() {

    }
    public MessageBody(String msgKey, Object data, String msgSource) {
        this.messageId = msgKey;
        this.data = data ;
        this.msgSource = msgSource;
        this.timestamp = System.currentTimeMillis();
    }
}
