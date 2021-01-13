package com.test.domain;

import lombok.*;

import java.util.Date;

/**
 * erc20币种配置
 *
 * @author lizhen
 * @date 2018-04-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Erc20Config {
    /**
     * 主键，自增
     */
    private Integer id;
    /**
     * 币种id
     */
    private Integer currencyId;
    /**
     * 发币地址
     */
    private String fromAddress;
    /**
     * 全名
     */
    private String fullName;
    /**
     * 名称
     */
    private String name;
    /**
     * 公司名
     */
    private String companyName;
    /**
     * 合约地址
     */
    private String contractAddress;
    /**
     * 精度
     */
    private Integer weiDecimal;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date modifyDate;
}