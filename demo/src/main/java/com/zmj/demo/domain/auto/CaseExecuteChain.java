package com.zmj.demo.domain.auto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CaseExecuteChain {

    /**
     * 域名
     */
    private String ip;

    /**
     * 路径
     */
    private String path;

    /**
     * 方法
     */
    private String method;

    /**
     * 请求类型
     */
    private String contentType;
	
	/**
	 *请求头
	 */
	 private String headerData;

    /**
     * 请求体信息
     */
    private String paramData;

    /**
     *断言方式
     */
    private String assertType;

    /**
     * 断言数据
     */
    private String assertData;

}
