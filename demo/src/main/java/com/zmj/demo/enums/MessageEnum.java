package com.zmj.demo.enums;

public enum MessageEnum {

    SYSTEM("1","系统消息"),
    ERROR_PLATFORM("100001","添加数据失败");

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

class MyTestTwo{
    public static void main(String[] args){
//        MessageEnum message=MessageEnum.ERROR_PLATFORM;    //每个枚举成员实际上是一个枚举实例
        System.out.println(MessageEnum.ERROR_PLATFORM.getType());
        System.out.println(MessageEnum.ERROR_PLATFORM.getDesc());
    }
}

