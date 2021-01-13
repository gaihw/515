package com.atom.dex;

import com.atom.dex.api.client.domain.jsonrpc.BlockInfoResult;
import com.atom.dex.api.client.test.AtomApiRestClient;
import com.atom.dex.rpc.RpcClient;
import com.google.common.collect.Lists;
import org.bouncycastle.util.encoders.Hex;

import java.util.List;

public class TestAtom {

    public static void main(String[] args) {





        if(1==1){

            System.out.println(":FJlsk");
        }
//        AtomApiRestClient client = RpcClient.getNodeClient();
//
//        List<BlockInfoResult.Transaction> list = client.getBlockTransactions(42680L);
//        System.out.println("JFL");


        List aa = Lists.newArrayList(1,2,3,4);
        aa.remove(1);
        System.out.println(aa);
        System.out.println("fse");



        List<String> avilableFromAddressSet = Lists.newArrayList("a","b","c");
        List<Integer> avilableBalance = Lists.newArrayList(5,6,7);
        List<WalletSendRecord> waitingSendList = Lists.newArrayList();
        waitingSendList.add(new WalletSendRecord(1,1));
        waitingSendList.add(new WalletSendRecord(2,2));
        waitingSendList.add(new WalletSendRecord(3,3));
        waitingSendList.add(new WalletSendRecord(4,4));
        waitingSendList.add(new WalletSendRecord(5,5));
        waitingSendList.add(new WalletSendRecord(6,6));
        waitingSendList.add(new WalletSendRecord(7,7));

        int index =0;
        for (String fromAddress : avilableFromAddressSet) {

            List<WalletSendRecord> toRemoveList = Lists.newArrayList();
            for (WalletSendRecord walletSendRecord : waitingSendList) {


                if (walletSendRecord.fee ==7 ||walletSendRecord.fee==1 ) {
                    // fee 高的移除
                    toRemoveList.add(walletSendRecord);
                    continue;
                }

                if(avilableBalance.get(index)>walletSendRecord.fee){
                    continue;
                }



                System.out.println("send "+fromAddress+ " " +walletSendRecord.id);

                    toRemoveList.add(walletSendRecord);

                index++;

            }

            for (WalletSendRecord record : toRemoveList) {
                boolean result = waitingSendList.removeIf(item -> record.id == item.id);
                if (!result) {

                    throw new RuntimeException("erc20剔除已处理提币交易失败");
                }
            }
        }
    }


}

class WalletSendRecord{
    public WalletSendRecord(int id,int fee ){
        this.id = id;
        this.fee = fee;
    }
    public int id;
    public int fee;
}
