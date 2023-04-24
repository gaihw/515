package lambdaPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/1/10 16:09
 */
public class Test5 {
    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2222");
        arrayList.add("333");
        arrayList.add("44444444");

        List l = arrayList.stream().filter(s -> "1".equalsIgnoreCase(s)).collect(Collectors.toList());
        System.out.println(l);
    }
}
