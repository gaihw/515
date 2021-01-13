package com.test.util;


import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 *
 * @author wenqi5
 *
 */
public class GZIPUtils {


    public static String compressStr(String unzipString) {
        /**
         *     https://www.yiibai.com/javazip/javazip_deflater.html#article-start
         *     0 ~ 9 压缩等级 低到高
         *     public static final int BEST_COMPRESSION = 9;            最佳压缩的压缩级别。
         *     public static final int BEST_SPEED = 1;                  压缩级别最快的压缩。
         *     public static final int DEFAULT_COMPRESSION = -1;        默认压缩级别。
         *     public static final int DEFAULT_STRATEGY = 0;            默认压缩策略。
         *     public static final int DEFLATED = 8;                    压缩算法的压缩方法(目前唯一支持的压缩方法)。
         *     public static final int FILTERED = 1;                    压缩策略最适用于大部分数值较小且数据分布随机分布的数据。
         *     public static final int FULL_FLUSH = 3;                  压缩刷新模式，用于清除所有待处理的输出并重置拆卸器。
         *     public static final int HUFFMAN_ONLY = 2;                仅用于霍夫曼编码的压缩策略。
         *     public static final int NO_COMPRESSION = 0;              不压缩的压缩级别。
         *     public static final int NO_FLUSH = 0;                    用于实现最佳压缩结果的压缩刷新模式。
         *     public static final int SYNC_FLUSH = 2;                  用于清除所有未决输出的压缩刷新模式; 可能会降低某些压缩算法的压缩率。
         */

        //使用指定的压缩级别创建一个新的压缩器。
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        //设置压缩输入数据。
        deflater.setInput(unzipString.getBytes());
        //当被调用时，表示压缩应该以输入缓冲区的当前内容结束。
        deflater.finish();

        final byte[] bytes = new byte[256];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);

        while (!deflater.finished()) {
            //压缩输入数据并用压缩数据填充指定的缓冲区。
            int length = deflater.deflate(bytes);
            outputStream.write(bytes, 0, length);
        }
        //关闭压缩器并丢弃任何未处理的输入。
        deflater.end();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }


    /**
     * 解压缩
     */
    public static String unCompressStr(String zipString) {
        byte[] decode = Base64.getDecoder().decode(zipString);
        //创建一个新的解压缩器  https://www.yiibai.com/javazip/javazip_inflater.html

        Inflater inflater = new Inflater();
        //设置解压缩的输入数据。
        inflater.setInput(decode);
        final byte[] bytes = new byte[256];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);
        try {
            //finished() 如果已到达压缩数据流的末尾，则返回true。
            while (!inflater.finished()) {
                //将字节解压缩到指定的缓冲区中。
                int length = inflater.inflate(bytes);
                outputStream.write(bytes, 0, length);
            }
        } catch (DataFormatException e) {
            e.printStackTrace();
            return null;
        } finally {
            //关闭解压缩器并丢弃任何未处理的输入。
            inflater.end();
        }

        return outputStream.toString();
    }






    public static void main(String[] args) throws Exception {
        String str =
                "[{\"outputList\":[]},{\"hex\":\"0100000001dbc02a1c69b1a9398846304fe1f27de540ee076cfc3413bd521bb7d8310248100000000000ffffffff0318e5f3050000000017a9143c074041c47f1c3a135b6f4e3db322f7c7939f68870000000000000000166a146f6d6e690000000080000003000000000000000b22020000000000001976a914e39141a55c46369c2ea9d10af237a3588bbc344488ac00000000\",\"outputList\":[{\"redeemScript\":\"52210322900f21c311f631585bebd222de4dc7ace1708899f51e6ea77861c4de906ebd210392f3e036935e0d06ca898f99a3cede5a62262e305155931b63b4df16ca0c8312210265f3b6148d34bda9df318b11d5619b1e3b264d98a90d7da907c3475caf12adea53ae\",\"scriptPubKey\":\"a9143c074041c47f1c3a135b6f4e3db322f7c7939f6887\",\"txId\":\"10480231d8b71b52bd1334fc6c07ee40e57df2e14f30468839a9b1691c2ac0db\",\"vOut\":0}]}][{\"outputList\":[]},{\"hex\":\"0100000001dbc02a1c69b1a9398846304fe1f27de540ee076cfc3413bd521bb7d8310248100000000000ffffffff0318e5f3050000000017a9143c074041c47f1c3a135b6f4e3db322f7c7939f68870000000000000000166a146f6d6e690000000080000003000000000000000b22020000000000001976a914e39141a55c46369c2ea9d10af237a3588bbc344488ac00000000\",\"outputList\":[{\"redeemScript\":\"52210322900f21c311f631585bebd222de4dc7ace1708899f51e6ea77861c4de906ebd210392f3e036935e0d06ca898f99a3cede5a62262e305155931b63b4df16ca0c8312210265f3b6148d34bda9df318b11d5619b1e3b264d98a90d7da907c3475caf12adea53ae\",\"scriptPubKey\":\"a9143c074041c47f1c3a135b6f4e3db322f7c7939f6887\",\"txId\":\"10480231d8b71b52bd1334fc6c07ee40e57df2e14f30468839a9b1691c2ac0db\",\"vOut\":0}]}][{\"outputList\":[]},{\"hex\":\"0100000001dbc02a1c69b1a9398846304fe1f27de540ee076cfc3413bd521bb7d8310248100000000000ffffffff0318e5f3050000000017a9143c074041c47f1c3a135b6f4e3db322f7c7939f68870000000000000000166a146f6d6e690000000080000003000000000000000b22020000000000001976a914e39141a55c46369c2ea9d10af237a3588bbc344488ac00000000\",\"outputList\":[{\"redeemScript\":\"52210322900f21c311f631585bebd222de4dc7ace1708899f51e6ea77861c4de906ebd210392f3e036935e0d06ca898f99a3cede5a62262e305155931b63b4df16ca0c8312210265f3b6148d34bda9df318b11d5619b1e3b264d98a90d7da907c3475caf12adea53ae\",\"scriptPubKey\":\"a9143c074041c47f1c3a135b6f4e3db322f7c7939f6887\",\"txId\":\"10480231d8b71b52bd1334fc6c07ee40e57df2e14f30468839a9b1691c2ac0db\",\"vOut\":0}]}][{\"outputList\":[]},{\"hex\":\"0100000001dbc02a1c69b1a9398846304fe1f27de540ee076cfc3413bd521bb7d8310248100000000000ffffffff0318e5f3050000000017a9143c074041c47f1c3a135b6f4e3db322f7c7939f68870000000000000000166a146f6d6e690000000080000003000000000000000b22020000000000001976a914e39141a55c46369c2ea9d10af237a3588bbc344488ac00000000\",\"outputList\":[{\"redeemScript\":\"52210322900f21c311f631585bebd222de4dc7ace1708899f51e6ea77861c4de906ebd210392f3e036935e0d06ca898f99a3cede5a62262e305155931b63b4df16ca0c8312210265f3b6148d34bda9df318b11d5619b1e3b264d98a90d7da907c3475caf12adea53ae\",\"scriptPubKey\":\"a9143c074041c47f1c3a135b6f4e3db322f7c7939f6887\",\"txId\":\"10480231d8b71b52bd1334fc6c07ee40e57df2e14f30468839a9b1691c2ac0db\",\"vOut\":0}]}][{\"outputList\":[]},{\"hex\":\"0100000001dbc02a1c69b1a9398846304fe1f27de540ee076cfc3413bd521bb7d8310248100000000000ffffffff0318e5f3050000000017a9143c074041c47f1c3a135b6f4e3db322f7c7939f68870000000000000000166a146f6d6e690000000080000003000000000000000b22020000000000001976a914e39141a55c46369c2ea9d10af237a3588bbc344488ac00000000\",\"outputList\":[{\"redeemScript\":\"52210322900f21c311f631585bebd222de4dc7ace1708899f51e6ea77861c4de906ebd210392f3e036935e0d06ca898f99a3cede5a62262e305155931b63b4df16ca0c8312210265f3b6148d34bda9df318b11d5619b1e3b264d98a90d7da907c3475caf12adea53ae\",\"scriptPubKey\":\"a9143c074041c47f1c3a135b6f4e3db322f7c7939f6887\",\"txId\":\"10480231d8b71b52bd1334fc6c07ee40e57df2e14f30468839a9b1691c2ac0db\",\"vOut\":0}]}]";
        System.out.println("原长度：" + str.length());
        String gStr =  GZIPUtils.compressStr(str);

        System.out.println("压缩后字符串：" + gStr);
        System.out.println("压缩后字符串：" + gStr.length());


        System.out.println(unCompressStr(gStr));

    }
}
