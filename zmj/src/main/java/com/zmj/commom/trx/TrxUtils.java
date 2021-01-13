package com.zmj.commom.trx;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.zmj.commom.HttpUtils;
import com.zmj.commom.trx.tools.ByteArray;
import com.zmj.commom.trx.tools.WalletApi;
import com.zmj.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
@Slf4j
public class TrxUtils {

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 生成地址，并生成指定的私钥
     * @return
     */
    public  String generateAddress(){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/generateaddress";
        String res = httpUtils.get(url);
        log.info("trx new address : {}",res);
        return res;
    }

    /**
     * 查看地址是否有效
     * @param address
     * @return
     */
    public  String validateAddress(String address){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/validateaddress";
        String params = "{\"address\":\""+address+"\"}";
        String res = httpUtils.postByJson(url,params);
        log.info("is validate address : {}",res);
        return res;
    }

    /**
     * 创建交易
     * @param ownerAddress
     * @param toAddress
     * @param value
     * @return
     */
    public  String createtransaction(String ownerAddress,String toAddress,String value,String privateKey){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/createtransaction";
        if(toAddress.length() != 42){
            toAddress = base58checkToHexString(toAddress);
        }
        if(ownerAddress.length() != 42){
            ownerAddress = Hex58Transfer.base58checkToHexString(ownerAddress);
        }
        String params = "{\"owner_address\": \""+ownerAddress+"\",\"to_address\": \""+toAddress+"\",\"amount\": "+value+"}";
        JSONObject paramsJson = JSONObject.parseObject(params);
        JSONObject jsonObject = restTemplate.postForEntity(url,paramsJson,JSONObject.class).getBody();
        log.info("create transaction hex : {}",jsonObject);
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
//        System.out.println("signResponse="+signResponse);
        log.info("transaction sign hex : {}",signResponse);
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
//        System.out.println("broadResponse="+broadResponse);
        log.info("broad cast transaction hex : {}",broadResponse);
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
    public  String gettransactionsign(String txId,String amount,String ownerAddress,String toAddress,String typeUrl,String type,String refBlockBytes,String refBlockHash,String expiration,String timestamp,String rawDataHex,String privateKey){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactionsign";
        String params = "{\"transaction\":{\"txID\":\""+txId+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"amount\":"+amount+",\"owner_address\":\""+ownerAddress+"\",\"to_address\":\""+toAddress+"\"},\"type_url\":\""+typeUrl+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+refBlockBytes+"\",\"ref_block_hash\":\""+refBlockHash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+"},\"raw_data_hex\":\""+rawDataHex+"\"},\"privateKey\": \""+privateKey+"\"}";
        return httpUtils.postByJson(url,params);
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
    public  String broadcasttransaction(String signature,String txId,String amount,String ownerAddress,String toAddress,String typeUrl,String type,String refBlockBytes,String refBlockHash,String expiration,String timestamp,String rawDataHex ){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/broadcasttransaction";
        String params = "{\"signature\":[\""+signature+"\"],\"txID\":\""+txId+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"amount\":"+amount+",\"owner_address\":\""+ownerAddress+"\",\"to_address\":\""+toAddress+"\"},\"type_url\":\""+typeUrl+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+refBlockBytes+"\",\"ref_block_hash\":\""+refBlockHash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+"},\"raw_data_hex\":\""+rawDataHex+"\"}";
        return httpUtils.postByJson(url,params);
    }

    /**
     * 查询交易所在的区块
     * @param txId
     * @return
     */
    public  String gettransactioninfobyid(String txId){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactioninfobyid";
        String params = "{\"value\" : \""+txId+"\"}";
        String res = httpUtils.postByJson(url,params);
        log.info("transaction info by id : {}",res);
        return res;
    }

    /**
     * 查询交易信息
     * @param txId
     * @return
     */
    public  String gettransactionbyid(String txId){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactionbyid";
        String params = "{\"value\" : \""+txId+"\"}";
        String res = httpUtils.postByJson(url,params);
        log.info("transaction by id : {}",res);
        return res;
    }

    /**
     * 查询账户资源
     * @param address
     * @return
     */
    public  String getaccountresource(String address){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/getaccountresource";
        if(address.length() != 42){
            address = Hex58Transfer.base58checkToHexString(address);
        }
        String params = "{\"address\" : \""+address+"\"}";
        String res = httpUtils.postByJson(url,params);
        log.info("account resource is : {}",res);
        return res;
    }

    /**
     * 查询合约信息
     * @param address
     * @return
     */
    public  String getcontract(String address){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/getcontract";
        String params = "{\"value\" : \""+address+"\"}";
        String res = httpUtils.postByJson(url,params);
        log.info("contract is : {}",res);
        return res;
    }

    /**
     * 查询账户余额
     * @param address
     * @return
     */
    public  String getaccount(String address){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/getaccount";
        if(address.length() != 42){
            address = base58checkToHexString(address);
        }
        String params = "{\"address\" : \""+address+"\"}";
        String res = httpUtils.postByJson(url,params);
        log.info("account is : {}",res);
        return res;
    }

    /**
     * 冻结 TRX, 获取带宽或者能量(投票权)
     * @param ownerAddress
     * @param frozenBalance
     * @param resource 冻结 TRX 获取资源的类型, 可以是 BANDWIDTH 或者 ENERGY
     * @param receiverAddress
     * @return
     */
    public  String freezebalance(String ownerAddress,String frozenBalance,String resource,String receiverAddress,String privateKey){

        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/freezebalance";
        String params = "{\"owner_address\":\""+ownerAddress+"\",\"frozen_balance\":"+frozenBalance+",\"frozen_duration\":3,\"resource\" :\""+resource+"\",\"receiver_address\":\""+receiverAddress+"\"}";
        String createResponse =  httpUtils.postByJson(url,params);
//        System.out.println("createResponse="+createResponse);
        log.info("create freeze balance hex is : {}",createResponse);
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
        String url_1 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactionsign";
        String params_1 = "{\"transaction\":{\"txID\":\""+txID+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"resource\":\""+resource+"\",\"frozen_duration\":3,\"frozen_balance\":"+frozenBalance+",\"receiver_address\":\""+receiverAddress+"\",\"owner_address\":\""+ownerAddress+"\"},\"type_url\":\""+type_url+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+ref_block_bytes+"\",\"ref_block_hash\":\""+ref_block_hash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+"},\"raw_data_hex\":\""+raw_data_hex+"\"},\"privateKey\": \""+privateKey+"\"}";
        String signResponse = httpUtils.postByJson(url_1,params_1);
//        System.out.println("signResponse="+signResponse);
        log.info("sign transaction hex is : {}",signResponse);
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
        String url_2 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/broadcasttransaction";
        String params_2 = "{\"signature\":[\""+signature+"\"],\"txID\":\""+txID_1+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"resource\":\""+resource+"\",\"frozen_duration\":3,\"frozen_balance\":"+frozenBalance+",\"receiver_address\":\""+receiverAddress+"\",\"owner_address\":\""+ownerAddress+"\"},\"type_url\":\""+type_url_1+"\"},\"type\":\""+type_1+"\"}],\"ref_block_bytes\":\""+ref_block_bytes_1+"\",\"ref_block_hash\":\""+ref_block_hash_1+"\",\"expiration\":"+expiration_1+",\"timestamp\":"+timestamp_1+"},\"raw_data_hex\":\""+raw_data_hex_1+"\"}";
        String broadResponse = httpUtils.postByJson(url_2,params_2);
//        System.out.println("broadResponse="+broadResponse);
        log.info("broad cast transaction hex is : {}",broadResponse);
        return broadResponse;



    }

    /**
     * 通过高度查询块
     * @param num
     * @return
     */
    public  String getblockbynum(String num){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/getblockbynum";
        String params = "{\"num\" : "+num+"}";
        String res = httpUtils.postByJson(url,params);
        log.info("block num is : {}",res);
        return res;
    }

    /**
     * 通过区块ID（即区块哈希）查询块
     * @param value
     * @return
     */
    public  String getblockbyid(String value){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/getblockbyid";
        String params = "{\"value\" : \""+value+"\"}";
        String res = httpUtils.postByJson(url,params);
        log.info("block by id : {}",res);
        return res;
    }

    /**
     *查询最新块
     * @return
     */
    public  String getnowblock(){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/getnowblock";
        String res = httpUtils.postByJson(url);
        log.info("now block is : {}",res);
        return res;
    }


    /**
     * 获取特定区块的所有交易 Info 信息
     * @param num
     * @return
     */
    public  String gettransactioninfobyblocknum(String num){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactioninfobyblocknum";
        String params = "{\"num\" : \""+num+"\"}";
        String res = httpUtils.postByJson(url,params);
        log.info("transaction info by block num is : {}",res);
        return res;
    }

    /**
     *
     * 在链上创建账号. 一个已经激活的账号创建一个新账号需要花费 0.1 TRX 或等值 Bandwidth.
     * @param ownerAddress
     * @param accountAddress
     * @param privateKey
     * @return
     */
    public  String createaccount(String ownerAddress,String accountAddress,String privateKey){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/createaccount";
        String params = "{\"owner_address\":\""+ownerAddress+"\",\"account_address\": \""+accountAddress+"\"}";
        String createResponse =  httpUtils.postByJson(url,params);
//        System.out.println("createResponse="+createResponse);
        log.info("create transaction hex is : {}",createResponse);
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
        String url_1 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactionsign";
        String params_1 = "{\"transaction\":{\"txID\":\""+txID+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"owner_address\":\""+ownerAddress+"\",\"account_address\":\""+accountAddress+"\"},\"type_url\":\""+type_url+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+ref_block_bytes+"\",\"ref_block_hash\":\""+ref_block_hash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+"},\"raw_data_hex\":\""+raw_data_hex+"\"},\"privateKey\": \""+privateKey+"\"}";
        String signResponse = httpUtils.postByJson(url_1,params_1);
//        System.out.println("signResponse="+signResponse);
        log.info("sign transaction hex is : {}",signResponse);
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
        String url_2 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/broadcasttransaction";
        String params_2 = "{\"signature\":[\""+signature+"\"],\"txID\":\""+txID_1+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"owner_address\":\""+ownerAddress+"\",\"account_address\":\""+accountAddress+"\"},\"type_url\":\""+type_url_1+"\"},\"type\":\""+type_1+"\"}],\"ref_block_bytes\":\""+ref_block_bytes_1+"\",\"ref_block_hash\":\""+ref_block_hash_1+"\",\"expiration\":"+expiration_1+",\"timestamp\":"+timestamp_1+"},\"raw_data_hex\":\""+raw_data_hex_1+"\"}";
        String broadResponse = httpUtils.postByJson(url_2,params_2);
//        System.out.println("broadResponse="+broadResponse);
        log.info("broad cast transaction hex is : {}",broadResponse);
        return broadResponse;
    }

    /**
     * base58checkToHexString
     * @param base58check
     * @return
     */
    private  String base58checkToHexString(String base58check) {
        String hexString = ByteArray.toHexString(WalletApi.decodeFromBase58Check(base58check));
        return hexString;
    }

    /**
     * hexStringTobase58check
     * @param hexString
     * @return
     */
    private  String hexStringTobase58check(String hexString) {
        String base58check = WalletApi.encode58Check(ByteArray.fromHexString(hexString));
        return base58check;
    }
}
