package com.atom.dex.api.client.test;


import com.atom.dex.api.client.domain.jsonrpc.BlockInfoResult;
import com.atom.dex.api.client.domain.rest.BalanceResult;
import com.atom.dex.api.client.domain.rest.request.BaseReqParam;
import com.atom.dex.api.client.domain.rest.reponse.CreateTxResponse;

import java.util.List;

public interface AtomApiRestClient<T> {

    CreateTxResponse createTx(String address, BaseReqParam baseReqParam);

    CreateTxResponse createTx2(String address, String baseReqParam);

    BalanceResult getBalance(String address);

    List<BlockInfoResult.Transaction> getBlockTransactions(Long height);
}
