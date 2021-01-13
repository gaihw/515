package com.zmj.commom.xrp;

import com.alibaba.fastjson.JSONObject;
import com.ripple.core.coretypes.AccountID;
import com.ripple.core.coretypes.Amount;
import com.ripple.core.coretypes.uint.UInt32;
import com.ripple.core.types.known.tx.signed.SignedTransaction;
import com.ripple.core.types.known.tx.txns.Payment;
import com.zmj.commom.HttpUtils;
import com.zmj.config.Config;
import com.zmj.domain.xrp.XrpChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class XrpUtils {

    @Autowired
    private HttpUtils httpUtils;


    /**
     * 交易
     * @param xrpChain
     * @return
     */
    public String transaction(XrpChain xrpChain){

        String tx_blob = sign(Config.XRP_FROM_ADDRESS, Config.XRP_FROM_ADDRESS_SECRET, xrpChain.getToAddress(), xrpChain.getAmount(), Config.XRP_FEE,xrpChain.getMemo());
        log.info("tx_blob is : {}",tx_blob);

        String submitResponse = submit(tx_blob);
        log.info("submit result is : {}",submitResponse);

        return submitResponse;
    }

    /**
     * 签名
     * @param fromAddress
     * @param secret
     * @param toAddress
     * @param amount
     * @param fee
     * @param memo
     * @return
     */
    public  String sign(String fromAddress, String secret, String toAddress, BigDecimal amount, BigDecimal fee, String memo) {
        JSONObject re = JSONObject.parseObject(getAccountInfo(fromAddress));
        if (re != null) {
            JSONObject result = re.getJSONObject("result");
            if ("success".equals(result.getString("status"))) {
                Payment payment = new Payment();
                payment.as(AccountID.Account, fromAddress);
                payment.as(AccountID.Destination, toAddress);
                payment.as(UInt32.DestinationTag, memo);
                payment.as(Amount.Amount, amount.toString());
                payment.as(UInt32.Sequence, result.getJSONObject("account_data").getString("Sequence"));
                payment.as(UInt32.LastLedgerSequence, result.getString("ledger_current_index") + 4);
                payment.as(Amount.Fee, fee.toString());
                SignedTransaction signed = payment.sign(secret);
                if (signed != null) {
                    return signed.tx_blob;
                }
            }
        }
        return null;
    }

    /**
     * 签名后，获取到到tx_blob,发送该笔签名的交易
     * @param tx_blob
     */
    public  String submit(String tx_blob){

        String url = Config.XRP_URL;
        String params = "{\"method\":\"submit\",\"params\":[{\"tx_blob\":\""+tx_blob+"\"}]}";
        return httpUtils.postByJson(url,params);
    }

    /**
     * 查询账户信息
     * @param account
     * @return
     */
    public  String getAccountInfo(String account) {
        String url = Config.XRP_URL;
        String params = "{\"method\": \"account_info\",\"params\": [{\"account\": \""+account+"\",\"strict\": true,\"ledger_index\": \"current\",\"queue\": true}]}";
        String res = httpUtils.postByJson(url,params);
        log.info("account info is : {}",res);
        return res;
    }

    /**
     * 查询交易信息  "method": "tx"
     * @param transaction
     */
    public String getTransaction(String transaction){
        String url = Config.XRP_URL;
        String params = "{\"method\": \"tx\",\"params\": [{\"transaction\": \""+transaction+"\",\"binary\": false}]}";
        String res = httpUtils.postByJson(url,params);
        log.info("transaction is : {}",res);
        return res;
    }
}
