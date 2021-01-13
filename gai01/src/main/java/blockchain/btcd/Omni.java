package blockchain.btcd;

import blockchain.Config;
import blockchain.Utils;
import blockchain.eth.Erc20;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class Omni {
    public static String fileName = "omni";
    static {
        System.setProperty("fileName", "omni/omni.log");
    }
    public static Logger log = LoggerFactory.getLogger(Omni.class);
    public static void main(String[] args)  {
        String propertyid = Config.PROPERTYID;
        String fromAddress = Config.OMNI_ADDRESS;//这个地址的钱比较多

        String toAddress = "2N4TCipHXTzhqybMH3Yr2b1Zin9Tu8Fq2QG";//2
        //热钱包地址
        String hotAddress = "n1wTYZy2Nf3pTE8TLH6sjBkwRQUNsW44Lu";//0

        String amount = "1.5252";
        String send = omni_send(fromAddress,hotAddress,propertyid,amount);
        System.out.println(send);
//        for (int i = 0; i < 20; i++) {
//            BigDecimal r = BigDecimal.valueOf(Math.random()*100).setScale(8, RoundingMode.HALF_UP);
//            BigDecimal amount1 = BigDecimal.valueOf(Math.random()*2).setScale(8, RoundingMode.HALF_UP);
//            if (r.compareTo(BigDecimal.valueOf(25))<0){
//                String send = omni_send(fromAddress,"2N4TCipHXTzhqybMH3Yr2b1Zin9Tu8Fq2QG",propertyid,amount1.toString());
//                System.out.println(send);
//            }else if (r.compareTo(BigDecimal.valueOf(25))>=0&&r.compareTo(BigDecimal.valueOf(50))<0){
//                String send = omni_send(fromAddress,"mzuE68Jrq4Y7uMtPP1tb1S2GTBWLsNsDWg",propertyid,amount1.toString());
//                System.out.println(send);
//            }else if (r.compareTo(BigDecimal.valueOf(50))>=0&&r.compareTo(BigDecimal.valueOf(75))<0){
//                String send = omni_send(fromAddress,"mg33N7fvpJbvaWijEEL8J5Yu1bbVD8PFeZ",propertyid,amount1.toString());
//                System.out.println(send);
//            }else{
//                String send = omni_send(fromAddress,"mgBgdzp4iGGmjBh9bgPS1f5jEys4p7muzP",propertyid,amount1.toString());
//                System.out.println(send);
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        System.out.println(generatetoaddress("10",fromAddress));
//        System.out.println(omni_getbalance("bcrt1qpgxwlrrlhmclqvfktztqs3jzapymwekerl0n2qyyz4drut0l4ydqg56zxt",propertyid));
    }

    /**
     * 查询omni代币，有金额的地址
     * @param propertyid
     * @return
     */
    public static String omni_getallbalancesforid(String propertyid){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"omni_getallbalancesforid\", \"params\": ["+propertyid+"] }";
        return Utils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }
    /**
     * 转账
     * @param fromAddress
     * @param toAddress
     * @param propertyid
     * @param amount
     * @return
     */
    public static String omni_send(String fromAddress,String toAddress,String propertyid,String amount){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"omni_send\", \"params\": [\""+fromAddress+"\", \""+toAddress+"\", "+propertyid+", \""+amount+"\"] }\n";
        String res = Utils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
        log.info("from:{},to:{},txid:{}",fromAddress,toAddress,res);
        return res;
    }

    /**
     * 查询未花费交易中，包含指定地址的交易
     * @param address
     * @return
     */
    public static String listunspentByAddress(String address){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"listunspent\", \"params\": [0, 9999999 ,[\""+address+"\"]]}";
        return Utils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }
    /**
     * 查询大于设定值的未花费交易
     * @param minimumAmount
     * @return
     */
    public static String listunspentByAmount(String minimumAmount){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"listunspent\", \"params\": [6, 9999999, [] , true, { \"minimumAmount\": "+minimumAmount+" } ]}";
        return Utils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }
    /**
     * 挖矿
     * @param nblocks
     * @param toAddress
     * @return
     */
    public static String generatetoaddress(String nblocks,String toAddress){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"generatetoaddress\", \"params\": ["+nblocks+",\""+toAddress+"\"] }\n";
        return Utils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }

    /**
     * 获取omni区块节点信息
     * @return
     */
    public static String omni_getinfo(){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"1.0\", \"method\": \"omni_getinfo\", \"params\":[]}";
        return Utils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }

    /**
     * 列出指定区块内的所有omni交易
     * @param blockNum
     * @return
     */
    public static String omni_listblocktransactions(String blockNum){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"1.0\", \"method\": \"omni_listblocktransactions\", \"params\":["+blockNum+"]}";
        return Utils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
    }

    /**
     * 获取omni地址代币余额
     * @param address
     * @return
     */
    public static String omni_getbalance(String address,String propertyid){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\":\"1.0\", \"method\": \"omni_getbalance\", \"params\": [\""+address+"\", "+propertyid+"]}";
        return Utils.postByJson("http://"+Config.OMNI_URL+":"+Config.OMNI_PORT,cred,params);
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
