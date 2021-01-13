package com.test.btc;

import com.test.util.GZIPUtils;
import de.bwaldvogel.base91.Base91;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestBase64 {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    public static void main(String[] args) throws DecoderException {

        String ss ="f8aa1f852b6474d9ed82d19994929fb884f0d842b95c355e099f413e57dccd61f080b844a9059cbb000000000000000000000000112fa5605a9823c8b4013cb5d25cce6961f0c8730000000000000000000000000000000000000000000000056bc75e2d6310000081eea003ba25cbb556e1a180baaed8ca3dc55d88122587690f52897274ac5b57a2bc07a04893700053ba9b05b92a33cc3e86f37402f8aac0b979029b05c2f35fc96c001c f8aa1e853c546b2a998296c194929fb884f0d842b95c355e099f413e57dccd61f080b844a9059cbb000000000000000000000000702736ec62d487feb68a160bf82d5b31f3dd708000000000000000000000000000000000000000000000000029a2241af62c000081eea0c7a02f7a76d11a8bdf836b9cc8ba1268bcebf5670ce02c1f6365d0db1206566ca066882cdd24e1d8acc777fea6694e4a6f1545d887252837459299efc7a0c2a6e4";

        System.out.println("原文" + ss.length());
        String zipSS = GZIPUtils.compressStr(ss);
        System.out.println("zip原文 "+zipSS.length());
        System.out.println("unzip原文 "+GZIPUtils.unCompressStr(zipSS).length());


        byte[] bytes = Hex.decodeHex(ss);
        String base64  = Base64.getEncoder().encodeToString(bytes);
        System.out.println("base64原文 " +base64.length());
        System.out.println("zip base64原文 " +GZIPUtils.compressStr(base64).length());


        byte[] base91bytes = Base91.encode(Hex.decodeHex(ss));
        String base91Str = new String(base91bytes, CHARSET);
        System.out.println("base91原文" + base91Str.length());
        System.out.println("zip base91原文" + GZIPUtils.compressStr(base91Str).length());

    }
}
