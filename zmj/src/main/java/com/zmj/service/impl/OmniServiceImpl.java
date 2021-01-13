package com.zmj.service.impl;

import com.zmj.commom.btcd.OmniUtils;
import com.zmj.config.Config;
import com.zmj.domain.btcd.OmniChain;
import com.zmj.service.OmniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OmniServiceImpl implements OmniService {

    @Autowired
    private OmniUtils omniUtils;

    @Override
    public String transaction(OmniChain omniChain) {
        return omniUtils.omni_send(Config.OMNI_ADDRESS,omniChain.getToAddress(),Config.PROPERTYID,omniChain.getAmount().toString());
    }
}
