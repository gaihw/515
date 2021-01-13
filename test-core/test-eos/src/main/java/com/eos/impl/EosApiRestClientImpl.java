package com.eos.impl;

import java.math.BigDecimal;
import java.util.*;

import com.google.common.collect.Lists;
import com.eos.EosApiRestClient;
import com.eos.chain.PackedTransaction;
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
import com.google.common.collect.Maps;
import retrofit2.Call;

public class EosApiRestClientImpl<T> implements EosApiRestClient<T> {

    private final EosApiService eosApiService;

    public EosApiRestClientImpl(String baseUrl){
        eosApiService = EosApiServiceGenerator.createService(EosApiService.class, baseUrl);
    }

    @Override
    public ChainInfo getChainInfo(){
        return EosApiServiceGenerator.executeSync(eosApiService.getChainInfo());
    }

    @Override
    public Block getBlock(String blockNumberOrId){
        return EosApiServiceGenerator.executeSync(eosApiService.getBlock(Collections.singletonMap("block_num_or_id", blockNumberOrId)));
    }

    @Override
    public List<Block> getBlocks(int start, int end) {
       List<Block> list = Lists.newArrayList();
       for(int index = start;index < end ;index++){
           Block block = getBlock(String.valueOf(index));
           list.add(block);
       }
        return list;
    }

    @Override
    public Account getAccount(String accountName){
        return EosApiServiceGenerator.executeSync(eosApiService.getAccount(Collections.singletonMap("account_name", accountName)));
    }

    @Override
    public Actions getActions(int pos, int offset, String accountName) {
        LinkedHashMap<String, String> requestParameters = new LinkedHashMap<>(7);

        requestParameters.put("pos", pos+"");
        requestParameters.put("offset", offset+"");
        requestParameters.put("account_name", accountName);
        Call<Actions> call =  eosApiService.getActions(requestParameters);
        return EosApiServiceGenerator.executeSync(call);
    }
    @Override
    public BigDecimal getBalance(String accountName) {
        LinkedHashMap<String, String> requestParameters = new LinkedHashMap<>(7);
        requestParameters.put("code", "eosio.token");
        requestParameters.put("symbol", "EOS");
        requestParameters.put("account",accountName);

        List<String> list = EosApiServiceGenerator.executeSync(eosApiService.getBalance(requestParameters));
        String value = list.get(0).split(" ")[0];
        return new BigDecimal(value);
    }

    @Override
    public Transaction getTransaction(String txid){
        return EosApiServiceGenerator.executeSync(eosApiService.getTransaction(Collections.singletonMap("id", txid)));
    }

    @Override
    public Code getCode(String accountName){
        return EosApiServiceGenerator.executeSync(eosApiService.getCode(Collections.singletonMap("account_name", accountName)));
    }

    @Override
    public TableRow getTableRows(String scope, String code, String table){
        LinkedHashMap<String, String> requestParameters = new LinkedHashMap<>(7);

        requestParameters.put("scope", scope);
        requestParameters.put("code", code);
        requestParameters.put("table", table);
        requestParameters.put("json", "true");

        return EosApiServiceGenerator.executeSync(eosApiService.getTableRows(requestParameters));
    }

    @Override
    public AbiBinToJson abiBinToJson(String code, String action, String binargs){
        LinkedHashMap<String, String> requestParameters = new LinkedHashMap<>(3);

        requestParameters.put("code", code);
        requestParameters.put("action", action);
        requestParameters.put("binargs", binargs);

        return EosApiServiceGenerator.executeSync(eosApiService.abiBinToJson(requestParameters));
    }

    @Override
    public AbiJsonToBin abiJsonToBin (String code, String action, T args){
        return EosApiServiceGenerator.executeSync(eosApiService.abiJsonToBin(new AbiJsonToBinRequest(code, action, args)));
    }

    @Override
    public PushTransaction pushTransaction(String packtx,List<String> signatures){
        LinkedHashMap<String, Object> requestParameters = new LinkedHashMap<>(3);
        requestParameters.put("signatures", signatures);
        requestParameters.put("compression", "none");
        requestParameters.put("packed_context_free_data", "");
        requestParameters.put("packed_trx", packtx);

        return EosApiServiceGenerator.executeSync(eosApiService.pushTransaction(requestParameters));
    }

    @Override
    public List<PushTransaction> pushTransactions(List<PushTransactionRequest> pushTransactionRequests){
        return EosApiServiceGenerator.executeSync(eosApiService.pushTransactions(pushTransactionRequests));
    }

    @Override
    public RequiredKeys getRequiredKeys(PackedTransaction transaction, List<String> keys){
        return EosApiServiceGenerator.executeSync(eosApiService.getRequiredKeys(new RequiredKeysRequest(transaction, keys)));
    }

    @Override
    public String createWallet(String walletName){
        return EosApiServiceGenerator.executeSync(eosApiService.createWallet(walletName));
    }

    @Override
    public void openWallet(String walletName){
        EosApiServiceGenerator.executeSync(eosApiService.openWallet(walletName));
    }

    @Override
    public void lockWallet(String walletName){
        EosApiServiceGenerator.executeSync(eosApiService.lockWallet(walletName));
    }

    @Override
    public void lockAllWallets(){
        EosApiServiceGenerator.executeSync(eosApiService.lockAll());
    }

    @Override
    public void unlockWallet(String walletName, String walletPassword){
        List<String> requestFields = new ArrayList<>(2);

        requestFields.add(walletName);
        requestFields.add(walletPassword);
        EosApiServiceGenerator.executeSync(eosApiService.unlockWallet(requestFields));
    }

    @Override
    public void importKeyIntoWallet(String walletName, String key){
        List<String> requestFields = new ArrayList<>(2);

        requestFields.add(walletName);
        requestFields.add(key);
        EosApiServiceGenerator.executeSync(eosApiService.importKey(requestFields));
    }

    @Override
    public List<String> listWallets(){
        return EosApiServiceGenerator.executeSync(eosApiService.listWallets());
    }

    @Override
    public List<List<String>> listKeys(){
       return EosApiServiceGenerator.executeSync(eosApiService.listKeys());
    }

    @Override
    public List<String> getPublicKeys(){
        return EosApiServiceGenerator.executeSync(eosApiService.getPublicKeys());
    }

    @Override
    public SignedPackedTransaction signTransaction(PackedTransaction packedTransaction, List<String> publicKeys, String chainId) {
        return EosApiServiceGenerator.executeSync(eosApiService.signTransaction(new SignTransactionRequest(packedTransaction, publicKeys, chainId)));
    }

    @Override
    public void setWalletTimeout(Integer timeout){
        EosApiServiceGenerator.executeSync(eosApiService.setTimeout(timeout));
    }

    @Override
    public void getAdaBlock(int blockNumber) {
        Map paramMap = Maps.newHashMap();
        paramMap.put("query","{blocks(where:{number:{_eq:"+blockNumber+"}}) {hash number fees transactions {block {number } blockIndex fee hash inputs{address sourceTxIndex sourceTxHash value } outputs{ index address value } size totalOutput } }}");

        EosApiServiceGenerator.executeSync(eosApiService.getAdaBlock(paramMap));

    }


}
