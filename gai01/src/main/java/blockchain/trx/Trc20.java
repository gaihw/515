package blockchain.trx;


import blockchain.Config;
import blockchain.Utils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;

public class Trc20 {
    static {
        System.setProperty("fileName", "trx/trx20.log");
    }
    public static Logger log = LoggerFactory.getLogger(Trc20.class);
    public static void main(String[] args) throws InterruptedException, SQLException {
        //转账地址
        //测试
        String owner_address = "TQrGzBmANSoHa3oafYc2i7ZL3TXFuTASLp";//
        String privateKey = "b6c5416864686c0e9be104a50fb2110e81047ff328b3647f3d6a6f5a4ffc56da";

        //线上
//        String owner_address = "41ee213cc7213df2ef24cad0eaaae6154d943ed273";//21541818
//        String privateKey = "fdbfd58e043910c185aa840f1b2d9ad1eba021091f88e55d66dc666037a5eaec";
        String callValue = "0";
        String contractAddress = Config.USDT_TRC20;

        String to_address = "TLeuPE1PgrfUaL9uWzjhAds8TXGBCPU6fV";
        Integer value = 1486813;

        //热钱包地址
        String address = "TDZXMjKFjF8NxCW4TmzZp21x55ZeyC7GK7";//
        String hotPriv = "b66c267a7f25c457fc1b6afec66cde391c33e86dc30925f7250a5bacbe98a1e8";

//        TLeuPE1PgrfUaL9uWzjhAds8TXGBCPU6fV
//        283ffd3aa0497c2f97edb5472b420c62d1894e959ee38f84495f34fa84c7d33d

//        TPsay8WkqcZviqR6W8Zkhe3Y5pqts8RMhV
//        f1869c3ac6e1d200cf8c724777412f3d1b2a7e6cd74d134846f81516fe1f9885
//       {"privateKey":"55b65141c965c99dd8868c905dbf72d6be0f28e8dd07f6475b8723e09bc957d4","address":"THFg5GxGPxr4Khyz1Wvy1B7qwnKaoMvPxp","hexAddress":"414fe622916094b9d78e47935453ea8fb980d12fce"}


        //转账
//        createtransaction(contractAddress,owner_address,"THFg5GxGPxr4Khyz1Wvy1B7qwnKaoMvPxp",callValue,privateKey,value);
//        createtransaction(contractAddress,owner_address,"TDZXMjKFjF8NxCW4TmzZp21x55ZeyC7GK7",callValue,privateKey,value);
        createtransaction(contractAddress,"THFg5GxGPxr4Khyz1Wvy1B7qwnKaoMvPxp","TDZXMjKFjF8NxCW4TmzZp21x55ZeyC7GK7",callValue,"55b65141c965c99dd8868c905dbf72d6be0f28e8dd07f6475b8723e09bc957d4",value);//TDZXMjKFjF8NxCW4TmzZp21x55ZeyC7GK7  TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1
//        for (int i = 0; i < 10; i++) {
//            BigDecimal value1 = BigDecimal.valueOf(Math.random()*10).setScale(6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(Math.pow(10.0,6))).setScale(0);
//            System.out.println(createtransaction(contractAddress,owner_address,to_address,callValue,privateKey,Integer.valueOf(String.valueOf(value1))));
//            System.out.println("value1==="+value1);
//        }
//        for (int i = 0; i < 20; i++) {
//            BigDecimal r = BigDecimal.valueOf(Math.random()*100).setScale(8, RoundingMode.HALF_UP);
//            String a = "";
//            if (r.compareTo(BigDecimal.valueOf(35))<0){
//                a = to_address;
//            }else if(r.compareTo(BigDecimal.valueOf(35))>=0&&r.compareTo(BigDecimal.valueOf(70))<0){
//                a = "TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1";
//            }else {
//                a = address;
//            }
//            BigDecimal value1 = BigDecimal.valueOf(Math.random()*10).setScale(6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(Math.pow(10.0,6))).setScale(0);
//            System.out.println(createtransaction(contractAddress,owner_address,a,callValue,privateKey,Integer.valueOf(String.valueOf(value1))));
//            System.out.println("value1==="+value1);
////            try {
////                Thread.sleep(1000);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
//        }

//        String txid = JSONObject.parseObject(tr).getString("txid");
        String txid = "f527e7f7cd597f5c34af34aeba5401a03a6bcc1b31fad3492c07e26f63ed908b";
//        System.out.println(gettransactionbyid(txid));
//        System.out.println(gettransactioninfobyid(txid));
//        THFg5GxGPxr4Khyz1Wvy1B7qwnKaoMvPxp","TLeuPE1PgrfUaL9uWzjhAds8TXGBCPU6fV
//        System.out.println(getaccount("TDZXMjKFjF8NxCW4TmzZp21x55ZeyC7GK7"));//128031068
//        System.out.println(getaccountresource("TDZXMjKFjF8NxCW4TmzZp21x55ZeyC7GK7"));
        System.out.println(balanceOf(contractAddress,"TDZXMjKFjF8NxCW4TmzZp21x55ZeyC7GK7"));//851178-851178  11903677-10177377 11664190-1486813

        //获取当前块高
//        System.out.println(getnowblock());


//        String[] trc20Address = {"TRdSDedzUkHpyxWVT9bEp7ktrV5ZYHWpgj","TMrCy9tWbXaAenUJq76ywiuXdsxMzigdjR","TFzTkjmPNRiC6REd2a9E2nLvW6T4boFMeo",
//                "TLe3RFCNnCg9w8EtcKHcD8DbyGAZXcmS75","TXgKe6D5XuPknzkWfkqTeG2hYfYTY46U6N","TPBUKVkZxvSnsRcw9J6u29mkpSyqkLyCU5",
//                "TUcoCoaXKgaKiM7LNLQe3YpMHcz2fE6mij"};
        BigDecimal sum = BigDecimal.ZERO;
//        for (int i = 0; i < trc20Address.length; i++) {
//            System.out.println(trc20Address[i]+"==="+Integer.parseInt(JSONObject.parseObject(balanceOf(contractAddress,Hex58Transfer.base58checkToHexString(trc20Address[i]))).getJSONArray("constant_result").getString(0),16));
//            sum = sum.add(BigDecimal.valueOf(Integer.parseInt(JSONObject.parseObject(balanceOf(contractAddress,Hex58Transfer.base58checkToHexString(trc20Address[i]))).getJSONArray("constant_result").getString(0),16)));
//        }
//        System.out.println("sum="+sum);
        //计算
        //十进制--->十六进制 ：Integer.toHexString(42677453)
//        十六进制--->十进制 ：Integer.parseInt("28e948c",16)
//        System.out.println(Integer.toHexString(42677453));
//        System.out.println(Integer.parseInt("6038c5",16));
//
//        ResultSet rs = selectColdTrx();
//        while (rs.next()){
//            String a = rs.getString("address");
//            int u = rs.getInt("user_id");
//            String b = Hex58Transfer.base58checkToHexString(a);
////            BigDecimal value1 = BigDecimal.valueOf(Math.random()*2).setScale(6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(Math.pow(10.0,6))).setScale(0);
////            System.out.println(createtransaction(contractAddress,owner_address,a,callValue,privateKey,Integer.valueOf(String.valueOf(value1))));
//            System.out.println(u+"==="+a+"==="+b+"==="+BigDecimal.valueOf(Integer.parseInt(JSONObject.parseObject(balanceOf(contractAddress,b)).getJSONArray("constant_result").getString(0),16)).divide(BigDecimal.valueOf(1000000)));
//            if (u != 0 ) {
//                sum = sum.add(BigDecimal.valueOf(Integer.parseInt(JSONObject.parseObject(balanceOf(contractAddress, b)).getJSONArray("constant_result").getString(0), 16)));
//            }
//        }
//        System.out.println("sum="+sum.divide(BigDecimal.valueOf(1000000)));
//

//        for (int i = 0; i < 500; i++) {
//            multipleSignatures();

//        }

    }

    /**
     * 多签
     * 214 trc20
     * owner:
     * {"privateKey":"3cabca99054100ef8436a6d2763527708bc798a0867734b0137e8d607cfbfd50","address":"TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1","hexAddress":"41ccbbde3e6076a1529c4c0c8b11838ad2d9c0adc8"}
     *active:
     * {"privateKey":"a5a2a1cc37b6b4527d6a4c791464ea024ac6b36c29905ab0c474c8f72311b940","address":"TJ95iZRLE9A2518EDBTmLjGNC5pH7W8CtM","hexAddress":"41599f15ca607df5a8df54c4493c3770f53d28e74e"}
     * {"privateKey":"c8a0675f5cd651a065e48a39df3d9ea26307fbe994c4d7b6b17fd2e2896b36ee","address":"TVLPFNm1mWgrYq2jCrF1WqHRJ7hZnTwhH7","hexAddress":"41d46badc0ded460284984d0cc5f52d994f5c95840"}
     * {"privateKey":"5f6126c2f1174038c565e27f3f048193fb0c0ec10cf81c93c916dc19da80fccd","address":"TLJnkjPh85wGhhqGjywco7QDaM7CCm25hY","hexAddress":"417165563090e91e4230b8eb43cab7018b38fd0d1d"}
     * to:
     * {"privateKey":"d53905fa5099aed41f948698a79111653e3108ef20db242a757cded3bc3b762b","address":"TSFRcLqmaKQpANGnRZBMTLAckEjh3RayjU","hexAddress":"41b293315e3748bb770d1dd6b4566b330f5e86ee66"}
     *
     *
     *  {"privateKey":"07045dd51dbbd38a0c62deb59829f7f3aaf729df376053b38569c45d4c156ddf","address":"TSdbWa4PPT1Zpthf98ieKEk1DeoWEKd5WR","hexAddress":"41b6c49bc735923e6580717c541bfcbb5ba6fff817"}
     *      * {"privateKey":"b694a39961cc02c8f67f9d8c6aae963268439d27aee87c21d9d21cd1ff616adb","address":"TQwmgDkgHqnqCM1MbR5rPjjTJoDvmiuzkA","hexAddress":"41a44454a9ecbe9d531855fce07a75ac6127d28510"}
     *      * {"privateKey":"112b67127c521c69097d55ef61704800be17468db8ed62b2c90afcb066b9afef","address":"TJqj7i72xB1T7J21UUrNJTbCqBYp5jsFbJ","hexAddress":"41614ef15c559ddbb68882f99f05bd7d19740862b2"}
     *      * {"privateKey":"5e4d322708ab3e5f8c0b86261999e10e213dfe52cee7a6e325fdf216ab36b6ba","address":"TLGxUo2r5pZEdiJ2VKB5ED9Xptno2RTZVg","hexAddress":"41710c9f7a4d616895979b4339b0110b82c632bf29"}
     */
    public static void multipleSignatures(){
        String ownerAddress = "TSdbWa4PPT1Zpthf98ieKEk1DeoWEKd5WR";
        String privateKey = "3cabca99054100ef8436a6d2763527708bc798a0867734b0137e8d607cfbfd50";

        String toAddress = "TSFRcLqmaKQpANGnRZBMTLAckEjh3RayjU";

        String callValue = "0";
        String contractAddress = Config.USDT_TRC20;

//        1、创建交易
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/triggersmartcontract";
        Integer value = 500000;
        String toHexString = Integer.toHexString(value);
        String  startZeroStr = String.format("%0"+(64-toHexString.length())+"d",Integer.valueOf("0"))+toHexString;
        if(toAddress.length() != 42){
            toAddress = Hex58Transfer.base58checkToHexString(toAddress);
        }
        if(ownerAddress.length() != 42){
            ownerAddress = Hex58Transfer.base58checkToHexString(ownerAddress);
        }
        String to_address_param = "0000000000000000000000"+toAddress+startZeroStr;
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"transfer(address,uint256)\",\"parameter\":\""+to_address_param+"\",\"fee_limit\":1000000000,\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\", \"Permission_id\" : 2}";
        String createResponse =  Utils.postByJson(url,params);
        System.out.println("createResponse="+createResponse);
        log.info("createResponse={}",createResponse);

//        2、签名
        String url_1 = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/addtransactionsign";
        String params_1 = "{\"transaction\":"+JSONObject.parseObject(createResponse).getJSONObject("transaction")+",\"privateKey\": \"b694a39961cc02c8f67f9d8c6aae963268439d27aee87c21d9d21cd1ff616adb\"}";
        String signResponse = Utils.postByJson(url_1,params_1);

        String params_2 = "{\"transaction\":"+signResponse+",\"privateKey\": \"112b67127c521c69097d55ef61704800be17468db8ed62b2c90afcb066b9afef\"}";
        String signResponse1 = Utils.postByJson(url_1,params_2);

//        3、广播
        String url_2 = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/broadcasttransaction";
        System.out.println(Utils.postByJson(url_2,signResponse1));


    }

    /**
     * 创建交易
     * @param ownerAddress
     * @param toAddress
     * @param callValue
     * @return
     */
    public static String createtransaction(String contractAddress,String ownerAddress,String toAddress,String callValue,String privateKey,Integer value){
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/triggersmartcontract";
        String toHexString = Integer.toHexString(value);
        String  startZeroStr = String.format("%0"+(64-toHexString.length())+"d",Integer.valueOf("0"))+toHexString;
//        System.out.println(startZeroStr);
        if(toAddress.length() != 42){
            toAddress = Hex58Transfer.base58checkToHexString(toAddress);
        }
        if(ownerAddress.length() != 42){
            ownerAddress = Hex58Transfer.base58checkToHexString(ownerAddress);
        }
        String to_address_param = "0000000000000000000000"+toAddress+startZeroStr;
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
        System.out.println("0a"+Integer.toHexString(raw_data_hex_1.length()/2)+"01"+raw_data_hex_1+1241+signature);
        log.info("sign:::::{}",("0a"+Integer.toHexString(raw_data_hex_1.length()/2)+"01"+raw_data_hex_1+1241+signature));

        //广播
        String broadResponse = broadcasttransaction(signature,txID_1,callValue,contractAddress,ownerAddress,data_1,type_url_1,type_1,ref_block_bytes_1,ref_block_hash_1,expiration_1,timestamp_1,raw_data_hex_1);
        System.out.println("broadResponse="+broadResponse);
        log.info("broadResponse={}",broadResponse);
        return broadResponse;
//        return null;
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
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/addtransactionsign";
        String params = "{\"transaction\":{\"visible\": false,\"txID\":\""+txId+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"contract_address\":\""+contract_address+"\",\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\",\"data\":\""+toAddress+"\"},\"type_url\":\""+typeUrl+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+refBlockBytes+"\",\"ref_block_hash\":\""+refBlockHash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+",\"fee_limit\":1000000000},\"raw_data_hex\":\""+rawDataHex+"\"},\"privateKey\": \""+privateKey+"\"}";
        System.out.println("s="+params);
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
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/broadcasttransaction";
        String params = "{\"signature\":[\""+signature+"\"],\"txID\":\""+txId+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"contract_address\":\""+contract_address+"\",\"call_value\":"+callValue+",\"owner_address\":\""+ownerAddress+"\",\"data\":\""+toAddress+"\"},\"type_url\":\""+typeUrl+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+refBlockBytes+"\",\"ref_block_hash\":\""+refBlockHash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+",\"fee_limit\":1000000000},\"raw_data_hex\":\""+rawDataHex+"\"}";
        System.out.println("b="+params);
        return Utils.postByJson(url,params);
    }

    /**
     * 对签名后的交易hex，广播
     * @param txidHex
     * @return
     */
    public static String broadcasthex(String txidHex){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/broadcasthex";
        String params = "{\"transaction\" : \""+txidHex+"\"}";
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
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/triggersmartcontract";
        if(ownerAddress.length() != 42){
            ownerAddress = Hex58Transfer.base58checkToHexString(ownerAddress);
        }
        String params = "{\"contract_address\":\""+contractAddress+"\",\"function_selector\":\"balanceOf(address)\",\"parameter\":\"0000000000000000000000"+ownerAddress+"\",\"owner_address\":\""+ownerAddress+"\"}";
        String res = Utils.postByJson(url,params);
        log.info(res);
        return res;
    }

    /**
     * 查询USDT通证资产的名称
     * @param contractAddress
     * @param ownerAddress
     * @return
     */
    public static String name(String contractAddress,String ownerAddress){
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/triggersmartcontract";
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
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/triggerconstantcontract";
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
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/triggerconstantcontract";
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
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/triggersmartcontract";
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
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/triggersmartcontract";
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
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/gettransactioninfobyid";
        String params = "{\"value\" : \""+txId+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 查询交易信息
     * @param txId
     * @return
     */
    public static String gettransactionbyid(String txId){
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/gettransactionbyid";
        String params = "{\"value\" : \""+txId+"\"}";
        return Utils.postByJson(url,params);
    }


    /**
     * 查询账户余额
     * @param address
     * @return
     */
    public static String getaccount(String address){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/getaccount";
        if(address.length() != 42){
            address = Hex58Transfer.base58checkToHexString(address);
        }
        String params = "{\"address\" : \""+address+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 查询账户资源
     * @param address
     * @return
     */
    public static String getaccountresource(String address){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/getaccountresource";
        if(address.length() != 42){
            address = Hex58Transfer.base58checkToHexString(address);
        }
        String params = "{\"address\" : \""+address+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     *查询最新块
     * @return
     */
    public static String getnowblock(){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/getnowblock";
        return Utils.postByJson(url);
    }

    public static ResultSet selectColdTrx(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = " select address,user_id from 58wallet.tb_wallet_address_cold_trx ";//where id>=1742 and id<=1756
        try{
            // 注册 JDBC 驱动
            Class.forName(Calc.Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(Calc.Config.DB_URL_36, Calc.Config.USER_36, Calc.Config.PASS_36);
            // 执行查询
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            return rs;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        return null;
    }
}
