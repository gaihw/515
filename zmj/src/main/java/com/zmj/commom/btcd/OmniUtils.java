package com.zmj.commom.btcd;

import com.zmj.config.Config;
import com.zmj.commom.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Slf4j
public class OmniUtils {

    @Autowired
    private HttpUtils httpUtils;

    /**
     * 查询omni代币，有金额的地址
     * @param propertyid
     * @return
     */
    public  String omni_getallbalancesforid(String propertyid){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"omni_getallbalancesforid\", \"params\": ["+propertyid+"] }";
        return httpUtils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }
    /**
     * 转账
     * @param fromAddress
     * @param toAddress
     * @param propertyid
     * @param amount
     * @return
     */
    public  String omni_send(String fromAddress,String toAddress,String propertyid,String amount){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"omni_send\", \"params\": [\""+fromAddress+"\", \""+toAddress+"\", "+propertyid+", \""+amount+"\"] }\n";
        return httpUtils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }

    /**
     * 查询未花费交易中，包含指定地址的交易
     * @param address
     * @return
     */
    public  String listunspentByAddress(String address){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"listunspent\", \"params\": [0, 9999999 ,[\""+address+"\"]]}";
        return httpUtils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }
    /**
     * 查询大于设定值的未花费交易
     * @param minimumAmount
     * @return
     */
    public  String listunspentByAmount(String minimumAmount){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"listunspent\", \"params\": [6, 9999999, [] , true, { \"minimumAmount\": "+minimumAmount+" } ]}";
        return httpUtils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }
    /**
     * 挖矿
     * @param nblocks
     * @param toAddress
     * @return
     */
    public  String generatetoaddress(String nblocks,String toAddress){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"generatetoaddress\", \"params\": ["+nblocks+",\""+toAddress+"\"] }\n";
        return httpUtils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }

    /**
     * 获取omni区块节点信息
     * @return
     */
    public  String omni_getinfo(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"1.0\", \"method\": \"omni_getinfo\", \"params\":[]}";
        return httpUtils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }

    /**
     * 列出指定区块内的所有omni交易
     * @param blockNum
     * @return
     */
    public  String omni_listblocktransactions(String blockNum){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"1.0\", \"method\": \"omni_listblocktransactions\", \"params\":["+blockNum+"]}";
        return httpUtils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }

    /**
     * 获取omni地址代币余额
     * @param address
     * @return
     */
    public  String omni_getbalance(String address,String propertyid){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"1.0\", \"method\": \"omni_getbalance\", \"params\": [\""+address+"\", "+propertyid+"]}";
        return httpUtils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }
    /**
     * 获取地址的信息
     * @param address
     * @return
     */
    public  String validateaddress(String address){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"validateaddress\", \"params\": [\""+address+"\"] }";
        return httpUtils.postByJson("http://"+Config.BCH_URL+":"+Config.BCH_PORT,cred,params);

    }
}
