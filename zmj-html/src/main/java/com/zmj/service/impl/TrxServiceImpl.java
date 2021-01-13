package com.zmj.service.impl;

import com.zmj.commom.trx.TrxUtils;
import com.zmj.domain.trx.TrxChain;
import com.zmj.service.TrxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrxServiceImpl implements TrxService {

    @Autowired
    private TrxUtils trxUtils;

    @Override
    public String generateAddress() {
        return trxUtils.generateAddress();
    }

    @Override
    public String createtransaction(TrxChain trxChain) {
        return trxUtils.createtransaction(trxChain.getOwnerAddress(),trxChain.getToAddress(),trxChain.getValue(),trxChain.getPrivateKey());
    }

    @Override
    public String validateAddress(String address) {
        return trxUtils.validateAddress(address);
    }

    @Override
    public String gettransactioninfobyid(String txid) {
        return trxUtils.gettransactioninfobyid(txid);
    }

    @Override
    public String gettransactionbyid(String txid) {
        return trxUtils.gettransactionbyid(txid);
    }

    @Override
    public String getaccountresource(String address) {
        return trxUtils.getaccountresource(address);
    }

    @Override
    public String getcontract(String address) {
        return trxUtils.getcontract(address);
    }

    @Override
    public String getaccount(String address) {
        return trxUtils.getaccount(address);
    }

    @Override
    public String freezebalance(TrxChain trxChain) {
        return trxUtils.freezebalance(trxChain.getOwnerAddress(),trxChain.getFrozenBalance(),trxChain.getResource(),trxChain.getReceiverAddress(),trxChain.getPrivateKey());
    }

    @Override
    public String createaccount(TrxChain trxChain) {
        return trxUtils.createaccount(trxChain.getOwnerAddress(),trxChain.getAccountAddress(),trxChain.getPrivateKey());
    }
}
