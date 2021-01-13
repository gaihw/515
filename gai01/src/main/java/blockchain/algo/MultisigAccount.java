package blockchain.algo;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Digest;
import com.algorand.algosdk.crypto.Ed25519PublicKey;
import com.algorand.algosdk.crypto.MultisigAddress;
import org.bouncycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.List;

/**
 * generate a multisignature account
 */
public class MultisigAccount {

    public static void main(String args[]) throws Exception {

//        Account 1 Address: L6HWJ2DEPNDHQY2HKJMACFNZAEZVW7Z2ADLVY35T7QAYDD666WZQLZYVMQ
//        Account 2 Address: 5K2RMBUZZ3RX3O4IQUG2V7WJ5GFKXLSZSERUMQWBP5WSZHQK6I65RXBUBQ
//        Account 3 Address: EZK2HYVUSR6IK4W2YOBIB6SBMZCP6SEUQ5KRQGBAA6PGYQCK3ZPWIQXOB4
//        Multisig Address: 7Y3NSG6ORANBSZHPFTM43HRLTNVMP4XNFZ476VU66WGNTDW64H6X7QR5IM

        String priv_1 = "bd2fbb92babbc2297557090819531ebd206caae1ca682b9bde5cbfc96c379f6c";
        String priv_2 = "7641c2de1dc5443280e2634af398080249cc22ca2d9cb61e55fdb66261c56829";
        String priv_3 = "4d73360b4db9cc1da96f146213052d6e6272369067827530d7374f7d0d818e0f";
        Account acct1 = new Account(Hex.decode(priv_1));
        Account acct2 = new Account(Hex.decode(priv_2));
        Account acct3 = new Account(Hex.decode(priv_3));
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
