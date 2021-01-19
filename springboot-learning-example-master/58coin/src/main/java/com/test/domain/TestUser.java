package com.test.domain;

import lombok.Data;

import java.util.Date;

@Data
public class TestUser {
    private int id;
    private String userName;
    private String name;
    private String password;
    private String phone;
    private int isDelete;
    private Date createTime;
    private Date updateTime;
}
