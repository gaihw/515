package com.zmj.demo;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.HttpUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.text.DecimalFormat;

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
        System.out.println(Double.parseDouble("144.33"));
        System.out.println(list);
        list = list.subList(1+1,list.size());
        System.out.println(list);

    }
    
    @Test
    public void test3() throws IOException {
        File mobile = new File("/Users/mac/Desktop/mobile.txt");
        FileWriter fw = new FileWriter(mobile,true);
        for (int i = 0; i < 500; i++) {
            fw.write("16610000"+String.format("%03d",i)+"\n");
            fw.flush();
        }
    }

    @Test
    public void test4() throws IOException {
        File mobile = new File("/Users/mac/Desktop/user.csv");
        FileWriter fw = new FileWriter(mobile,true);
        HttpUtil httpUtil = new HttpUtil();
        String url = "https://www-demo.hpx.today/v1/users/membership/sign-in";
        String param = "";
        for (int i = 0; i < 500; i++) {
            param = "{\"username\":\"16600000"+String.format("%03d",i)+"\",\"password\":\"ghw111111\"}";
            String res = httpUtil.postByJson(url,param);
            String accessToken = JSONObject.parseObject(res).getJSONObject("data").getString("accessToken");
            int rd=Math.random()>0.5?1:0;
            fw.write(accessToken+","+rd+"\n");
            fw.flush();

        }
    }

    @Test
    public void test5(){
        String s = "market_maticusdt_mark_price";
        System.out.println(s.split("_")[1].substring(0,s.split("_")[1].length()-4));
        System.out.println(JSONObject.parseObject("") == null);
    }
}
