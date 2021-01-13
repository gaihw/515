package com.test.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.test.domain.Erc20Contract;
import com.test.domain.TraceTransaction;
import lombok.extern.slf4j.Slf4j;
import org.ethereum.core.Transaction;
import org.spongycastle.util.encoders.Hex;
import org.springframework.util.CollectionUtils;
import org.web3j.crypto.*;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.parity.Parity;
import org.web3j.protocol.parity.methods.response.ParityTracesResponse;
import org.web3j.protocol.parity.methods.response.Trace;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Slf4j
public class EthUtil {

    public static BigInteger getPrivateKey(String filePath,String pwd) throws IOException, CipherException {
        Credentials credentials = WalletUtils.loadCredentials(pwd, filePath);
        BigInteger hotPubKey = credentials.getEcKeyPair().getPrivateKey();
        System.out.println(hotPubKey);
        System.out.println(System.getProperty("java.library.path"));
        return hotPubKey;
    }

    public static BigInteger getEthTokenBalance(Parity parity,String address,String contractAddress) throws ExecutionException, InterruptedException {
        return Erc20Contract.balanceOf(address, parity, contractAddress)
                .sendAsync().get();
    }

    public static BigDecimal getLastBlockBalance(Parity parity, String from) {
        EthGetBalance ethGetBalance = null;
        try {
            ethGetBalance = parity
                    .ethGetBalance(from, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
        } catch (InterruptedException e) {
            log.error("", e);
        } catch (ExecutionException e) {
            log.error("", e);
        }

        BigInteger wei = ethGetBalance.getBalance();
        return Convert.fromWei(wei.toString(), Unit.ETHER);
    }

    public static List<TraceTransaction> getTraceTransaction(Parity parity, String txid) {
        List<TraceTransaction> traceTransactions = Lists.newArrayList();
        try {
            ParityTracesResponse parityTracesResponse =
                    parity.traceTransaction(txid)
                            .sendAsync()
                            .get();
            List<Trace> traceList = parityTracesResponse.getTraces();
            if (CollectionUtils.isEmpty(traceList)) {
                return null;
            }
            for (Trace trace : traceList) {
                if (trace.getError() != null) {
                    return null;
                }
            }

            for (Trace trace : traceList) {
                if (!(trace.getAction() instanceof Trace.CallAction)) {
                    continue;
                }
                Trace.CallAction callAction = (Trace.CallAction) trace.getAction();
                TraceTransaction traceTransaction = new TraceTransaction();
                traceTransaction.setFromAddress(callAction.getFrom());
                traceTransaction.setToAddress(callAction.getTo());
                traceTransaction.setValue(callAction.getValue());
                traceTransaction.setGas(callAction.getGas());
                traceTransaction.setInputData(callAction.getInput());
                traceTransactions.add(traceTransaction);
            }
        } catch (InterruptedException e) {
            log.error("", e);
        } catch (ExecutionException e) {
            log.error("", e);
        }
        return traceTransactions;
    }

    public static TransactionReceipt getTransactionReceipt(Parity parity, String txid) {
        EthGetTransactionReceipt ethGetTransactionReceipt = null;
        try {
            ethGetTransactionReceipt = parity.ethGetTransactionReceipt(txid).sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        TransactionReceipt transactionReceipt = null;
        if (ethGetTransactionReceipt != null) {
            transactionReceipt = ethGetTransactionReceipt.getResult();
        }
        return transactionReceipt;
    }

    public static Set<String> getTxidList(List<TransactionResult> transactionResultList) {

        Set<String> txidSet = Sets.newHashSet();
        for (TransactionResult transactionResult : transactionResultList) {
            Object tGetObj = transactionResult.get();
            if (!(tGetObj instanceof TransactionObject)) {
                continue;
            }
            TransactionObject transactionObject
                    = (TransactionObject) tGetObj;
            String txid = transactionObject.getHash();
            txidSet.add(txid);
        }
        return txidSet;
    }

    public static String getHexString(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                                      BigInteger value, BigInteger privKey, byte chainId) {
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice,
                gasLimit, to, value);

        Credentials credentials = getCredentials(privKey);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        return Numeric.toHexString(signedMessage);
    }

    public static Credentials getCredentials(BigInteger privKey) {
        ECKeyPair ecKeyPair = ECKeyPair.create(privKey);
        return Credentials.create(ecKeyPair);
    }

    public static BigInteger getLastBlockNumber(Parity web3j) {
        BigInteger blockHeight = BigInteger.ZERO;
        try {
            blockHeight = web3j.ethBlockNumber().send().getBlockNumber();
        } catch (IOException e) {
            log.error("decodeeth ", e);
        }
        return blockHeight;
    }

    public static String getTransactionHash(String hexValue) {
        hexValue = hexValue.substring(2, hexValue.length());
        Transaction txSigned = new Transaction(Hex.decode(hexValue));
        String hash = Hex.toHexString(txSigned.getHash());
        return "0x" + hash;
    }

    public static BigDecimal toEther(BigInteger value) {
        return Convert.fromWei(new BigDecimal(value), Unit.ETHER);
    }

    public static BigInteger toWei(BigDecimal value) {
        return Convert.toWei(value, Unit.ETHER).toBigInteger();
    }
}
