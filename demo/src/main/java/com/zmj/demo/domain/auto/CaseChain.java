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
	
	private String platform;
	private String project;
	private String module;
	private String name;

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
