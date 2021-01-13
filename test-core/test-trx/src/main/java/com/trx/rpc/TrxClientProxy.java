package com.trx.rpc;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

/**
 * @author lizhen
 * @date 2018-04-15
 */
@Slf4j
public class TrxClientProxy {

//FullNode http 基础url： https://api.nileex.io/wallet/
//SolidityNode http 基础url： https://api.nileex.io/walletsolidity/


    //usdt-contract-owner {"privateKey":"69f3c111a8872dafc24fa448071e747f7a166254eb7eb4311f9c41ed844d00b5","address":"TZGjkJDpXfwPLoPKm4G9iXRLpS6t3HwyBV","hexAddress":"41ff9ba121f45dcb85b33c17576642ea88ea1a9cb8"}

    // witness {"privateKey":"1579e4f0399cbed7d6ce4e5a362118dd715274f92025c20c2a9b68e2afccb252","address":"TCMLkPvFEjUQJSvTTyqkw7bJZ8BV1LCrPy","hexAddress":"411a1fdea83b92c6da221137ef761a65b44e9956db"}

    //6bde58d607a91068cf6c048f11fa594b667b3be90036581464aa0c9e42b0c03c
    //TDLp4xjvLgVLnR5FqXe4pQyU4XRxgKeuUu
    //TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t

    //


    private OkHttpClient rpcClient;
    private MediaType mediaType;
    private String baseUrl;

    public TrxClientProxy(String url) {
        rpcClient = new OkHttpClient();
        mediaType = MediaType.parse("application/json");
        this.baseUrl = url;
    }

    private RequestBody getBody(String param){
        return RequestBody.create(mediaType, param);
    }

    private Request getRequest(RequestBody body,String method) {
        return new Request.Builder()
                .url(this.baseUrl+"/"+method)
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();
    }


    public String getNewAddress() throws IOException{
        String param = "";
        Call call = rpcClient.newCall(getRequest(getBody(param),"generateaddress"));
        Response response = call.execute();
        return  response.body().string();
    }




}