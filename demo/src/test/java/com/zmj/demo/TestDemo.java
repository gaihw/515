package com.zmj.demo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestDemo {

    @Test
    public void test1(){

        String val = "";
        Random random = new Random();
        for ( int i = 0; i < "Tx9C7XoB0cXftHbaU8Tl".length(); i++ )
        {
            String str = random.nextInt( 2 ) % 2 == 0 ? "num" : "char";
            if ( "char".equalsIgnoreCase( str ) )
            { // 产生字母
                int nextInt = random.nextInt( 2 ) % 2 == 0 ? 65 : 97;
                // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                val += (char) ( nextInt + random.nextInt( 26 ) );
            }
            else if ( "num".equalsIgnoreCase( str ) )
            { // 产生数字
                val += String.valueOf( random.nextInt( 10 ) );
            }
        }
        System.out.println(val);
    }

    @Test
    public void test2(){
        List list = new ArrayList<>();
        list.add("fadsfa");
        list.add(1);
        list.add("vvv");
        list.add("373");
        list.add("vvv");
        list.add("vvv");
        list.add("37443");
        list.add(333373);
        list.add(333373);
        list.add(333373);
        list.add("vvv");
        list.add(333373);
        System.out.println(list);
        for (int i = 0; i <list.size() ; i++) { //i=3
            if (list.get(i).equals("vvv")){
                list.remove(i);// i=2 [fadsfa, 1, 373, vvv, 37443, 333373, 333373, 333373, vvv, 333373]
                i--;
            }
//            if (list.get(i).equals("373")){
//                list.remove(i);// i=2 [fadsfa, 1, vvv, 37443, 333373, 333373, 333373, vvv, 333373] i--=2-1=1
//                i--;
//            }
        }
        System.out.println(list);
    }
}
