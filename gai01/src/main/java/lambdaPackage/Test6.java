package lambdaPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/1/10 16:21
 */
public class Test6 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
        }};
        int i = list.stream()
                .map(s -> Integer.valueOf(s))
                // s1 和 s2 表示循环中的前后两个数
                .reduce((s1,s2) -> s1+s2)
                .orElse(0);
        list.stream()
                .map(s -> Integer.valueOf(s))
                // 第一个参数表示基数，会从 100 开始加
                .reduce(100,(s1,s2) -> s1+s2);

        String channel = "market_GjFt.btcusdt_ticker";
        String symbol = channel.split("_")[1].substring(5,channel.split("_")[1].length()-4);
        System.out.println(symbol);

    }
}
