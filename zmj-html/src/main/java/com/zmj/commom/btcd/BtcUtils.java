package com.zmj.commom.btcd;

import com.alibaba.fastjson.JSONObject;
import com.zmj.config.Config;
import com.zmj.commom.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

@Component
@Slf4j
public class BtcUtils {



    @Autowired
    private HttpUtils httpUtils;

    /**
     * 获取地址对应的未花费总金额
     * @param fromAddress
     * @param txid
     * @param vout
     * @return
     */
    public static BigDecimal getAmount(String fromAddress, String txid, String vout){
//        String listunspent = listunspent(fromAddress);
//        JSONArray jsonArray = JSONObject.parseObject(listunspent).getJSONArray("result");
//        for (int i = 0 ; i < jsonArray.size() ; i++){
//            if (jsonArray.getJSONObject(i).getString("txid").equals(txid)
//                    &&jsonArray.getJSONObject(i).getString("vout").equals(vout)){
//                return jsonArray.getJSONObject(i).getBigDecimal("amount");
//            }
//        }
//        return null;
        String gettxout = new BtcUtils().gettxout(txid,vout);
        return JSONObject.parseObject(gettxout).getJSONObject("result").getBigDecimal("value");
    }
    /**
     * 根据txid和vout，获取utxo信息
     * @param txid
     * @param vout
     * @return
     */
    public  String gettxout(String txid,String vout){

        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"gettxout\", \"params\": [\""+txid+"\","+vout+"] }";
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);
    }
    /**
     * getpeerinfo
     * @return
     */
    public  String getpeerinfo(){
        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getpeerinfo\", \"params\":[]}";
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);
    }

    /**
     * getbalance
     * @return
     */
    public  String getbalance(){
        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getbalance\", \"params\":[]}";
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);
    }

    /**
     * 查看未花费交易
     * @param address
     * @return
     */
    public  String listunspent(String address){
        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        String params = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"listunspent\", \"params\": [0, 99999999 ,[\""+address+"\"]]}";
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);
    }
    /**
     * 查看未花费交易
     * @return
     */
    public  String listunspent(){
        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        String params = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"listunspent\", \"params\": []}";
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);
    }

    /**
     * 挖矿，并指定奖励的地址
     * @param generateNum
     * @param toAddress
     * @return
     */
    public String generatetoaddress(String generateNum,String toAddress){
        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        String params = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"generatetoaddress\", \"params\": ["+generateNum+",\""+toAddress+"\"]}";
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);
    }
    /**
     * getblockcount
     * @return
     */
    public  String getblockcount(){
        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getblockcount\", \"params\":[]}";
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);
    }

    /**
     * 转账 方式一 sendtoaddress
     * @param toAddress
     * @param value
     * @return
     */
    public  String sendtoaddress(String toAddress,String value){
        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"sendtoaddress\", \"params\": [\""+toAddress+"\", "+value+", \"donation\", \"seans outpost\"]}";
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);
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

        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"createrawtransaction\", \"params\": [[{\"txid\":\""+txid+"\",\"vout\":"+vout+"}], [{\""+toAddress+"\":\""+value+"\"}]]}";
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);
    }

    /**
     * 签名交易
     * @param hex
     * @param txid
     * @param vout
     * @param scriptPubKey
     * @param amount
     * @param privatekey
     * @return
     */
    public  String signrawtransactionwithkey(String hex,String txid,String vout,String scriptPubKey,String amount,String privatekey,String redeemScript){

        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        String params = null;
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        if ("null".equals(String.valueOf(redeemScript))){
            params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransactionwithkey\", \"params\": [\""+hex+"\", [\""+privatekey+"\"],[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"amount\":"+amount+"}]]}";
        }else{
            params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransactionwithkey\", \"params\": [\""+hex+"\", [\""+privatekey+"\"],[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"redeemScript\":\""+redeemScript+"\",\"amount\":"+amount+"}]]}";
        }
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);
    }

    /**
     * 广播签名的交易
     * @param signHex
     * @return
     */
    public  String sendrawtransaction(String signHex){
        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"sendrawtransaction\", \"params\": [\""+signHex+"\"]}";
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);
    }
    /**
     * 获取地址的信息
     * @param address
     * @return
     */
    public  String validateaddress(String address){

        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"validateaddress\", \"params\": [\""+address+"\"] }";
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);

    }

    /**
     * 获取地址私钥
     * @param address
     * @return
     */
    public  String dumpprivkey(String address){
        String username = Config.BTC_USERNMAME;
        String password = Config.BTC_PASSWORD;
        String url = Config.BTC_URL;
        String port = Config.BTC_PORT;

        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((username + ":" + password).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"dumpprivkey\", \"params\": [\""+address+"\"] }";
        return httpUtils.postByJson("http://"+url+":"+port,cred,params);
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
}
