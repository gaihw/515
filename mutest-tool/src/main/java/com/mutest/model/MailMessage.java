package com.mutest.model;

import lombok.Data;

@Data
public class MailMessage {
    private Long userId;
    private String email;
    private String keywords;
    private String code;
    private String sendTime;
}
