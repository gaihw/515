package old.Calc;

import java.util.*;

public class AlgorithmDemo {

    public static void main(String[] args) {
//        System.out.println(palindrome("abcdllodcba"));

//        System.out.println(getIndexOf("hello papamelon",'h'));
//        System.out.println(isHello_PapaMelon("hello papamelon","helloheleeelopapapapaaaaameloooonmelonmon"));
//        System.out.println(find_left_sub_str("162360838722829306","678"));
        System.out.println(max_palindrome("acbcxdx"));

    }

    /**
     * 给定一个长度为 nn 的字符串，仅包含小写英文字母，判断是否存在长度为 KK 的回文子串。
     * 字符串中任意个连续的字符组成的子序列称为该字符串的子串
     * @param str
     */
    public static List max_palindrome(String str){
        List result = new ArrayList();
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j <= i; j++) {
                String tmp= str.substring(j,str.length()-i+j);
//                System.out.println(tmp);
                if (tmp.length()<=2){
                    continue;
                }
                if(palindrome(tmp)) {
                    result.add(tmp);
                }
            }
        }
        return result;
    }

    /**
     * 要在 a 寻找一个连续的子串 ss，使得 ss 包含 b 中出现的所有字符，且要保证 ss 的长度尽可能短。如果存在多个符合上述条件的 ss，返回最靠左的那个
     * @param a
     * @param b
     * @return
     */
    public static Set find_left_sub_str(String a,String b){
        List b_l = new ArrayList();
        Collections.addAll(b_l,b.split(""));
        char[] a_c = a.toCharArray();

        //定义SET集合，元素类型为LIST，并且自定义比较器，如果两个列表的首位元素位置相同， 即为相同的list
        Set<List> result = new TreeSet<List>(new Comparator<List>() {
            @Override
            public int compare(List o1, List o2) {
                return (int)o1.get(0)-(int)o2.get(0);
            }
        });
        int start = 0;
        List tmp = new ArrayList();
        for (int i = start; i < a_c.length; i++) {
            for (int j = i; j < a_c.length; j++) {
                int index = b_l.indexOf(String.valueOf(a_c[j]));
                if (index == -1) {
                    continue;
                } else {
                    tmp.add(j);
                    b_l.remove(index);
                }
                if (b_l.size() == 0) {
                    start += 1;
                    result.add(tmp);
                    tmp = new ArrayList();
                    Collections.addAll(b_l,b.split(""));
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 判断字符串data，组合为指定的target字符串
     * @param target
     * @param data
     * @return
     */
    public static String isHello_PapaMelon(String target,String data){
        int data_len = data.length();
        int target_len = target.length();
//        根据目标字符串和测试数据，创建data_len/target_len+1行，target_len列的二维数组
        int row = data_len/target_len+1;
        int col = target_len;
        String str = "";
        char[][] data_char = new char[row][col];

        for (int i = 0; i < data_len; i++) {
            char tmp = data.charAt(i);
//            判断字符tmp是否在target中
//            if (target.indexOf(tmp) == -1){
//                return false;
//            }

            List<Integer> list = getIndexOf(target,tmp);
            for1 : for (int k = 0; k < row; k++) {
                for (int j = 0; j < list.size(); j++) {
                    int j1 = list.get(j);
                    //如果字符对应位置的最后一行最后一列有值，那么存入str中
                    if (data_char[row-1][list.get(list.size()-1)] == tmp){
                        str = str+tmp;
                        break for1;
                    }
                    //如果字符对应的位置，无值，那么在该位置赋值
                    if (data_char[k][j1] == '\u0000'){
                        data_char[k][j1] = tmp;
                        break for1;
                    }
                }
            }
        }
        //输出
        String result = "";
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < target_len; j++) {
                if (data_char[i][j] == '\u0000'){
                    result = result + "";
                }else {
                    result = result + data_char[i][j];
                }
            }
            result = result + "\r\n";
        }
        result = result + str;
        return result;
    }

    /**
     * 查找字符c，在字符串str中出现的位置
     * @param str
     * @param c
     * @return
     */
    public static List getIndexOf(String str, char c){
        List list = new ArrayList();
        int index = str.indexOf(c);
        if (index == -1){
            return list;
        }
        list.add(index);
        while (index != -1){
            index = str.indexOf(c,index+1);
            if (index == -1){
                continue;
            }
            list.add(index);
        }
        return list;
    }

    /**
     * 回文
     * @param palin
     * @return
     */
    public static boolean palindrome(String palin){
        int palin_len = palin.length();
        char[] palin_char = palin.toCharArray();
        for (int i = 0; i <= palin_len/2; i++) {
            if (palin.charAt(i) != palin.charAt(palin_len-i-1)){
                return false;
            }
        }
        return true;
    }
}
