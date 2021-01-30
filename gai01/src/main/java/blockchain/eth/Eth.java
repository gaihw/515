package blockchain.eth;

import blockchain.Config;
import blockchain.Utils;
import blockchain.algo.Algo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.geth.Geth;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


public class Eth {

    public static String fileName = "eth";
    static {
        System.setProperty("fileName", "eth/eth/eth.log");
    }
    public static Logger log = LoggerFactory.getLogger(Eth.class);

    public static void main(String[] args) throws Exception {
        //建立以太坊连接
        Web3j web3 = Web3j.build(new HttpService("http://"+ Config.ETH_URL +":"+Config.ETH_PORT));

        String path= "/Users/gaihongwei/Desktop/ETH.json";//0x32624623a4b60b2891428433b4b5c952506a0e2e
        //加载账户信息
        Credentials credentials = null;
        byte chainId = Config.ETH_CHAINLD;

        try {
            credentials = WalletUtils.loadCredentials("",path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }
        System.out.println(unlock_acount(credentials.getAddress(),""));
        System.out.println(credentials.getAddress());

        String toAccount = "0x3f5ce5fbfe3e9af3971dd833d26ba9b5c936f0be";
        //热钱包
//        String address = "0x7c18cb0bda976230b0259a859095ce6a91eb894e";//0.988999660000004
//        String address = "0xe5ddbf0df90ba664d8fe7c3c6ae6f05b05316a94";//0.75328807100037949
        String address = "0x6f6bd092aa04fdf11cfe5b471b1496c7433e3ac4";//0.575656659666822061
//        String address = "0xaeff5099ba926baf86ef9877c661db461668cdcf";//0.575656659666822061

        BigInteger value = BigInteger.valueOf((long) (0.11*Math.pow(10.0,18)));//实际到账去掉18个0;200000000000000000L/10**18=0.2
        BigInteger gasPrice = web3.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(21000);

        System.out.println("gasPrice==="+gasPrice);
        System.out.println("free="+gasPrice.multiply(gasLimit));

//        String transation = transaction(web3,toAccount,gasPrice,gasLimit,value,credentials,chainId);
//        System.out.println("transation===="+transation);


        for (int i = 0; i < 10; i++) {
            String transation = transaction(web3,toAccount,gasPrice,gasLimit,BigInteger.valueOf((long) (Math.random()*2*Math.pow(10,18))),credentials,chainId);
            System.out.println("transation===="+transation);
        }

//        System.out.println(getTransactionByHash("0x79a74b5be8feddfa3a44bad95133f6a5857751475afd13aa773a0ccbfcd44a12"));

//        String transation = transaction(web3,toAccount,gasPrice,gasLimit,value,credentials,chainId);
//            System.out.println("transation===="+transation);

//        System.out.println(getBalance(web3,credentials.getAddress()));//1.6
//        System.out.println(getBalance(web3,"0x7c18cb0bda976230b0259a859095ce6a91eb894e"));
//        System.out.println(getBalance(web3,address));
//        System.out.println(getBalance(web3,"0xaecde9d65e88ac2de7e413d9555ff1eb578ed7fb"));

//        System.out.println(get_blockNumber(web3));

//        miner(1);
        web3.shutdown();



//        try {
//            String s = WalletUtils.generateNewWalletFile("", new File("/Users/gaihongwei/Desktop"),true);
//        } catch (CipherException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * 挖矿
     * @param blockNum
     */
    public static void miner(int blockNum){
        String url = "http://"+ Config.ETH_URL +":"+Config.ETH_PORT;
        String params = "{\"jsonrpc\":\"2.0\",\"method\":\"miner_start\",\"params\":["+blockNum+"],\"id\":74}";
        Utils.postByJson(url,params);
        System.out.println("开始挖矿...");
        String params1 = "{\"jsonrpc\":\"2.0\",\"method\":\"miner_stop\", \"params\": [],\"id\":74}";
        Utils.postByJson(url,params1);
        System.out.println("停止挖矿");
    }

    /**
     * 获取交易信息
     * @param txid
     * @return
     */
    public static String getTransactionByHash(String txid){
        String url = "http://"+ Config.ETH_URL +":"+Config.ETH_PORT;
        String params = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_getTransactionByHash\",\"params\":[\""+txid+"\"],\"id\":74}";
        String res = Utils.postByJson(url,params);
        return res;
    }
    /**
     * 获取地址的nonce值
     * @param web3
     * @param address
     * @return
     * @throws Exception
     */
    public static BigInteger getNonce(Web3j web3,String address) throws Exception {

        FileReader fr = null;
        BufferedReader bufr = null;
        try {
            fr = new FileReader("/Users/gaihongwei/tools/eclipse/workspace/gai01/data/eth-nonce");
            bufr = new BufferedReader(fr);
            String line = null;
            //BufferedReader提供了按行读取文本文件的方法readLine()
            //readLine()返回行有效数据，不包含换行符，未读取到数据返回null
            while((line = bufr.readLine())!=null) {
//                System.out.println(line);
                return BigInteger.valueOf(Long.parseLong(line));
            }
        }catch(IOException e) {
            System.out.println("异常：" + e.toString());
        }finally {
            try {
                if(bufr!=null)
                    bufr.close();
            }catch(IOException e) {
                System.out.println("异常：" + e.toString());
            }
        }
        return null;
//        EthGetTransactionCount ethGetTransactionCount =
//                web3.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
//        return ethGetTransactionCount.getTransactionCount();

    }

    /**
     * 获取账户余额
     * @param web3
     * @param address
     * @return
     * @throws IOException
     */
    public static BigDecimal getBalance(Web3j web3,String address) throws IOException {
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
    public static Boolean unlock_acount(String address,String password) throws IOException {
        Admin admin = Admin.build(new HttpService("http://"+ Config.ETH_URL +":"+Config.ETH_PORT));
//        Request<?, PersonalUnlockAccount> request = admin.personalUnlockAccount(address, password);
//        PersonalUnlockAccount account = request.send();
//        System.out.println(account.accountUnlocked());
        //or
        PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, password).send();
        return personalUnlockAccount.accountUnlocked();
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
    public static String transaction(Web3j web3,String toAddress, BigInteger gasPrice, BigInteger gasLimit,BigInteger value,Credentials credentials, byte chainId) throws Exception {
        //1、 get the next available eth-nonce
        BigInteger nonce = getNonce(web3,credentials.getAddress());
        System.out.println("eth-nonce==="+nonce);

        //2、 create our transaction
        RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
                nonce,gasPrice , gasLimit, toAddress, value);

        //3、 sign & send our transactionnn
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,  chainId, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        log.info("hexValue={}",hexValue);
        System.out.println("hexValue="+hexValue);
//
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
        String transactionHash = ethSendTransaction.getTransactionHash();
        if(transactionHash != null){
            File file = new File("/Users/gaihongwei/tools/eclipse/workspace/gai01/data/eth-nonce");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(nonce.add(BigInteger.ONE).toString());
            bw.flush();
            bw.close();
            fw.close();
        }
        log.info("transactionHash==={}",transactionHash);
        return transactionHash;
//        return "test...";
    }

}
