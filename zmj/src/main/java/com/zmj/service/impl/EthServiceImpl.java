package com.zmj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.commom.eth.EthUtils;
import com.zmj.config.Config;
import com.zmj.domain.eth.EthChain;
import com.zmj.service.EthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class EthServiceImpl implements EthService {

    @Autowired
    private EthUtils ethUtils;

    @Override
    public JSONObject getAddPrivPub(EthChain ethChain) {
        return ethUtils.getAddPrivPub(ethChain.getPassword(),ethChain.getText());
    }

    @Override
    public JSONObject getPubPri(String password, String fileName) {
        return ethUtils.getPubPri(password,fileName);
    }

    @Override
    public BigInteger getBlockNumber(EthChain ethChain) {
        return ethUtils.get_blockNumber(ethUtils.getWeb3j(ethChain.getUrl()));
    }

    @Override
    public String transaction(EthChain ethChain) {
        try {
            return ethUtils.transaction(ethUtils.getWeb3j("http://"+ Config.ETH_URL +":"+ Config.ETH_PORT),ethChain.getToAddress(),ethChain.getAmount(),ethUtils.getCre("ETH"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return ethUtils.uploadFile(file);
    }

    @Override
    public List getFileList() {
        return ethUtils.getFileList();
    }

    @Override
    public String getrawtransaction(String txid) {
        return ethUtils.getTransactionByHash(txid);
    }
}
