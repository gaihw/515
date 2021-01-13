package com.zmj.service.impl;

import com.zmj.commom.eos.EosUtils;
import com.zmj.config.Config;
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

    @Override
    public String getblock() {
        return eosUtils.get_info();
    }

    @Override
    public String transaction(EosChain eosChain) {
        return eosUtils.createTransaction(Config.ACCOUNT,eosChain.getToAddress(),eosChain.getAmount().toString(),eosChain.getMemo());
    }

    @Override
    public String open(String testcreate) {
        return eosUtils.open(testcreate);
    }
}
