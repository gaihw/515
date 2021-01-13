package com.zmj.service.impl;

import com.zmj.commom.btcd.BtcUtils;
import com.zmj.dao.btcd.BtcDao;
import com.zmj.domain.btcd.BtcChain;
import com.zmj.service.BtcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BtcServiceImpl implements BtcService {


    @Autowired
    private BtcDao btcDao;

    @Autowired
    private BtcUtils btcUtils;

    @Override
    public String getblockcount(){

//        Map<String,Object> map = btcDao.getRpcUrl(1);
//        Iterator iterator = map.values().iterator();
//
//        String[] rpc_url = null;
//        while (iterator.hasNext()){
//            rpc_url = String.valueOf(iterator.next()).split("@@");
//        }

//        String[] rpc_url = btcDao.getRpcUrl(1).split("@@");
//        String url = rpc_url[0];
//        String port = rpc_url[1];
//        String username = rpc_url[2];
//        String password = rpc_url[3];
//
//        Base64.Encoder encoder = Base64.getEncoder();
//        String cred = encoder.encodeToString((username+":"+password).getBytes());
//        String params = "{\"jsonrpc\":\"2.0\", \"id\": \"curltest\", \"method\": \"getblockcount\", \"params\":[]}";
//        return HttpUtils.postByJson("http://"+url+":"+port,cred,params);
        return btcUtils.getblockcount();
    }

    @Override
    public String getpeerinfo() {

        return btcUtils.getpeerinfo();
    }

    @Override
    public String sendtoaddress(BtcChain btcChain) {
        return btcUtils.sendtoaddress(btcChain.getToAddress(),btcChain.getAmount().toString());
    }

//    @Override
//    public String createrawtransaction(String txid, String vout, String toAddress, String value) {
//        return btcUtils.createrawtransaction(txid,vout,toAddress,value);
//    }

    @Override
    public String getrawtransaction(String txId) {
        return btcUtils.getrawtransaction(txId);
    }

    @Override
    public String getnewaddress() {
        return btcUtils.getnewaddress();
    }


}
