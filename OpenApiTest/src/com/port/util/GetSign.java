package com.port.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetSign {
	private static final Log _log = LogFactory.getLog(ASCIIToSort.class);
    public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";
    public static final String  ENCODE = "gbk";
    public static void main(String[] args) {
    	String[] param = { "page=1", "size=10", "api_key=eb4f25dd532c14cee5e9b0301c8d4671" };
		String privatekey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALSQaafkE4wNY21T+B2sCydP2FwkTQ6xi+bWLMVeF9bhFKyAZndmwxGgEc9bR6/rur5dAblka7OKpw3nJ27p3b5Ig/kd+dOSj4SfMp9pt96nHWud2KNZdAKUH4ybhg51D8TM0AXL474Gh8CcPrRvZspMdnlPM17YyUd6VjKQJs55AgMBAAECgYBF7mBdO8IuTckiQJEpvMYdFZlwJkcJ182dO3nffs+w0z+Uh64ntE78dogvTOT4x01uCMtwJ+pmgN6uTcZB+KV+NEEC7298kRZyclUZP4IDyc8qW95ljEHIJnDYwOzLop/IAY3PYwYT3ATJe8ceJ7nan32ShowHKNV+8abiD2yt4QJBANh/II1cF8wtSwVgUxaiKchZwz71GPIMxENRwF+Pgzpfp61FIw8+If8sR1X036HgyY1+fmuakcqqjg68eF19SC0CQQDVgtOtx8BT9YtPEchvdChqduHZi7f3IkrMuq7dzkZ9XI5QUdnAhnm8WiW8z8ruKUo32yyxJEWIUGOy6NLikqL9AkEAwGvVee7Vc/L5z/B6SQ6exmUJxUZBArnYIuFhc03x3Asy1C0z6RNXUh5/1OVNcuqBGdLI+Eistg37LxvSe32jjQJAEWnPG9A7xl0zVGqN31Eo7q3tc5GqmlRIp3PeSSbGpvjCfph+Wu5cxVjQ1RpZYZ0qeW29smDT7u8ngnLsqB/vfQJBAJwsJYVC8V0D8waVTeDS0zgVcmKtjqHOtuJ3VuFivuSX0BZQALQIq5UlwUhIHwUKimEhffHc84m43hIAVLHj7eM=";
		System.out.println(GetSign.sign(param, privatekey));
    }
    public static String sign(String[] param,String privatekey) {
		return getURLEncoderString(sign_(paramSort(param).getBytes(),privatekey));
    }
	public static String paramSort(String[] param) {
		String[] str_new = getUrlParam(param);
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		for (int i = 0; i < str_new.length; i++) {
			String[] str_split = str_new[i].split("=");
			String name = str_split[0];
			String value = str_split[1];
			if (i != 0)
				sb.append(",");
			// 添加键值对，区分字符串与json对象
			if (value.startsWith("{") || value.startsWith("[")) {
				sb.append(String.format("\"%s\":%s", name, value));
			} else {
				sb.append(String.format("\"%s\":\"%s\"", name, value));
			}
		}
		sb.append("}");
		return sb.toString();	
	}
	public static String sign_(byte[] data, String privateKey) {   
		// 解密由base64编码的私钥  		
        byte[] keyBytes;
		try {
			keyBytes = Base64.decodeBase64(privateKey.getBytes("UTF-8"));
//	        byte[] keyBytes = Base64.encodeBase64(privateKey.getBytes("UTF-8")); 
			// 构造PKCS8EncodedKeySpec对象  
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
			// KEY_ALGORITHM =RSA 指定的加密算法 
			KeyFactory keyFactory = KeyFactory.getInstance("RSA"); 
			// 取私钥匙对象 
			PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
			// 用私钥对信息生成数字签名SIGNATURE_ALGORITHM = SHA1WithRSA
			Signature signature = Signature.getInstance("SHA1WithRSA");  
			signature.initSign(privateK);  
			signature.update(data);  
			return Base64.encodeBase64String(signature.sign());  
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
	    }
	/**
	 * 对字符串数组进行排序
	 * 
	 * @param keys
	 * @return
	 */
	public static String[] getUrlParam(String[] keys) {

		for (int i = 0; i < keys.length - 1; i++) {
			for (int j = 0; j < keys.length - i - 1; j++) {
				String pre = keys[j];
				String next = keys[j + 1];
				if (isMoreThan(pre, next)) {
					String temp = pre;
					keys[j] = next;
					keys[j + 1] = temp;
				}
			}
		}
		return keys;
	}

	/**
	 * 比较两个字符串的大小，按字母的ASCII码比较
	 * 
	 * @param pre
	 * @param next
	 * @return
	 */
	private static boolean isMoreThan(String pre, String next) {
		if (null == pre || null == next || "".equals(pre) || "".equals(next)) {
			_log.error("字符串比较数据不能为空！");
			return false;
		}

		char[] c_pre = pre.toCharArray();
		char[] c_next = next.toCharArray();

		int minSize = Math.min(c_pre.length, c_next.length);

		for (int i = 0; i < minSize; i++) {
			if ((int) c_pre[i] > (int) c_next[i]) {
				return true;
			} else if ((int) c_pre[i] < (int) c_next[i]) {
				return false;
			}
		}
		if (c_pre.length > c_next.length) {
			return true;
		}

		return false;
	}
    /**
     * URL 解码
     *
     * @return String
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * URL 转码
     *
     * @return String
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
