package blockchain.eth;

import blockchain.Config;
import blockchain.Utils;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Link {
    public static String fileName = "link";
    public static void main(String[] args) throws Exception {
        //建立以太坊连接
        Web3j web3 = Web3j.build(new HttpService("http://192.168.112.170:8545"));

        String path= "/Users/gaihongwei/Desktop/LINK.json";
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
        String fromAccount = credentials.getAddress();
        String toAccount = "0xaeff5099ba926baf86ef9877c661db461668cdcf";
        String contractAddress = Config.LINK;
        BigInteger value = BigInteger.valueOf(900000000000000000L);
        byte chainId = Config.ETH_CHAINLD;
//        String[] address = {"0x254382181f81e82254cbecf7330aa2f1ecb70cba",
//                "0xc55d5c9cba19720d3f4a789af39c577d6ec9ab1b",
//                "0x17cf37ec36cad7bcc25d6ddfb766a48fcf801bd2",
//                "0x6c53c8ea363d470158beacb3c71b9655d3d6867a"};
//        EthSendTransaction transferERC20Token = transferERC20Token(web3,fromAccount,toAccount,value,privateKey,contractAddress,chainId);
//        String transactionHash = transferERC20Token.getTransactionHash();
//        Utils.writerData(fileName,transactionHash);
//        System.out.println("transactionHash==="+transactionHash);
//        System.out.println(getTokenBalance(web3,toAccount,contractAddress));

        System.out.println(getTokenBalance(web3,credentials.getAddress(),contractAddress));
//        System.out.println(getTokenBalance(web3,fromAccount,contractAddress));
//        5331650 5341650  5746720
//        String[] address = {"0xd7b9a9b2f665849c4071ad5af77d8c76aa30fb32",
//                "0xd87e95a01e044d705dc33774f336c179812fbf99",
//                "0xd9b5284bb92b18cedcd233f2a2520a6111df8c14",
//                "0xe00ba79d6fd13e51feb736ce032d9788df2452de",
//                "0xe11883ea68a21758d1d554552d959a823aee81fb",
//                "0xe6aeac95e0debf3b15cebcfb6a3a65fe6237e1db",
//                "0xe6d2a1d38fffa73e8da2782a21bdcd88144da5db",
//                "0xeddf70a1b91bbe0f401863a28ef8d4089843ba89",
//                "0xedf45d2c18b27009e5b932b5b0f986e796571735",
//                "0xee05507e5821c04ebd08b894266f1f70a2445094",
//                "0xf1ef71d630749858d75ac5dfa8c6efe33f41afd0",
//                "0xf22ed13bb9aa00e24b1b7600fe7fef48b6848731",
//                "0xfa19e046f7705206d408b33ee51863051dbdfc03",
//                "0x0314803350c097a2391a0392fc46f656f5766d70"};
//        BigInteger balance = BigInteger.ZERO;
//        for (int i = 0 ; i < address.length ; i ++){
//            balance = balance.add(getTokenBalance(web3,address[i],contractAddress));
//        }
//        System.out.println("balance==="+balance);
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
    public static EthSendTransaction transferERC20Token(Web3j web3, String from, String to, BigInteger value, String privateKey, String contractAddress, byte chainId) throws Exception {

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
//        System.out.println("gasLimit==="+gasLimit);
//        System.out.println("gasLimit1=="+gasLimit);

        // 获得余额
        BigDecimal ethBalance = getBalance(web3, from);
        BigInteger tokenBalance = getTokenBalance(web3, from, contractAddress);
        BigInteger balance = Convert.toWei(ethBalance, Convert.Unit.ETHER).toBigInteger();
//        System.out.println("balance=="+balance);

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
//        System.out.println("signData=="+signData);
        if (!"".equals(signData)) {
            try {
                EthSendTransaction send = web3j.ethSendRawTransaction(signData).send();
                return send;
//                txHash = send.getTransactionHash();
//                System.out.println(JSON.toJSONString(send));
            } catch (IOException e) {
                throw new RuntimeException("交易异常");
            }
        }
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
