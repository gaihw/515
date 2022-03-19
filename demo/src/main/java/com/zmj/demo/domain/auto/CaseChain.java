package com.zmj.demo.domain.auto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CaseChain {

    /**
     * 主键id
     */
    private int id;

    /**
     * 对应的接口ID
     */
    private Integer interfaceManageID;

    /**
     * 平台名称
     */
	private String platform;

    /**
     * 项目编号
     */
    private Integer platformId;

    /**
     * 接口编号
     */
    private Integer interfaceId;

    /**
     * 项目名称
     */
	private String project;

    /**
     * 模块名称
     */
	private String module;

    /**
     * 接口名称
     */
	private String name;
    /**
     * IP
     */
    private String ip;

    /**
     * 接口路径
     */
    private String path;

    /**
     * 接口方法，请求方法：0-GET；1-POST'
     */
    private String method;
    /**
     * 参数格式：0-FORM；1-JSON；2-TEXT；3-无'
     */
    private String contentType;

    /**
     * 用例名称
     */
    private String caseName;

    /**
     * 请求头信息
     */
    private String headerData;

    /**
     * 请求体信息
     */
    private String paramData;

    /**
     *断言方式：0-包含；1-相等；2-无
     */
    private String assertType;

    /**
     * 断言数据
     */
    private String assertData;

    /**
     * 是否成功,0-未执行；1-未通过；2-通过
     */
    private Integer isSuccess;

    /**
     * 结果
     */
    private String result;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 备注
     */
    private String state;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     *
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
}
