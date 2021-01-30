package blockchain.btcd;

import blockchain.Config;
import blockchain.Utils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class Zec {
    static {
        System.setProperty("fileName", "zec/zec.log");
    }
    public static Logger log = LoggerFactory.getLogger(Zec.class);
    public static void main(String[] args) {
        /**String[] toaddress = {"tmMVVhaCvwmx1FTjRjTgBQd2Don5pqWKTum","tmVjTrukKWTHezba1zoCv5ZrY3qYGftX67c","tmALph7TnkWn5oGLmhUyoy5bfJ4kMrqDqrb","tmQRprU2tX2ArSFJ33ya2CWKbKfsCFkMbZU"};
        String res = null;
        for (int i = 0; i < 30; i++) {
            BigDecimal amount = BigDecimal.valueOf(Math.random()*20).setScale(7, RoundingMode.HALF_UP);
            if (amount.compareTo(BigDecimal.valueOf(6.3333))<0){
                res = sendtoaddress(toaddress[0],"0.1");
            }else if (amount.compareTo(BigDecimal.valueOf(6.3333))>=0&&amount.compareTo(BigDecimal.valueOf(12.6666))<0){
                res = sendtoaddress(toaddress[1],"0.1");
            }else {
                res = sendtoaddress(toaddress[2],"0.1");
            }
            log.info("{}",res);
            System.out.println(res);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }**/

        String txid = null;
        String vout = null;
        String fromAddress = null;
        //热钱包地址
        String hotAddress = "tmRmphY9giuTaoT61ZeorRexcxc2iP8y8zZ";

        String toAddress = "tmN6jtDMBstVngQmkhiAMkkTxAGCPLQbFwo";
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal free = new BigDecimal(0.00000258);
        String scriptPubKey = null;
        String privateKey = null;
        String redeemScript = null;

//        JSONArray jsonArray = JSONObject.parseObject(listunspent()).getJSONArray("result");
//        for (int i = 0; i < jsonArray.size(); i++) {
//            String address = jsonArray.getJSONObject(i).getString("address");
//            if (!"null".equals(String.valueOf(JSONObject.parseObject(dumpprivkey(address)).get("result")))
//                    &&jsonArray.getJSONObject(i).getBigDecimal("amount").compareTo(BigDecimal.valueOf(1))>0
//                    &&jsonArray.getJSONObject(i).getBigDecimal("amount").compareTo(BigDecimal.valueOf(100))<0
//                    &&!address.equals(hotAddress)){
//                System.out.println(dumpprivkey(jsonArray.getJSONObject(i).getString("address"))+"---"+jsonArray.getJSONObject(i));
//                txid = jsonArray.getJSONObject(i).getString("txid");
//                vout = jsonArray.getJSONObject(i).getString("vout");
//                fromAddress = address;
//                privateKey = JSONObject.parseObject(dumpprivkey(address)).getString("result");
//                scriptPubKey = jsonArray.getJSONObject(i).getString("scriptPubKey");
//                amount = jsonArray.getJSONObject(i).getBigDecimal("amount");
//                if (jsonArray.getJSONObject(i).containsKey("redeemScript")){
//                    redeemScript = jsonArray.getJSONObject(i).getString("redeemScript");
//                }
//                break;
//            }
//        }
//
//        System.out.println("from==="+fromAddress);
//        System.out.println("to==="+hotAddress);
//        System.out.println("amount==="+amount);
//        BigDecimal value = BigDecimal.valueOf(Math.random()*1).setScale(6, RoundingMode.HALF_UP);
//        System.out.println("value==="+value);
//        String value1 = amount.subtract(free).subtract(value).setScale(4,BigDecimal.ROUND_DOWN).toString();
//        System.out.println("value1=="+value1);

        //转账
//        createrawtransaction(txid,vout,fromAddress,hotAddress,value.toString(),value1,scriptPubKey,String.valueOf(amount),privateKey,redeemScript);

//        String value = "0.00101";
//        String sendtoaddress = sendtoaddress(toAddress,value);
//        log.info("{}",sendtoaddress);
//        System.out.println(sendtoaddress);

        for (int i = 0; i < 10; i++) {
            BigDecimal amount1 = BigDecimal.valueOf(Math.random()*1).setScale(8, RoundingMode.HALF_UP);
            String sendtoaddress = sendtoaddress(toAddress,amount1.toString());
            log.info("{}",sendtoaddress);
            System.out.println(sendtoaddress);
        }


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
        return JSONObject.parseObject(gettxout).getJSONObject("result").getBigDecimal("value");
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
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
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
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
    }
    /**
     * getpeerinfo
     * @return
     */
    public static String getpeerinfo(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getpeerinfo\", \"params\":[]}";
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
    }
    /**
     * listunspent
     * @return
     */
    public static String listunspent(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"listunspent\", \"params\":[]}";
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
    }

    /**
     * getbalance
     * @return
     */
    public static String getbalance(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getbalance\", \"params\":[]}";
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
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
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
    }

    /**
     * 挖矿，并指定奖励的地址
     * @param generateNum
     * @return
     */
    public static String generate(String generateNum){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"generate\", \"params\": []}";
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
    }
    /**
     * getblockcount
     * @return
     */
    public static String getblockcount(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getblockcount\", \"params\":[]}";
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
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
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
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
    public static void createrawtransaction(String txid,String vout,String fromAddress,String toAddress,String value,String value1,String scriptPubKey,String amount,String privateKey,String redeemScript){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"createrawtransaction\", \"params\": [[{\"txid\":\""+txid+"\",\"vout\":"+vout+"}], {\""+toAddress+"\":\""+value+"\",\""+fromAddress+"\":\""+value1+"\"}]}";
        String rr = Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
        System.out.println("rr==="+rr);
        String hex = JSONObject.parseObject(rr).getString("result");
        System.out.println("hex==="+hex);
        log.info("{}",rr);

        String rr1 = signrawtransaction(hex,txid,vout,scriptPubKey,amount,privateKey,redeemScript);
        System.out.println("rr1==="+rr1);
        log.info("{}",rr1);

//        String signHex = JSONObject.parseObject(rr1).getJSONObject("result").getString("hex");
//        String sendtransactiongHex = sendrawtransaction(signHex);
//        log.info("{}",sendtransactiongHex);
//        System.out.println("sendtransactiongHex===="+sendtransactiongHex);

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
    public static String signrawtransaction(String hex,String txid,String vout,String scriptPubKey,String amount,String privatekey,String redeemScript){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
//        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransactionwithkey\", \"params\": [\""+hex+"\", [\""+privatekey+"\"],[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"amount\":"+value+"}]]}";
        String params = null;
        if ("null".equals(String.valueOf(redeemScript))){
            params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransaction\", \"params\": [\""+hex+"\",[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"amount\":"+amount+"}], [\""+privatekey+"\"]]}";
        }else{
            params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransaction\", \"params\": [\""+hex+"\",[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"redeemScript\":\""+redeemScript+"\",\"amount\":"+amount+"}], [\""+privatekey+"\"]]}";
        }
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
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
//        System.out.println(params);
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
    }

    /**
     * 解析交易信息
     * @param txid
     * @return
     */
    public static String getrawtransaction(String txid){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"getrawtransaction\", \"params\": [\""+txid+"\", true] }";
//        System.out.println(params);
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);
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
        return Utils.postByJson("http://"+Config.ZEC_URL+":"+Config.ZEC_PORT,cred,params);

    }

    public static void connectLinux() throws JSchException, IOException {
        long currentTimeMillis = System.currentTimeMillis();

        String command = "zec generate 1";

        JSch jsch = new JSch();
        Session session = jsch.getSession("root", "192.168.112.211", 22);
        session.setPassword("cfca1234");
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(60 * 1000);
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        channel.setInputStream(null);

        ((ChannelExec) channel).setErrStream(System.err);

        InputStream in = channel.getInputStream();

        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                System.out.print(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                if (in.available() > 0) continue;
                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {
            }
        }
        channel.disconnect();
        session.disconnect();

        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("Jsch方式"+(currentTimeMillis1-currentTimeMillis));
    }
}
