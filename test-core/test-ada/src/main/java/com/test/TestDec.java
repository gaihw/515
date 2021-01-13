package com.test;

import com.upokecenter.cbor.CBORObject;
import org.bouncycastle.util.encoders.Hex;
import org.lasinger.tools.hexdump.Hexdump;

public class TestDec {

    public static void main(String[] args) {

        //./cardano-cli shelley transaction build-raw --tx-in 4e3a6e7fdcb0d0efa17bf79c13aed2b4cb9baf37fb1aa2e39553d5bd720c5c99#4
        // --tx-out addr1q83zxy863pgesju6kp7wtzyf4f0ym5w636835l3hnrxhelftjkdy8vq7h2z4udu6mzu0k2q5uqm3xknwxjxyrhs8kldq4eqajj+100000000
        // --tx-out addr1q9qgfv7aj9mpzfq9m05er5hg0rnawzl9xp7ewha9dj22rletjkdy8vq7h2z4udu6mzu0k2q5uqm3xknwxjxyrhs8kldq9v8c0m+999899832035
        // --ttl 2000 --fee 167965 --out-file tx001.raw


        //[
        // {
        // 2: 167965,
        // 3: 2000,
        // 1: [[h'01E22310FA8851984B9AB07CE58889AA5E4DD1DA8E8F1A7E3798CD7CFD2B959A43B01EBA855E379AD8B8FB2814E037135A6E348C41DE07B7DA', 100000000],
        //     [h'014084B3DD9176112405DBE991D2E878E7D70BE5307D975FA56C94A1FF2B959A43B01EBA855E379AD8B8FB2814E037135A6E348C41DE07B7DA', 999899832035]],
        // 0: [[h'4E3A6E7FDCB0D0EFA17BF79C13AED2B4CB9BAF37FB1AA2E39553D5BD720C5C99', 4]]
        // }
        //
        // , null]


        //addr1qr6q8xzqgvmygv6eretw8rxd7sqf5060gl4kdwut6rqjj3yadgdzan5emz2npkfg4jss63njfdl2nc222w6cdu5ua74qatcavn zhaoling

        //76dc27cbb16b3a00e90edc1ddd0bca474382daf1031a7d0373b69c4929d16568

        CBORObject obj = CBORObject.NewArray();
        CBORObject objMap = CBORObject.NewMap();

        //vin
        CBORObject vinArr = CBORObject.NewArray();
        CBORObject vinArr0 = CBORObject.NewArray();
        vinArr.Add(vinArr0);
        vinArr0.Add(CBORObject.FromObject("h'76dc27cbb16b3a00e90edc1ddd0bca474382daf1031a7d0373b69c4929d16568'"));
        vinArr0.Add(CBORObject.FromObject(1));
        objMap.set(0,vinArr);

        //vout
        CBORObject voutArr = CBORObject.NewArray();
        CBORObject voutArr0 = CBORObject.NewArray();
        CBORObject voutArr1 = CBORObject.NewArray();
        voutArr.Add(voutArr0);
        voutArr.Add(voutArr1);

        voutArr0.Add(CBORObject.FromObject("h'00f403984043364433591e56e38ccdf4009a3f4f47eb66bb8bd0c129449d6a1a2ece99d89530d928aca10d46724b7ea9e14a53b586f29cefaa'")); //zhaoling
        voutArr0.Add(CBORObject.FromObject(990));
        voutArr1.Add(CBORObject.FromObject("h'0056d2c8156242a3124835b021939f6764a89fe0a31171ed606755273706e2ae44dff6770dc0f4ada3cf4cf2605008e27aecdb332ad349fda7'"));
        voutArr1.Add(CBORObject.FromObject(6));

        objMap.set(1,voutArr);


        //

        objMap.set(2,CBORObject.FromObject(167965));
        objMap.set(3,CBORObject.FromObject(20000000));

        obj.Add(objMap);
        obj.Add("null");


        printCborHexString(obj);

    }

    private static void printCborHexString(CBORObject obj) {
        byte[] bytes = obj.EncodeToBytes();
        String hexString = Hexdump.hexdump(bytes);
        System.out.println(obj.toString());
        System.out.println(hexString);
        System.out.println(Hex.toHexString(bytes));
    }
}
