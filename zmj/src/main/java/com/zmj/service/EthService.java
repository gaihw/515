package com.zmj.service;

import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.eth.EthChain;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface EthService {
    /**
     * 获取地址，公私钥
     * @return
     */
    JSONObject getAddPrivPub(EthChain ethChain);

    JSONObject getPubPri(String password, String fileName);

    /**
     * 查询区块高度
     */
    BigInteger getBlockNumber(EthChain ethChain);

    /**
     * 创建交易
     * @param ethChain
     * @return
     */
    String transaction(EthChain ethChain);

    /**
     * 上传
     * @param file
     * @return
     */
    String uploadFile(MultipartFile file);

    /**
     * 获取data目录下的文件
     * @return
     */
    List getFileList();

    /**
     * 查询交易信息
     */
    String getrawtransaction(String txid);

}
