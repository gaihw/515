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
     * 方法，请求方法：0-GET；1-POST'
     */
    private Integer method;

    /**
     * 请求类型，参数格式：0-FORM；1-JSON；2-TEXT；3-无'
     */
    private Integer contentType;
	
	/**
	 *请求头
	 */
	 private String headerData;

    /**
     * 请求体信息
     */
    private String paramData;

    /**
     *断言方式：0-包含；1-相等；2-无
     */
    private Integer assertType;

    /**
     * 断言数据
     */
    private String assertData;

}
