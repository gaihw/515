package com.test.rpc;

import com.test.dao.ConfigDao;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;

public class RpcClient {

    public static Parity getRpcClient() {
        String rpcArr = ConfigDao.getCurrencyInfoRpcUrl("eth");
        return Parity.build(new HttpService(rpcArr));
    }
}
