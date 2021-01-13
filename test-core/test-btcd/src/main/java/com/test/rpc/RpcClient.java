package com.test.rpc;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import com.test.dao.ConfigDao;

public class RpcClient {

    public static BtcdClient getBtcdClient(String coinName) throws BitcoindException, CommunicationException {
        String[] rpcArr = ConfigDao.getCurrencyInfoRpcUrl(coinName).split("@@");
        return new BtcdClientImpl(rpcArr[0], Integer.parseInt(rpcArr[1]), rpcArr[2], rpcArr[3]);
    }
}
