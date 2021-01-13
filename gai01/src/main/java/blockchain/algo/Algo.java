package blockchain.algo;



import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.util.Encoder;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;

public class Algo {
    static {
        System.setProperty("fileName", "algo/algo.log");
    }
    public static Logger log = LoggerFactory.getLogger(Algo.class);

    final static String ALGOD_API_ADDR = "192.168.112.89";
    final static Integer ALGOD_PORT = 9080;
    final static String ALGOD_API_TOKEN = "81be2652f02c9e62a32236952558fa2a6be89a2d34563351e874aee48ab24cd1";
    final static String PASSPHRASE = "f57eab0948363b7b9cbf549d8196f3b499f390d2a6bb2d3f30e3a5e3f6b2f137";
    final static String RECEIVER = "LGGGP6U3CJMEGOXUHM23OTD5IX5VV4RCRZNAYEAF2WYQUEW7UYAD275FFU";


    public static void main(String[] args) throws Exception {

        Account myAccount = new Account(Hex.decode(PASSPHRASE));
        String myAddress = myAccount.getAddress().toString();
        System.out.println("myAddress:"+myAddress);

        AlgodClient client = new AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN);

        com.algorand.algosdk.v2.client.model.Account accountInfo = client.AccountInformation(myAccount.getAddress()).execute().body();
//
//        System.out.println(String.format("Account Balance: %d microAlgos", accountInfo.amount));

        log.info("Account Balance: {} microAlgos", accountInfo.amount);
        // Construct the transaction
        String note = "Hello World";
        TransactionParametersResponse params = client.TransactionParams().execute().body();
        log.info(params.lastRound.toString());
//        System.out.println(params.lastRound);
         Transaction txn = Transaction.PaymentTransactionBuilder()
                .sender(myAddress)
                .note(note.getBytes())
                .amount(100000)
                .receiver(new Address(RECEIVER))
                .suggestedParams(params)
                .build();

         log.info("{}",txn);
//        System.out.println(txn);

        // sign the transaction
        SignedTransaction signedTxn = myAccount.signTransaction(txn);
        log.info("Signed transaction with txid: {}" , signedTxn.transactionID);
//        System.out.println("Signed transaction with txid: " + signedTxn.transactionID);
//
//        //Submit the transaction
        byte[] encodedTxBytes = Encoder.encodeToMsgPack(signedTxn);
        String txId = client.RawTransaction().rawtxn(encodedTxBytes).execute().body().txId;
        log.info("Successfully sent tx with ID: {}" ,txId);
//        System.out.println("Successfully sent tx with ID: " + id);
//
//
//        //Read the transaction from the blockchain
        PendingTransactionResponse pTrx = client.PendingTransactionInformation(txId).execute().body();
        log.info("Transaction information (with notes): {}" , pTrx.toString());
        log.info("Decoded note: {}" ,new String(pTrx.txn.tx.note));
//        System.out.println("Transaction information (with notes): " + pTrx.toString());
//        System.out.println("Decoded note: " + new String(pTrx.txn.tx.note));


    }

    /**
     * Wait for confirmation
     * @param txID
     * @throws Exception
     */
    // utility function to wait on a transaction to be confirmed
    // public void waitForConfirmation( String txID) throws Exception{
    //    AlgodClient client = (AlgodClient) new AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN);
    //    Long lastRound = client.GetStatus().execute().body().lastRound;
//        while(true) {
//            try {
//                //Check the pending tranactions
//                Response<PendingTransactionResponse> pendingInfo = client.PendingTransactionInformation(txID).execute();
//                if (pendingInfo.body().confirmedRound != null && pendingInfo.body().confirmedRound > 0) {
//                    //Got the completed Transaction
//                    System.out.println("Transaction " + txID + " confirmed in round " + pendingInfo.body().confirmedRound);
//                    break;
//                }
//                lastRound++;
//                client.WaitForBlock(lastRound).execute();
//            } catch (Exception e) {
//                throw( e );
//            }
//        }
//    }
}
