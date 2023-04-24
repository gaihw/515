package lambdaPackage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 通过max、min方法，可以获取集合中最大、最小的对象
 *
 * @author gaihw
 * @date 2023/1/10 16:31
 */
public class Test7 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("2");
        }};

        System.out.println(list.stream().max(Comparator.comparingInt(s -> Integer.valueOf(s))).get());

        System.out.println(list.stream().min(Comparator.comparing(s -> Integer.valueOf(s))).get());

        System.out.printf("Original List : %s, filtered list : %s %n","strList", "filtered");
    }
}
