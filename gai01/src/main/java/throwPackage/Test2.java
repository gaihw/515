package throwPackage;

import java.io.*;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/1/13 17:53
 */
public class Test2 {
    public static String file = "/Users/mac/tools/gitPro/515/gai01/src/main/java/throwPackage/test.txt";
    public static void main(String[] args) {
//        writeUser();
        readUser();
    }

    private static void readUser() {
        User u = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(file)));
            u = (User) objectInputStream.readObject();
            System.out.println(u.getCode()+"____"+u.getArgs()+"___"+u.getDefaultMessage());

        }catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void writeUser() {
        try{
            User u = new User();
            u.setCode("1");
            u.setArgs("aaa");
            u.setDefaultMessage("success");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(file)));
            objectOutputStream.writeObject(u);
            objectOutputStream.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
