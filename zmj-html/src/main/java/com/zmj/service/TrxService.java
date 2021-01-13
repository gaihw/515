package com.zmj.service;

import com.zmj.domain.trx.TrxChain;

public interface TrxService {
    /**
     * 生成地址，并生成指定的私钥
     */
    String generateAddress();

    /**
     * 交易
     * @param trxChain
     * @return
     */
    String createtransaction(TrxChain trxChain);

    /**
     * 检查地址是否有效
     * @param address
     * @return
     */
    String validateAddress(String address);

    /**
     * 查询交易具体信息
     * @param txid
     * @return
     */
    String gettransactioninfobyid(String txid);

    /**
     * 查询交易信息
     * @param txid
     * @return
     */
    String gettransactionbyid(String txid);

    /**
     * 查询账户资源
     * @param address
     * @return
     */
    String getaccountresource(String address);

    /**
     * 查询合约信息
     * @param address
     * @return
     */
    String getcontract(String address);

    /**
     * 查询账户余额
     * @param address
     * @return
     */
    String getaccount(String address);

    /**
     * 冻结 TRX, 获取带宽或者能量(投票权)
     * @return
     */
    String freezebalance(TrxChain trxChain);

    /**
     *
     * 在链上创建账号. 一个已经激活的账号创建一个新账号需要花费 0.1 TRX 或等值 Bandwidth.
     * @return
     */
    String createaccount(TrxChain trxChain);
}
