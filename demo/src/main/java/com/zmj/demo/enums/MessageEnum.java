package com.zmj.demo.enums;

public enum MessageEnum {

    SYSTEM("1","系统消息"),
    ERROR_PLATFORM_100001("100001","添加失败"),
    ERROR_PLATFORM_100002("100002","删除失败"),
    ERROR_PLATFORM_100003("100003","修改失败"),
    ERROR_PLATFORM_100004("100004","查询失败");

    private String type;    //类型
    private String desc;    //描述

    private MessageEnum(String type,String desc){
        this.type=type;
        this.desc=desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

