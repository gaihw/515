package com.zmj.service.impl;

import com.zmj.commom.eth.Erc20Utils;
import com.zmj.config.Config;
import com.zmj.domain.eth.Erc20Chain;
import com.zmj.service.Erc20Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Erc20ServiceImpl implements Erc20Service {

    @Autowired
    private Erc20Utils erc20Utils;


    @Override
    public String transaction(Erc20Chain erc20Chain) {
        try {
            return erc20Utils.transferERC20Token(erc20Utils.getWeb3j("http://"+ Config.ETH_URL +":"+ Config.ETH_PORT),erc20Chain.getToAddress(),erc20Chain.getAmount(),Config.USDT_ERC20,erc20Utils.getCre(),Config.ETH_CHAINLD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getrawtransaction(String txid) {
        return erc20Utils.getTransactionByHash(txid);
    }
}
