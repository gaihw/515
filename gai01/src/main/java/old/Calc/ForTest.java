package old.Calc;

import java.util.*;

public class ForTest {
    public static void main(String[] args) {

        byte b = 127;
        short s = 32767;

        List list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        //输出列表内容
        System.out.println("----------list----------");
        for (Object o: list
             ) {
            System.out.println(o);
        }

        HashMap hashMap = new HashMap();
        hashMap.put("a",1);
        hashMap.put("b",2);
        hashMap.put("c",3);

        //输出map
        System.out.println("----------map----------");
        Set set = hashMap.keySet();
        for (Object o: hashMap.keySet()
             ) {
            System.out.println(o);
            System.out.println(hashMap.get(o));
        }


        //输出数组
        System.out.println("----------arr----------");
        int[] arr = {11,3,44,55,100,2,3};
        for (int i:arr
             ) {
            System.out.print(i+",");
        }

        System.out.println();
        System.out.println("----------选择排序----------");
        //选择排序
        sort(arr);
        System.out.println();
        for (int i:arr
        ) {
            System.out.print(i+",");
        }
        System.out.println();
        System.out.println("----------冒泡排序----------");
        //冒泡排序
        sort_1(arr);
        System.out.println();
        for (int i:arr
        ) {
            System.out.print(i+",");
        }

        System.out.println();
        System.out.println("------------折半查找------------");
        //折半查找
        int[] sear = {3,44,55,67,89,90,121};
        int key = 68;
        System.out.println("halfSearch="+halfSearch(sear,key));
        System.out.println("------------折半查找,元素插入的位置------------");
        System.out.println("halfSearch_1="+halfSearch_1(sear,233));
    }

    /**
     * 选择排序
     * @param arr
     */
    public static void sort(int[] arr){
        for (int i =0 ; i < arr.length-1;i++){
            for (int j = i+1; j < arr.length; j++) {
                if (arr[j]<arr[i]){
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    /**
     * 冒泡排序
     * @param arr
     */
    public static void sort_1(int[] arr){
        for (int i =0 ; i < arr.length-1;i++){
            for (int j = 0; j < arr.length-1-i; j++) {
                if (arr[j]>arr[j+1]){
                    int temp = arr[j+1];
                    arr[j+1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }
    /**
     * 折半查找
     * @param arr
     * @param key
     * @return
     */
    public static int halfSearch(int[] arr,int key){
        int min = 0;
        int max = arr.length-1;
        int mid = (min+max)>>1;
        while (key != arr[mid]){
            if (key > arr[mid]){
                min = mid + 1;
            }else if (key < arr[mid]){
                max = mid - 1;
            }
            if (max < min){
                return -1;
            }
            mid = (min+max)>>1;
        }
        return mid;
    }

    /**
     * 折半查找插入元素的位置
     * @param arr
     * @param key
     * @return
     */
    public static int halfSearch_1(int[] arr,int key){
        int min = 0;
        int max = arr.length-1;
        int mid = 0;
        while (min<=max){
            mid = (min+max)>>1;
            if (key > arr[mid]){
                min = mid + 1;
            }else if (key < arr[mid]){
                max = mid - 1;
            }else{
                //折半查找数字应插入位置的索引
                return -mid-1;

                //折半查找数字所在的位置索引，返回的mid，代表数组中存在该元素，并且该mid即为该元素的索引，如果不存在，返回的就是下方的-1值
//                return mid;
            }
        }
        //折半查找数字应插入位置的索引
        return -min-1;

        //折半查找数字所在的位置索引，返回-1，代表数组中没有该元素
//        return -1;
    }

}
