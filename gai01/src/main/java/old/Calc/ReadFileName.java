package old.Calc;

import java.io.File;
import java.util.ArrayList;

public class ReadFileName {
    public static void getFileList() {
        File file = new File("logs/");

        File[] fileList = file.listFiles();

        ArrayList<String> arrayList = new ArrayList<String>();

        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                String[] fileName = fileList[i].getName().split("\\.");
                System.out.println("文件：" + fileName[0]);
                arrayList.add(fileName[0]);
            }
//
//            if (fileList[i].isDirectory()) {
//                String fileName = fileList[i].getName();
//                System.out.println("目录：" + fileName);
//            }
        }

        System.out.println("name="+arrayList);
    }

    public static void main(String[] args) {

        getFileList();
    }
}
