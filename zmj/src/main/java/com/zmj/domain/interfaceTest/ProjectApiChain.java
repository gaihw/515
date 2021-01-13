package com.zmj.domain.interfaceTest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ProjectApiChain {

    /**
     * 主键id
     */
    private int id;
    /**
     * 关联外键
     */
    private int platformId;

    /**
     * 平台
     */
    private String platform;
    /**
     * 项目
     */
    private String project;

    /**
     * 模块
     */
    private String module;

    /**
     * name
     */
    private String name;
    /**
     * path
     */
    private String path;
    /**
     * method
     */
    private String method;
    /**
     * param
     */
    private String param;
    /**
     * 是否需要token
     */
    private int isToken;


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
    @JsonFormat (timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;
}
