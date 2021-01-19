package com.mutest.advice;

/**
 * @author guozhengMu
 * @version 1.0
 * @date 2019/08/09 11:28
 * @description 自定义业务异常
 * @modify
 */
public enum BusinessMsgEnum {
    /**
     * 参数异常
     */
    PARMETER_EXCEPTION("102", "参数异常!"),
    /**
     * 等待超时
     */
    SERVICE_TIME_OUT("103", "服务调用超时！"),
    /**
     * 参数过大
     */
    PARMETER_BIG_EXCEPTION("102", "输入的图片数量不能超过50张!"),
    /**
     * 数据库操作失败
     */
    DATABASE_EXCEPTION("509", "数据库操作异常，请联系管理员！"),
    /**
     * 数据库操作失败
     */
    NULLPOINTER_EXCEPTION("10091", "空指针异常！"),
    /**
     * 数据库操作失败
     */
    NULLCASE_EXCEPTION("10092", "请添加用例！"),
    /**
     * 500 : 一劳永逸的提示也可以在这定义
     */
    UNEXPECTED_EXCEPTION("500", "系统发生异常，请联系管理员！");

    /**
     * 消息码
     */
    private String code;
    /**
     * 消息内容
     */
    private String msg;

    BusinessMsgEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
