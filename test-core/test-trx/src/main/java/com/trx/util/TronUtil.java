package com.trx.util;


import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.tron.common.crypto.ECKey;
import org.tron.common.crypto.Sha256Hash;
import org.tron.common.utils.ByteArray;
import org.tron.protos.Protocol;

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



    public static String signTx(Protocol.Transaction tx, String privateKey) throws InvalidProtocolBufferException {
        byte[] privateBytes = ByteArray.fromHexString(privateKey);
        byte[] transactionBytes = tx.toByteArray();
        byte[] hexBytes = signTransaction2Byte(transactionBytes, privateBytes);
        return ByteArray.toHexString(hexBytes);
    }

    private static byte[] signTransaction2Byte(byte[] transaction, byte[] privateKey)
            throws InvalidProtocolBufferException {
        ECKey ecKey = ECKey.fromPrivate(privateKey);
        Protocol.Transaction transaction1 = Protocol.Transaction.parseFrom(transaction);
        byte[] rawdata = transaction1.getRawData().toByteArray();
        byte[] hash = Sha256Hash.hash(rawdata);
        byte[] sign = ecKey.sign(hash).toByteArray();
        return transaction1.toBuilder().addSignature(ByteString.copyFrom(sign)).build().toByteArray();
    }


}
