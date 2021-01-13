package com.atom.dex.api.client.test;

import com.atom.dex.api.client.domain.jsonrpc.*;
import com.atom.dex.api.client.domain.rest.BalanceResult;
import com.atom.dex.api.client.domain.rest.NodeInfos;
import com.atom.dex.api.client.domain.rest.reponse.CreateTxResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface AtomApiService {

    @GET("/abci_query")
    Call<JsonRpcResponse<AccountResult>> getAccount(@Query("path") String pathWithAddress);

    @GET("/abci_query?path=%22/store/acc/key%22")
    Call<JsonRpcResponse<AccountResult>> getCommittedAccount(@Query("data") String address);

    @GET("/abci_query")
    Call<JsonRpcResponse<ABCIQueryResult>> getTokenInfo(@Query("path") String pathWithSymbol);

    @GET("/abci_query?path=%22/param/fees%22")
    Call<JsonRpcResponse<ABCIQueryResult>> getFees();

    @GET("abci_query?path=%22custom/atomicSwap/swapid%22")
    Call<JsonRpcResponse<ABCIQueryResult>> getSwapByID(@Query("data") String data);

    @GET("/abci_query?path=%22custom/stake/validators%22")
    Call<JsonRpcResponse<ABCIQueryResult>> getStakeValidators();

    @GET("/abci_query?path=%22custom/gov/proposal%22")
    Call<JsonRpcResponse<ABCIQueryResult>> getProposalById(@Query("data") String data);

    @GET("/tx_search")
    Call<JsonRpcResponse<BlockInfoResult>> getBlockTransactions(@Query("query") String height, @Query("page") int page, @Query("per_page") int perPage);

    @GET("/broadcast_tx_commit")
    Call<JsonRpcResponse<CommitBroadcastResult>> commitBroadcast(@Query("tx") String tx);

    @GET("/broadcast_tx_sync")
    Call<JsonRpcResponse<AsyncBroadcastResult>> asyncBroadcast(@Query("tx") String tx);

    @GET("/status")
    Call<JsonRpcResponse<NodeInfos>> getNodeStatus();

    @GET("/tx")
    Call<JsonRpcResponse<TransactionResult>> getTransaction(@Query("hash") String hash);


    //reset API
    @GET("/bank/balances")
    Call<JsonRpcResponse<BalanceResult>> getBalance(@Query("address") String address);

    @POST("/bank/accounts/{address}/transfers")
    Call<JsonRpcResponse<CreateTxResponse>> createTx(@Path("address") String address, @Body RequestBody transaction);
}
