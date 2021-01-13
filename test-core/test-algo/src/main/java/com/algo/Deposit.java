package com.algo;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.util.AlgoConverter;
import com.algorand.algosdk.util.Encoder;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import com.test.dao.AddressDao;
import com.test.db.JdbcTemplateMgr;
import com.test.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Slf4j
public class Deposit {

    static String miner = "VU7LU3KJYDUZBPOKUZEM3KL6OAP34OD5MGM2F2CCAF6WJXERKZBVH6RKAQ";
    static String privateKeyStr = "a1c850f58352496d75d45fe1c1af7e1219105f0ad68955670a2a8bc9a171cefc";
    static String miner_key = "3051020101300506032b657004220420a1c850f58352496d75d45fe1c1af7e1219105f0ad68955670a2a8bc9a171cefc812100ad3eba6d49c0e990bdcaa648cda97e701fbe387d6199a2e842017d64dc915643";

    static {
        JdbcTemplateMgr.getInstance().initJdbcTemplate(true);
    }

    public static void main(String[] args) throws Exception {

//        coldDeposit();

        String signHex = "82a46d73696783a67375627369679382a2706bc42044ab67fc1dec67dd22852bbbadf4313127c9b760192ba403ee9f0fcbf1584413a173c440e9799c0477895f6173acc01302b0543280444b51bcf2d456e41dcdfe3673090ce6f9a68f3a82d6b27022092e4facc31473607215243fa46834183a4e7cc3cc0a82a2706bc4200f81ea007fe7bf7e6512d516ce436453e044e21e0129795afdbbc4b30071e43fa173c440d7d44211ff9a0c15cc823cea9e861529955e65891773675d195f3941debabcdd544346b0d49db40631c1838a39fec6043a4548851c2a5f2a677d7e32a4ab180582a2706bc4203385e96803376a4e2a897df0bdb4c0f386cf23ab84bc5326a6bae0c7a33bcdafa173c44000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000a374687202a17601a374786e8aa3616d74d2ff7d5d31a3666565cd03e8a26676ce007ef564a367656eac746573746e65742d76312e30a26768c4204863b518a4b3c84ec810f22d4f1081cb0f71f059a7ac20dec62f7f70e5093a22a26c76ce007ef94ca46e6f7465c407636f6c64207478a3726376c420282c8982e6a1402dfe6b22e6bb76136cee3beac6d2d9214f1cf07eebee55426ba3736e64c420b2f468364e3d5603534b";

        //        SignedTransaction signedTx = Encoder.decodeFromMsgPack(Hex.decode(signHex), SignedTransaction.class);

        String id = RpcClient.getV2Client().RawTransaction().rawtxn(Hex.decode(signHex)).execute().body().txId;
        System.out.println(id);
        System.out.println(33);

    }


    public static void maind(String[] args) throws Exception {

        coldDeposit(AddressDao.getColdAddressList("algo"));

    }


    static void coldDeposit(List<String> coldAddressList) throws Exception {
        AlgodClient client = RpcClient.getV2Client();
        Account accountMiner = new Account(Hex.decode(privateKeyStr));
        Address minerAddress = accountMiner.getAddress();
        com.algorand.algosdk.v2.client.model.Account accountInfo = client.AccountInformation(minerAddress).execute().body();
        BigDecimal minerBalance = AlgoConverter.toAlgos(BigInteger.valueOf(accountInfo.amount));
        if (minerBalance.compareTo(new BigDecimal(10)) <= 0) {
            log.warn("no balance {}" + minerBalance);
            return;
        }
        // Construct the transaction
        int index = 0;
        for (String coldAddress : coldAddressList) {
            String note = "cold deposit " + index++;
            TransactionParametersResponse params = client.TransactionParams().execute().body();
            Transaction txn = Transaction.PaymentTransactionBuilder()
                    .sender(minerAddress)
                    .note(note.getBytes())
                    .amount(RandomUtil.intRandom(1000000, 5000000))
                    .receiver(new Address(coldAddress))
                    .suggestedParams(params)
                    .build();
            SignedTransaction signedTxn = accountMiner.signTransaction(txn);

            byte[] encodedTxBytes = Encoder.encodeToMsgPack(signedTxn);
            String id = client.RawTransaction().rawtxn(encodedTxBytes).execute().body().txId;
            log.info("send txid {}", id);
        }

    }


}
