package gai;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.util.Encoder;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import org.bouncycastle.util.encoders.Hex;

import java.security.NoSuchAlgorithmException;

public class Algo {
    public static void main(String[] args) throws Exception {
        final String ALGOD_API_ADDR = "192.168.112.89";
        final Integer ALGOD_PORT = 9080;
        final String ALGOD_API_TOKEN = "81be2652f02c9e62a32236952558fa2a6be89a2d34563351e874aee48ab24cd1";
        final String PASSPHRASE = "f57eab0948363b7b9cbf549d8196f3b499f390d2a6bb2d3f30e3a5e3f6b2f137";
        final String RECEIVER = "LGGGP6U3CJMEGOXUHM23OTD5IX5VV4RCRZNAYEAF2WYQUEW7UYAD275FFU";

        Account myAccount = new Account(Hex.decode(PASSPHRASE));
        String myAddress = myAccount.getAddress().toString();
        System.out.println("myAddress:" + myAddress);

        AlgodClient client = new AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN);

        com.algorand.algosdk.v2.client.model.Account accountInfo = client.AccountInformation(myAccount.getAddress()).execute().body();
//
        System.out.println(String.format("Account Balance: %d microAlgos", accountInfo.amount));

        // Construct the transaction
        String note = "Hello World";
        TransactionParametersResponse params = client.TransactionParams().execute().body();
         System.out.println(params.lastRound);
//        Transaction txn = Transaction.PaymentTransactionBuilder()
//                .sender(myAddress)
//                .note(note.getBytes())
//                .amount(100000)
//                .receiver(new Address(RECEIVER))
//                .suggestedParams(params)
//                .build();
//
//        System.out.println(txn);
//
//        //sign the transaction
//        SignedTransaction signedTxn = myAccount.signTransaction(txn);
//        System.out.println("Signed transaction with txid: " + signedTxn.transactionID);
////
////        //Submit the transaction
//        byte[] encodedTxBytes = Encoder.encodeToMsgPack(signedTxn);
//        String id = client.RawTransaction().rawtxn(encodedTxBytes).execute().body().txId;
//        System.out.println("Successfully sent tx with ID: " + id);
////
////
////        //Read the transaction from the blockchain
//        PendingTransactionResponse pTrx = client.PendingTransactionInformation(id).execute().body();
//        System.out.println("Transaction information (with notes): " + pTrx.toString());
//        System.out.println("Decoded note: " + new String(pTrx.txn.tx.note));
    }
}
