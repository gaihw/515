package com.atom.dex.api.client.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atom.dex.api.client.domain.jsonrpc.BlockInfoResult;
import com.atom.dex.api.client.domain.jsonrpc.JsonRpcResponse;
import com.atom.dex.api.client.domain.rest.BalanceResult;
import com.atom.dex.api.client.domain.rest.request.BaseReqParam;
import com.atom.dex.api.client.domain.rest.reponse.CreateTxResponse;
import com.test.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.RequestBody;

import java.util.List;

@Slf4j
public class AtomApiRestClientImpl<T> implements AtomApiRestClient<T> {


    private final static int TX_SEARCH_PAGE = 1;

    private final static int TX_SEARCH_PERPAGE = 10000;

    private final AtomApiService atomApiService;

    private String baseUrl;

    private static final okhttp3.MediaType MEDIA_TYPE = okhttp3.MediaType.parse("text/plain; charset=utf-8");

    public AtomApiRestClientImpl(String baseUrl) {
        atomApiService = AtomApiServiceGenerator.createService(AtomApiService.class, baseUrl);
        this.baseUrl = baseUrl;
    }


    @Override
    public CreateTxResponse createTx(String address, BaseReqParam baseReqParam) {

        String unsignTx = null;
        try {
            JSONObject jsonObject = (JSONObject) JSONObject.parse(JSONObject.toJSONString(baseReqParam));
            unsignTx = HttpUtil.httpPost(this.baseUrl + "/bank/accounts/" + address + "/transfers", jsonObject);
            log.info("unsignTx {}", unsignTx);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }

        return JSON.toJavaObject(JSON.parseObject(unsignTx), CreateTxResponse.class);
    }

    @Override
    public CreateTxResponse createTx2(String address, String payload) {
        JsonRpcResponse<CreateTxResponse> response = AtomApiServiceGenerator.executeSync(atomApiService.createTx(address, RequestBody.create(MEDIA_TYPE, payload)));
        return response.getResult();

    }

    @Override
    public BalanceResult getBalance(String address) {
        JsonRpcResponse<BalanceResult> response = AtomApiServiceGenerator.executeSync(atomApiService.getBalance(address));
        return response.getResult();
    }


    @Override
    public List<BlockInfoResult.Transaction> getBlockTransactions(Long height) {
        JsonRpcResponse<BlockInfoResult> response = AtomApiServiceGenerator.executeSync(atomApiService.getBlockTransactions("\"tx.height=" + height.toString() + "\"",
                TX_SEARCH_PAGE, TX_SEARCH_PERPAGE));
        return response.getResult().getTxs();
    }
}
