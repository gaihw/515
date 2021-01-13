package com.trx;

import com.google.protobuf.InvalidProtocolBufferException;
import com.test.dao.AddressDao;
import com.test.db.JdbcTemplateMgr;
import com.test.db.SQLUtil;
import com.test.util.RandomUtil;
import com.trx.domain.Trc20Config;
import com.trx.util.TronUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.spongycastle.util.encoders.Hex;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.tron.common.utils.ByteArray;
import org.tron.protos.Protocol;
import org.tron.studio.utils.AbiUtil;
import org.tron.walletserver.WalletClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class Depoist {

    private static String minerSecret = "61cae49321a8f13788392b9b4b76dd3410007bb8aca89acdbefe0692d175c684";
    private static String coinName = "trx";

    //{"privateKey":"61cae49321a8f13788392b9b4b76dd3410007bb8aca89acdbefe0692d175c684","address":"TG5qmNjwsPg6YLVuLkbJ5SE5PkNajNAaQT","hexAddress":"414311f08873669b35f995e0bfb603509e1d96c380"}
    public static void main(String[] args) throws IOException {
        JdbcTemplateMgr.getInstance().initJdbcTemplate(true);

        deposit();
    }


    public static void deposit() throws InvalidProtocolBufferException {

        Trc20Config trc20Config = Trc20Config.builder().
                fromAddress("TG5qmNjwsPg6YLVuLkbJ5SE5PkNajNAaQT").
                fromAddressHex("414311f08873669b35f995e0bfb603509e1d96c380").
                contractAddress("4181aebbded41c5372b6e44a2f0d47ff60fe488f09").build();
        WalletClient.init("192.168.112.214:16669",true);
        List<String> coldAddressList = AddressDao.getColdAddressList(coinName);


        int index =0;

        for (String address : coldAddressList) {
            if(index>1){
                return;
            }
            long callValue = 0L;
            long feeLimit = 10000000L;
            String params = "\"" + address + "\"," + TronUtil.trxToDrop(new BigDecimal(RandomUtil.intRandom(10, 100) + ""));
            byte[] data = Hex.decode(AbiUtil.parseMethod("transfer(address,uint256)", params, false));
            byte[] owner = Hex.decode(trc20Config.getFromAddressHex());
            byte[] contractAddress = Hex.decode(trc20Config.getContractAddress());

            Protocol.Transaction tx = WalletClient.triggerContract(owner, contractAddress, callValue, data, feeLimit);

            String hash = org.tron.TronUtil.getTransactionHash(tx);
            log.info(hash);
            String hex = TronUtil.signTx(tx, minerSecret);

            WalletClient.broadcastTransaction(ByteArray.fromHexString(hex));
index++;


        }
    }


}
