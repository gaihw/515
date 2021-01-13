package com.test;

import com.alibaba.fastjson.JSONObject;
import com.test.ada.domain.block.Data;
import com.test.sign.Signing;
import okhttp3.*;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//@SpringBootApplication
public class Test {

    // ./cardano-wallet wallet create from-recovery-phrase --port 8090 "${wallet-name}"
   // curl 127.0.0.1:8090/v2/wallets/1f45b46545fabdf5a5e5463f6a22b1c1f93cf233/addresses

// 76dc27cbb16b3a00e90edc1ddd0bca474382daf1031a7d0373b69c4929d16568
    //  mistake outer virtual bar best replace task anchor knee puppy game great antique tower symptom

// slot 55000, block 53970, hash 8652627b20465f5d287585a0c46f298eabcbf7ee93a04cd7b0d0a614974575b2

    public static void main(String[] args) throws IOException{
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"query\": \"{blocks(where:{number:{_eq:200001}}) {hash number fees transactions {block {number } blockIndex fee hash inputs{address sourceTxIndex sourceTxHash value } outputs{ index address value } size totalOutput } }}\"}");
        Request request = new Request.Builder()
                .url("http://192.168.112.89:3100/graphql")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();


        JSONObject json = JSONObject.parseObject(response.body().string());
        Data data = json.getJSONObject("data").toJavaObject(Data.class) ;


    }


    public static void maidn(String[] args) throws ParseException {


        //(String privatekey, byte[] data, BigInteger k, String coin, boolean testnet) {
     //   Signing.signature_create()

    }
}

