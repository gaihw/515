package com.zmj.commom.btcd;

import com.zmj.config.Config;
import com.zmj.commom.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

@Component
@Slf4j
public class DogeUtils {

    @Autowired
    private HttpUtils httpUtils;

    /**
     * getpeerinfo
     * @return
     */
    public  String getpeerinfo(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getpeerinfo\", \"params\":[]}";
        return httpUtils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * getbalance
     * @return
     */
    public  String getbalance(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getbalance\", \"params\":[]}";
        return httpUtils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * 查看未花费交易
     * @param address
     * @return
     */
    public  String listunspent(String address){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"listunspent\", \"params\": [0, 99999999 ,[\""+address+"\"]]}";
        return httpUtils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * 挖矿，并指定奖励的地址
     * @param generateNum
     * @param toAddress
     * @return
     */
    public  String generatetoaddress(String generateNum,String toAddress){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"generatetoaddress\", \"params\": ["+generateNum+",\""+toAddress+"\"]}";
        return httpUtils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }
    /**
     * getblockcount
     * @return
     */
    public  String getblockcount(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getblockcount\", \"params\":[]}";
        return httpUtils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * 转账 方式一 sendtoaddress
     * @param toAddress
     * @param amount
     * @return
     */
    public  String sendtoaddress(String toAddress,String amount){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"sendtoaddress\", \"params\": [\""+toAddress+"\", "+amount+", \"donation\", \"seans outpost\"]}";
        return httpUtils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * 转账 方式二 创建交易&签名&广播
     */
    /**
     * 创建交易
     * @param txid
     * @param vout
     * @param toAddress
     * @param value
     * @return
     */
    public  String createrawtransaction(String txid,String vout,String toAddress,String value){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"createrawtransaction\", \"params\": [[{\"txid\":\""+txid+"\",\"vout\":"+vout+"}], [{\""+toAddress+"\":\""+value+"\"}]]}";
        return httpUtils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * 签名交易
     * @param hex
     * @param txid
     * @param vout
     * @param scriptPubKey
     * @param value
     * @param privatekey
     * @return
     */
    public  String signrawtransactionwithkey(String hex,String txid,String vout,String scriptPubKey,String value,String privatekey){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
//        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransactionwithkey\", \"params\": [\""+hex+"\", [\""+privatekey+"\"],[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"amount\":"+value+"}]]}";
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransactionwithwallet\", \"params\": [\""+hex+"\",[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"amount\":"+value+"}]]}";
        return httpUtils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * 广播签名的交易
     * @param signHex
     * @return
     */
    public  String sendrawtransaction(String signHex){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"sendrawtransaction\", \"params\": [\""+signHex+"\"]}";
        System.out.println(params);
        return httpUtils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * 直接使用btcjar包调用方法
     */
    public void orther(){
        String user = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String host = Config.BCH_URL;
        String port = Config.BCH_PORT;
        try {
            URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
            BitcoinJSONRPCClient bitcoinClient = new BitcoinJSONRPCClient(url);
            System.out.println(bitcoinClient.getBlockCount());
            System.out.println(bitcoinClient.getBalance());
        } catch (MalformedURLException e) {
            System.out.println("error"+e.getMessage());
            e.printStackTrace();
        }
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
