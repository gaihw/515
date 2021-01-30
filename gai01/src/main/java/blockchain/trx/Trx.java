package blockchain.trx;

import blockchain.Config;
import blockchain.Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Trx {

    static {
        System.setProperty("fileName", "trx/trx.log");
    }
    public static Logger log = LoggerFactory.getLogger(Trx.class);

    public static void main(String[] args) throws SQLException {

        //线上
//        String owner_address = "41ee213cc7213df2ef24cad0eaaae6154d943ed273";//944 908380
//        String privateKey = "fdbfd58e043910c185aa840f1b2d9ad1eba021091f88e55d66dc666037a5eaec";

        //测试
        String owner_address = "41eaa0cda6897646bf6a98953c2d5ee78f9ad6e113";//944 908380
        String privateKey = "f8611b717e20ca143678fff2d363136be448d9e23019e9bc345551f4e8fde515";
        String amount = "242425";
        String to_address = "TLTGs9HsT17mtiNY5i2UU8pbNvPJ7hyfuR";


        //热钱包地址
        String address = "TDBz4EqbZixSAvCSriK9tYrejotf4XD9Rh";//58.611457
        String hotPriv = "e838856944f0690c3c588a5b9e7bf72903c8999eea8d201c1c72de247b588e2c";
//        String address = "TEDJa5GjZVeo7NyiSyX2EzEJLA7iLXsnXv";
//        String hotPriv = "23f31b95eed43414119c262029b59086ac9e3a37ecd753eea0690064f965b045";
        //trc20热钱包
//        String address = "TLBTvCWDzmVHRbngWJzQuEKrWdQojDMugP";//TLBTvCWDzmVHRbngWJzQuEKrWdQojDMugP//0.654985 0.554985+2-1.383883
//        String hotPriv = "b66c267a7f25c457fc1b6afec66cde391c33e86dc30925f7250a5bacbe98a1e8";


//        {"privateKey":"35cc31220a6bbed17f1a0693a54d72ffc481a7a674e9f7a9e2c60172b1a8c182","address":"TTLBYDu1JYGHjyJpm8VY6aQ16iVGKrXKei","hexAddress":"41be71a70c5ddfd8fa4e0a5f6babd2935aeb27140f"}
//        {"privateKey":"7e590daee675dc903bd4c03da56598698038b53aba623e39b2612873d588d366","address":"TEcsZyMJfiAmkcbZJroRbDcCttnZUp2Yt7","hexAddress":"4133001695f32ce2b7a3550d7c8a63a01cd6b5ebb5"}
        System.out.println(createtransaction(owner_address,"THFg5GxGPxr4Khyz1Wvy1B7qwnKaoMvPxp","5000000",privateKey));//TDZXMjKFjF8NxCW4TmzZp21x55ZeyC7GK7  TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1


        //转账
//        for (int i = 0; i < 10; i++) {
//            BigDecimal value = BigDecimal.valueOf(Math.random()*11).setScale(6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(Math.pow(10.0,6))).setScale(0);
//            System.out.println(createtransaction(owner_address,to_address,String.valueOf(value),privateKey));
//            System.out.println("value==="+value);
//        }
//        for (int i = 0; i < 20; i++) {
//            BigDecimal r = BigDecimal.valueOf(Math.random()*100).setScale(8, RoundingMode.HALF_UP);
//            String a = "";
//            if (r.compareTo(BigDecimal.valueOf(35))<0){
//                a = to_address;
//            }else if(r.compareTo(BigDecimal.valueOf(35))>=0&&r.compareTo(BigDecimal.valueOf(70))<0){
//                a = "TD7CmRKmVQ3keh6WmttpdLRYoCVQkpr3nS";
//            }else {
//                a = address;
//            }
//            BigDecimal value = BigDecimal.valueOf(Math.random()*10).setScale(6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(Math.pow(10.0,6))).setScale(0);
//            System.out.println(createtransaction(owner_address,a,String.valueOf(value),privateKey));
//            System.out.println("value==="+value);
////            try {
////                Thread.sleep(1000);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
//        }

        //查询账户资源
        String txId = "bd45666c394aca816aeb6711691845cd71592a02b889b607abb5937873c16b05";
//        System.out.println(gettransactioninfobyid(txId));
//        System.out.println(gettransactionbyid(txId));
//        System.out.println(getaccount(owner_address));//1982750
//        System.out.println(getaccountresource("TSdbWa4PPT1Zpthf98ieKEk1DeoWEKd5WR"));
//        获取块高
//        System.out.println(getnowblock());
        //生成地址
//        System.out.println(generateAddress());
        //获取块信息
//        System.out.println(getblockbynum("2411618"));

        //冻结
//        String resoure = "BANDWIDTH";
        String resoure = "ENERGY";
        String frozen_balance = "10000000";
//        freezebalanceMutile("TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1",frozen_balance,resoure,"TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1","b6c5416864686c0e9be104a50fb2110e81047ff328b3647f3d6a6f5a4ffc56da");
//        freezebalanceMutile("TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1",frozen_balance,"BANDWIDTH","TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1","b6c5416864686c0e9be104a50fb2110e81047ff328b3647f3d6a6f5a4ffc56da");
//        System.out.println(freezebalance("TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1",frozen_balance,resoure,"TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1","3cabca99054100ef8436a6d2763527708bc798a0867734b0137e8d607cfbfd50"));
//        System.out.println(freezebalance("TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1",frozen_balance,"BANDWIDTH","TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1","3cabca99054100ef8436a6d2763527708bc798a0867734b0137e8d607cfbfd50"));


        //创建账户
//        System.out.println(createaccount(owner_address,owner_address,"61cae49321a8f13788392b9b4b76dd3410007bb8aca89acdbefe0692d175c684"));

        String[] trc20Address = {"TRdSDedzUkHpyxWVT9bEp7ktrV5ZYHWpgj","TMrCy9tWbXaAenUJq76ywiuXdsxMzigdjR","TFzTkjmPNRiC6REd2a9E2nLvW6T4boFMeo",
                "TLe3RFCNnCg9w8EtcKHcD8DbyGAZXcmS75","TXgKe6D5XuPknzkWfkqTeG2hYfYTY46U6N","TPBUKVkZxvSnsRcw9J6u29mkpSyqkLyCU5",
                "TUcoCoaXKgaKiM7LNLQe3YpMHcz2fE6mij"};
//        BigDecimal sum = BigDecimal.ZERO;
//        for (int i = 0; i < trc20Address.length; i++) {
//            String a = JSONObject.parseObject(getaccount(Hex58Transfer.base58checkToHexString(trc20Address[i]))).getString("balance");
//            if (a != null){
//                System.out.println(trc20Address[i]+"==="+a);
//                sum = sum.add(BigDecimal.valueOf(Integer.parseInt(a)));
//            }
//        }
//        System.out.println("sum="+sum);

//        for (int i = 0; i < 10; i++) {
//            String a = JSONObject.parseObject(generateAddress()).getString("address");
//            insertSendRecord(a);
//        }
//        System.out.println(validateAddress("T3V9QE19LrPW23GSarTmU7zDGRTH12122c9ASAS19aZcUsfxVH"));


//        ResultSet rs = selectColdTrx();
//        while (rs.next()){
//            String a = rs.getString("address");
//            String b = Hex58Transfer.base58checkToHexString(a);
//            System.out.println(createtransaction(owner_address,b,amount,privateKey));
//        }

//        multipleSignatures();

//        for (int i = 0; i < 4; i++) {
//            System.out.println(generateAddress());
//        }


    }

    /**
     * 多签
     * 211
     * owner:
     * {"privateKey":"6f233686ab793673ed9d2202aca5d283297b65dd92afcf522f29116983855f96","address":"TNRAD7oh5R65q4nEogP47buRzmVcnuDyP6","hexAddress":"418889fe62f9c980627db5d9f2ba5bd0017c07473b"}
     * active:
     * {"privateKey":"35cc31220a6bbed17f1a0693a54d72ffc481a7a674e9f7a9e2c60172b1a8c182","address":"TTLBYDu1JYGHjyJpm8VY6aQ16iVGKrXKei","hexAddress":"41be71a70c5ddfd8fa4e0a5f6babd2935aeb27140f"}
     * {"privateKey":"7e590daee675dc903bd4c03da56598698038b53aba623e39b2612873d588d366","address":"TEcsZyMJfiAmkcbZJroRbDcCttnZUp2Yt7","hexAddress":"4133001695f32ce2b7a3550d7c8a63a01cd6b5ebb5"}
     *
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
     * 214 trx
     * owner:
     *{"privateKey":"8b661c71873a636a6b0613b19826d2462a29d26022909c84aebdcdd08a19fe0f","address":"TMKNRoYhmBwTGZ2Wu2G3MwtKusd1Pjxrzk","hexAddress":"417c79926212b66c37af29e99969abdade92cbeaa4"}
     * active
     * {"privateKey":"382eb3134b4ea207ee8245102b5eb4e80ded3b76f85844655d2090283274e7dc","address":"TBLmb9A5m7NtViTZAyEg7hDhPV5z4Qz3bJ","hexAddress":"410f0c1052d034db05965e7ddd5fe1e655f1288bf9"}
     * {"privateKey":"069d57e824fb713deff0da1ad18fe8c8218754015096b003cbba086cb58fba2b","address":"TBqEAvsJhf3Z8W2zSQAb77ESn1xyFyMPch","hexAddress":"41146de693c1f4309996d51bca5e503eb0186fd77a"}
     * {"privateKey":"c597ed4bd8dc3fa9d2c92b7e6a0d01a3b87e4519874c4b7ffa17a74e3197ae30","address":"TTGpKbcfC757R8d5ZwPGiwZPGukdoUYYD6","hexAddress":"41bdceb169a410e3af20a9d7b0cd12873fe0464042"}
     * to:
     * {"privateKey":"4a18e8f02ee626de7ea6d4c79bc3492b3c44d2c1e4e3b7f28ed9bf7dc5fedb06","address":"TWjgtzQqFARwTwwCfiLzkDnPB21kX7kCqB","hexAddress":"41e3cc1a97d69e6d129a1864045732388aa641e074"}
     *
     *
     * {"privateKey":"07045dd51dbbd38a0c62deb59829f7f3aaf729df376053b38569c45d4c156ddf","address":"TSdbWa4PPT1Zpthf98ieKEk1DeoWEKd5WR","hexAddress":"41b6c49bc735923e6580717c541bfcbb5ba6fff817"}
     * {"privateKey":"b694a39961cc02c8f67f9d8c6aae963268439d27aee87c21d9d21cd1ff616adb","address":"TQwmgDkgHqnqCM1MbR5rPjjTJoDvmiuzkA","hexAddress":"41a44454a9ecbe9d531855fce07a75ac6127d28510"}
     * {"privateKey":"112b67127c521c69097d55ef61704800be17468db8ed62b2c90afcb066b9afef","address":"TJqj7i72xB1T7J21UUrNJTbCqBYp5jsFbJ","hexAddress":"41614ef15c559ddbb68882f99f05bd7d19740862b2"}
     * {"privateKey":"5e4d322708ab3e5f8c0b86261999e10e213dfe52cee7a6e325fdf216ab36b6ba","address":"TLGxUo2r5pZEdiJ2VKB5ED9Xptno2RTZVg","hexAddress":"41710c9f7a4d616895979b4339b0110b82c632bf29"}
     *
     */
    public static void multipleSignatures(){
        //多签地址
//        String ownerAddress = "TMKNRoYhmBwTGZ2Wu2G3MwtKusd1Pjxrzk";
//        String privateKey = "8b661c71873a636a6b0613b19826d2462a29d26022909c84aebdcdd08a19fe0f";
        //to地址
        String toAddress = "TSFRcLqmaKQpANGnRZBMTLAckEjh3RayjU";
        //数量
        String amount = "1000";

        //1、生成地址
//        System.out.println(generateAddress());

        //2、创建账户
        String owner_address = "TSdbWa4PPT1Zpthf98ieKEk1DeoWEKd5WR";
        String privateKey = "07045dd51dbbd38a0c62deb59829f7f3aaf729df376053b38569c45d4c156ddf";
//        System.out.println(createaccount(owner_address,address,privateKey));

//        3、查看账户
//        System.out.println(getaccount(address));

//        4、更新权限
        System.out.println(accountPermissionUpdate(owner_address,privateKey));

//        5、转账
//        multipleTransaction(ownerAddress,toAddress,amount);
    }

    /**
     * 多签交易
     * @param ownerAddress
     * @param toAddress
     * @param amount
     */
    public static void multipleTransaction(String ownerAddress,String toAddress,String amount){
//        5.1、创建交易
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/createtransaction";
        if(toAddress.length() != 42){
            toAddress = Hex58Transfer.base58checkToHexString(toAddress);
        }
        if(ownerAddress.length() != 42){
            ownerAddress = Hex58Transfer.base58checkToHexString(ownerAddress);
        }
        String params = "{\"owner_address\": \""+ownerAddress+"\",\"to_address\": \""+toAddress+"\",\"amount\":"+amount+", \"Permission_id\" : 2}";
        String createResponse =  Utils.postByJson(url,params);
        System.out.println("createResponse="+createResponse);
        log.info("createResponse={}",createResponse);

//        5.2 签名
        String url_1 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactionsign";
        String params_1 = "{\"transaction\":"+createResponse+",\"privateKey\": \"35cc31220a6bbed17f1a0693a54d72ffc481a7a674e9f7a9e2c60172b1a8c182\"}";
        System.out.println("params1="+params_1);
        String signResponse = Utils.postByJson(url_1,params_1);
        System.out.println("signResponse="+signResponse);
        log.info("signResponse={}",signResponse);

        String params_2 = "{\"transaction\":"+signResponse+",\"privateKey\": \"7e590daee675dc903bd4c03da56598698038b53aba623e39b2612873d588d366\"}";
        System.out.println("params2="+params_2);
        String signResponse1 = Utils.postByJson(url_1,params_2);
        System.out.println("signResponse1="+signResponse1);
        log.info("signResponse1={}",signResponse1);

//        5.3 广播
        String url_2 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/broadcasttransaction";
        System.out.println(Utils.postByJson(url_2,signResponse1));
    }

    public static void freezebalanceMutile(String ownerAddress,String frozenBalance,String resource,String receiverAddress,String privateKey){

        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/freezebalance";
        if(ownerAddress.length() != 42){
            ownerAddress = Hex58Transfer.base58checkToHexString(ownerAddress);
        }
        if(receiverAddress.length() != 42){
            receiverAddress = Hex58Transfer.base58checkToHexString(receiverAddress);
        }
        String params = "{\"owner_address\":\""+ownerAddress+"\",\"frozen_balance\":"+frozenBalance+",\"frozen_duration\":3,\"resource\" :\""+resource+"\",\"receiver_address\":\""+receiverAddress+"\"}";
        String createResponse =  Utils.postByJson(url,params);
        System.out.println("createResponse="+createResponse);
        log.info("createResponse={}",createResponse);


        //签名
        String url_1 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactionsign";
        String params_1 = "{\"transaction\":"+createResponse+",\"privateKey\": \"a5a2a1cc37b6b4527d6a4c791464ea024ac6b36c29905ab0c474c8f72311b940\"}";
        System.out.println("params1="+params_1);
        String signResponse = Utils.postByJson(url_1,params_1);
        System.out.println("signResponse="+signResponse);
        log.info("signResponse={}",signResponse);

        String params_2 = "{\"transaction\":"+signResponse+",\"privateKey\": \"c8a0675f5cd651a065e48a39df3d9ea26307fbe994c4d7b6b17fd2e2896b36ee\"}";
        System.out.println("params2="+params_2);
        String signResponse1 = Utils.postByJson(url_1,params_2);
        System.out.println("signResponse1="+signResponse1);
        log.info("signResponse1={}",signResponse1);

//        5.3 广播
        String url_2 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/broadcasttransaction";
        System.out.println(Utils.postByJson(url_2,signResponse1));


    }

    /**
     * 账户修改权限
     * @param owner_address
     * @param privateKey
     * @return
     */
    public static String accountPermissionUpdate(String owner_address,String privateKey){
        //创建交易
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/accountpermissionupdate";
        String params = "{\"owner_address\":\""+owner_address+"\",\"owner\":{\"type\":0,\"permission_name\":\"owner\",\"threshold\":1,\"keys\":[{\"address\":\""+owner_address+"\",\"weight\":1}]},\"actives\":[{\"type\":2,\"permission_name\":\"active\",\"threshold\":2,\"operations\":\"0200008000000000000000000000000000000000000000000000000000000000\",\"keys\":[{\"address\":\"TQwmgDkgHqnqCM1MbR5rPjjTJoDvmiuzkA\",\"weight\":1},{\"address\":\"TJqj7i72xB1T7J21UUrNJTbCqBYp5jsFbJ\",\"weight\":1},{\"address\":\"TLGxUo2r5pZEdiJ2VKB5ED9Xptno2RTZVg\",\"weight\":1}]}],\"visible\":true}";
        String createResponse = Utils.postByJson(url,params);
        System.out.println("createResponse=="+createResponse);
        JSONObject jsonObject = JSONObject.parseObject(createResponse);
        String txID = jsonObject.getString("txID");
        JSONObject value = jsonObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value");
        String type_url = jsonObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getString("type_url");
        String type = jsonObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getString("type");
        String ref_block_bytes = jsonObject.getJSONObject("raw_data").getString("ref_block_bytes");
        String ref_block_hash = jsonObject.getJSONObject("raw_data").getString("ref_block_hash");
        String expiration = jsonObject.getJSONObject("raw_data").getString("expiration");
        String timestamp = jsonObject.getJSONObject("raw_data").getString("timestamp");
        String raw_data_hex = jsonObject.getString("raw_data_hex");


        //签名
        String url_1 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactionsign";
        String params_1 = "{\"transaction\":{\"visible\":true,\"txID\":\""+txID+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":"+value+",\"type_url\":\""+type_url+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+ref_block_bytes+"\",\"ref_block_hash\":\""+ref_block_hash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+"},\"raw_data_hex\":\""+raw_data_hex+"\"},\"privateKey\": \""+privateKey+"\"}";
        System.out.println("params_1=="+params_1);
        String signResponse = Utils.postByJson(url_1,params_1);
        System.out.println("signResponse=="+signResponse);
        JSONObject jsonObject1 = JSONObject.parseObject(signResponse);
        String signature = jsonObject1.getJSONArray("signature").getString(0);
        String txID_1 = jsonObject1.getString("txID");
        String ref_block_bytes_1 = jsonObject1.getJSONObject("raw_data").getString("ref_block_bytes");
        String ref_block_hash_1 = jsonObject1.getJSONObject("raw_data").getString("ref_block_hash");
        String expiration_1 = jsonObject1.getJSONObject("raw_data").getString("expiration");
        String timestamp_1 = jsonObject1.getJSONObject("raw_data").getString("timestamp");
        String raw_data_hex_1 = jsonObject1.getString("raw_data_hex");


        //广播
        String url_2 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/broadcasttransaction";
        String params_2 = "{\"signature\":[\""+signature+"\"],\"txID\":\""+txID_1+"\",\"visible\":true,\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":"+value+",\"type_url\":\""+type_url+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+ref_block_bytes_1+"\",\"ref_block_hash\":\""+ref_block_hash_1+"\",\"expiration\":"+expiration_1+",\"timestamp\":"+timestamp_1+"},\"raw_data_hex\":\""+raw_data_hex_1+"\"}";
        System.out.println("params_2=="+params_2);
        String broadResponse = Utils.postByJson(url_2,params_2);
        System.out.println("broadResponse="+broadResponse);

        return broadResponse;
    }

    /**
     * 生成地址，并生成指定的私钥
     * @return
     */
    public static String generateAddress(){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/generateaddress";
        String res = Utils.get(url);
        log.info(res);
        return res;
    }

    /**
     * 查看地址是否有效
     * @param address
     * @return
     */
    public static String validateAddress(String address){
        String url = "http://"+Config.TRX_URL+":"+Config.TRX_PORT+"/wallet/validateaddress";
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
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/createtransaction";
        if(toAddress.length() != 42){
            toAddress = Hex58Transfer.base58checkToHexString(toAddress);
        }
        if(ownerAddress.length() != 42){
            ownerAddress = Hex58Transfer.base58checkToHexString(ownerAddress);
        }
        System.out.println("toAddress==="+toAddress);
        System.out.println("ownerAddress=="+ownerAddress);
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
        System.out.println("0a"+Integer.toHexString(raw_data_hex_1.length()/2)+"01"+raw_data_hex_1+1241+signature);
        log.info("sign:::::{}",("0a"+Integer.toHexString(raw_data_hex_1.length()/2)+"01"+raw_data_hex_1+1241+signature));

        //广播
        String broadResponse = broadcasttransaction(signature,txID_1,value,ownerAddress,toAddress,type_url_1,type_1,ref_block_bytes_1,ref_block_hash_1,expiration_1,timestamp_1,raw_data_hex_1);
        System.out.println("broadResponse="+broadResponse);
        log.info("broadResponse={}",broadResponse);
        return broadResponse;
//        return null;
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
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactionsign";
        String params = "{\"transaction\":{\"txID\":\""+txId+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"amount\":"+amount+",\"owner_address\":\""+ownerAddress+"\",\"to_address\":\""+toAddress+"\"},\"type_url\":\""+typeUrl+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+refBlockBytes+"\",\"ref_block_hash\":\""+refBlockHash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+"},\"raw_data_hex\":\""+rawDataHex+"\"},\"privateKey\": \""+privateKey+"\"}";
        System.out.println("signParam="+params);
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
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/broadcasttransaction";
        String params = "{\"signature\":[\""+signature+"\"],\"txID\":\""+txId+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"amount\":"+amount+",\"owner_address\":\""+ownerAddress+"\",\"to_address\":\""+toAddress+"\"},\"type_url\":\""+typeUrl+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+refBlockBytes+"\",\"ref_block_hash\":\""+refBlockHash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+"},\"raw_data_hex\":\""+rawDataHex+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 查询交易所在的区块
     * @param txId
     * @return
     */
    public static String gettransactioninfobyid(String txId){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactioninfobyid";
        String params = "{\"value\" : \""+txId+"\"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 查询交易信息
     * @param txId
     * @return
     */
    public static String gettransactionbyid(String txId){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactionbyid";
        String params = "{\"value\" : \""+txId+"\"}";
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
     * 查询合约信息
     * @param address
     * @return
     */
    public static String getcontract(String address){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/getcontract";
        if(address.length() != 42){
            address = Hex58Transfer.base58checkToHexString(address);
        }
        String params = "{\"value\" : \""+address+"\"}";
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
     * 冻结 TRX, 获取带宽或者能量(投票权)
     * @param ownerAddress
     * @param frozenBalance
     * @param resource 冻结 TRX 获取资源的类型, 可以是 BANDWIDTH 或者 ENERGY
     * @param receiverAddress
     * @return
     */
    public static String freezebalance(String ownerAddress,String frozenBalance,String resource,String receiverAddress,String privateKey){

        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/freezebalance";
        if(ownerAddress.length() != 42){
            ownerAddress = Hex58Transfer.base58checkToHexString(ownerAddress);
        }
        if(receiverAddress.length() != 42){
            receiverAddress = Hex58Transfer.base58checkToHexString(receiverAddress);
        }
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
        String url_1 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactionsign";
        String params_1 = "{\"transaction\":{\"txID\":\""+txID+"\",\"raw_data\":{\"contract\":[{\"parameter\":{\"value\":{\"resource\":\""+resource+"\",\"frozen_duration\":3,\"frozen_balance\":"+frozenBalance+",\"receiver_address\":\""+receiverAddress+"\",\"owner_address\":\""+ownerAddress+"\"},\"type_url\":\""+type_url+"\"},\"type\":\""+type+"\"}],\"ref_block_bytes\":\""+ref_block_bytes+"\",\"ref_block_hash\":\""+ref_block_hash+"\",\"expiration\":"+expiration+",\"timestamp\":"+timestamp+"},\"raw_data_hex\":\""+raw_data_hex+"\"},\"privateKey\": \""+privateKey+"\"}";
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
        String url_2 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/broadcasttransaction";
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
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/getblockbynum";
        String params = "{\"num\" : "+num+"}";
        return Utils.postByJson(url,params);
    }

    /**
     * 通过区块ID（即区块哈希）查询块
     * @param value
     * @return
     */
    public static String getblockbyid(String value){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/getblockbyid";
        String params = "{\"value\" : \""+value+"\"}";
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


    /**
     * 获取特定区块的所有交易 Info 信息
     * @param num
     * @return
     */
    public static String gettransactioninfobyblocknum(String num){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactioninfobyblocknum";
        String params = "{\"num\" : \""+num+"\"}";
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
     *
     * 在链上创建账号. 一个已经激活的账号创建一个新账号需要花费 0.1 TRX 或等值 Bandwidth.
     * @param ownerAddress
     * @param accountAddress
     * @param privateKey
     * @return
     */
    public static String createaccount(String ownerAddress,String accountAddress,String privateKey){
        String url = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/createaccount";
        if(ownerAddress.length() != 42){
            ownerAddress = Hex58Transfer.base58checkToHexString(ownerAddress);
        }
        if(accountAddress.length() != 42){
            accountAddress = Hex58Transfer.base58checkToHexString(accountAddress);
        }
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
        String url_1 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/gettransactionsign";
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
        String url_2 = "http://"+ Config.TRX_URL+":"+ Config.TRX_PORT+"/wallet/broadcasttransaction";
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

//            String sql="insert into tb_wallet_address_cold_trx (address,user_id,site_id) values(?,?,?)";//sql语句
            String sql="insert into tb_wallet_address_cold_trx (address) values(?)";//sql语句
            PreparedStatement pstmt=conn.prepareStatement(sql);//获得预置对象
            pstmt.setString(1, address);
//            pstmt.setString(2, userId);
//            pstmt.setString(3, "1");


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

    /**
     * 冷钱包地址查询
     * @return
     */
    public static ResultSet selectColdTrx(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = " select address,user_id from 58wallet.tb_wallet_address_cold_trx where id>=1742 and id<=1756";
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
