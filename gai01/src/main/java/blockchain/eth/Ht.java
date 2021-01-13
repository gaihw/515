package blockchain.eth;

import blockchain.Config;
import blockchain.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Ht {
    public static String fileName = "ht";
    static {
        System.setProperty("fileName", "eth/ht/ht.log");
    }
    public static Logger log = LoggerFactory.getLogger(Ht.class);
    public static void main(String[] args) throws Exception {
        //建立以太坊连接
        Web3j web3 = Web3j.build(new HttpService("http://"+ Config.ETH_URL +":"+Config.ETH_PORT));

        String path= "/Users/gaihongwei/Desktop/ETH.json";
        //加载账户信息
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials("",path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }
        String privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
        String fromAccount = credentials.getAddress();//499999499.991895853758084320
        System.out.println("fromAccount==="+fromAccount);

        //转账地址
        String toAccount = "0x86d47694b3ba70789eefe76a921ae906462234ab";

        //热钱包
        String address = "0xaeff5099ba926baf86ef9877c661db461668cdcf";//38945000000000000
//        String address = "0xeaa7d6ccb236f44e89e8d4a571d9583b291062c2";//1438900000000000000
//        String address = "0xaecde9d65e88ac2de7e413d9555ff1eb578ed7fb";//78620000000000000

        String contractAddress = Config.HT;
        BigInteger value = BigInteger.valueOf((long) (10.01*Math.pow(10.0,18)));//实际到账去掉18个0
        byte chainId = Config.ETH_CHAINLD;

//        for (int i = 0; i < 1; i++) {
//            EthSendTransaction transferERC20Token = transferHTToken(web3,fromAccount,address,value,privateKey,contractAddress,chainId);
//            String transactionHash = transferERC20Token.getTransactionHash();
//            log.info("{}",transactionHash);
//            System.out.println("transactionHash==="+transactionHash);
//        }
        transferHTToken(web3,fromAccount,address,value,privateKey,contractAddress,chainId);



//        System.out.println(getTokenBalance(web3,"0xe5ddbf0df90ba664d8fe7c3c6ae6f05b05316a94",contractAddress));
//        System.out.println(getTokenBalance(web3,address,contractAddress));
//        System.out.println(getTokenBalance(web3,"0xaecde9d65e88ac2de7e413d9555ff1eb578ed7fb",contractAddress));

        web3.shutdown();
    }
    /**
     * ERC-20Token 创建交易&签名&发送
     *
     * @param from
     * @param to
     * @param value
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static EthSendTransaction transferHTToken(Web3j web3, String from, String to, BigInteger value, String privateKey, String contractAddress, byte chainId) throws Exception {

        /**
         *转账方法一
         **/
        /**

         //加载转账所需的凭证，用私钥
         Credentials credentials = Credentials.create(privateKey);
         //获取nonce，交易笔数
         BigInteger eth-nonce = getNonce(web3,from);
         //get gasPrice
         BigInteger gasPrice = web3.ethGasPrice().send().getGasPrice();
         BigInteger gasLimit = Contract.GAS_LIMIT;

         //创建RawTransaction交易对象
         Function function = new Function(
         "transfer",
         Arrays.asList(new Address(to), new Uint256(value)),
         Arrays.asList(new TypeReference<Type>() {
         }));

         String encodedFunction = FunctionEncoder.encode(function);

         RawTransaction rawTransaction = RawTransaction.createTransaction(eth-nonce,
         gasPrice,
         gasLimit,
         contractAddress, encodedFunction);

         System.out.println("rawTransaction==="+rawTransaction.getData());
         //签名Transaction，这里要对交易做签名
         byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
         String hexValue = Numeric.toHexString(signMessage);
         System.out.println("hexValue==="+hexValue);
         //发送交易
         EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
         return ethSendTransaction;
         **/


        /**
         * 转账方法二
         */

        BigInteger nonce = getNonce(web3, from);
        // 构建方法调用信息
        String method = "transfer";

        // 构建输入参数
        List<Type> inputArgs = new ArrayList<>();
        inputArgs.add(new Address(to));
        inputArgs.add(new Uint256(value));
        BigInteger gasPrice = web3.ethGasPrice().send().getGasPrice();
//        BigInteger gasLimit = Contract.GAS_LIMIT;

        // 合约返回值容器
        List<TypeReference<?>> outputArgs = new ArrayList<>();

        String funcABI = FunctionEncoder.encode(new Function(method, inputArgs, outputArgs));

        Transaction transaction = Transaction.createFunctionCallTransaction(from, nonce, gasPrice, null, contractAddress, funcABI);

        BigInteger gasLimit = getTransactionGasLimit(web3, transaction);
        System.out.println("gasLimit==="+gasLimit);
        System.out.println("gasPrice==="+gasPrice);
        System.out.println("fee="+gasPrice.multiply(gasLimit));
//        System.out.println("gasLimit1=="+gasLimit);

        // 获得余额
        BigDecimal ethBalance = getBalance(web3, from);
        BigInteger tokenBalance = getTokenBalance(web3, from, contractAddress);
//        System.out.println("tokenBalance===="+tokenBalance);
        BigInteger balance = Convert.toWei(ethBalance, Convert.Unit.ETHER).toBigInteger();
//        System.out.println("balance==="+balance);
//        System.out.println("value==="+value);

        if (balance.compareTo(gasLimit) < 0) {
            throw new RuntimeException("手续费不足，请核实");
        }
        if (tokenBalance.compareTo(value) < 0) {
            throw new RuntimeException("代币不足，请核实");
        }
        return signAndSend(web3, nonce, gasPrice, gasLimit, contractAddress, BigInteger.ZERO, funcABI, chainId, privateKey);

    }

    /**
     * 对交易签名，并发送交易
     * @param web3j
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param to
     * @param value
     * @param data
     * @param chainId
     * @param privateKey
     * @return
     */
    public static EthSendTransaction signAndSend(Web3j web3j, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data, byte chainId, String privateKey) {
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
        if (privateKey.startsWith("0x")){
            privateKey = privateKey.substring(2);
        }
//        System.out.println("rawTransaction==="+rawTransaction.getData());

        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);

        byte[] signMessage;
        // 主网是1 responst测试网是3  具体查看ChainId
        if (chainId > ChainId.NONE){
            signMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        String signData = Numeric.toHexString(signMessage);
        System.out.println("signData=="+signData);
        log.info("signData==={}",signData);
//        if (!"".equals(signData)) {
//            try {
//                EthSendTransaction send = web3j.ethSendRawTransaction(signData).send();
//                if(send.getTransactionHash() != null){
//                    File file = new File("/Users/gaihongwei/tools/eclipse/workspace/gai01/data/eth-nonce");
//                    FileWriter fw = new FileWriter(file);
//                    BufferedWriter bw = new BufferedWriter(fw);
//                    bw.write(nonce.add(BigInteger.ONE).toString());
//                    bw.flush();
//                    bw.close();
//                    fw.close();
//                }
//                log.info("transactionHash==={}",send.getTransactionHash());
//                return send;
////                txHash = send.getTransactionHash();
////                System.out.println(JSON.toJSONString(send));
//            } catch (IOException e) {
//                throw new RuntimeException("交易异常");
//            }
//        }
        return null;
    }

    /**
     * 估算手续费上限
     * @param web3j
     * @param transaction
     * @return
     */
    public static BigInteger getTransactionGasLimit(Web3j web3j, Transaction transaction) {
        try {
            EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
            if (ethEstimateGas.hasError()){
                throw new RuntimeException(ethEstimateGas.getError().getMessage());
            }
            return ethEstimateGas.getAmountUsed();
        } catch (IOException e) {
            throw new RuntimeException("net error");
        }
    }
    /**
     * 查询代币发行总量
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static BigInteger getTokenTotalSupply(Web3j web3j, String from,String contractAddress) {
        String methodName = "totalSupply";
        BigInteger totalSupply = BigInteger.ZERO;
        List inputParameters = new ArrayList<>();
        List outputParameters = new ArrayList<>();

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {};
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(from, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            totalSupply = (BigInteger) results.get(0).getValue();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return totalSupply;
    }
    /**
     * 查询代币精度
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static int getTokenDecimals(Web3j web3j, String from,String contractAddress) {
        String methodName = "decimals";
        int decimal = 0;
        List inputParameters = new ArrayList<>();
        List outputParameters = new ArrayList<>();

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {};
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(from, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            decimal = Integer.parseInt(results.get(0).getValue().toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return decimal;
    }
    /**
     * 查询代币符号
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static String getTokenSymbol(Web3j web3j, String from,String contractAddress) {
        String methodName = "symbol";
        String symbol = null;
        List inputParameters = new ArrayList<>();
        List outputParameters = new ArrayList<>();

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {};
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(from, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            symbol = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return symbol;
    }
    /**
     * 查询代币名称
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static String getTokenName(Web3j web3j, String from,String contractAddress) {
        String methodName = "name";
        String name = null;
        List inputParameters = new ArrayList<>();
        List outputParameters = new ArrayList<>();

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {};
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(from, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            name = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return name;
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
     * 获取代币余额
     * @param web3j
     * @param fromAddress
     * @param contractAddress
     * @return
     */
    public static BigInteger getTokenBalance(Web3j web3j, String fromAddress, String contractAddress){
        String methodName = "balanceOf";
        List inputParameters = new ArrayList<>();
        List outputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {};
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

        EthCall ethCall;
        BigInteger balanceValue = BigInteger.ZERO;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            balanceValue = (BigInteger) results.get(0).getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return balanceValue;

    }
}
