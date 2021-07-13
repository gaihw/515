package com.zmj.demo.enums;

public enum MessageEnum {

    SYSTEM("1","系统消息"),
    ERROR_PLATFORM_100001("100001","添加失败"),
    ERROR_PLATFORM_100002("100002","删除失败"),
    ERROR_PLATFORM_100003("100003","修改失败"),
    ERROR_PLATFORM_100004("100004","查询失败"),
    ERROR_CASE_EXCEL("200001","导入文件为空!"),
    ERROR_CASE_EXECUTE("300001","用例执行失败!");

    private String code;    //类型
    private String desc;    //描述

    private MessageEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String type) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

