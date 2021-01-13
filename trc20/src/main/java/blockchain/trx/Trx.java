package blockchain.trx;

import Calc.Config;
import blockchain.Utils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Trx {

    static {
        System.setProperty("fileName", "trx/trx.log");
    }
    public static Logger log = LoggerFactory.getLogger(Trx.class);

    public static void main(String[] args) {

        String owner_address = "414311f08873669b35f995e0bfb603509e1d96c380";
        String privateKey = "61cae49321a8f13788392b9b4b76dd3410007bb8aca89acdbefe0692d175c684";
        String to_address = "418cbd4514e26ade28ef5e65e62908a59a05c0461d";
        String amount = "2000000";

        //热钱包地址
        String address = "41c89c621abd67ef381d0be302613d6fb2c7508b1c";
        String hotPriv = "700888090ee69a21925ad5a55a7147bd0c58bddd76cec022611a668f8580efc8";

//        System.out.println(createtransaction(owner_address,address,amount,privateKey));

        String txId = "6230a5ef80c36c5962c693739c0ba3cd0d2d8f7c52a14ec280f67276d8347330";
        System.out.println(gettransactioninfobyid(txId));
        System.out.println(gettransactionbyid(txId));
        System.out.println(getaccount(address));//3584480 3581030 3577580
//        System.out.println(getnowblock());
//        System.out.println(generateAddress());

        //冻结
        String resoure = "BANDWIDTH";
//        String resoure = "ENERGY";
        String frozen_balance = "100000000";
//        System.out.println(freezebalance(address,frozen_balance,resoure,address,hotPriv));
//        345600000
//        System.out.println(freezebalance("41eaa0cda6897646bf6a98953c2d5ee78f9ad6e113","86400000000000",resoure,"41eaa0cda6897646bf6a98953c2d5ee78f9ad6e113","f8611b717e20ca143678fff2d363136be448d9e23019e9bc345551f4e8fde515"));


        //创建账户
//        System.out.println(createaccount(owner_address,address,"61cae49321a8f13788392b9b4b76dd3410007bb8aca89acdbefe0692d175c684"));

        //查询账户资源
        System.out.println(getaccountresource(address));


//        for (int i = 0; i < 100; i++) {
//            String addr = JSONObject.parseObject(generateAddress()).getString("address");
//            insertSendRecord(addr);
//        }


    }

    /**
     * 生成地址，并生成指定的私钥
     * @return
     */
    public static String generateAddress(){
        String url = "http://192.168.112.214:16667/wallet/generateaddress";
        return Utils.get(url);
    }

    /**
     * 查看地址是否有效
     * @param address
     * @return
     */
    public static String validateAddress(String address){
        String url = "http://192.168.112.214:16667/wallet/validateaddress";
        String params = "{\"address\":\""+address+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 创建交易
     * @param ownerAddress
     * @param toAddress
     * @param value
     * @return
     */
    public static String createtransaction(String ownerAddress,String toAddress,String value,String privateKey){
        String url = "http://192.168.112.214:16667/wallet/createtransaction";
        String params = "{\"owner_address\": \""+ownerAddress+"\",\"to_address\": \""+toAddress+"\",\"amount\": "+value+"}";
        String createResponse =  Utils.postByJson(url,params);
        System.out.println("createResponse="+createResponse);
        log.info("createResponse={}",createResponse);
        JSONObject jsonObject = JSONObject.parseObject(createResponse);
        String txID = jsonObject.getString("txID");
        String type_url = jsonObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getString("type_url");
        String type = jsonObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getString("type");
        String ref_block_bytes = jsonObject.getJSONObject("raw_data").getString("ref_block_bytes");
        String ref_block_hash = jsonObject.getJSONObject("raw_data").getString("ref_block_hash");
        String expiration = jsonObject.getJSONObject("raw_data").getString("expiration");
        String timestamp = jsonObject.getJSONObject("raw_data").getString("timestamp");
        String raw_data_hex = jsonObject.getString("raw_data_hex");


        //签名
        String signResponse = gettransactionsign(txID,value,ownerAddress,toAddress,type_url,type,ref_block_bytes,ref_block_hash,expiration,timestamp,raw_data_hex,privateKey);
        System.out.println("signResponse="+signResponse);
        log.info("signResponse={}",signResponse);
        JSONObject jsonObject1 = JSONObject.parseObject(signResponse);
        String signature = jsonObject1.getJSONArray("signature").getString(0);
        String txID_1 = jsonObject1.getString("txID");
        String type_url_1 = jsonObject1.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getString("type_url");
        String type_1 = jsonObject1.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getString("type");
        String ref_block_bytes_1 = jsonObject1.getJSONObject("raw_data").getString("ref_block_bytes");
        String ref_block_hash_1 = jsonObject1.getJSONObject("raw_data").getString("ref_block_hash");
        String expiration_1 = jsonObject1.getJSONObject("raw_data").getString("expiration");
        String timestamp_1 = jsonObject1.getJSONObject("raw_data").getString("timestamp");
        String raw_data_hex_1 = jsonObject1.getString("raw_data_hex");

        //广播
        String broadResponse = broadcasttransaction(signature,txID_1,value,ownerAddress,toAddress,type_url_1,type_1,ref_block_bytes_1,ref_block_hash_1,expiration_1,timestamp_1,raw_data_hex_1);
        System.out.println("broadResponse="+broadResponse);
        log.info("broadResponse={}",broadResponse);
        return broadResponse;
    }

    /**
     * 签名
     * @param txId
     * @param amount
     * @param ownerAddress
     * @param toAddress
     * @param rawDataHex
     * @param privateKey
     * @return
     */
    public static String gettransactionsign(String txId,String amount,String ownerAddress,String toAddress,String typeUrl,String type,String refBlockBytes,String refBlockHash,String expiration,String timestamp,String rawDataHex,String privateKey){
        String url = "http://192.168.112.214:16667/wallet/gettransactionsign";
        String params = "{\"transaction\":{\"txID\":\""+txId+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"amount\":"+amount+",\"owner_address\":\""+ownerAddress+"\",\"to_address\":\""+toAddress+"\"},\"type_url\":\""+typeUrl+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+refBlockBytes+"\",\"ref_block_hash\":\""+refBlockHash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+"},\"raw_data_hex\":\""+rawDataHex+"\"},\"privateKey\": \""+privateKey+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 广播
     * @param signature
     * @param txId
     * @param amount
     * @param ownerAddress
     * @param toAddress
     * @param rawDataHex
     * @return
     */
    public static String broadcasttransaction(String signature,String txId,String amount,String ownerAddress,String toAddress,String typeUrl,String type,String refBlockBytes,String refBlockHash,String expiration,String timestamp,String rawDataHex ){
        String url = "http://192.168.112.214:16667/wallet/broadcasttransaction";
        String params = "{\"signature\":[\""+signature+"\"],\"txID\":\""+txId+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"amount\":"+amount+",\"owner_address\":\""+ownerAddress+"\",\"to_address\":\""+toAddress+"\"},\"type_url\":\""+typeUrl+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+refBlockBytes+"\",\"ref_block_hash\":\""+refBlockHash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+"},\"raw_data_hex\":\""+rawDataHex+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 查询交易所在的区块
     * @param txId
     * @return
     */
    public static String gettransactioninfobyid(String txId){
        String url = "http://192.168.112.214:16667/wallet/gettransactioninfobyid";
        String params = "{\"value\" : \""+txId+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 查询交易信息
     * @param txId
     * @return
     */
    public static String gettransactionbyid(String txId){
        String url = "http://192.168.112.214:16667/wallet/gettransactionbyid";
        String params = "{\"value\" : \""+txId+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 查询账户资源
     * @param address
     * @return
     */
    public static String getaccountresource(String address){
        String url = "http://192.168.112.214:16667/wallet/getaccountresource";
        String params = "{\"address\" : \""+address+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 查询合约信息
     * @param address
     * @return
     */
    public static String getcontract(String address){
        String url = "http://192.168.112.214:16667/wallet/getcontract";
        String params = "{\"value\" : \""+address+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 查询账户余额
     * @param address
     * @return
     */
    public static String getaccount(String address){
        String url = "http://192.168.112.214:16667/wallet/getaccount";
        String params = "{\"address\" : \""+address+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 冻结 TRX, 获取带宽或者能量(投票权)
     * @param ownerAddress
     * @param frozenBalance
     * @param resource 冻结 TRX 获取资源的类型, 可以是 BANDWIDTH 或者 ENERGY
     * @param receiverAddress
     * @return
     */
    public static String freezebalance(String ownerAddress,String frozenBalance,String resource,String receiverAddress,String privateKey){

        String url = "http://192.168.112.214:16667/wallet/freezebalance";
        String params = "{\"owner_address\":\""+ownerAddress+"\",\"frozen_balance\":"+frozenBalance+",\"frozen_duration\":3,\"resource\" :\""+resource+"\",\"receiver_address\":\""+receiverAddress+"\"}";
        String createResponse =  Utils.postByJson(url,params);
        System.out.println("createResponse="+createResponse);
        log.info("createResponse={}",createResponse);
        JSONObject jsonObject = JSONObject.parseObject(createResponse);
        String txID = jsonObject.getString("txID");
        String type_url = jsonObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getString("type_url");
        String type = jsonObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getString("type");
        String ref_block_bytes = jsonObject.getJSONObject("raw_data").getString("ref_block_bytes");
        String ref_block_hash = jsonObject.getJSONObject("raw_data").getString("ref_block_hash");
        String expiration = jsonObject.getJSONObject("raw_data").getString("expiration");
        String timestamp = jsonObject.getJSONObject("raw_data").getString("timestamp");
        String raw_data_hex = jsonObject.getString("raw_data_hex");


        //签名
        String url_1 = "http://192.168.112.214:16667/wallet/gettransactionsign";
        String params_1 = "{\"transaction\":{\"txID\":\""+txID+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"resource\":\""+resource+"\",\"frozen_duration\":3,\"frozen_balance\":"+frozenBalance+",\"receiver_address\":\""+receiverAddress+"\",\"owner_address\":\""+ownerAddress+"\"},\"type_url\":\""+type_url+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+ref_block_bytes+"\",\"ref_block_hash\":\""+ref_block_hash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+"},\"raw_data_hex\":\""+raw_data_hex+"\"},\"privateKey\": \""+privateKey+"\"}";
        System.out.println(params_1);
        String signResponse = Utils.postByJson(url_1,params_1);
        System.out.println("signResponse="+signResponse);
        log.info("signResponse={}",signResponse);
        JSONObject jsonObject1 = JSONObject.parseObject(signResponse);
        String signature = jsonObject1.getJSONArray("signature").getString(0);
        String txID_1 = jsonObject1.getString("txID");
        String type_url_1 = jsonObject1.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getString("type_url");
        String type_1 = jsonObject1.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getString("type");
        String ref_block_bytes_1 = jsonObject1.getJSONObject("raw_data").getString("ref_block_bytes");
        String ref_block_hash_1 = jsonObject1.getJSONObject("raw_data").getString("ref_block_hash");
        String expiration_1 = jsonObject1.getJSONObject("raw_data").getString("expiration");
        String timestamp_1 = jsonObject1.getJSONObject("raw_data").getString("timestamp");
        String raw_data_hex_1 = jsonObject1.getString("raw_data_hex");

        //广播
        String url_2 = "http://192.168.112.214:16667/wallet/broadcasttransaction";
        String params_2 = "{\"signature\":[\""+signature+"\"],\"txID\":\""+txID_1+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"resource\":\""+resource+"\",\"frozen_duration\":3,\"frozen_balance\":"+frozenBalance+",\"receiver_address\":\""+receiverAddress+"\",\"owner_address\":\""+ownerAddress+"\"},\"type_url\":\""+type_url_1+"\"},\"type\":\""+type_1+"\"}],\"ref_block_bytes\":\""+ref_block_bytes_1+"\",\"ref_block_hash\":\""+ref_block_hash_1+"\",\"expiration\":"+expiration_1+",\"timestamp\":"+timestamp_1+"},\"raw_data_hex\":\""+raw_data_hex_1+"\"}";

        String broadResponse = Utils.postByJson(url_2,params_2);
        System.out.println("broadResponse="+broadResponse);
        log.info("broadResponse={}",broadResponse);
        return broadResponse;



    }

    /**
     * 通过高度查询块
     * @param num
     * @return
     */
    public static String getblockbynum(String num){
        String url = "http://192.168.112.214:16667/wallet/getblockbynum";
        String params = "{\"num\" : \""+num+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 通过区块ID（即区块哈希）查询块
     * @param value
     * @return
     */
    public static String getblockbyid(String value){
        String url = "http://192.168.112.214:16667/wallet/getblockbyid";
        String params = "{\"value\" : \""+value+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     *查询最新块
     * @return
     */
    public static String getnowblock(){
        String url = "http://192.168.112.214:16667/wallet/getnowblock";
        return Utils.postByJson(url);
    }


    /**
     * 获取特定区块的所有交易 Info 信息
     * @param num
     * @return
     */
    public static String gettransactioninfobyblocknum(String num){
        String url = "http://192.168.112.214:16667/wallet/gettransactioninfobyblocknum";
        String params = "{\"num\" : \""+num+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     *
     * 在链上创建账号. 一个已经激活的账号创建一个新账号需要花费 0.1 TRX 或等值 Bandwidth.
     * @param ownerAddress
     * @param accountAddress
     * @param privateKey
     * @return
     */
    public static String createaccount(String ownerAddress,String accountAddress,String privateKey){
        String url = "http://192.168.112.214:16667/wallet/createaccount";
        String params = "{\"owner_address\":\""+ownerAddress+"\",\"account_address\": \""+accountAddress+"\"}";
        String createResponse =  Utils.postByJson(url,params);
        System.out.println("createResponse="+createResponse);
        log.info("createResponse={}",createResponse);
        JSONObject jsonObject = JSONObject.parseObject(createResponse);
        String txID = jsonObject.getString("txID");
        String type_url = jsonObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getString("type_url");
        String type = jsonObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getString("type");
        String ref_block_bytes = jsonObject.getJSONObject("raw_data").getString("ref_block_bytes");
        String ref_block_hash = jsonObject.getJSONObject("raw_data").getString("ref_block_hash");
        String expiration = jsonObject.getJSONObject("raw_data").getString("expiration");
        String timestamp = jsonObject.getJSONObject("raw_data").getString("timestamp");
        String raw_data_hex = jsonObject.getString("raw_data_hex");


        //签名
        String url_1 = "http://192.168.112.214:16667/wallet/gettransactionsign";
        String params_1 = "{\"transaction\":{\"txID\":\""+txID+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"owner_address\":\""+ownerAddress+"\",\"account_address\":\""+accountAddress+"\"},\"type_url\":\""+type_url+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+ref_block_bytes+"\",\"ref_block_hash\":\""+ref_block_hash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+"},\"raw_data_hex\":\""+raw_data_hex+"\"},\"privateKey\": \""+privateKey+"\"}";
        String signResponse = Utils.postByJson(url_1,params_1);
        System.out.println("signResponse="+signResponse);
        log.info("signResponse={}",signResponse);
        JSONObject jsonObject1 = JSONObject.parseObject(signResponse);
        String signature = jsonObject1.getJSONArray("signature").getString(0);
        String txID_1 = jsonObject1.getString("txID");
        String type_url_1 = jsonObject1.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getString("type_url");
        String type_1 = jsonObject1.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getString("type");
        String ref_block_bytes_1 = jsonObject1.getJSONObject("raw_data").getString("ref_block_bytes");
        String ref_block_hash_1 = jsonObject1.getJSONObject("raw_data").getString("ref_block_hash");
        String expiration_1 = jsonObject1.getJSONObject("raw_data").getString("expiration");
        String timestamp_1 = jsonObject1.getJSONObject("raw_data").getString("timestamp");
        String raw_data_hex_1 = jsonObject1.getString("raw_data_hex");

        //广播
        String url_2 = "http://192.168.112.214:16667/wallet/broadcasttransaction";
        String params_2 = "{\"signature\":[\""+signature+"\"],\"txID\":\""+txID_1+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"owner_address\":\""+ownerAddress+"\",\"account_address\":\""+accountAddress+"\"},\"type_url\":\""+type_url_1+"\"},\"type\":\""+type_1+"\"}],\"ref_block_bytes\":\""+ref_block_bytes_1+"\",\"ref_block_hash\":\""+ref_block_hash_1+"\",\"expiration\":"+expiration_1+",\"timestamp\":"+timestamp_1+"},\"raw_data_hex\":\""+raw_data_hex_1+"\"}";
        String broadResponse = Utils.postByJson(url_2,params_2);
        System.out.println("broadResponse="+broadResponse);
        log.info("broadResponse={}",broadResponse);
        return broadResponse;
    }

    /**
     * 冷钱包插入地址
     * @param address
     */
    public static void insertSendRecord(String address){
        Connection conn = null;

        try{
            // 注册 JDBC 驱动
            Class.forName(Calc.Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(Config.DB_URL_58WALLET, Calc.Config.USER_36, Calc.Config.PASS_36);

            String sql="insert into tb_wallet_address_cold_trx (address) values(?)";//sql语句
            PreparedStatement pstmt=conn.prepareStatement(sql);//获得预置对象
            pstmt.setString(1, address);


            int res=pstmt.executeUpdate();//执行sql语句
            if(res>0){
                System.out.println("数据录入成功");
            }
            pstmt.close();//关闭资源
            conn.close();//关闭资源

        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
}
