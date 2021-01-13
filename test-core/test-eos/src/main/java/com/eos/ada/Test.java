package com.eos.ada;

import com.eos.EosApiRestClient;
import com.eos.impl.EosApiRestClientImpl;

public class Test {

    public static void main(String[] args) {

        String baseUrl = "http://192.168.112.89:3100/graphql";

        EosApiRestClient client = new EosApiRestClientImpl(baseUrl);

//        client.getBlock()
    }
}
