package blockchain;

import java.net.URLEncoder;



import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.swetake.util.Qrcode;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

public class Test {
    static {
        System.setProperty("fileName", "test.log");
    }

    public static Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        String s = "0a02232c2208e940db8bee0dd03d40c0dfdcd5e42e5aae01081f12a9010a31747970652e676f6f676c65617069732e636f6d2f70726f746f636f6c2e54726967676572536d617274436f6e747261637412740a15414311f08873669b35f995e0bfb603509e1d96c38012154181aebbded41c5372b6e44a2f0d47ff60fe488f092244a9059cbb00000000000000000000004127a26fcfb475816f5d62a7c79da687388948aa40000000000000000000000000000000000000000000000000000000000007a12070f996d9d5e42e90018094ebdc03";
        System.out.println(Integer.toHexString(s.length()/2));
        System.out.println(s.length());
        System.out.println(s.length()/2);

    }

}
