package throwPackage;

import java.util.ArrayList;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/1/13 15:53
 */
public class Test1 {
    public static void main(String[] args) {
        ArrayList list = new ArrayList<>();
//        list.add(1);
//        try {
//            System.out.println(list.get(2));
//        }catch (Exception baseException){
//            throw new BaseException("0",list,"test");
//
//        }

        throw new BaseException("0",list,"test11");

    }
}
