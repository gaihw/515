package com;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

public class GetSign {
	private static final Log _log = LogFactory.getLog(ASCIIToSort.class);
    public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";
    public static final String  ENCODE = "gbk";
    public static String sign(JSONObject param,String privatekey) {
		return getURLEncoderString(sign_(paramSort(param).getBytes(),privatekey));
    }
	public static String paramSort(JSONObject param) {
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
	 * @param params
	 * @return
	 */
	public static String[] getUrlParam(JSONObject params) {
//		JSONObject jsonb = JSONObject.fromObject(params);
		String[] keys = null;
		StringBuffer keysBuf = new StringBuffer();
		Set<String> set = params.keySet();
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			keysBuf.append(key+"="+params.getString(key)+",");
		}
		keys = keysBuf.toString().split(",");
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
    public static void main(String[] args) {
    	String p ="{\"page\":\"1\",\"api_key\":\"eb4f25dd532c14cee5e9b0301c8d4671\",\"size\":\"10\"}";
    	JSONObject param = JSONObject.fromObject(p);
    	String privatekey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALSQaafkE4wNY21T\n" + 
    			"+B2sCydP2FwkTQ6xi+bWLMVeF9bhFKyAZndmwxGgEc9bR6/rur5dAblka7OKpw3n\n" + 
    			"J27p3b5Ig/kd+dOSj4SfMp9pt96nHWud2KNZdAKUH4ybhg51D8TM0AXL474Gh8Cc\n" + 
    			"PrRvZspMdnlPM17YyUd6VjKQJs55AgMBAAECgYBF7mBdO8IuTckiQJEpvMYdFZlw\n" + 
    			"JkcJ182dO3nffs+w0z+Uh64ntE78dogvTOT4x01uCMtwJ+pmgN6uTcZB+KV+NEEC\n" + 
    			"7298kRZyclUZP4IDyc8qW95ljEHIJnDYwOzLop/IAY3PYwYT3ATJe8ceJ7nan32S\n" + 
    			"howHKNV+8abiD2yt4QJBANh/II1cF8wtSwVgUxaiKchZwz71GPIMxENRwF+Pgzpf\n" + 
    			"p61FIw8+If8sR1X036HgyY1+fmuakcqqjg68eF19SC0CQQDVgtOtx8BT9YtPEchv\n" + 
    			"dChqduHZi7f3IkrMuq7dzkZ9XI5QUdnAhnm8WiW8z8ruKUo32yyxJEWIUGOy6NLi\n" + 
    			"kqL9AkEAwGvVee7Vc/L5z/B6SQ6exmUJxUZBArnYIuFhc03x3Asy1C0z6RNXUh5/\n" + 
    			"1OVNcuqBGdLI+Eistg37LxvSe32jjQJAEWnPG9A7xl0zVGqN31Eo7q3tc5GqmlRI\n" + 
    			"p3PeSSbGpvjCfph+Wu5cxVjQ1RpZYZ0qeW29smDT7u8ngnLsqB/vfQJBAJwsJYVC\n" + 
    			"8V0D8waVTeDS0zgVcmKtjqHOtuJ3VuFivuSX0BZQALQIq5UlwUhIHwUKimEhffHc\n" + 
    			"84m43hIAVLHj7eM=+xysYFfCPAb2HirNojLe8kKNRoI1GZYMDD79jfTqWvcA6w2Y7LcWL7Fbw4w7x2BEGM60OukXj7p5jmhQXFH4iPXZaWAuBoL2czGuKXbHy1hfW8VrbtAmsgyVWH9E2brMDYQuu/+fIWljdULXPZtQyI7qyKux3mfueCBsIdFjoP3yYysA4t6olMCfhWzZJHfsDdKWxKqf70Gm1ExjTgjkDD4OnWX/ExWz2y+TNzO8Q1nX+8hYWQCVredrz6+UB/woHJ6UU5xR11YeeZ2x4547FZGGXJRZAgMBAAECggEAK6vZFvURWsRJBe7k+G8Y6bL5K8/bZSE/OvAqIrlgq23aKHbh3H+SLFByrYDwLJYw4L2PfD7NpSswmc2QB4MWq42zjarqIKUgZ3O4sS7G0Tz9vWkV+PrUgb+GgIzdDyQbRdQiduHP7Sg+b/D6l/ZKrttrPmIGU+8TgZOlfVu53LnyJ9HhUm+7+oMCvk1G2wWR8obIXcshiOb2caP8KvUvSxhstIb+QSwfdZZwKhQDDcPLhnMnPIteIiUbU3rUN7dZ8hTDr5Brpv8Vf2lahMjVqHaqDVS7sISEPmMGbC10hq0H1CXD5udwC5k0Kgd21kuUfDL4OI4Eq0skaQnGYzSqBQKBgQD4912XtXORmV6rNJ0QfWOP2qd6XYw27iK07gssJrOubav17UE0ud046X/yhUCUH6kC3slknYB9a0Pq5zfGa78cL40JFmqo3g/POqtpGX/aGjS3qrIoVEcGGQoYp/nrdn6HFAWWdrL5JtJokG/ZXGfDwphTUXY5783FVBNjTKVyJwKBgQDJLAEfGqOYdeXANVqygryjol/g+nl2y/6mU7e8oHbghv9eKUI2MFPhDXEKBG6+rXKatBDNiEhC7YOjSRTXSuVUCR/QgaQnO1jruk2H+Y3ZtYJmBABt28PWuCX5OeEOdLv/y19Xrtz/c4osV2fgkwinaEjJKnTRQ7UUjhKoWs5VfwKBgQDsTf2rjTJQM/5DCgE6MYladPMZlckMKiMPEBHSqjlJihb5qWOGpBNxfU9P5WJSNWq5qrQEp+nZIQKp5yRtVL9kBU4cHgbGmV7wFoOzkdxkKxN2cLIAzV1MUF/g3y67axDVLQ/QrIFbMpnrXYyMBsUGbaEfE+bfPtlUPmTYj1tJaQKBgCJ6SDbsed1HV3j5q/GsNmhVC9mjE+4oSBx5fKP3XGheuqWIrLBc2ANaKmVtHQ3Q7DNXaQnQsZHG4yi4/bloPKWKaslxbpDKuxKre14yhv0o/pA9X1V0tLu4E8oDAW9Y0l+km1KLxZSCWMwAruzuRi1q0g6oGB1FXWOIdeokpfhBAoGAJm7lIuDDA7uebNY1ufxNR8HJ5esoYQDdCGrDdyxa4SVpxzk2bER62l82DQCUpuVuE33EpflsH8nIpgSrQ6XLt0xs2b3/FbkGI3tDbgE+UW/XvhWv0X6plI9ENdFFB3L9ovwJhp/A0/4AoJQ8TNy5j8ZNKQM0tLj2/YqrkJLUz9s=";
//    	System.out.println("$$$$$$$$$$$$");
    	System.out.println(GetSign.sign(param, privatekey));
	}
}
