package com.zmj.demo.domain.auto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CaseExcelChain {

    /**
     * 用例编号
     */
    private Integer interfaceManageID;

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
     *断言方式
     */
    private String assertType;

    /**
     * 断言数据
     */
    private String assertData;

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
    private String createDate;

    /**
     * 修改时间
     */
    private String updateDate;
}
