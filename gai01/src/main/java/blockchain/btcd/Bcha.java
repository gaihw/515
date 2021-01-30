package blockchain.btcd;

import blockchain.Config;
import blockchain.Utils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.Base64;

public class Bcha {
    static {
        System.setProperty("fileName", "bcha/bcha.log");
    }
    public static Logger log = LoggerFactory.getLogger(Bcha.class);
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //热钱包地址转账
//        for (int i = 0; i < 5; i++) {
//            transactionToHot();
//        }
//        System.out.println(listunspent());

        //普通地址
        String toAddress = "mhoQV6bneUy8gJQZvYMwdSmF973iVmDt59";
//        String value = "0.0011";
//        String sendtoaddress = sendtoaddress(toAddress,value);
//        log.info("{}",sendtoaddress);
//        System.out.println(sendtoaddress);
        String address = "mx6q5vuDxexeVmJiCbzxWP3mfpcjEGQbc2";

        for (int i = 0; i < 10; i++) {
            BigDecimal amount1 = BigDecimal.valueOf(Math.random()*5).setScale(8, RoundingMode.HALF_UP);
            String sendtoaddress = sendtoaddress(toAddress,amount1.toString());
            log.info("{}",sendtoaddress);
            System.out.println(sendtoaddress);
        }

//        for (int i = 0; i < 20; i++) {
//            BigDecimal r = BigDecimal.valueOf(Math.random()*100).setScale(8, RoundingMode.HALF_UP);
//            BigDecimal amount1 = BigDecimal.valueOf(Math.random()*2).setScale(8, RoundingMode.HALF_UP);
//            String a ="";
//            if (r.compareTo(BigDecimal.valueOf(25))<0){
//                a = toAddress;
//            }else if (r.compareTo(BigDecimal.valueOf(25))>=0&&r.compareTo(BigDecimal.valueOf(50))<0){
//               a = "mhi97v599C5RcrN7eZzYCjveby2zgjq16m";
//            }else if (r.compareTo(BigDecimal.valueOf(50))>=0&&r.compareTo(BigDecimal.valueOf(75))<0){
//                a = toAddress;
//            }else{
//                a = "mhi97v599C5RcrN7eZzYCjveby2zgjq16m";
//            }
//            String sendtoaddress = sendtoaddress(a,amount1.toString());
//            log.info("{}",sendtoaddress);
//            System.out.println(sendtoaddress);
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }

    /**
     * 使用utxo进行转账
     */
    public static void transactionToHot(){
        String txid = null;
        String vout = null;
        //热钱包地址
        String hotAddress = "mx6q5vuDxexeVmJiCbzxWP3mfpcjEGQbc2";

        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal free = new BigDecimal(0.00000258);
        String scriptPubKey = null;
        String privateKey = null;
        String redeemScript = null;
        String fromAddress = null;

        JSONArray jsonArray = JSONObject.parseObject(listunspent()).getJSONArray("result");
        for (int i = 0; i < jsonArray.size(); i++) {
            String address = jsonArray.getJSONObject(i).getString("address");
            if (!"null".equals(String.valueOf(JSONObject.parseObject(dumpprivkey(address)).get("result")))
                    &&jsonArray.getJSONObject(i).getBigDecimal("amount").compareTo(BigDecimal.valueOf(1))>0
                    &&jsonArray.getJSONObject(i).getBigDecimal("amount").compareTo(BigDecimal.valueOf(100))<0
                    &&!address.equals(hotAddress)){
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
    public static BigDecimal getAmount(String fromAddress,String txid,String vout){
//        String listunspent = listunspent(fromAddress);
//        JSONArray jsonArray = JSONObject.parseObject(listunspent).getJSONArray("result");
//        for (int i = 0 ; i < jsonArray.size() ; i++){
//            if (jsonArray.getJSONObject(i).getString("txid").equals(txid)
//            &&jsonArray.getJSONObject(i).getString("vout").equals(vout)){
//                return jsonArray.getJSONObject(i).getBigDecimal("amount");
//            }
//        }
        String gettxout = gettxout(txid,vout);
        return JSONObject.parseObject(gettxout).getJSONObject("result").getBigDecimal("value");
    }
    /**
     * getpeerinfo
     * @return
     */
    public static String getpeerinfo(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getpeerinfo\", \"params\":[]}";
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+ Config.BCHA_PORT,cred,params);
    }

    /**
     * getbalance
     * @return
     */
    public static String getbalance(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getbalance\", \"params\":[]}";
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);
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
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);
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
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);
    }
    /**
     * getblockcount
     * @return
     */
    public static String getblockcount(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getblockcount\", \"params\":[]}";
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);
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
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);
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
        String rr = Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);
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
        return null;
    }

    /**
     * 签名交易
     * @param hex
     * @param txid
     * @param vout
     * @param scriptPubKey
     * @param privatekey
     * @return
     */
    public static String signrawtransactionwithkey(String hex,String txid,String vout,String scriptPubKey,String amount,String privatekey,String redeemScript){
//        Base64.Encoder encoder = Base64.getEncoder();
//        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
//        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransactionwithkey\", \"params\": [\""+hex+"\", [\""+privatekey+"\"],[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"amount\":"+value+"}]]}";
////        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"signrawtransactionwithwallet\", \"params\": [\""+hex+"\",[{\"txid\":\""+txid+"\",\"vout\":"+vout+",\"scriptPubKey\":\""+scriptPubKey+"\",\"amount\":"+value+"}]]}";
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
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);
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
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);
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
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);
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
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);

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
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);
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
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);
    }

    /**
     * 查看未花费交易
     * @return
     */
    public static String listunspent(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"2.0\", \"id\": \"curltest\", \"method\": \"listunspent\", \"params\": [6, 9999999]}";
        return Utils.postByJson("http://"+Config.BCHA_URL+":"+Config.BCHA_PORT,cred,params);
    }

    /**
     * 其他方法
     */
    public static void updateNewAddress(){
        Connection conn = null;
        Statement stmt = null;
        Statement stmt1 = null;

        try{
            // 注册 JDBC 驱动
            Class.forName(Calc.Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(Calc.Config.DB_URL_58WALLET, Calc.Config.USER_36, Calc.Config.PASS_36);
            // 执行查询
            stmt = conn.createStatement();
            stmt1 = conn.createStatement();
            String sql = "select address from tb_wallet_address_cold_btc";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
//            while(rs.next()){
//                String address = rs.getString("address");
//                System.out.println(address);
//                System.out.println(validateaddress(address));
//                String newAddress = JSONObject.parseObject(validateaddress(address)).getJSONObject("result").getString("address");
//                System.out.println(address+"==="+newAddress);
//                String update = "update tb_wallet_address_cold_bch set newAddress = \'"+newAddress +"\' where address = \'"+address+"\'";
//                stmt1.execute(update);
//            }
            for (int i =451; i <= 3015; i++) {
                String update = "update tb_wallet_address_cold_btc set user_id = 1000"+i+" ,site_id=1 where id="+i;
                System.out.println(update);

                stmt1.execute(update);
            }
            stmt.close();
            stmt1.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

}
