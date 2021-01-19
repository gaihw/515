package com.mutest.model;

import lombok.Data;

/**
 * @author muguozheng
 * @date 2020/6/22 21:12
 * @Description: 短信验证码实体
 * @modify
 */
@Data
public class ShortMessage {
    private Long id;
    private String mobile;
    private String keywords;
    private String sendTime;
}
