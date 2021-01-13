package com.atom.dex.rpc;

import com.atom.dex.api.client.test.AtomApiRestClient;
import com.atom.dex.api.client.test.AtomApiRestClientImpl;

public class RpcClient {

    public static AtomApiRestClient getRestClient(){
        return  new AtomApiRestClientImpl("http://192.168.112.89:1317");
    }

    public static AtomApiRestClient getNodeClient(){
        return  new AtomApiRestClientImpl("http://192.168.112.89:26657");
    }
}
