package com.zmj.domain;

import lombok.Data;

@Data
public class JsonResult<T> {
    /**
     * 状态码
     */
    protected long code;
    /**
     * 响应信息
     */
    protected String msg;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 返回数据数量
     */
    private long count;

    public JsonResult(long code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(long code, T data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 有数据返回时，状态码为 0，默认提示信息为“操作成功！”
     *
     * @param data 响应数据
     */
    public JsonResult(T data) {
        this.data = data;
        this.code = 0;
        this.msg = "操作成功！";
    }

    /**
     * 有数据返回，状态码为 0，人为指定提示信息
     *
     * @param data 响应数据
     * @param msg  响应信息
     */
    public JsonResult(T data, String msg) {
        this.data = data;
        this.code = 0;
        this.msg = msg;
    }

    public JsonResult(T data, String msg, long count) {
        this.data = data;
        this.code = 0;
        this.msg = msg;
        this.count = count;
    }
}
