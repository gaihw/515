package blockchain.btcd;

import blockchain.Config;
import blockchain.Utils;
import blockchain.algo.Algo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Btc {
    static {
        System.setProperty("fileName", "btc/btc.log");
    }
    public static Logger log = LoggerFactory.getLogger(Btc.class);
    public static String fileName = "btc";

    public static void main(String[] args) throws IOException {

        //给热钱包转账
        transactionToHot();

        //普通地址
//        String toAddress = "bcrt1qp7zxq7at52mqt0mhdx54w3x6vgmz6e078mj7cx";
//        String value = "0.003928";
//        String sendtoaddress = sendtoaddress(toAddress,value);
//        log.info("{}",sendtoaddress);
//        System.out.println(sendtoaddress);

//        for (int i = 0; i < 20; i++) {
//            BigDecimal r = BigDecimal.valueOf(Math.random()*100).setScale(8, RoundingMode.HALF_UP);
//            BigDecimal amount1 = BigDecimal.valueOf(Math.random()*2).setScale(8, RoundingMode.HALF_UP);
//            String a = "";
//            if (r.compareTo(BigDecimal.valueOf(25))<0){
//                a = "bcrt1qp7zxq7at52mqt0mhdx54w3x6vgmz6e078mj7cx";
//            }else if (r.compareTo(BigDecimal.valueOf(25))>=0&&r.compareTo(BigDecimal.valueOf(50))<0){
//                a = "mkfxVXRNe5MHCQaTfttEFUMzZX8Qt6hpdR";
//            }else if (r.compareTo(BigDecimal.valueOf(50))>=0&&r.compareTo(BigDecimal.valueOf(75))<0){
//                a = "my8nYC4Wn9vgsWdXnuqGPADDuqvBzW7pmH";
//            }else{
//                a = "n3GbLPdTANXzoETBrp9p9DmuWkcrDV66wU";
//            }
//            String sendtoaddress = sendtoaddress(a,amount1.toString());
//            System.out.println(sendtoaddress);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        System.out.println(dumpprivkey(fromAddress));
//        System.out.println(getblockcount());
//        BigDecimal amount = getAmount(fromAddress,txid,vout);
//        String scriptPubKey = JSONObject.parseObject(validateaddress(fromAddress)).getJSONObject("result").getString("scriptPubKey");

//        System.out.println(getnewaddress());

    }

    /**
     * 使用utxo进行转账
     */
    public static void transactionToHot(){
        String txid = null;
        String vout = null;
        String fromAddress = null;
        //omni地址btc有钱账户，不进行转账
        String omniAddress = Config.OMNI_ADDRESS;
        //热钱包地址
        String hotAddress = "mzsmrgCw2ugzQpsj9yRF3uCBUmyAjvDn9A";


        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal free = new BigDecimal(0.00000258);
        String scriptPubKey = null;
        String privateKey = null;
        String redeemScript = null;

        JSONArray jsonArray = JSONObject.parseObject(listunspent()).getJSONArray("result");
        for (int i = 0; i < jsonArray.size(); i++) {
            String address = jsonArray.getJSONObject(i).getString("address");
            if (!"null".equals(String.valueOf(JSONObject.parseObject(dumpprivkey(address)).get("result")))
                    &&jsonArray.getJSONObject(i).getBigDecimal("amount").compareTo(BigDecimal.valueOf(1))>0
                    &&jsonArray.getJSONObject(i).getBigDecimal("amount").compareTo(BigDecimal.valueOf(100))<0
                    &&!address.equals(hotAddress)
                    &&!address.equals(omniAddress)){
                System.out.println(dumpprivkey(jsonArray.getJSONObject(i).getString("address"))+"---"+jsonArray.getJSONObject(i));
                txid = jsonArray.getJSONObject(i).getString("txid");
                vout = jsonArray.getJSONObject(i).getString("vout");
                fromAddress = address;
                privateKey = JSONObject.parseObject(dumpprivkey(address)).getString("result");
                scriptPubKey = jsonArray.getJSONObject(i).getString("scriptPubKey");
                amount = jsonArray.getJSONObject(i).getBigDecimal("amount");
                if (jsonArray.getJSONObject(i).containsKey("redeemScript")){
                    redeemScript = jsonArray.getJSONObject(i).getString("redeemScript");
                }
                break;
            }
        }
        System.out.println("from==="+fromAddress);
        System.out.println("to==="+hotAddress);
        System.out.println("amount==="+amount);
        BigDecimal value = BigDecimal.valueOf(Math.random()*1).setScale(6, RoundingMode.HALF_UP);
        System.out.println("value==="+value);
        String value1 = amount.subtract(free).subtract(value).setScale(4,BigDecimal.ROUND_DOWN).toString();
        System.out.println("value1=="+value1);
        String rr = createrawtransaction(txid,vout,fromAddress,hotAddress,value.toString(),value1,scriptPubKey,amount,privateKey,redeemScript);
        System.out.println("rr==="+rr);
    }
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
        String gettxout = gettxout(txid,vout);
        System.out.println(gettxout);
        return JSONObject.parseObject(gettxout).getJSONObject("result").getBigDecimal("value");
    }
    /**
     * 根据txid和vout，获取utxo信息
     * @param txid
     * @param vout
     * @return
     */
    public static String gettxout(String txid,String vout){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"gettxout\", \"params\": [\""+txid+"\","+vout+"] }";
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }
    /**
     * getpeerinfo
     * @return
     */
    public static String getpeerinfo(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getpeerinfo\", \"params\":[]}";
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * getbalance
     * @return
     */
    public static String getbalance(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getbalance\", \"params\":[]}";
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * 查看未花费交易
     * @param address
     * @return
     */
    public static String listunspent(String address){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"listunspent\", \"params\": [0, 99999999 ,[\""+address+"\"]]}";
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }
    /**
     * 查看未花费交易
     * @return
     */
    public static String listunspent(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"listunspent\", \"params\": []}";
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * 挖矿，并指定奖励的地址
     * @param generateNum
     * @param toAddress
     * @return
     */
    public static String generatetoaddress(String generateNum,String toAddress){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"generatetoaddress\", \"params\": ["+generateNum+",\""+toAddress+"\"]}";
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }
    /**
     * getblockcount
     * @return
     */
    public static String getblockcount(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getblockcount\", \"params\":[]}";
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * 转账 方式一 sendtoaddress
     * @param toAddress
     * @param value
     * @return
     */
    public static String sendtoaddress(String toAddress,String value){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"sendtoaddress\", \"params\": [\""+toAddress+"\", "+value+", \"donation\", \"seans outpost\"]}";
        String res = Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
        log.info("address:{}",res);
        return res;
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
    public static String createrawtransaction(String txid,String vout,String fromAddress,String toAddress,String value,String value1,String scriptPubKey,BigDecimal amount,String privateKey,String redeemScript){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"createrawtransaction\", \"params\": [[{\"txid\":\""+txid+"\",\"vout\":"+vout+"}], [{\""+toAddress+"\":\""+value+"\"},{\""+fromAddress+"\":\""+value1+"\"}]]}";
        String rr = Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
        System.out.println("rr==="+rr);
        String hex = JSONObject.parseObject(rr).getString("result");
        System.out.println("hex==="+hex);
        log.info(hex);

        String rr1 = signrawtransactionwithkey(hex,txid,vout,scriptPubKey,amount.toString(),privateKey,redeemScript);
        System.out.println("rr1==="+rr1);
        log.info(rr1);

//        String signHex = JSONObject.parseObject(rr1).getJSONObject("result").getString("hex");
//        String txidHex = sendrawtransaction(signHex);
//        log.info(txidHex);
//        System.out.println("txidHex===="+txidHex);
//        return txidHex;
        return "test...";
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
    public static String signrawtransactionwithkey(String hex,String txid,String vout,String scriptPubKey,String amount,String privatekey,String redeemScript){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = null;
        if ("null".equals(String.valueOf(redeemScript))){
             params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransactionwithkey\", \"params\": [\""+hex+"\", [\""+privatekey+"\"],[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"amount\":"+amount+"}]]}";
        }else{
            params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransactionwithkey\", \"params\": [\""+hex+"\", [\""+privatekey+"\"],[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"redeemScript\":\""+redeemScript+"\",\"amount\":"+amount+"}]]}";
        }
//        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransactionwithwallet\", \"params\": [\""+hex+"\",[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"amount\":"+value+"}]]}";
        log.info(params);
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * 广播签名的交易
     * @param signHex
     * @return
     */
    public static String sendrawtransaction(String signHex){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"sendrawtransaction\", \"params\": [\""+signHex+"\"]}";
        System.out.println(params);
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }
    /**
     * 获取地址的信息
     * @param address
     * @return
     */
    public static String validateaddress(String address){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"validateaddress\", \"params\": [\""+address+"\"] }";
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);

    }

    /**
     * 获取地址私钥
     * @param address
     * @return
     */
    public static String dumpprivkey(String address){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"dumpprivkey\", \"params\": [\""+address+"\"] }";
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
    }

    /**
     * 生成新地址
     * @return
     */
    public static String getnewaddress(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"getnewaddress\", \"params\": [] }";
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
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
