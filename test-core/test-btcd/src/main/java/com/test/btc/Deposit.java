package com.test.btc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.domain.Output;
import com.neemre.btcdcli4j.core.domain.OutputOverview;
import com.neemre.btcdcli4j.core.domain.RawOutput;
import com.test.btc.domian.BtcAddressBean;
import com.test.btc.domian.BtcdUtxo;
import com.test.dao.AddressDao;
import com.test.db.JdbcTemplateMgr;
import com.test.rpc.RpcClient;
import com.test.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
public class Deposit {

    public static void main(String[] args) throws BitcoindException, CommunicationException {
        JdbcTemplateMgr.getInstance().initJdbcTemplate(true);
        depositCold("btc");
    }

    public static void depositCold(String coinName) throws BitcoindException, CommunicationException {

        List<String> coldAddressList = AddressDao.getColdAddressList(coinName);
        BtcdClient client = RpcClient.getBtcdClient(coinName);


        for (String addr : coldAddressList) {
            BigDecimal amount = RandomUtil.doubleRandom(0,2,6);
            String txid = client.sendToAddress(addr, amount);
            log.info(txid);
        }
    }



    public static String sentByUtxo(String coinName, BtcdUtxo vin, BtcAddressBean vinAddress,
                                    String toAddress, BigDecimal sendAmount) throws BitcoindException, CommunicationException {

        BtcdClient client = RpcClient.getBtcdClient(coinName);
        if (sendAmount == null) {
            sendAmount = vin.getAmount().subtract(new BigDecimal("0.001"));
        }

        Map<String, BigDecimal> voutMap = Maps.newHashMap();

        List<OutputOverview> outputOverviewList = Lists.newArrayList();
        List<Output> outputList = Lists.newArrayList();
        voutMap.put(toAddress, sendAmount);

        OutputOverview outputOverview = new OutputOverview();
        outputOverview.setTxId(vin.getTxid());
        outputOverview.setVOut(vin.getVout());
        outputOverviewList.add(outputOverview);

        Output output = new Output();
        output.setTxId(vin.getTxid());
        output.setVOut(vin.getVout());
        output.setAmount(vin.getAmount());
        RawOutput tmpOutput = client.getRawTransactionV(vin.getTxid()).getVOut().get(vin.getVout());
        String vinSpkHex = tmpOutput.getScriptPubKey().getHex();
        output.setScriptPubKey(vinSpkHex);
        output.setRedeemScript(vinAddress.getRedeemscript());
        outputList.add(output);

        //zhao ling
        String unSignHex = client.createRawTransaction(outputOverviewList, voutMap);
        // 签名交易数据
        if (vinAddress.getPrivkey() == null) {
            throw new RuntimeException("no privkey " + vinAddress.getAddress());
        }
        List<String> privateKey = Lists.newArrayList(vinAddress.getPrivkey());
        String signHex = null;
        if (coinName.equals("dash") || coinName.equals("zec")) {
            signHex = client.signRawTransaction(unSignHex, outputList, privateKey).getHex();
        } else {
            signHex = client.signRawTransactionWithKey(unSignHex, outputList, privateKey).getHex();
        }

        String txid = client.sendRawTransaction(signHex, true);
        log.info("new txid {}", txid);
        return txid;
    }
}
