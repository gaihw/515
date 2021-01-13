package blockchain.xrp;

import blockchain.Config;
import blockchain.Utils;
import blockchain.algo.Algo;
import com.alibaba.fastjson.JSONObject;
import com.ripple.core.coretypes.AccountID;
import com.ripple.core.coretypes.Amount;
import com.ripple.core.coretypes.uint.UInt32;
import com.ripple.core.types.known.tx.signed.SignedTransaction;
import com.ripple.core.types.known.tx.txns.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static blockchain.Config.XRP_URL;


public class Xrp {
    public static String fileName = "xrp";
    static {
        System.setProperty("fileName", "xrp/xrp.log");
    }
    public static Logger log = LoggerFactory.getLogger(Xrp.class);
    public static void main(String[] args) throws IOException {
        String fromAddress = "rsYLXcRNUNfu33tA8Chr6oyvbw83pTYLkK";
        String secret = "snWY8wzD14NJnm74SNxQBkmaYj6Mx";
        String toAddress = Config.XRP_COLD_58_ACCOUNT;
        //热钱包地址
        String address = "rNwryt9zV48Brnooznef5C229EoNmpZpgd";//174.627932-20.1092=154.518632
        String memo = "110";
        //精度是6位，100的话，会转化成0.0001；100.11会保留原数值
        BigDecimal value = BigDecimal.valueOf(31.542345);
        BigDecimal fee = BigDecimal.valueOf(0.00001);

        //交易
//        for (int i = 0; i < 1; i++) {
//            System.out.println(createtransaction(fromAddress, secret, toAddress, BigDecimal.valueOf(Math.random()*50).setScale(4, RoundingMode.HALF_UP), fee, memo));
//        }
        System.out.println(createtransaction(fromAddress, secret, address, BigDecimal.valueOf(1.362675), fee, memo));
//        System.out.println(submit("12000022800000002400C4E8232E00018770201B07CBE4C461400000000014CAF368400000000000000A73210302B5662A273EDC056E5505378540B55558F9D62623668467F16F2B4A69BD312D7446304402203B35A2EF6428917329593F798E609D2A927465FB95065D0B3B9E42772C5B258702207A9B72A7AB36AC6916917883C330B3A069D5991576BA8F627619121B3ECCA70381141BDD72852915835FBC9DFFBAAC25B8060B86B3C28314904E329029B6D26F7C4BC471DC700855FB3BAF74"));
//        System.out.println(createtransaction(fromAddress, secret, toAddress, BigDecimal.valueOf(2.819384), fee, memo));
//        System.out.println(createtransaction(fromAddress, secret, toAddress, BigDecimal.valueOf(4.29831), fee, memo));
//        System.out.println(createtransaction(fromAddress, secret, toAddress, BigDecimal.valueOf(1.55), fee, memo));

//        String transactionHash = "";
//        System.out.println(getTransaction(transactionHash));

//        System.out.println(getAccountInfo(address));//
//        System.out.println(wallet_propose(secret));
//        System.out.println(getTransaction("C101FBFCB8B44C57771BB296DAEC718C2C82B44ADDF0B9586168AB86BFD2210B"));

//        for (int i = 0; i < 10; i++) {
//            String createTransaction = createtransaction(fromAddress, secret, toAddress, BigDecimal.valueOf(Math.random()*5).setScale(4, RoundingMode.HALF_UP), fee, memo);
//            log.info("{}",createTransaction);
//            System.out.println(createTransaction);
//        }

    }

    public static String wallet_propose(String passphrase){
        String params = "{\"method\":\"wallet_propose\",\"params\":[{\"seed\":\""+passphrase+"\",\"key_type\": \"secp256k1\"}]}";
        return Utils.postByJson(XRP_URL,params);
    }
    /**
     * 签名后，获取到到tx_blob,发送该笔签名的交易
     * @param tx_blob
     */
    public static String submit(String tx_blob){
        String params = "{\"method\":\"submit\",\"params\":[{\"tx_blob\":\""+tx_blob+"\"}]}";
        return Utils.postByJson(XRP_URL,params);
    }

    /**
     * 签名
     * @param fromAddress
     * @param secret
     * @param toAddress
     * @param value
     * @param fee
     * @param memo
     * @return
     */
    public static String createtransaction(String fromAddress, String secret, String toAddress, BigDecimal value, BigDecimal fee, String memo) {
        JSONObject re = JSONObject.parseObject(getAccountInfo(fromAddress));
        if (re != null) {
            JSONObject result = re.getJSONObject("result");
            if ("success".equals(result.getString("status"))) {
                Payment payment = new Payment();
                payment.as(AccountID.Account, fromAddress);
                payment.as(AccountID.Destination, toAddress);
                payment.as(UInt32.DestinationTag, memo);
                payment.as(Amount.Amount, value.toString());
                payment.as(UInt32.Sequence, result.getJSONObject("account_data").getString("Sequence"));
                payment.as(UInt32.LastLedgerSequence, result.getString("ledger_current_index") + 4);
                payment.as(Amount.Fee, fee.toString());
                //签名
                SignedTransaction signed = payment.sign(secret);
                if (signed != null) {
                    String tx_blob = signed.tx_blob;
                    log.info(tx_blob);
                    System.out.println("tx_blob==="+tx_blob);

                    //广播
//                    String submitResponse = submit(tx_blob);
//                    log.info(submitResponse);
//                    System.out.println("submitResponse=="+submitResponse);

//                    JSONObject re1 = JSONObject.parseObject(submitResponse);
//                    String transactionHash = re1.getJSONObject("result").getJSONObject("tx_json").getString("hash");
//                    log.info(transactionHash);
//                    System.out.println("transactionHash==="+transactionHash);
//                    return transactionHash;
                }
            }
        }
        return null;
    }

    /**
     * 查询账户信息
     * @param account
     * @return
     */
    public static String getAccountInfo(String account) {
        String params = "{\"method\": \"account_info\",\"params\": [{\"account\": \""+account+"\",\"strict\": true,\"ledger_index\": \"current\",\"queue\": true}]}";
        return Utils.postByJson(XRP_URL,params);
    }

    /**
     * 查询交易信息  "method": "tx"
     * @param transaction
     */
    public static String getTransaction(String transaction){
        String params = "{\"method\": \"tx\",\"params\": [{\"transaction\": \""+transaction+"\",\"binary\": false}]}";
        return Utils.postByJson(XRP_URL,params);
    }

}
