package com.zmj.commom.eth;


import com.alibaba.fastjson.JSONObject;
import com.zmj.commom.HttpUtils;
import com.zmj.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
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


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class EthUtils {

    @Autowired
    private HttpUtils httpUtils;

    /**
     * 获取web3j
     * @param rpc
     * @return
     */
    public Web3j getWeb3j(String rpc) {

        Web3j web3j = Web3j.build(new HttpService(rpc));
        return web3j;
    }


    /**
     * 获取地址的nonce值
     * @param web3
     * @param address
     * @return
     * @throws Exception
     */
    public  BigInteger getNonce(Web3j web3, String address) throws Exception {
        EthGetTransactionCount ethGetTransactionCount =
                web3.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        return ethGetTransactionCount.getTransactionCount();

    }

    /**
     * 查询交易信息
     * @param txid
     * @return
     */
    public String getTransactionByHash(String txid){
        String url = "http://"+ Config.ETH_URL +":"+Config.ETH_PORT;
        String params = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_getTransactionByHash\",\"params\":[\""+txid+"\"],\"id\":74}";
        String res = httpUtils.postByJson(url,params);
        return res;
    }

    /**
     * 获取账户余额
     * @param web3
     * @param address
     * @return
     * @throws IOException
     */
    public  BigDecimal getBalance(Web3j web3, String address) throws IOException {
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
    public  Boolean unlock_acount(String address,String password) throws IOException {
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
//     * @param web3
     * @return
     * @throws IOException
     */
    public  BigInteger get_blockNumber(Web3j web3j) {
        Request<?, EthBlockNumber> request1;
        request1 = web3j.ethBlockNumber();
        EthBlockNumber ethBlockNumber = null;
        try {
            ethBlockNumber = request1.send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ethBlockNumber.getBlockNumber();
    }

    /**
     * 通过区块号查询区块信息
     * @param web3
     * @param num
     * @return
     * @throws IOException
     */
    public  String get_blockInfoByNum(Web3j web3,BigInteger num ) throws IOException {
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
    public  String transaction(Web3j web3, String toAddress, BigInteger value, Credentials credentials) throws Exception {
        //1、 get the next available nonce
        BigInteger nonce = getNonce(web3,credentials.getAddress());

        BigInteger gasPrice = web3.ethGasPrice().send().getGasPrice();
//        System.out.println(gasPrice);
        BigInteger gasLimit = BigInteger.valueOf(21000);

        //2、 create our transaction
        RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
                nonce,gasPrice , gasLimit, toAddress, value);

        //3、 sign & send our transactionnn
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
        String transactionHash = ethSendTransaction.getTransactionHash();
        log.info("transaction={}",transactionHash);
        return transactionHash;
    }

    /**
     * 获取Credentials
     * @return
     */
    public Credentials getCre(String fileName){
        String path = "/root/ghw/java/data/"+fileName+".json";
//        log.info("开始写入数据...");
//        FileWriter fileWriter = null;
//        try {
//            fileWriter = new FileWriter(new File(path));
//            fileWriter.write(text.toString());
//            fileWriter.flush();
//            log.info("写入数据成功...");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials("",path);
            return credentials;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取地址，公私钥
     */
    public JSONObject getAddPrivPub(String password, JSONObject text){
        String path = "/root/ghw/java/data/ETH.json";
        log.info("开始写入数据...");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(path));
            fileWriter.write(text.toString());
            fileWriter.flush();
            log.info("写入数据成功...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(password,path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address",credentials.getAddress());
        jsonObject.put("priv_16",credentials.getEcKeyPair().getPrivateKey().toString(16));
        jsonObject.put("pub_16",credentials.getEcKeyPair().getPublicKey().toString(16));
        jsonObject.put("priv",credentials.getEcKeyPair().getPrivateKey().toString());
        jsonObject.put("pub",credentials.getEcKeyPair().getPublicKey().toString());
        return jsonObject;
    }

    /**
     * 获取地址，公私钥
     */
    public JSONObject getPubPri(String password, String fileName){
        String path = "/root/ghw/java/data/"+fileName+".json";
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(password,path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address",credentials.getAddress());
        jsonObject.put("priv_16",credentials.getEcKeyPair().getPrivateKey().toString(16));
        jsonObject.put("pub_16",credentials.getEcKeyPair().getPublicKey().toString(16));
        jsonObject.put("priv",credentials.getEcKeyPair().getPrivateKey().toString());
        jsonObject.put("pub",credentials.getEcKeyPair().getPublicKey().toString());
        return jsonObject;
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    public String uploadFile(MultipartFile file){

        String targetFilePath = "/root/ghw/java/data/";
        //获取文件名称
        String fileName = file.getOriginalFilename();
        File targetFile = new File(targetFilePath + fileName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(targetFile);
            IOUtils.copy(file.getInputStream(), fileOutputStream);
            log.info("------>>>>>>uploaded a file successfully!<<<<<<------");
        } catch (IOException e) {
            return null;
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return null;

//        String targetFilePath = "data/";
//        String fileSuffix = getFileSuffix(file);
//        String fileName = UUID.randomUUID().toString().replace("-", "");
//        if (fileSuffix != null) {   // 拼接后缀
//            fileName += fileSuffix;
//        }
//        File targetFile = new File(targetFilePath + File.separator + fileName);
//        FileOutputStream fileOutputStream = null;
//        try {
//            fileOutputStream = new FileOutputStream(targetFile);
//            IOUtils.copy(file.getInputStream(), fileOutputStream);
//            log.info("------>>>>>>uploaded a file successfully!<<<<<<------");
//        } catch (IOException e) {
//            return null;
//        } finally {
//            try {
//                fileOutputStream.close();
//            } catch (IOException e) {
//                log.error("", e);
//            }
//        }
//        return null;
    }

    /**
     * 文件上传获得文件后缀
     * @param file
     * @return
     */
    private String getFileSuffix(MultipartFile file) {
        if (file == null) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        int suffixIndex = fileName.lastIndexOf(".");
        if (suffixIndex == -1) {    // 无后缀
            return null;
        } else {                    // 存在后缀
            return fileName.substring(suffixIndex, fileName.length());
        }
    }

    /**
     * 获取data目录下的文件
     * @return
     */
    public List<String> getFileList(){
        File file = new File("/root/ghw/java/data/");

        File[] fileList = file.listFiles();

        ArrayList<String> arrayList = new ArrayList<String>();

        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                String[] fileName = fileList[i].getName().split("\\.");
                arrayList.add(fileName[0]);
//                System.out.println("文件：" + fileName);
            }
            //获得文件下的目录
//            if (fileList[i].isDirectory()) {
//                String fileName = fileList[i].getName();
////                System.out.println("目录：" + fileName);
//            }
        }
        return arrayList;
    }
}
