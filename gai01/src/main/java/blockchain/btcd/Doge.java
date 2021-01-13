package blockchain.btcd;

import blockchain.Config;
import blockchain.Utils;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class Doge {
    public static String fileName = "doge";

    public static void main(String[] args) throws IOException {

        String txid = "88309b1ea5245a4b916c9710a2558c876daf2c583edb7a8aff6826f766678ffd";
        String vout = "1";
        String toAddress = "2NDFZwuewLEV3cV53M8Sp9jogD4pt9BJ3tr";
        String value = "0.345";
        String scriptPubKey = "76a91400185fba396591632008113608736d94ba74b96588ac";
        String privateKey = "cUES1tXGqyQHueyULrAjTLCA6HuobENiw1gM1fDuKEqPa1Ee9GZn";

//        0001 2MsGVmQZFvQXEzqyTmPMPu1DAuQmayu6pj5
//            00002 2NDFZwuewLEV3cV53M8Sp9jogD4pt9BJ3tr
//            00000 2Mz49eFWaPDHsrNQSfKkugLW9pn5cgCbvA1

        String sendtoaddress = sendtoaddress(toAddress,value);
        Utils.writerData(fileName,sendtoaddress);
        System.out.println(sendtoaddress);


//        String rr = createrawtransaction(txid,vout,toAddress,value);
//        System.out.println("rr==="+rr);
////        String rr = "{\"result\":\"0200000001fec6b5ec1dcf093c807a4fc5c6752d29c99598f9d3d9ce4a7201ba4354e036a30000000000ffffffff014004a80400000000160014056fb2fbffb5f8a42882b1da86702f19b8f04be000000000\",\"error\":null,\"id\":\"curltest\"}\n\n";
//        String hex = JSONObject.parseObject(rr).getString("result");
//        System.out.println("hex==="+hex);
//
//        String rr1 = signrawtransactionwithkey(hex,txid,vout,scriptPubKey,value,privateKey);
//        System.out.println("rr1==="+rr1);
//        Utils.writerData(fileName,rr1);
//
//        String signHex = JSONObject.parseObject(rr1).getJSONObject("result").getString("hex");
//        String sendtransactiongHex = sendrawtransaction(signHex);
//        Utils.writerData(fileName,sendtransactiongHex);
//        System.out.println("sendtransactiongHex===="+sendtransactiongHex);

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
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
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
    public static String createrawtransaction(String txid,String vout,String toAddress,String value){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"createrawtransaction\", \"params\": [[{\"txid\":\""+txid+"\",\"vout\":"+vout+"}], [{\""+toAddress+"\":\""+value+"\"}]]}";
        return Utils.postByJson("http://"+Config.BTC_URL+":"+Config.BTC_PORT,cred,params);
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
    public static String signrawtransactionwithkey(String hex,String txid,String vout,String scriptPubKey,String value,String privatekey){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
//        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransactionwithkey\", \"params\": [\""+hex+"\", [\""+privatekey+"\"],[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"amount\":"+value+"}]]}";
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransactionwithwallet\", \"params\": [\""+hex+"\",[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"amount\":"+value+"}]]}";
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
    public static String validateaddress(String address){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"validateaddress\", \"params\": [\""+address+"\"] }";
        return Utils.postByJson("http://"+Config.BCH_URL+":"+Config.BCH_PORT,cred,params);

    }
}
