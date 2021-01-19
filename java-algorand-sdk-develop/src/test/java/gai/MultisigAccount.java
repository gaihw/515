package gai;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Ed25519PublicKey;
import com.algorand.algosdk.crypto.MultisigAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * generate a multisignature account
 */
public class MultisigAccount {

    public static void main(String args[]) throws Exception {

        Account acct1 = new Account();
        Account acct2 = new Account();
        Account acct3 = new Account();
        System.out.println("Account 1 Address: " + acct1.getAddress());
        System.out.println("Account 2 Address: " + acct2.getAddress());
        System.out.println("Account 3 Address: " + acct3.getAddress());

        List<Ed25519PublicKey> publicKeys = new ArrayList<>();
        publicKeys.add(acct1.getEd25519PublicKey());
        publicKeys.add(acct2.getEd25519PublicKey());
        publicKeys.add(acct3.getEd25519PublicKey());

        MultisigAddress msig = new MultisigAddress(1, 2, publicKeys);

        System.out.println("Multisig Address: " + msig.toString());

    }
}
