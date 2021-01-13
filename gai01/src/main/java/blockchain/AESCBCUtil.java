package blockchain;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具  模式：CBC  补码方式：PKCS5Padding
 *
 * @author Administrator
 */
public class AESCBCUtil {

    private static String aesPriKey = "www.58coin.aes.a";

    // 加密
    public static String encrypt(String sSrc) throws Exception {
        if (aesPriKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (aesPriKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = aesPriKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return Base64Utils.encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public static String decrypt(String sSrc){
        try {
            // 判断Key是否正确
            if (aesPriKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (aesPriKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = aesPriKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    public static void main(String[] args) throws Exception {

        /*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         */

        // 需要加密的字串
        String cSrc = "TDamwqwJorRSMwrSdP877A9xUmQuWxKcKz";
        System.out.println("原字符串是："+cSrc);
        // 加密
        String enString = encrypt(cSrc);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = decrypt(enString);
        System.out.println("解密后的字串是：" + DeString);


    }
}




