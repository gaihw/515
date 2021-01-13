package com.zmj.commom.eth;

import com.zmj.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@Component
@Slf4j
public class EtcUtils {
    /**
     * 获取地址的nonce值
     * @param web3
     * @param address
     * @return
     * @throws Exception
     */
    public static BigInteger getNonce(Web3j web3, String address) throws Exception {
        EthGetTransactionCount ethGetTransactionCount =
                web3.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        return ethGetTransactionCount.getTransactionCount();

    }

    /**
     * 获取账户余额
     * @param web3
     * @param address
     * @return
     * @throws IOException
     */
    public static BigDecimal getBalance(Web3j web3, String address) throws IOException {
        //获取账户余额，方法一
//        String address = "0xe7d92b07dff83ffd4e9e3dcd4ed8653e0c69bf8f";
        EthGetBalance ethGetBalance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        if(ethGetBalance!=null){
            // 打印账户余额
//            System.out.println(ethGetBalance.getBalance());
            // 将单位转为以太，方便查看
//            System.out.println(Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER));
            return Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER);
        }
        //获取账户余额，方法二
//        EthGetBalance ethGetBalance1 = web3.ethGetBalance("0xc52ec29cd9528a1652382f289bf657468a118cc0", DefaultBlockParameter.valueOf("latest")).send();
        //eth默认会部18个0这里处理比较随意，大家可以随便处理哈
//        BigDecimal balance = new BigDecimal(ethGetBalance1.getBalance().divide(new BigInteger("10000000000000")).toString());
//        BigDecimal nbalance = balance.divide(new BigDecimal("100000"), 8, BigDecimal.ROUND_DOWN);
//        System.out.println(nbalance);
        return null;
    }



    /**
     * 解锁账户
     * @param address
     * @param password
     * @throws IOException
     */
    public static void unlock_acount(String address,String password) throws IOException {
        Admin admin = Admin.build(new HttpService("http://"+ Config.ETH_URL +":"+ Config.ETH_PORT));
//        Request<?, PersonalUnlockAccount> request = admin.personalUnlockAccount(address, password);
//        PersonalUnlockAccount account = request.send();
//        System.out.println(account.accountUnlocked());
        //or
        PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, password).send();
        System.out.println(personalUnlockAccount.accountUnlocked());
    }

    /**
     * 获取区块高度
     * @param web3
     * @return
     * @throws IOException
     */
    public static BigInteger get_blockNumber(Web3j web3) throws IOException {
        Request<?, EthBlockNumber> request1;
        request1 = web3.ethBlockNumber();
        EthBlockNumber ethBlockNumber = request1.send();
        return ethBlockNumber.getBlockNumber();
    }

    /**
     * 通过区块号查询区块信息
     * @param web3
     * @param num
     * @return
     * @throws IOException
     */
    public static String get_blockInfoByNum(Web3j web3,BigInteger num ) throws IOException {
        DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(num);
        Request<?, EthBlock> request = web3.ethGetBlockByNumber(defaultBlockParameter, true);
        EthBlock ethBlock = request.send();
//        System.out.println("Hash==>" + ethBlock.getBlock().getHash());
        return ethBlock.getBlock().getHash();
    }

    /**
     * ETH 创建交易&签名&发送
     * @param web3
     * @param toAddress
     * @param value
     * @param credentials
     * @return
     * @throws Exception
     */
    public static String transaction(Web3j web3, String toAddress, BigInteger gasPrice, BigInteger gasLimit, BigInteger value, Credentials credentials) throws Exception {
        //1、 get the next available nonce
        BigInteger nonce = getNonce(web3,credentials.getAddress());

        //2、 create our transaction
        RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
                nonce,gasPrice , gasLimit, toAddress, value);

        //3、 sign & send our transactionnn
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
        String transactionHash = ethSendTransaction.getTransactionHash();
        return transactionHash;
    }
}
