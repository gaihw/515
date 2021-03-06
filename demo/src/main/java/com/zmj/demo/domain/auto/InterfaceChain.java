package com.zmj.demo.domain.auto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class InterfaceChain {

    /**
     * 主键id
     */
    private int id;

    /**
     * 对应的平台ID
     */
    private Integer platformManageID;
	
	private String platform;
	private String project;
	private String module;

    /**
     * 接口名称
     */
    private String name;

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
