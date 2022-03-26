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

        System.out.println(list);
        list = list.subList(1+1,list.size());
        System.out.println(list);
    }
}
