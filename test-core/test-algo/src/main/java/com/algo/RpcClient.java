package com.algo;

import com.algorand.algosdk.algod.client.ApiClient;
import com.algorand.algosdk.algod.client.StringUtil;
import com.algorand.algosdk.algod.client.api.AlgodApi;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.test.db.JdbcTemplateMgr;
import com.test.db.SQLUtil;

public class RpcClient {

  public   static AlgodClient getV2Client() {

        String rpcUrlParam = JdbcTemplateMgr.query(SQLUtil.select("rpc_url", "tb_currency_info") + " where name = 'algo'", "rpc_url").get(0).toString();

        String[] rpcArr = rpcUrlParam.split("@@");
        final String ALGOD_API_ADDR = rpcArr[0];
        final Integer ALGOD_PORT = Integer.parseInt(rpcArr[1]);
        final String ALGOD_API_TOKEN = rpcArr[2];

        AlgodClient client = (AlgodClient) new AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN);

        return client;
    }

    public static AlgodApi getV1Client(){
        String rpcUrlParam = JdbcTemplateMgr.query(SQLUtil.select("rpc_url", "tb_currency_info") + " where name = 'algo'", "rpc_url").get(0).toString();

        String[] rpcArr = rpcUrlParam.split("@@");
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://" + rpcArr[0] + ":" + rpcArr[1]);
        apiClient.setApiKey(rpcArr[2]);
        return new AlgodApi(apiClient);
    }

}
