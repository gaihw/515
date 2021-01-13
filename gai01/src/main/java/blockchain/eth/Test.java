package blockchain.eth;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Test {
    public static void main(String[] args) {
//        String value = "00000000000000000000000000000000000000000000000000000000005720a9";
//        System.out.println(value.length());
//        String toHexString = Integer.toHexString(19432842);
//        String  startZeroStr = String.format("%0"+(64-toHexString.length())+"d",Integer.valueOf("0"))+toHexString;
//        System.out.println(startZeroStr);
//        000000000000000000000000000000000000000000000000000000000000000b
//        00000000000000000000000000000000000000000000000000000000005720a9
//        000000000000000000000000000000000000000000000000000000000000000b
//        0000000000000000000000000000000000000000000000000000000000000457
//        000000000000000000000000000000000000000000000000000000000128858a
//        String address = "41ABC4B9F3504EA2D5139300F12D6CA6D76073EDF2";
//        System.out.println(address.length());
//        System.out.println("TRdSDedzUkHpyxWVT9bEp7ktrV5ZYHWpgj".length());

//        for (int i = 0; i < 10; i++) {
//            System.out.println(BigInteger.valueOf((long) (Math.random()*2*Math.pow(10.0,18))));
//        }

        String aa = "a9059cbb0000000000000000000000412e8af9be5d1c81f80ddc65f00967bc5eb57401450000000000000000000000000000000000000000000000000000000000020c5b";
        System.out.println("a9059cbb0000000000000000000000412e8af9be5d1c81f80ddc65f00967bc5eb5740145".substring(30,72));
        System.out.println(aa.substring(34,74));//74
        System.out.println(BigDecimal.valueOf(Integer.parseInt(aa.substring(74),16)).divide(BigDecimal.valueOf(1000000)));
        System.out.println("0x814".substring(2));
        System.out.println(aa.substring(74).replaceAll("^(0+)", ""));

    }
}
