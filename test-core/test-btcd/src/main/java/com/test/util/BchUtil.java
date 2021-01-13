package com.test.util;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;

import java.io.File;
import java.io.IOException;

public class BchUtil {

    public static void createBchColdAddr() throws IOException, BitcoindException, CommunicationException {
        String str = null;//FileUtils.readFileToString(new File("/Users/zhaozhilong/Downloads/fex_useraddress/bch_useraddress.csv"), "UTF-8");
        String[] addrArr = str.split("\n");
        BtcdClient client = new BtcdClientImpl("192.168.112.89", 6332, "btctest", "btctest");
        StringBuilder stringBuilder = new StringBuilder("insert into fex_wallet.tb_wallet_address_cold_bch (address,newAddress) values");
        for (String addr : addrArr) {
            stringBuilder.append("('").append(addr).append("','").append(client.validateAddress(addr).getAddress()).append("'),");
        }
        // FileUtils.writeStringToFile(new File("/Users/zhaozhilong/Desktop/bch-cold-addr.sql"), stringBuilder.toString(), "UTF-8");

    }
}
