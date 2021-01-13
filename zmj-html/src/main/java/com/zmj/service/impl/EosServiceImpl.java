package com.zmj.service.impl;

import com.zmj.commom.eos.EosUtils;
import com.zmj.domain.eos.EosChain;
import com.zmj.service.EosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EosServiceImpl implements EosService {

    @Autowired
    private EosUtils eosUtils;

    @Override
    public String get_public_keys() {
        return eosUtils.get_public_keys();
    }

    @Override
    public String unlock(EosChain eosChain) {

        return eosUtils.unlock(eosChain.getWallet(),eosChain.getPassword());
    }
}
