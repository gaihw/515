package com.test;

import com.google.common.collect.Lists;
import com.test.dao.AddressDao;
import com.test.domain.Erc20Config;
import com.test.domain.SendTxInfo;
import com.test.rpc.RpcClient;
import com.test.util.EthUtil;
import com.test.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Slf4j
public class Deposit {

    private static BigInteger usdtFromAddressPrivateKey = BigInteger.ZERO;
    private static String usdtFromAddress = "0x140a09ae7889312ee55faedc4a65c73c74f84616";
    private static String usdtContractAddress = "0xa58f94fdf7ee95815f68a0bd4fade7720f1f6bcd";
    private static String usdtCollectAddress = "0x6233c5737da6e940813331568a2c8acd1b16b390";


    private static BigDecimal ethFee = new BigDecimal("0.001");
    private static Parity parity;

    public static void main(String[] args) {
        parity = RpcClient.getRpcClient();
    }

    public static void depostFexErc20Token() throws IOException, ExecutionException, InterruptedException, CipherException {

        usdtFromAddressPrivateKey = EthUtil.getPrivateKey("/Users/zhaozhilong/tmp/eth.key", "");
        Erc20Config erc20Config = Erc20Config.builder().name("USDT").contractAddress(usdtContractAddress).weiDecimal(6).build();

        List<String> coldAddressList = AddressDao.getColdAddressList("eth");

        BigInteger nonce = parity.ethGetTransactionCount(usdtFromAddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        for (String toAddress : coldAddressList) {

            SendTxInfo sendTxInfo = createSendTxInfo(parity, erc20Config, toAddress, nonce);
            nonce = nonce.add(BigInteger.ONE);
            String txid = parity.ethSendRawTransaction(sendTxInfo.getTransHex()).send().getTransactionHash();
            if (txid.equals(sendTxInfo.getTxid())) {
                log.info("send erc20 {} {}", erc20Config.getName(), txid);
            }
        }

    }

    private static SendTxInfo createSendTxInfo(Parity parity, Erc20Config erc20Config, String toAddress, BigInteger nonce)
            throws ExecutionException, InterruptedException {
        BigInteger sendAmount = EthUtil.toWei(RandomUtil.doubleRandom(1, 3, 2));


        Credentials credentials = EthUtil.getCredentials(usdtFromAddressPrivateKey);
        String contractAddress = erc20Config.getContractAddress();

        Function function = new Function("transfer", Lists.newArrayList(new Address(toAddress),
                new Uint256(sendAmount)), Lists.newArrayList());
        String data = FunctionEncoder.encode(function);

        //gas evm , gas price danjian
        Transaction transaction = new Transaction(erc20Config.getFromAddress(), null, null, null, contractAddress, null, data);
        EthEstimateGas ethEstimateGas = parity.ethEstimateGas(transaction).sendAsync().get();
        BigInteger gasLimit = ethEstimateGas.getAmountUsed();


        BigDecimal realFee = getRealSendFee(parity, ethFee, gasLimit);

        BigDecimal gasPrice = Convert.toWei(realFee, Convert.Unit.ETHER)
                .divide(new BigDecimal(gasLimit), 0, BigDecimal.ROUND_DOWN);
        BigInteger gasPriceInteger = gasPrice.toBigInteger();

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPriceInteger, gasLimit,
                contractAddress, BigInteger.ZERO, data);

        int chainId = 101;
        if (chainId < 1) {
            throw new RuntimeException("invalid chainId");
        }

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, (byte) chainId, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        String txid = EthUtil.getTransactionHash(hexValue);

        return SendTxInfo.builder()
                .txid(txid)
                .transHex(hexValue)
                .build();
    }

    private static BigDecimal getRealSendFee(Parity parity, BigDecimal limit, BigInteger gasLimit) {
        BigDecimal feeFactor = new BigDecimal("5");
        if (feeFactor == null || feeFactor.compareTo(BigDecimal.ONE) < 0) {
            return limit;
        }
        try {
            BigInteger price = parity.ethGasPrice().send().getGasPrice();
            if (price == null || price.compareTo(BigInteger.ONE) < 0) {
                return limit;
            }
            BigDecimal estimate = Convert.fromWei(new BigDecimal(price.multiply(gasLimit)), Convert.Unit.ETHER);
            estimate = feeFactor.multiply(estimate);
            estimate = estimate.setScale(8, RoundingMode.UP);
            return limit.min(estimate);
        } catch (IOException e) {
            log.error("parity.ethGasPrice ERROR:", e);
            return limit;
        }
    }

    private BigDecimal toErc20(BigInteger value, int factor) {
        return new BigDecimal(value).divide(BigDecimal.TEN.pow(factor));
    }

    public static void depositFexETHCold() throws IOException {

        List<String> coldAddressList = AddressDao.getColdAddressList("eth");
        String miner = parity.ethAccounts().send().getAccounts().get(0);
        String pwd = "";

        BigInteger minerNonce = parity.ethGetTransactionCount(miner, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        BigInteger gasLimit = new BigInteger("42000");

        BigInteger feeWei = Convert.toWei(new BigDecimal("0.001"), Convert.Unit.ETHER).toBigInteger();
        BigInteger gasPrice = feeWei.divide(gasLimit);

        for (int index = 0; index < coldAddressList.size(); index++) {
            BigDecimal amout = RandomUtil.doubleRandom(2, 4, 2);
            BigInteger value = Convert.toWei(amout, Convert.Unit.ETHER).toBigInteger();
            Transaction transaction = Transaction.createEtherTransaction(
                    miner, minerNonce, gasPrice, gasLimit, coldAddressList.get(index),
                    value);
            EthSendTransaction ethSendTransaction = parity.personalSendTransaction(transaction, pwd).send();
            String hash = ethSendTransaction.getTransactionHash();
            log.info("send eth tx {}", hash);
            minerNonce = minerNonce.add(BigInteger.ONE);
        }

    }

}
