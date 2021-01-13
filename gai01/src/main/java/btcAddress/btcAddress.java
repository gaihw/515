package btcAddress;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet2Params;
import org.bitcoinj.params.TestNet3Params;

import java.nio.charset.Charset;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.RIPEMD256Digest;
import org.bouncycastle.crypto.digests.RIPEMD320Digest;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.web3j.ens.Contracts.MAINNET;

public class btcAddress {
    public static void main(String[] args) {

        String ltcPriv = "cQC2nQr8pKCSVbhcHkHzME5Xq3igduYcHd4r7upo2tY45J4TsiCf";
        String btcPriv = "cQxPiMCybkm6Ef7MtZCZKjHbffvi4YCs2tGi5UPEY6m17R1zEGNu";
        String bchPriv = "cNJGvRo4mkWv3U4t9AH2cCNuKpx2dNYrwQPvfHGGrM25EYRi27CK";
        String bsvPriv = "cPLoGzk4p6j5xfXUv58NPqyNAjH8FbmsVdLFVbahGM8LriYc5mcA";
        String dashPrivOnline = "XEnZcjSVwbNBMzNsGpfdssMNZVVqj3GaAMjqgBwbSTcUrAvhT3gj";
        String omniPrivOnline = "L5f61aGP7L2mjNngXDUqatPwfhBc3XUfzRnWdRz7NSVHzFkPb81C";
        String dashPriv = "cUVvyx67JARP8Y7PU5cNHdpHepyU3tnFULMBsvfNZrg8dPZpn6Pd";
        String p2sh_p2pk = "cQxPiMCybkm6Ef7MtZCZKjHbffvi4YCs2tGi5UPEY6m17R1zEGNu";
        String p2wsh_p2pkh = "cQxPiMCybkm6Ef7MtZCZKjHbffvi4YCs2tGi5UPEY6m17R1zEGNu";
//        System.out.println("公钥："+getPublicKey(readWif(dashPrivOnline)));
        String p2pkh = "cUZmwsNQb6vhqJN5JoUTjUQkGrpQUd29jYkzBE4zFCjjeM7yfdXh";
        System.out.println("公钥："+getPublicKey(readWif(p2pkh)));


//        String pub = getPublicKey(readWif(p2wsh_p2pkh));
//        System.out.println("sha256:"+getSHA256(pub));
//        System.out.println("ripemd160:"+hash160(pub));
//        String witnessScript = "76a914"+hash160(pub)+"88ac";
//        System.out.println("witnessScript:"+witnessScript);
//
//        String redeemScript = "0020"+getSHA256(witnessScript);
//        System.out.println("redeemScript:"+redeemScript);
//
//        String scriptPubKey = "a914" + hash160(redeemScript) + "87";
//        System.out.println("scriptPubKey:"+scriptPubKey);


//        #p2wsh-p2pkh
//        redeemScript:76a914 + hash160(pubkey) + 88ac
//        scriptPubKey:0020 + sha256(redeemScript)
//        String pub = getPublicKey(readWif(p2pkh));
        String pub = "210283189dca75fcfe88adab5354684f30a8a58f10de6d133bdabcd4c9477b455fb0ac";
        System.out.println("sha256:"+getSHA256(pub));
        System.out.println("ripemd160:"+ripemd160("0a0cef8c7fbef1f031365896084642e849b766d91fdf350084155a3e2dffa91a"));
        String scriptPubKey = "76a914"+hash160(pub)+"88ac";
        System.out.println("scriptPubKey:"+scriptPubKey);


//        System.out.println(getSHA256(dashPub));
//        System.out.println("hash160:"+ripemd160(getSHA256(dashPub).getBytes(Charset.forName("UTF-8"))));

//        NetworkParameters params = MainNetParams.get();//生成正式链地址用这个
//        NetworkParameters params = TestNet2Params.get();//test2
//        NetworkParameters params = TestNet3Params.get();//test3
//        //生成地址
//        ECKey key = new ECKey();
//        System.out.println("地址："+key.toAddress(params).toString());
//        System.out.println("公钥："+key.getPublicKeyAsHex());
//        System.out.println("私钥（但是这个私钥导入不了IMtoken）："+key.getPrivateKeyAsHex());
//        System.out.println("私钥（可以导进IMtoken）："+key.getPrivateKeyAsWiF(params));
//
//        //根据上面不能导进IMtoken的私钥获得可以导进IMtoken的私钥：
//        BigInteger priKey = new BigInteger("d4c0822e59cc3d428e068b6eeb589eb18ef5db45ce8c02c947d96c575876503a",16);
//        key = ECKey.fromPrivate(priKey);
//        System.out.println("私钥："+key.getPrivateKeyAsWiF(params));
//        System.out.println("公钥："+key.getPublicKeyAsHex());
//        System.out.println("地址："+key.toAddress(params));
    }
    /**
     * 根据未base58编码的私钥，得到公钥
     * 私钥 ：'20f34219eed055a8292767876e97cedc681cdee65f8d100f0c192d0b61cb13d6'
     * 公钥 ：'02fe868857f1dcb31137b34c55cf1b6e031447b5fe7902e49a083eaf95c54aaecf'
     * @param key
     * @return
     */
    public static String getPublicKeyFromHex(String key){
        BigInteger privateKeyBigInt = new BigInteger(key,16);
        ECKey privateKey = ECKey.fromPrivate(privateKeyBigInt);
        return privateKey.getPublicKeyAsHex();
    }

    /**
     * 根据私钥，获取公钥
     * 私钥: 5JkisHAXScTk6Tah9jq9S5B4ByiputRjnKnQrF6k1uBLBQAD8Mi
     * 公钥: 03bc976dc770b84decb90abeb94364c6368da051a9417cc4046f9f478d995b2ba7
     * @param privateKey
     * @return
     */
    public static String getPublicKey(ECKey privateKey) {
        return ECKey.fromPrivate(privateKey.getPrivKey(), true).getPublicKeyAsHex();
    }
    public static ECKey readWif(String wif) {
        return DumpedPrivateKey.fromBase58(NetworkParameters.fromID(MAINNET), wif).getKey();
    }


    /**
         * 利用java原生的类实现SHA256加密
         * @param str 加密后的报文
         * @return
         */
    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    /**
     *     * 将byte转为16进制
     *     * @param bytes
     *     * @return
     *     
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
        //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    public static String ripemd160(String str) {
        byte[] bytes = str.getBytes(Charset.forName("UTF-8"));
        Digest digest = new RIPEMD160Digest();
        digest.update(bytes, 0, bytes.length);
        byte[] rsData = new byte[digest.getDigestSize()];
        digest.doFinal(rsData, 0);
        return Hex.toHexString(rsData);
    }

    public static String hash160(String str){
        return ripemd160(getSHA256(str));
    }
}
