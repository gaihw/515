package org.tron.walletserver;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.tron.api.GrpcAPI;
import org.tron.api.GrpcAPI.BlockList;
import org.tron.common.crypto.ECKey;
import org.tron.common.crypto.Sha256Hash;
import org.tron.common.utils.Base58;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.TransactionUtils;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;
import org.tron.protos.Protocol.Account;
import org.tron.protos.Protocol.Block;
import org.tron.protos.Protocol.Transaction;
import org.tron.walletserver.Parameter.CommonConstant;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.spongycastle.util.encoders.Hex;

import java.util.Optional;


@Slf4j
public class WalletClient {

  private static byte addressPreFixByte = CommonConstant.ADD_PRE_FIX_BYTE_MAINNET;

  public static GrpcClient getRpcCli() {
    return rpcCli;
  }

  private static GrpcClient rpcCli ;


  public static void init(String fullNode,boolean isMainnet) {
    if (isMainnet) {
      WalletClient.setAddressPreFixByte(CommonConstant.ADD_PRE_FIX_BYTE_MAINNET);
    } else {
      WalletClient.setAddressPreFixByte(CommonConstant.ADD_PRE_FIX_BYTE_TESTNET);
    }
    rpcCli = new GrpcClient(fullNode);
  }

  public static byte getAddressPreFixByte() {
    return addressPreFixByte;
  }

  public static void setAddressPreFixByte(byte addressPreFixByte) {
    WalletClient.addressPreFixByte = addressPreFixByte;
  }


  public static Account queryAccount(byte[] address) {
    return rpcCli.queryAccount(address);//call rpc
  }


  public static boolean broadcastTransaction(byte[] transactionBytes)
      throws InvalidProtocolBufferException {
    Transaction transaction = Transaction.parseFrom(transactionBytes);
    return TransactionUtils.validTransaction(transaction)
        && rpcCli.broadcastTransaction(transaction);
  }

  public static Block getBlock(long blockNum) {
    return rpcCli.getBlock(blockNum);
  }

  public static boolean addressValid(byte[] address) {
    if (ArrayUtils.isEmpty(address)) {
      return false;
    }
    if (address.length != CommonConstant.ADDRESS_SIZE) {
      return false;
    }
    byte preFixbyte = address[0];
    return preFixbyte == WalletClient.getAddressPreFixByte();
  }

  public static String encode58Check(byte[] input) {
    byte[] hash0 = Sha256Hash.hash(input);
    byte[] hash1 = Sha256Hash.hash(hash0);
    byte[] inputCheck = new byte[input.length + 4];
    System.arraycopy(input, 0, inputCheck, 0, input.length);
    System.arraycopy(hash1, 0, inputCheck, input.length, 4);
    return Base58.encode(inputCheck);
  }

  private static byte[] decode58Check(String input) {
    byte[] decodeCheck = Base58.decode(input);
    if (decodeCheck.length <= 4) {
      return null;
    }
    byte[] decodeData = new byte[decodeCheck.length - 4];
    System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
    byte[] hash0 = Sha256Hash.hash(decodeData);
    byte[] hash1 = Sha256Hash.hash(hash0);
    if (hash1[0] == decodeCheck[decodeData.length] &&
        hash1[1] == decodeCheck[decodeData.length + 1] &&
        hash1[2] == decodeCheck[decodeData.length + 2] &&
        hash1[3] == decodeCheck[decodeData.length + 3]) {
      return decodeData;
    }
    return null;
  }

  public static byte[] decodeFromBase58Check(String addressBase58) {
    if (StringUtils.isEmpty(addressBase58)) {
      return null;
    }
    byte[] address = decode58Check(addressBase58);
    if (!addressValid(address)) {
      return null;
    }
    return address;
  }

  public static Optional<BlockList> getBlockByLimitNext(long start, long end) {
    return rpcCli.getBlockByLimitNext(start, end);
  }

  public static Contract.TriggerSmartContract triggerCallContract(byte[] address,
                                                                  byte[] contractAddress,
                                                                  long callValue, byte[] data) {
    Contract.TriggerSmartContract.Builder builder = Contract.TriggerSmartContract.newBuilder();
    builder.setOwnerAddress(ByteString.copyFrom(address));
    builder.setContractAddress(ByteString.copyFrom(contractAddress));
    builder.setData(ByteString.copyFrom(data));
    builder.setCallValue(callValue);
    return builder.build();
  }




  public static Transaction triggerContract(byte[] owner,byte[] contractAddress, long callValue, byte[] data, long feeLimit){

    Contract.TriggerSmartContract triggerContract = triggerCallContract(owner, contractAddress,
            callValue, data);
    GrpcAPI.TransactionExtention transactionExtention = rpcCli.triggerContract(triggerContract);

    if (transactionExtention == null || !transactionExtention.getResult().getResult()) {
      System.out.println("RPC create call trx failed!");
      System.out.println("Code = " + transactionExtention.getResult().getCode());
      System.out
              .println("Message = " + transactionExtention.getResult().getMessage().toStringUtf8());
      return null;
    }


    Protocol.Block newestBlock = WalletClient.getBlock(-1);
    Transaction transaction = transactionExtention.getTransaction();
    transaction.getRawData().toBuilder()
            .setTimestamp(System.currentTimeMillis())
            .setExpiration(newestBlock.getBlockHeader().getRawData().getTimestamp() + 10 * 1000);


    if (transaction.getRetCount() != 0 &&
            transactionExtention.getConstantResult(0) != null &&
            transactionExtention.getResult() != null) {
      byte[] result = transactionExtention.getConstantResult(0).toByteArray();
      System.out.println("message:" + transaction.getRet(0).getRet());
      System.out.println(":" + ByteArray
              .toStr(transactionExtention.getResult().getMessage().toByteArray()));
      System.out.println("Result:" + Hex.toHexString(result));
      return transactionExtention.getTransaction();
    }

    GrpcAPI.TransactionExtention.Builder texBuilder = GrpcAPI.TransactionExtention.newBuilder();

    Transaction.Builder transBuilder = Transaction.newBuilder();
    Transaction.raw.Builder rawBuilder = transactionExtention.getTransaction().getRawData()
            .toBuilder();
    rawBuilder.setFeeLimit(feeLimit);
    transBuilder.setRawData(rawBuilder);
    for (int i = 0; i < transactionExtention.getTransaction().getSignatureCount(); i++) {
      ByteString s = transactionExtention.getTransaction().getSignature(i);
      transBuilder.setSignature(i, s);
    }
    for (int i = 0; i < transactionExtention.getTransaction().getRetCount(); i++) {
      Transaction.Result r = transactionExtention.getTransaction().getRet(i);
      transBuilder.setRet(i, r);
    }
    texBuilder.setTransaction(transBuilder);
    texBuilder.setResult(transactionExtention.getResult());
    texBuilder.setTxid(transactionExtention.getTxid());
    transactionExtention = texBuilder.build();

    return processTransactionExtention(transactionExtention);
  }

  private static Transaction processTransactionExtention(GrpcAPI.TransactionExtention transactionExtention){
    GrpcAPI.TransactionExtention lastTransactionExtention = transactionExtention;
    if (transactionExtention == null) {
      return null;
    }
    GrpcAPI.Return ret = transactionExtention.getResult();
    if (!ret.getResult()) {
      System.out.println("Code = " + ret.getCode());
      System.out.println("Message = " + ret.getMessage().toStringUtf8());
      return null;
    }
    Transaction transaction = transactionExtention.getTransaction();
    if (transaction == null || transaction.getRawData().getContractCount() == 0) {
      System.out.println("Transaction is empty");
      return null;
    }
    // ByteArray.toHexString(transactionExtention.getTxid().toByteArray());
    return transaction;

//    System.out.println(
//            "Receive unsigned txid = " + ByteArray
//                    .toHexString(transactionExtention.getTxid().toByteArray()));
//    transaction = signTransaction(null,transaction);
//    return rpcCli.broadcastTransaction(transaction);
  }

  private static Transaction signTransaction(ECKey ecKey,Transaction transaction) {
    if (transaction.getRawData().getTimestamp() == 0) {
      transaction = TransactionUtils.setTimestamp(transaction);
    }

    System.out.println(
            "Signed txid = " + ByteArray
                    .toHexString(Sha256Hash.hash(transaction.getRawData().toByteArray())));
    transaction = TransactionUtils.sign(transaction, ecKey);
    return transaction;
  }

}
