package blockchain.eth;

import com.alibaba.fastjson.JSONObject;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.IOException;

public class GetPrivaPubAdd {
    public static void main(String[] args) {

        //创建钱包地址与密钥
//        String filePath = "/Users/gaihongwei/Desktop";
//        String fileName = "";
//
//        //eth-密码需要自己管理，自己设置哦！
//        try {
//            fileName = WalletUtils.generateNewWalletFile("123456", new File(filePath), false);
//        } catch (CipherException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        }
//        System.out.println(fileName);//保存你的加密文件信息

        String path= "/Users/gaihongwei/Desktop/1b8e.json";
        //加载账户信息
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials("qianbaotest",path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address",credentials.getAddress());
        jsonObject.put("priv_16",credentials.getEcKeyPair().getPrivateKey().toString(16));
        jsonObject.put("pub_16",credentials.getEcKeyPair().getPublicKey().toString(16));
        jsonObject.put("priv",credentials.getEcKeyPair().getPrivateKey());
        jsonObject.put("pub",credentials.getEcKeyPair().getPublicKey());

        System.out.println(credentials.getAddress());//钱包地址
        System.out.println(credentials.getEcKeyPair().getPrivateKey().toString(16));//私钥
        System.out.println(credentials.getEcKeyPair().getPublicKey().toString(16));//公钥
        System.out.println("-----------------非16-----------------");
        System.out.println(credentials.getEcKeyPair().getPrivateKey());//私钥
        System.out.println(credentials.getEcKeyPair().getPublicKey());//公钥
    }
}
