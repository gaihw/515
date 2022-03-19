package com.test;

import lombok.Data;

@Data
public class EnvEntity {
    private String sender;
    private String subject;
    private String content;
    private String receiverList;
    private String account;
    private String password;
    private String host;
    private String port;
    private String protocol;
}
