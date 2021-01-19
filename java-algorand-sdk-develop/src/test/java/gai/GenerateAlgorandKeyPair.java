package gai;

import com.algorand.algosdk.account.Account;
import org.bouncycastle.util.encoders.Hex;

import java.security.NoSuchAlgorithmException;

/**
 * generate a standalone account
 */
public class GenerateAlgorandKeyPair {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // f57eab0948363b7b9cbf549d8196f3b499f390d2a6bb2d3f30e3a5e3f6b2f137
        // VTJ45LQO5DRDBMTCSCQ6GDSYXVKUR5F2S55OG6A2MG4LOLUJTZJF6JENUY
        // Hex.toHexString(((Ed25519PrivateKeyParameters) ((BCEdDSAPrivateKey) myAccount.privateKeyPair.getPrivate()).eddsaPrivateKey).data)
        Account myAccount = new Account(Hex.decode("f57eab0948363b7b9cbf549d8196f3b499f390d2a6bb2d3f30e3a5e3f6b2f137"));
        System.out.println("My Address: " + myAccount.getAddress());
        System.out.println("My Passphrase: " + myAccount.toMnemonic());
    }
}
