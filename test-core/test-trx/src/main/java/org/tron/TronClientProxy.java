package org.tron;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.deploy.config.ClientConfig;
import org.tron.api.GrpcAPI;
import org.tron.common.crypto.ECKey;
import org.tron.common.crypto.Sha256Hash;
import org.tron.common.utils.ByteArray;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;
import org.tron.walletserver.WalletClient;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TronClientProxy {

    public TronClientProxy(String url) {
        boolean isMainnet = true; // todo
        WalletClient.init(url, isMainnet);
    }

    public TronClientProxy() {
        boolean isMainnet = true;
        WalletClient.init(null,  isMainnet);
    }

    public List<Protocol.Block> getBlocks(long start, long end) {
        Optional<GrpcAPI.BlockList> list = WalletClient.getBlockByLimitNext(start, end);
        if (list.isPresent()){
            return list.get().getBlockList();
        }else{
            return Collections.emptyList();
        }

    }

    public Protocol.Block getBlock(long height){
        return WalletClient.getBlock(height);

    }

    public Protocol.Transaction createTransfer (String from, String to, BigDecimal amountInTrx){
        byte[] fromBytes = WalletClient.decodeFromBase58Check(from);
        byte[] toBytes = WalletClient.decodeFromBase58Check(to);
        long amountInDrop = TronUtil.trxToDrop(amountInTrx);
        return createTransaction(fromBytes, toBytes, amountInDrop);
    }

    public String signTx(Protocol.Transaction tx, String privateKey) throws InvalidProtocolBufferException {
        byte[] privateBytes = ByteArray.fromHexString(privateKey);
        byte[] transactionBytes = tx.toByteArray();
        byte[] hexBytes = signTransaction2Byte(transactionBytes, privateBytes);
        return ByteArray.toHexString(hexBytes);
    }

    public boolean submit(String hex) throws InvalidProtocolBufferException {
        return WalletClient.broadcastTransaction(ByteArray.fromHexString(hex));
    }

    public long getCurrentHeight(){
        return WalletClient.getBlock(-1).getBlockHeader().getRawData().getNumber();
    }

    public BigDecimal getAccountBalance(String address){
        Protocol.Account account = WalletClient.queryAccount(WalletClient.decodeFromBase58Check(address));
        long drop = account.getBalance();
        return TronUtil.dropToTrx(drop);
    }

//   -   -   -   -   -

    private Protocol.Transaction createTransaction(byte[] from, byte[] to, long amount) {
        Protocol.Transaction.Builder transactionBuilder = Protocol.Transaction.newBuilder();
        Protocol.Block newestBlock = WalletClient.getBlock(-1);

        Protocol.Transaction.Contract.Builder contractBuilder = Protocol.Transaction.Contract.newBuilder();
        Contract.TransferContract.Builder transferContractBuilder = Contract.TransferContract
                .newBuilder();
        transferContractBuilder.setAmount(amount);
        ByteString bsTo = ByteString.copyFrom(to);
        ByteString bsOwner = ByteString.copyFrom(from);
        transferContractBuilder.setToAddress(bsTo);
        transferContractBuilder.setOwnerAddress(bsOwner);
        try {
            Any any = Any.pack(transferContractBuilder.build());
            contractBuilder.setParameter(any);
        } catch (Exception e) {
            return null;
        }
        contractBuilder.setType(Protocol.Transaction.Contract.ContractType.TransferContract);
        transactionBuilder.getRawDataBuilder().addContract(contractBuilder)
                .setTimestamp(System.currentTimeMillis())
                .setExpiration(newestBlock.getBlockHeader().getRawData().getTimestamp() + 10 * 60 * 60 * 1000);
        Protocol.Transaction transaction = transactionBuilder.build();
        Protocol.Transaction refTransaction = setReference(transaction, newestBlock);
        return refTransaction;
    }

    private Protocol.Transaction setReference(Protocol.Transaction transaction, Protocol.Block newestBlock) {
        long blockHeight = newestBlock.getBlockHeader().getRawData().getNumber();
        byte[] blockHash = getBlockHash(newestBlock).getBytes();
        byte[] refBlockNum = ByteArray.fromLong(blockHeight);
        Protocol.Transaction.raw rawData = transaction.getRawData().toBuilder()
                .setRefBlockHash(ByteString.copyFrom(ByteArray.subArray(blockHash, 8, 16)))
                .setRefBlockBytes(ByteString.copyFrom(ByteArray.subArray(refBlockNum, 6, 8)))
                .build();
        return transaction.toBuilder().setRawData(rawData).build();
    }

    private Sha256Hash getBlockHash(Protocol.Block block) {
        return Sha256Hash.of(block.getBlockHeader().getRawData().toByteArray());
    }


    private byte[] signTransaction2Byte(byte[] transaction, byte[] privateKey)
            throws InvalidProtocolBufferException {
        ECKey ecKey = ECKey.fromPrivate(privateKey);
        Protocol.Transaction transaction1 = Protocol.Transaction.parseFrom(transaction);
        byte[] rawdata = transaction1.getRawData().toByteArray();
        byte[] hash = Sha256Hash.hash(rawdata);
        byte[] sign = ecKey.sign(hash).toByteArray();
        return transaction1.toBuilder().addSignature(ByteString.copyFrom(sign)).build().toByteArray();
    }

    private Protocol.Transaction signTransaction2Object(byte[] transaction, byte[] privateKey)
            throws InvalidProtocolBufferException {
        ECKey ecKey = ECKey.fromPrivate(privateKey);
        Protocol.Transaction transaction1 = Protocol.Transaction.parseFrom(transaction);
        byte[] rawdata = transaction1.getRawData().toByteArray();
        byte[] hash = Sha256Hash.hash(rawdata);
        byte[] sign = ecKey.sign(hash).toByteArray();
        return transaction1.toBuilder().addSignature(ByteString.copyFrom(sign)).build();
    }

}
