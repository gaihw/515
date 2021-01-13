package com.algo;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Ed25519PublicKey;
import com.algorand.algosdk.crypto.MultisigAddress;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.util.encoders.Hex;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;


//@Slf4j
public class MultisigAccount {

    private static Account acct1;
    private static Account acct2;

    static {
        try {

            acct1 = new Account(Hex.decode("c57ede2929116630377d8f393328ddd7be9ccbbae30d2a93e7532451fae3bb1e"));
            acct2 = new Account(Hex.decode("386bd73c109b968441b306ecde675e0630e51e4ffdd4c638f8939381b12de4ac"));
        } catch (NoSuchAlgorithmException e) {
//            log.error("", e);
        }

    }
//
//    Account 1 Address: ISVWP7A55RT52IUFFO5235BRGET4TN3ADEV2IA7OT4H4X4KYIQJ2LRCSS4
//    Account 2 Address: B6A6UAD7467X4ZIS2ULM4Q3EKPQEJYQ6AEUXSWX5XPCLGADR4Q74JV73CI
//    3051020101300506032b657004220420c57ede2929116630377d8f393328ddd7be9ccbbae30d2a93e7532451fae3bb1e81210044ab67fc1dec67dd22852bbbadf4313127c9b760192ba403ee9f0fcbf1584413
//    3051020101300506032b657004220420386bd73c109b968441b306ecde675e0630e51e4ffdd4c638f8939381b12de4ac8121000f81ea007fe7bf7e6512d516ce436453e044e21e0129795afdbbc4b30071e43f

    public static void main(String args[]) throws Exception {

        System.out.println("Account 1 Address: " + acct1.getAddress());
        System.out.println("Account 2 Address: " + acct2.getAddress());
        System.out.println(Hex.toHexString(acct1.getPrivateKeyPair().getPrivate().getEncoded()));
        System.out.println(Hex.toHexString(acct2.getPrivateKeyPair().getPrivate().getEncoded()));


        StringBuilder stringBuilder= new StringBuilder();
        for (int index = 0; index < 10; index++) {
            Account acct3 = new Account();
            List<Ed25519PublicKey> publicKeys = Lists.newArrayList(acct1.getEd25519PublicKey(),
                    acct2.getEd25519PublicKey(),
                    acct3.getEd25519PublicKey());

            MultisigAddress msig = new MultisigAddress(1, 2, publicKeys);
            stringBuilder.append("('").append(msig.toString()).append("','").append(Hex.toHexString(acct3.getClearTextPublicKey())).append("'),");
        }
        System.out.println(stringBuilder.toString());


    }


//3051020101300506032b657004220420
//    2744103e9d3777ad805796adf60582baa883be51e847a62f3e6794c7f6ed659a
//    812100
//    2ab2b96f4ef5fbc0383eae1614720ac72fc60b254a54157cb7cead6cbf897905
//
//
//2744103e9d3777ad805796adf60582baa883be51e847a62f3e6794c7f6ed659a
//
//    Ed25519 Private Key [ba:08:c1:4b:c4:bc:b8:18:cd:3d:33:4b:45:45:43:81:5c:8d:87:dc]
//    public data: 2ab2b96f4ef5fbc0383eae1614720ac72fc60b254a54157cb7cead6cbf897905


}
