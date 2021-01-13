package com.algo;


//import com.algorand.algosdk.algod.client.AlgodClient;
import com.algorand.algosdk.algod.client.ApiClient;
import com.algorand.algosdk.algod.client.api.AlgodApi;
import com.algorand.algosdk.algod.client.model.Block;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;

import java.math.BigInteger;
import java.util.HashMap;

public class TestClient {

    public static void main(String[] args) throws Exception {

        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://192.168.112.89:9080");
        apiClient.setApiKey("81be2652f02c9e62a32236952558fa2a6be89a2d34563351e874aee48ab24cd1");

        AlgodApi client = new AlgodApi(apiClient);

        AlgodClient client2 = null;


        Block block = client.getBlock(BigInteger.valueOf(200000L));
        System.out.println("fjdls");


        final String RECEIVER = "GD64YIY3TWGDMCNPP553DZPPR6LDUSFQOIJVFDPPXWEG3FVOJCCDBBHU5A";
        String note = "Hello World";
        TransactionParametersResponse params = client2.TransactionParams().execute().body();


    }
}
