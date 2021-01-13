package org.tron;

import com.google.common.primitives.Longs;
import com.google.protobuf.ByteString;
import org.tron.common.crypto.Sha256Hash;
import org.tron.common.utils.ByteArray;
import org.tron.protos.Protocol;
import org.tron.walletserver.WalletClient;

import java.math.BigDecimal;

public class TronUtil {
    private static final BigDecimal TRON_TO_DROP =  BigDecimal.valueOf(1000000L);

    /**
     * 将 trx 转化为 drop， long 类型表示。
     * <p>
     * 注意：
     * <p>
     * 1. 如果输入的精度很高，使得drop不为整数，则小数部分损失，不抛异常。
     * <p>
     * 2. 如果结果超过 long 的表示范围，抛出异常。
     */
    public static long trxToDrop(BigDecimal trx){
        if (trx==null){
            throw new RuntimeException("trxToDrop got null input");
        }
        return trx.multiply(TRON_TO_DROP).toBigInteger().longValueExact();
    }

    public static BigDecimal dropToTrx(long drop){
        return new BigDecimal(drop).divide(TRON_TO_DROP);
    }

    public static String getTransactionHash(Protocol.Transaction transaction) {
        return ByteArray.toHexString(Sha256Hash.hash(transaction.getRawData().toByteArray()));
    }

    public static String getBlockHash(Protocol.Block block) {
        Sha256Hash blockHash = Sha256Hash.of(block.getBlockHeader().getRawData().toByteArray());
        long blockNum = block.getBlockHeader().getRawData().getNumber();
        return ByteArray.toHexString(generateBlockId(blockNum, blockHash));
    }

    private static byte[] generateBlockId(long blockNum, Sha256Hash blockHash) {
        byte[] numBytes = Longs.toByteArray(blockNum);
        byte[] hash = blockHash.getBytes();
        System.arraycopy(numBytes, 0, hash, 0, 8);
        return hash;
    }

    public static boolean isValidAddress(String address){
        try{
            byte[] bytes = WalletClient.decodeFromBase58Check(address);
            return bytes != null;
        }catch(Exception ignored){
            return false;
        }
    }

    public static String parseAddress(ByteString bs){
        return WalletClient.encode58Check(bs.toByteArray());
    }

}
