package lambdaPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/1/13 14:49
 */
public class Test9 {
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);

        arrayList.forEach(l -> add(l,l));

        System.out.println("------------");

        arrayList.stream().forEach(l -> add(l,l+1));

        System.out.println("------------");

        new Thread(() -> System.out.println("I love Java.")).start();



    }

    public static void add(int a,int b){
        System.out.println(a+b);
    }
}
