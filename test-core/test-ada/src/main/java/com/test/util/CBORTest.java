package com.test.util;

import com.upokecenter.cbor.CBORObject;
import com.upokecenter.numbers.EDecimal;
import org.bouncycastle.util.encoders.DecoderException;
import org.bouncycastle.util.encoders.Hex;
import org.joda.time.DateTime;
import org.junit.Test;
import org.lasinger.tools.hexdump.Hexdump;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CBORTest {

    @Test
    public void decode() {

//        {
//            "address": "addr1vxpfffuj3zkp5g7ct6h4va89caxx9ayq2gvkyfvww48sdncxsce5t",
//              "base16":"618294a79288ac1a23d85eaf5674e5c74c62f480521962258e754f06cf",
//                "type": "payment",
//                "encoding": "bech32",
//                "era": "shelley"
//        }



//        "address":        "addr1q83zxy863pgesju6kp7wtzyf4f0ym5w636835l3hnrxhelftjkdy8vq7h2z4udu6mzu0k2q5uqm3xknwxjxyrhs8kldq4eqajj",
//                "base16": "01e22310fa8851984b9ab07ce58889aa5e4dd1da8e8f1a7e3798cd7cfd2b959a43b01eba855e379ad8b8fb2814e037135a6e348c41de07b7da",
//                "type": "payment",
//                "encoding": "bech32",
//                "era": "shelley"
        //http://cbor.me/
//        String hexString = Hexdump.hexdump(bytes);


        //./cardano-cli shelley transaction build-raw
        // --tx-in 4e3a6e7fdcb0d0efa17bf79c13aed2b4cb9baf37fb1aa2e39553d5bd720c5c99#4
        // --tx-out addr1q83zxy863pgesju6kp7wtzyf4f0ym5w636835l3hnrxhelftjkdy8vq7h2z4udu6mzu0k2q5uqm3xknwxjxyrhs8kldq4eqajj+100000000

        // --tx-out addr1q9qgfv7aj9mpzfq9m05er5hg0rnawzl9xp7ewha9dj22rletjkdy8vq7h2z4udu6mzu0k2q5uqm3xknwxjxyrhs8kldq9v8c0m+999899832035
        // --ttl 2000
        // --fee 167965
        // --out-file tx001.raw

        //[{2: 167965, 3: 2000,
        // 1: [[h'01E22310FA8851984B9AB07CE58889AA5E4DD1DA8E8F1A7E3798CD7CFD2B959A43B01EBA855E379AD8B8FB2814E037135A6E348C41DE07B7DA', 100000000],
        //     [h'014084B3DD9176112405DBE991D2E878E7D70BE5307D975FA56C94A1FF2B959A43B01EBA855E379AD8B8FB2814E037135A6E348C41DE07B7DA', 999899832035]],
        // 0: [[h'4E3A6E7FDCB0D0EFA17BF79C13AED2B4CB9BAF37FB1AA2E39553D5BD720C5C99', 4]]}, null]


        String hexStr = "82a400818258204e3a6e7fdcb0d0efa17bf79c13aed2b4cb9baf37fb1aa2e39553d5bd720c5c9904018282583901e22310fa8851984b9ab07ce58889aa5e4dd1da8e8f1a7e3798cd7cfd2b959a43b01eba855e379ad8b8fb2814e037135a6e348c41de07b7da1a05f5e100825839014084b3dd9176112405dbe991d2e878e7d70be5307d975fa56c94a1ff2b959a43b01eba855e379ad8b8fb2814e037135a6e348c41de07b7da1b000000e8ceac9ee3021a0002901d031907d0f6";
        CBORObject obj = CBORObject.DecodeFromBytes(Hex.decode(hexStr));
        printCborHexString(obj);
        System.out.println("=====");
    }



    @Test
    public void hexTest(){
        String addrHex ="01E22310FA8851984B9AB07CE58889AA5E4DD1DA8E8F1A7E3798CD7CFD2B959A43B01EBA855E379AD8B8FB2814E037135A6E348C41DE07B7DA";
        String addr =   "addr1q83zxy863pgesju6kp7wtzyf4f0ym5w636835l3hnrxhelftjkdy8vq7h2z4udu6mzu0k2q5uqm3xknwxjxyrhs8kldq4eqajj";

    }


    @Test
    public void encodeB(){


        //[{2: 167965, 3: 2000, 1: [[h'01E22310FA8851984B9AB07CE58889AA5E4DD1DA8E8F1A7E3798CD7CFD2B959A43B01EBA855E379AD8B8FB2814E037135A6E348C41DE07B7DA', 100000000], [h'014084B3DD9176112405DBE991D2E878E7D70BE5307D975FA56C94A1FF2B959A43B01EBA855E379AD8B8FB2814E037135A6E348C41DE07B7DA', 999899832035]], 0: [[h'4E3A6E7FDCB0D0EFA17BF79C13AED2B4CB9BAF37FB1AA2E39553D5BD720C5C99', 4]]}, null]
        CBORObject obj = CBORObject.NewArray();


        obj = CBORObject.FromObject("[{2: 167965, 3: 2000, 1: [[01E22310FA8851984B9AB07CE58889AA5E4DD1DA8E8F1A7E3798CD7CFD2B959A43B01EBA855E379AD8B8FB2814E037135A6E348C41DE07B7DA, 100000000], [014084B3DD9176112405DBE991D2E878E7D70BE5307D975FA56C94A1FF2B959A43B01EBA855E379AD8B8FB2814E037135A6E348C41DE07B7DA, 999899832035]], 0: [[4E3A6E7FDCB0D0EFA17BF79C13AED2B4CB9BAF37FB1AA2E39553D5BD720C5C99, 4]]}, null]");


        printCborHexString(obj);
    }



    private void printCborHexString(CBORObject obj) {
        byte[] bytes = obj.EncodeToBytes();
        String hexString = Hexdump.hexdump(bytes);
        System.out.println(obj.toString());
        System.out.println(hexString);
    }

    @Test
    public void testInt() {
        CBORObject obj = CBORObject.FromObject(1);
        printCborHexString(obj);
    }

    @Test
    public void testInt100() {
        CBORObject obj = CBORObject.FromObject(100);
        printCborHexString(obj);
    }

    @Test
    public void testIntNegative100() {
        CBORObject obj = CBORObject.FromObject(-100);
        printCborHexString(obj);
    }

    @Test
    public void testByteArray() {
        int length = 500;
        byte[] testByte = new byte[length];
        for (int i = 0; i < length; i++) {
            testByte[i] = 0x30;
        }
        CBORObject obj = CBORObject.FromObject(testByte);
        printCborHexString(obj);
    }

    @Test
    public void testString() {
        CBORObject obj = CBORObject.FromObject("IETF");
        printCborHexString(obj);
    }

    @Test
    public void testLargeString() {
        StringBuilder builder = new StringBuilder("addr1q83zxy863pgesju6kp7wtzyf4f0ym5w636835l3hnrxhelftjkdy8vq7h2z4udu6mzu0k2q5uqm3xknwxjxyrhs8kldq4eqajj");



        CBORObject obj = CBORObject.FromObject(builder.toString());
        printCborHexString(obj);
    }

    @Test
    public void testArray() {
        CBORObject obj = CBORObject.NewArray();

        obj.Add(CBORObject.FromObject(1));
        obj.Add(CBORObject.FromObject(2));
        obj.Add(CBORObject.FromObject(3));
        printCborHexString(obj);
    }

    @Test
    public void testArray24() {
        CBORObject obj = CBORObject.NewArray();

        obj.Add(CBORObject.FromObject(500));
        obj.Add(CBORObject.FromObject(501));
        obj.Add(CBORObject.FromObject(502));
        printCborHexString(obj);
    }

    /**
     * 嵌套数组 [1, [2,3], [4,5]]
     */
    @Test
    public void testMultiArray() {
        CBORObject obj = CBORObject.NewArray();
        obj.Add(CBORObject.FromObject(1));

        CBORObject subArray1 = CBORObject.NewArray();
        subArray1.Add(CBORObject.FromObject(2));
        subArray1.Add(CBORObject.FromObject(3));
        obj.Add(subArray1);

        CBORObject subArray2 = CBORObject.NewArray();
        subArray2.Add(CBORObject.FromObject(4));
        subArray2.Add(CBORObject.FromObject(5));
        obj.Add(subArray2);

        printCborHexString(obj);
    }

    @Test
    public void testLargeArray() {
        CBORObject obj = CBORObject.NewArray();

        int length = 25;
        for (int i = 0; i < length; i++) {
            int temp = i + 100;
            obj.Add(CBORObject.FromObject(temp));
        }

        printCborHexString(obj);
    }

    @Test
    public void testMap() {
        CBORObject obj = CBORObject.NewMap();

        obj.set(1, CBORObject.FromObject(2));
        obj.set(3, CBORObject.FromObject(4));

        printCborHexString(obj);
    }

    @Test
    public void testJavaMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);

        CBORObject obj = CBORObject.FromObject(map);

        printCborHexString(obj);
    }

    @Test
    public void testTrue() {
        CBORObject obj = CBORObject.FromObject(true);

        printCborHexString(obj);
    }

    @Test
    public void testBigDecimal() {
        String decimalString = BigDecimal.valueOf(273.15).toString();
        CBORObject obj = CBORObject.FromObject(EDecimal.FromString(decimalString));

        printCborHexString(obj);
    }

    @Test
    public void testDateTime() {
        DateTime dt = new DateTime(2013, 3, 21, 20, 04, 0);
        CBORObject obj = CBORObject.FromObject(dt.toDate());

        printCborHexString(obj);
    }

    @Test
    public void testCBORTag() {
        byte[] array = new byte[] {0x01, 0x02, 0x03, 0x04};
        CBORObject obj = CBORObject.FromObjectAndTag(array, 23);

        printCborHexString(obj);
    }
}
