package com.zmj.commom.trx;

import com.alibaba.fastjson.JSONObject;
import com.zmj.commom.HttpUtils;
import com.zmj.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Trc20Utils {

    @Autowired
    private HttpUtils httpUtils;
    /**
     * 创建交易
     * @param ownerAddress
     * @param toAddress
     * @param callValue
     * @return
     */
    public  String createtransaction(String contractAddress,String ownerAddress,String toAddress,String callValue,String privateKey,String value){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/triggersmartcontract";
        String to_address_param = "0000000000000000000000"+toAddress+value;
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"transfer(address,uint256)\",\"parameter\":\""+to_address_param+"\",\"fee_limit\":1000000000,\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\"}";
        String createResponse =  httpUtils.postByJson(url,params);
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
    public  String gettransactionsign(String txId,String callValue,String contract_address,String ownerAddress,String toAddress,String typeUrl,String type,String refBlockBytes,String refBlockHash,String expiration,String timestamp,String rawDataHex,String privateKey){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/addtransactionsign";
        String params = "{\"transaction\":{\"visible\": false,\"txID\":\""+txId+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"contract_address\":\""+contract_address+"\",\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\",\"data\":\""+toAddress+"\"},\"type_url\":\""+typeUrl+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+refBlockBytes+"\",\"ref_block_hash\":\""+refBlockHash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+",\"fee_limit\":1000000000},\"raw_data_hex\":\""+rawDataHex+"\"},\"privateKey\": \""+privateKey+"\"}";
//        System.out.println("s="+params);
        return httpUtils.postByJson(url,params);
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
    public  String broadcasttransaction(String signature,String txId,String callValue,String contract_address,String ownerAddress,String toAddress,String typeUrl,String type,String refBlockBytes,String refBlockHash,String expiration,String timestamp,String rawDataHex ){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/broadcasttransaction";
        String params = "{\"signature\":[\""+signature+"\"],\"txID\":\""+txId+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"contract_address\":\""+contract_address+"\",\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\",\"data\":\""+toAddress+"\"},\"type_url\":\""+typeUrl+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+refBlockBytes+"\",\"ref_block_hash\":\""+refBlockHash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+",\"fee_limit\":1000000000},\"raw_data_hex\":\""+rawDataHex+"\"}";
//        System.out.println("b="+params);
        return httpUtils.postByJson(url,params);
    }

    /**
     * 查询代币余额
     * parameter是合约方法要传入的参数，本例中应该传入的是address。由于波场的地址结构是地址前缀"41"+20字节地址，传地址参数的时候要求是32字节的，所以前面用"0"进行补齐
     * @param contractAddress
     * @param ownerAddress
     * @return
     */
    public  String balanceOf(String contractAddress,String ownerAddress){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/triggersmartcontract";
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"balanceOf(address)\",\"parameter\":\"0000000000000000000000"+ownerAddress+"\",\"owner_address\":\""+ownerAddress+"\"}";
        return httpUtils.postByJson(url,params);
    }

    /**
     * 查询USDT通证资产的名称
     * @param contractAddress
     * @param ownerAddress
     * @return
     */
    public  String name(String contractAddress,String ownerAddress){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/triggersmartcontract";
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"name()\",\"parameter\":\"0000000000000000000000"+ownerAddress+"\",\"owner_address\":\""+ownerAddress+"\"}";
        System.out.println(params);
        return httpUtils.postByJson(url,params);
    }

    /**
     * 查询USDT通证的精度
     * @param contractAddress
     * @param ownerAddress
     * @return
     */
    public  String decimals(String contractAddress,String ownerAddress){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/triggerconstantcontract";
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"decimals()\",\"parameter\":\"0000000000000000000000"+ownerAddress+"\",\"owner_address\":\""+ownerAddress+"\"}";
        return httpUtils.postByJson(url,params);
    }

    /**
     * 查询USDT通证的发行总量
     * @param contractAddress
     * @param ownerAddress
     * @return
     */
    public  String totalSupply(String contractAddress,String ownerAddress){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/triggerconstantcontract";
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"totalSupply()\",\"parameter\":\"0000000000000000000000"+ownerAddress+"\",\"owner_address\":\""+ownerAddress+"\"}";
        return httpUtils.postByJson(url,params);
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
    public  String approve(String contractAddress,String toAddress,String ownerAddress,String callValue,String privateKey){
        String parameter = "0000000000000000000000"+toAddress+"00000000000000000000000000000000000000000000000000000000000f4240";
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/triggersmartcontract";
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"approve(address,uint256)\",\"parameter\":\""+parameter+"\",\"fee_limit\":1000,\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\"}";
        String appResponse = httpUtils.postByJson(url,params);
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
    public  String transferFrom(String contractAddress,String toAddress,String toAddress_1,String ownerAddress,String callValue,String privateKey){
        String parameter = "0000000000000000000000"+toAddress_1+"0000000000000000000000"+toAddress+"00000000000000000000000000000000000000000000000000000000000f4240";
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/triggersmartcontract";
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"transferFrom(address,address,uint256)\",\"parameter\":\""+parameter+"\",\"fee_limit\":1000,\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\"}";
        String transferFrom = httpUtils.postByJson(url,params);
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
    public  String gettransactioninfobyid(String txId){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactioninfobyid";
        String params = "{\"value\" : \""+txId+"\"}";
        return httpUtils.postByJson(url,params);
    }

    /**
     * 查询交易信息
     * @param txId
     * @return
     */
    public  String gettransactionbyid(String txId){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactionbyid";
        String params = "{\"value\" : \""+txId+"\"}";
        return httpUtils.postByJson(url,params);
    }
}
