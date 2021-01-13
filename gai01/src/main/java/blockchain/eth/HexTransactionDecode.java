package blockchain.eth;

import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionDecoder;

public class HexTransactionDecode {
    public static void main(String[] args) {
        String hexTransaction = "0xf86c2d85174876e80082a410943370b57c83545fffbde9002afbdb994e5ffed7be872386f26fc100008081eea008bb880f2c45bbce6c657d3cb1abf2c800aacc70406855910e6ec60ab7fb9786a0472f1efee4363a02237625b61406912832be06f4498e492b78bf80a5cdfa8a1b";
        RawTransaction rawTx = TransactionDecoder.decode(hexTransaction);
        System.out.println(rawTx.getData());
        System.out.println(rawTx.getTo());
        System.out.println(rawTx.getNonce());
        System.out.println(rawTx.getGasLimit());
        System.out.println(rawTx.getValue());

    }
}
