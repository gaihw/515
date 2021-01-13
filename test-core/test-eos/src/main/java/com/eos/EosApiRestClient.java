package com.eos;

import java.math.BigDecimal;
import java.util.List;

import com.eos.chain.PackedTransaction;
import com.eos.domain.common.transaction.SignedPackedTransaction;
import com.eos.domain.request.chain.transaction.PushTransactionRequest;
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

public interface EosApiRestClient<T> {

    ChainInfo getChainInfo();

    Block getBlock(String blockNumberOrId);

    Account getAccount(String accountName);

    Actions getActions(int pos, int offset, String accountName);

    BigDecimal getBalance(String accountName);

    List<Block> getBlocks(int start, int end);

    Code getCode(String accountName);

    TableRow getTableRows(String scope, String code, String table);

    public Transaction getTransaction(String txid);

    AbiBinToJson abiBinToJson(String code, String action, String binargs);

    AbiJsonToBin abiJsonToBin(String code, String action, T args);

    PushTransaction pushTransaction(String packtx, List<String> signatures);

    List<PushTransaction> pushTransactions(List<PushTransactionRequest> pushTransactionRequests);

    RequiredKeys getRequiredKeys(PackedTransaction transaction, List<String> keys);

    String createWallet(String walletName);

    void openWallet(String walletName);

    void lockWallet(String walletName);

    void lockAllWallets();

    void unlockWallet(String walletName, String walletPassword);

    void importKeyIntoWallet(String walletName, String walletKey);

    List<String> listWallets();

    List<List<String>> listKeys();

    List<String> getPublicKeys();

    SignedPackedTransaction signTransaction(PackedTransaction unsignedTransaction,
                                            List<String> publicKeys, String chainId);

    void setWalletTimeout(Integer timeout);


    void getAdaBlock(int blockNumber);
}
