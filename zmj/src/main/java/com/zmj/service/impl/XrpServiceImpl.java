package com.zmj.service.impl;


import com.zmj.commom.xrp.XrpUtils;
import com.zmj.domain.xrp.XrpChain;
import com.zmj.service.XrpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class XrpServiceImpl implements XrpService {

    @Autowired
    private XrpUtils xrpUtils;

    @Override
    public String transaction(XrpChain xrpChain){
        return xrpUtils.transaction(xrpChain);
    }

    @Override
    public String getAccountInfo(String account){

        return xrpUtils.getAccountInfo(account);
    }

    @Override
    public String getTransaction(String transaction){

        return xrpUtils.getTransaction(transaction);
    }

}
