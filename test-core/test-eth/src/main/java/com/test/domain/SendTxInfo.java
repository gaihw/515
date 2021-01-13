package com.test.domain;

import lombok.*;

/**
 * 打包发币tx信息
 *
 * @author lizhen
 * @date 2018-04-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SendTxInfo {
    /**
     * 主键，自增
     */
    private Integer id;
    /**
     * 币种id
     */
    private Integer currencyId;
    /**
     * 交易txid
     */
    private String txid;
    /**
     * 签名交易数据
     */
    private String transHex;
}