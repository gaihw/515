package com.zmj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.commom.trx.Trc20Utils;
import com.zmj.config.Config;
import com.zmj.domain.trx.Trc20Chain;
import com.zmj.service.Trc20Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Trc20ServiceImpl implements Trc20Service {

    @Autowired
    private Trc20Utils trc20Utils;

    @Override
    public String createtransaction(Trc20Chain trc20Chain) {
        return trc20Utils.createtransaction(Config.TRC20_CONTRANCT_ADDRESS,Config.TRC20_OWNER_ADDRESS,trc20Chain.getToAddress(),Config.CALLVALUE,Config.TRC20_PRIVATEKEY,Integer.parseInt(trc20Chain.getAmount()));
    }

    @Override
    public String balanceof(String address) {
        return trc20Utils.balanceOf(Config.TRC20_CONTRANCT_ADDRESS,address);
    }

    @Override
    public JSONObject getrawtransaction(String txid) {
        String gtfbid = trc20Utils.gettransactioninfobyid(txid);
        String gtbid = trc20Utils.gettransactionbyid(txid);
        String getblockbynum = trc20Utils.getblockbynum(JSONObject.parseObject(gtfbid).getString("blockNumber"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("blockNumber",JSONObject.parseObject(gtfbid).getString("blockNumber"));
        jsonObject.put("contractRet",JSONObject.parseObject(gtbid).getJSONArray("ret").getJSONObject(0).getString("contractRet"));
        jsonObject.put("from",JSONObject.parseObject(gtbid).getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("owner_address"));
        jsonObject.put("contranctaddress",JSONObject.parseObject(gtbid).getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("contract_address"));
        jsonObject.put("to",JSONObject.parseObject(gtbid).getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("data").substring(30,72));
        jsonObject.put("value",JSONObject.parseObject(gtbid).getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("data").substring(72).replaceAll("^(0+)", ""));
        jsonObject.put("blockHash",JSONObject.parseObject(getblockbynum).getString("blockID"));
        return jsonObject;
    }
}
