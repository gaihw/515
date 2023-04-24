package lambdaPackage;

import java.util.ArrayList;

/**
 * 集合的遍历forEach方法
 *
 * @author gaihw
 * @date 2023/1/9 17:48
 */
public class Test3 {
    public static void main(String[] args) {

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        arrayList.add("4");

        arrayList.forEach(s -> {if(s.equals("1")){
            System.out.println(s);
        }});
        System.out.println("---------");
        arrayList.forEach(s -> show(s));
    }

    public static void show(String s){
        System.out.println(s);
    }
}
