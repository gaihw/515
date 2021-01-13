package blockchain.trx;

import blockchain.trx.tools.ByteArray;
import blockchain.trx.tools.WalletApi;

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
        System.out.println(base58checkToHexString("TFbTJhfPwHKSbajYZWf53w2ZZ2KUk2rAuz"));//413db38b48afdc458b290c13fca6c92bd27869709c
        System.out.println(base58checkToHexString("TDVTMU3WW4tHjFFctHDhEsfANAknBgtiZe"));//4126a0d6274ee69cc2415b86c09ef94435d3eb6620
        System.out.println(base58checkToHexString("TDBz4EqbZixSAvCSriK9tYrejotf4XD9Rh"));//412352fa90f94e05a749576c6b429fc2141903183e
        System.out.println(base58checkToHexString("TUczgZhaVzK7pXCLMcviCXquYvRYtB2Qdr"));//41cc97c991df6fc3b6f5474b344cab6203ebd14398

        System.out.println(hexStringTobase58check("41eaa0cda6897646bf6a98953c2d5ee78f9ad6e113"));//TXMokkvGXxXJb4bz2oJGKLrz4RvDMFNzcD
        System.out.println(hexStringTobase58check("414311f08873669b35f995e0bfb603509e1d96c380"));//TG5qmNjwsPg6YLVuLkbJ5SE5PkNajNAaQT

        System.out.println(base58checkToHexString("TUdjuVvnQQ7v9MKJcktPwuBmEBJTCqu1v1"));

    }
}
