package com.zmj.commom.trx;

import com.zmj.commom.trx.tools.ByteArray;
import com.zmj.commom.trx.tools.WalletApi;

public class Hex58Transfer {
    public static String base58checkToHexString(String base58check) {
        String hexString = ByteArray.toHexString(WalletApi.decodeFromBase58Check(base58check));
        return hexString;
    }

    public static String hexStringTobase58check(String hexString) {
        String base58check = WalletApi.encode58Check(ByteArray.fromHexString(hexString));
        return base58check;
    }

    public static void main(String[] args) {
        System.out.println(base58checkToHexString("TRZvF3abMRrMYvXbQu1uxqhF7Ryfuu4M7A"));
        System.out.println(hexStringTobase58check("41a614f803b6fd780986a42c78ec9c7f77e6ded13c"));
    }
}
