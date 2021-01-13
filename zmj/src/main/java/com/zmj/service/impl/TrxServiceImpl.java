package com.zmj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.commom.trx.TrxUtils;
import com.zmj.config.Config;
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
        return trxUtils.createtransaction(
                Config.TRX_OWNER_ADDRESS,trxChain.getToAddress(),trxChain.getAmount(),Config.TRX_PRIVATEKEY);
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
    public JSONObject getrawtransaction(String txid) {
        String gtfbid = trxUtils.gettransactioninfobyid(txid);
        String gtbid = trxUtils.gettransactionbyid(txid);
        String getblockbynum = trxUtils.getblockbynum(JSONObject.parseObject(gtfbid).getString("blockNumber"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("blockNumber",JSONObject.parseObject(gtfbid).getString("blockNumber"));
        jsonObject.put("contractRet",JSONObject.parseObject(gtbid).getJSONArray("ret").getJSONObject(0).getString("contractRet"));
        jsonObject.put("from",JSONObject.parseObject(gtbid).getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("owner_address"));
        jsonObject.put("to",JSONObject.parseObject(gtbid).getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("to_address"));
        jsonObject.put("value",JSONObject.parseObject(gtbid).getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("amount"));
        jsonObject.put("blockHash",JSONObject.parseObject(getblockbynum).getString("blockID"));
        return jsonObject;
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
        return trxUtils.createaccount(
                trxChain.getOwnerAddress(),trxChain.getAccountAddress(),trxChain.getPrivateKey());
    }
}
