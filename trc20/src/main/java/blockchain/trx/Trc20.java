package blockchain.trx;


import blockchain.Config;
import blockchain.Utils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class Trc20 {
    static {
        System.setProperty("fileName", "trx/trx20.log");
    }
    public static Logger log = LoggerFactory.getLogger(Trc20.class);
    public static void main(String[] args) {

        //转账地址
        String owner_address = "414311f08873669b35f995e0bfb603509e1d96c380";
        String privateKey = "61cae49321a8f13788392b9b4b76dd3410007bb8aca89acdbefe0692d175c684";
        String callValue = "0";
        String contractAddress = Config.USDT_TRC20;

        String to_address = "415841C20EC3AB0251675C322E5AB7807891A18A84";
        String value = "000000000000000000000000000000000000000000000000000000000cca0b98";//cca0b98 214567832

        String to_address_1 = "41F033DDCF1EC270C7B77A0BD5F4B5B0051FC9300C";
        String value_1 = "00000000000000000000000000000000000000000000000000000000017ab721";//17ab721 24819489

        String to_address_2 = "416C000FFF1C2E4F835C1F9E518F88E139E6AFC65F";
        String value_2 = "00000000000000000000000000000000000000000000000000000000028e948c";//28e948c 42898572

        String to_address_3 = "41D77F59A81218EC1E37E7C0DD44E799523B57B412";
        String value_3 = "00000000000000000000000000000000000000000000000000000000028b34cd";//28b34cd 42677453

        //热钱包地址
        String address = "41c89c621abd67ef381d0be302613d6fb2c7508b1c";//TNQR9CgoMNqFjcsQZBYZZB7thj91jZQDcW  trx:30103450 trc20:8974623
        String value_4 = "00000000000000000000000000000000000000000000000000000019debb4fc0";//19debb4fc0
        String value_5 = "000000000000000000000000000000000000000000000000000000000cca0b98";//1295716
        String value_6 = "00000000000000000000000000000000000000000000000000000000000f4240";//132359f
        String value_7 = "000000000000000000000000000000000000000000000000000000e8d495cdc0";//e8d495cdc0

        System.out.println(createtransaction(contractAddress,owner_address,address,callValue,privateKey,value_6));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address,callValue,privateKey,value));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_1,callValue,privateKey,value_1));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_1,callValue,privateKey,value_1));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_2,callValue,privateKey,value_2));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_2,callValue,privateKey,value_2));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_3,callValue,privateKey,value_1));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_3,callValue,privateKey,value_3));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_2,callValue,privateKey,value_2));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_3,callValue,privateKey,value_2));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address,callValue,privateKey,value_7));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address,callValue,privateKey,value));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_1,callValue,privateKey,value_1));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_1,callValue,privateKey,value_1));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_2,callValue,privateKey,value_2));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_2,callValue,privateKey,value_2));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_3,callValue,privateKey,value_1));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_3,callValue,privateKey,value_3));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_2,callValue,privateKey,value_2));
//        System.out.println(createtransaction(contractAddress,owner_address,to_address_3,callValue,privateKey,value_2));

//        System.out.println(createtransaction(contractAddress,owner_address,address,callValue,privateKey,value_5));
//        System.out.println(createtransaction(contractAddress,owner_address,address,callValue,privateKey,value_5));
//        System.out.println(createtransaction(contractAddress,owner_address,address,callValue,privateKey,value_6));
        System.out.println(balanceOf(contractAddress,address));//120000000
//        System.out.println(balanceOf(contractAddress,to_address_1));//393481e 688b660
//        System.out.println(balanceOf(contractAddress,to_address_2));//7abbda4
//        System.out.println(balanceOf(contractAddress,to_address_3));//7a19e67
//        System.out.println(balanceOf(contractAddress,owner_address));//6a94d6fd358e7a 6a94d6d15eacfd 6a94d6d15eacfc
//        System.out.println(balanceOf(contractAddress,address));//88f11f e036a6(14694054) 322e2f(3288623)

        String txId = "fce2a77e16381afffe028d326aa428552478296473fbfde0b3737f505b47ec84";
        System.out.println(gettransactioninfobyid(txId));
        System.out.println(gettransactionbyid(txId));

        //计算
        //十进制--->十六进制 ：Integer.toHexString(42677453)
        //十六进制--->十进制 ：Integer.parseInt("28e948c",16)
//        System.out.println(Integer.toHexString(42677453));
//        System.out.println(Integer.parseInt("28e948c",16));

//        BigDecimal to_address_sum = BigDecimal.valueOf(214567832).multiply(BigDecimal.valueOf(2));
//        BigDecimal to_address_1_sum = BigDecimal.valueOf(24819489).multiply(BigDecimal.valueOf(2));
//        BigDecimal to_address_2_sum = BigDecimal.valueOf(42898572).multiply(BigDecimal.valueOf(3));
//        BigDecimal to_address_3_sum = BigDecimal.valueOf(42677453).multiply(BigDecimal.valueOf(3));
//        BigDecimal sum = to_address_sum.add(to_address_1_sum).add(to_address_2_sum).add(to_address_3_sum);
//        System.out.println("to_address_sum="+to_address_sum);
//        System.out.println("to_address_1_sum="+to_address_1_sum);
//        System.out.println("to_address_2_sum="+to_address_2_sum);
//        System.out.println("to_address_3_sum="+to_address_3_sum);
//        System.out.println("sum="+sum);

//        to_address_sum=429135664
//        to_address_1_sum=49638978
//        to_address_2_sum=128695716
//        to_address_3_sum=128032359
//        sum=735502717
//        System.out.println(Integer.toHexString(14694054));
//        System.out.println(Integer.toHexString(1295716));
//        System.out.println(Integer.toHexString(132359));
//        System.out.println(Integer.toHexString(Integer.parseInt("393481e",16)+49638978));
//        System.out.println(BigDecimal.valueOf(29999998623387256L).subtract(BigDecimal.valueOf(735502717)));
//        System.out.println(Integer.parseInt("88f11f",16));

    }

    /**
     * 创建交易
     * @param ownerAddress
     * @param toAddress
     * @param callValue
     * @return
     */
    public static String createtransaction(String contractAddress,String ownerAddress,String toAddress,String callValue,String privateKey,String value){
        String url = "http://192.168.112.214:16667/wallet/triggersmartcontract";
        String to_address_param = "0000000000000000000000"+toAddress+value;
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"transfer(address,uint256)\",\"parameter\":\""+to_address_param+"\",\"fee_limit\":1000000000,\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\"}";
//        System.out.println("c="+params);
        String createResponse =  Utils.postByJson(url,params);
        System.out.println("createResponse="+createResponse);
        log.info("createResponse={}",createResponse);
        JSONObject jsonObject = JSONObject.parseObject(createResponse);
        String txID = jsonObject.getJSONObject("transaction").getString("txID");
        String type_url = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getString("type_url");
        String type = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getString("type");
        String ref_block_bytes = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getString("ref_block_bytes");
        String ref_block_hash = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getString("ref_block_hash");
        String expiration = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getString("expiration");
        String timestamp = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getString("timestamp");
        String raw_data_hex = jsonObject.getJSONObject("transaction").getString("raw_data_hex");
        String data = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("data");

        //签名
        String signResponse = gettransactionsign(txID,callValue,contractAddress,ownerAddress,data,type_url,type,ref_block_bytes,ref_block_hash,expiration,timestamp,raw_data_hex,privateKey);
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
        String data_1 = jsonObject1.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("data");

        //广播
        String broadResponse = broadcasttransaction(signature,txID_1,callValue,contractAddress,ownerAddress,data_1,type_url_1,type_1,ref_block_bytes_1,ref_block_hash_1,expiration_1,timestamp_1,raw_data_hex_1);
        System.out.println("broadResponse="+broadResponse);
        log.info("broadResponse={}",broadResponse);
        return broadResponse;
    }

    /**
     * 签名
     * @param txId
     * @param callValue
     * @param ownerAddress
     * @param toAddress
     * @param rawDataHex
     * @param privateKey
     * @return
     */
    public static String gettransactionsign(String txId,String callValue,String contract_address,String ownerAddress,String toAddress,String typeUrl,String type,String refBlockBytes,String refBlockHash,String expiration,String timestamp,String rawDataHex,String privateKey){
//        String url = "http://192.168.112.214:16667/wallet/gettransactionsign";
        String url = "http://192.168.112.214:16667/wallet/addtransactionsign";
        String params = "{\"transaction\":{\"visible\": false,\"txID\":\""+txId+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"contract_address\":\""+contract_address+"\",\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\",\"data\":\""+toAddress+"\"},\"type_url\":\""+typeUrl+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+refBlockBytes+"\",\"ref_block_hash\":\""+refBlockHash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+",\"fee_limit\":1000000000},\"raw_data_hex\":\""+rawDataHex+"\"},\"privateKey\": \""+privateKey+"\"}";
//        System.out.println("s="+params);
        return Utils.postByJson(url,params);
    }

    /**
     * 广播
     * @param signature
     * @param txId
     * @param callValue
     * @param ownerAddress
     * @param toAddress
     * @param rawDataHex
     * @return
     */
    public static String broadcasttransaction(String signature,String txId,String callValue,String contract_address,String ownerAddress,String toAddress,String typeUrl,String type,String refBlockBytes,String refBlockHash,String expiration,String timestamp,String rawDataHex ){
        String url = "http://192.168.112.214:16667/wallet/broadcasttransaction";
        String params = "{\"signature\":[\""+signature+"\"],\"txID\":\""+txId+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"contract_address\":\""+contract_address+"\",\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\",\"data\":\""+toAddress+"\"},\"type_url\":\""+typeUrl+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+refBlockBytes+"\",\"ref_block_hash\":\""+refBlockHash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+",\"fee_limit\":1000000000},\"raw_data_hex\":\""+rawDataHex+"\"}";
//        System.out.println("b="+params);
        return Utils.postByJson(url,params);
    }

    /**
     * 查询代币余额
     * parameter是合约方法要传入的参数，本例中应该传入的是address。由于波场的地址结构是地址前缀"41"+20字节地址，传地址参数的时候要求是32字节的，所以前面用"0"进行补齐
     * @param contractAddress
     * @param ownerAddress
     * @return
     */
    public static String balanceOf(String contractAddress,String ownerAddress){
        String url = "http://192.168.112.214:16667/wallet/triggersmartcontract";
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"balanceOf(address)\",\"parameter\":\"0000000000000000000000"+ownerAddress+"\",\"owner_address\":\""+ownerAddress+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 查询USDT通证资产的名称
     * @param contractAddress
     * @param ownerAddress
     * @return
     */
    public static String name(String contractAddress,String ownerAddress){
//        String url = "http://192.168.112.214:16667/wallet/triggerconstantcontract";
        String url = "http://192.168.112.214:16667/wallet/triggersmartcontract";
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"name()\",\"parameter\":\"0000000000000000000000"+ownerAddress+"\",\"owner_address\":\""+ownerAddress+"\"}";
        System.out.println(params);
        return Utils.postByJson(url,params);
    }

    /**
     * 查询USDT通证的精度
     * @param contractAddress
     * @param ownerAddress
     * @return
     */
    public static String decimals(String contractAddress,String ownerAddress){
        String url = "http://192.168.112.214:16667/wallet/triggerconstantcontract";
//        String url = "http://192.168.112.214:16667/wallet/triggersmartcontract";
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"decimals()\",\"parameter\":\"0000000000000000000000"+ownerAddress+"\",\"owner_address\":\""+ownerAddress+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 查询USDT通证的发行总量
     * @param contractAddress
     * @param ownerAddress
     * @return
     */
    public static String totalSupply(String contractAddress,String ownerAddress){
        String url = "http://192.168.112.214:16667/wallet/triggerconstantcontract";
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"totalSupply()\",\"parameter\":\"0000000000000000000000"+ownerAddress+"\",\"owner_address\":\""+ownerAddress+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 调用TRC20合约的approve函数授权代币使用权给其他地址
     * @param contractAddress
     * @param toAddress
     * @param ownerAddress
     * @param callValue
     * @param privateKey
     * @return
     */
    public static String approve(String contractAddress,String toAddress,String ownerAddress,String callValue,String privateKey){
        String parameter = "0000000000000000000000"+toAddress+"00000000000000000000000000000000000000000000000000000000000f4240";
        String url = "http://192.168.112.214:16667/wallet/triggersmartcontract";
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"approve(address,uint256)\",\"parameter\":\""+parameter+"\",\"fee_limit\":1000,\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\"}";
        String appResponse = Utils.postByJson(url,params);
        System.out.println("appResponse="+appResponse);
        log.info("appResponse={}",appResponse);
        JSONObject jsonObject = JSONObject.parseObject(appResponse);
        String txID = jsonObject.getJSONObject("transaction").getString("txID");
        String type_url = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getString("type_url");
        String type = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getString("type");
        String ref_block_bytes = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getString("ref_block_bytes");
        String ref_block_hash = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getString("ref_block_hash");
        String expiration = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getString("expiration");
        String timestamp = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getString("timestamp");
        String raw_data_hex = jsonObject.getJSONObject("transaction").getString("raw_data_hex");
        String data = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("data");

        //签名
        String signResponse = gettransactionsign(txID,callValue,contractAddress,ownerAddress,data,type_url,type,ref_block_bytes,ref_block_hash,expiration,timestamp,raw_data_hex,privateKey);
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
        String broadResponse = broadcasttransaction(signature,txID_1,callValue,contractAddress,ownerAddress,data,type_url_1,type_1,ref_block_bytes_1,ref_block_hash_1,expiration_1,timestamp_1,raw_data_hex_1);
        System.out.println("broadResponse="+broadResponse);
        log.info("broadResponse={}",broadResponse);
        return broadResponse;
    }

    /**
     * 调用TRC20合约的transferFrom函数从他们账户中转账代币，需要配合approve方法使用
     * @param contractAddress
     * @param toAddress
     * @param toAddress_1
     * @param ownerAddress
     * @param callValue
     * @param privateKey
     * @return
     */
    public static String transferFrom(String contractAddress,String toAddress,String toAddress_1,String ownerAddress,String callValue,String privateKey){
        String parameter = "0000000000000000000000"+toAddress_1+"0000000000000000000000"+toAddress+"00000000000000000000000000000000000000000000000000000000000f4240";
        String url = "http://192.168.112.214:16667/wallet/triggersmartcontract";
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"transferFrom(address,address,uint256)\",\"parameter\":\""+parameter+"\",\"fee_limit\":1000,\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\"}";
        String transferFrom = Utils.postByJson(url,params);
        System.out.println("transferFrom="+transferFrom);
        log.info("transferFrom={}",transferFrom);
        JSONObject jsonObject = JSONObject.parseObject(transferFrom);
        String txID = jsonObject.getJSONObject("transaction").getString("txID");
        String type_url = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getString("type_url");
        String type = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getString("type");
        String ref_block_bytes = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getString("ref_block_bytes");
        String ref_block_hash = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getString("ref_block_hash");
        String expiration = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getString("expiration");
        String timestamp = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getString("timestamp");
        String raw_data_hex = jsonObject.getJSONObject("transaction").getString("raw_data_hex");
        String data = jsonObject.getJSONObject("transaction").getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("data");

        //签名
        String signResponse = gettransactionsign(txID,callValue,contractAddress,ownerAddress,data,type_url,type,ref_block_bytes,ref_block_hash,expiration,timestamp,raw_data_hex,privateKey);
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
        String broadResponse = broadcasttransaction(signature,txID_1,callValue,contractAddress,ownerAddress,data,type_url_1,type_1,ref_block_bytes_1,ref_block_hash_1,expiration_1,timestamp_1,raw_data_hex_1);
        System.out.println("broadResponse="+broadResponse);
        log.info("broadResponse={}",broadResponse);
        return broadResponse;
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
}
