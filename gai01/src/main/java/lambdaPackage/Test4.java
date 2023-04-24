package lambdaPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 集合的遍历forEach方法
 *
 * @author gaihw
 * @date 2023/1/9 17:48
 */
public class Test4 {
    public static void main(String[] args) {

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2222");
        arrayList.add("333");
        arrayList.add("44444444");

        System.out.println(arrayList);

        System.out.println("----------");

        System.out.println(arrayList.stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList()));

        System.out.println("----------");

        System.out.println(arrayList.stream().collect(Collectors.toList()));

        System.out.println("----------");

        System.out.println(arrayList.stream().collect(Collectors.joining(",")));

        System.out.println("----------");

        Set l = arrayList.stream().map(s -> s + "aaa" + s).collect(Collectors.toSet());
        System.out.println(l);
    }


}
