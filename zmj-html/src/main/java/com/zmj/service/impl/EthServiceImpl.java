package com.zmj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.commom.eth.EthUtils;
import com.zmj.domain.eth.EthChain;
import com.zmj.service.EthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class EthServiceImpl implements EthService {

    @Autowired
    private EthUtils ethUtils;

    @Override
    public JSONObject getAddPrivPub(EthChain ethChain) {
        return ethUtils.getAddPrivPub(ethChain.getPassword(),ethChain.getText());
    }

    @Override
    public BigInteger getBlockNumber(EthChain ethChain) {
        return ethUtils.get_blockNumber(ethUtils.getWeb3j(ethChain.getUrl()));
    }

    @Override
    public String transaction(EthChain ethChain) {
        try {
            return ethUtils.transaction(ethUtils.getWeb3j(ethChain.getUrl()),ethChain.getToAddress(),ethChain.getGasPrice(),ethChain.getGasLimit(),ethChain.getValue(),ethUtils.getCre(ethChain.getPassword(),ethChain.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
