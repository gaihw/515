package com.eos.impl;

import java.util.List;
import java.util.Map;

import com.eos.domain.common.transaction.SignedPackedTransaction;
import com.eos.domain.request.chain.AbiJsonToBinRequest;
import com.eos.domain.request.chain.RequiredKeysRequest;
import com.eos.domain.request.chain.transaction.PushTransactionRequest;
import com.eos.domain.request.wallet.transaction.SignTransactionRequest;
import com.eos.domain.response.chain.AbiBinToJson;
import com.eos.domain.response.chain.AbiJsonToBin;
import com.eos.domain.response.chain.ChainInfo;
import com.eos.domain.response.chain.RequiredKeys;
import com.eos.domain.response.chain.TableRow;
import com.eos.domain.response.chain.account.Account;
import com.eos.domain.response.chain.block.Block;
import com.eos.domain.response.chain.code.Code;
import com.eos.domain.response.chain.push.PushTransaction;
import com.eos.domain.response.chain.transaction.Transaction;
import com.eos.domain.response.chain.transaction.actions.Actions;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EosApiService {

    @GET("/v1/chain/get_info")
    Call<ChainInfo> getChainInfo();

    @POST("/v1/chain/get_block")
    Call<Block> getBlock(@Body Map<String, String> requestFields);

    @POST("/v1/chain/get_account")
    Call<Account> getAccount(@Body Map<String, String> requestFields);

    @POST("/v1/history/get_actions")
    Call<Actions> getActions(@Body Map<String, String> requestFields);

    @POST("/v1/chain/get_currency_balance")
    Call<List<String>> getBalance(@Body Map<String, String> requestFields);

    @POST("/v1/chain/get_code")
    Call<Code> getCode(@Body Map<String, String> requestFields);

    @POST("/v1/chain/get_table_rows")
    Call<TableRow> getTableRows(@Body Map<String, String> requestFields);

    @POST("/v1/history/get_transaction")
    Call<Transaction> getTransaction(@Body Map<String, String> requestFields);

    @POST("/v1/chain/abi_json_to_bin")
    Call<AbiJsonToBin> abiJsonToBin(@Body AbiJsonToBinRequest abiJsonToBinRequest);

    @POST("/v1/chain/abi_bin_to_json")
    Call<AbiBinToJson> abiBinToJson(@Body Map<String, String> requestFields);

    @POST("/v1/chain/push_transaction")
    Call<PushTransaction> pushTransaction(@Body Map<String, Object> requestFields);

    @POST("/v1/chain/push_transactions")
    Call<List<PushTransaction>> pushTransactions(
            @Body List<PushTransactionRequest> pushTransactionRequests);

    @POST("/v1/chain/get_required_keys")
    Call<RequiredKeys> getRequiredKeys(@Body RequiredKeysRequest requiredKeysRequest);

    @POST("/v1/wallet/create")
    Call<String> createWallet(@Body String walletName);

    @POST("/v1/wallet/open")
    Call<Void> openWallet(@Body String walletName);

    @POST("/v1/wallet/lock")
    Call<Void> lockWallet(@Body String walletName);

    @GET("/v1/wallet/lock_all")
    Call<Void> lockAll();

    @POST("/v1/wallet/unlock")
    Call<Void> unlockWallet(@Body List<String> requestFields);

    @POST("/v1/wallet/import_key")
    Call<Void> importKey(@Body List<String> requestFields);

    @GET("/v1/wallet/list_wallets")
    Call<List<String>> listWallets();

    @GET("/v1/wallet/list_keys")
    Call<List<List<String>>> listKeys();

    @GET("/v1/wallet/get_public_keys")
    Call<List<String>> getPublicKeys();

    @POST("/v1/wallet/set_timeout")
    Call<Void> setTimeout(@Body Integer timeOut);

    @POST("/v1/wallet/sign_transaction")
    Call<SignedPackedTransaction> signTransaction(@Body SignTransactionRequest unsignedTransaction);


    @POST("/")
    Call<PushTransaction> getAdaBlock(@Body Map<String, Object> requestFields);


}
