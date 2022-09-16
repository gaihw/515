package com.zmj.demo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.HttpUtil;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.text.DecimalFormat;

public class TestDemo {

    @Test
    public void test1() {

        String val = "";
        Random random = new Random();
        for (int i = 0; i < "Tx9C7XoB0cXftHbaU8Tl".length(); i++) {
            String str = random.nextInt(2) % 2 == 0 ? "num" : "char";
            if ("char".equalsIgnoreCase(str)) { // 产生字母
                int nextInt = random.nextInt(2) % 2 == 0 ? 65 : 97;
                // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                val += (char) (nextInt + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(str)) { // 产生数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        System.out.println(val);
    }

    @Test
    public void test2() {
        List list = new ArrayList<>();
        list.add("fadsfa");
        list.add(1);
        list.add("vvv");
        list.add("373");
        System.out.println(Double.parseDouble("144.33"));
        System.out.println(list);
        list = list.subList(1 + 1, list.size());
        System.out.println(list);

    }

    @Test
    public void test3() throws IOException {
        File mobile = new File("/Users/mac/Desktop/mobile-1660.txt");
        FileWriter fw = new FileWriter(mobile, true);
        for (int i = 0; i < 500; i++) {
            fw.write("16600000" + String.format("%03d", i) + "\n");
            fw.flush();
        }
    }

    @Test
    public void test4() throws IOException {
        File mobile = new File("/Users/mac/Desktop/user-1662.csv");
        FileWriter fw = new FileWriter(mobile, true);
        HttpUtil httpUtil = new HttpUtil();
        String url = "https://www-demo.hpx.today/v1/users/membership/sign-in";
        String param = "";
        for (int i = 0; i < 500; i++) {
            param = "{\"username\":\"16620000" + String.format("%03d", i) + "\",\"password\":\"ghw111111\"}";
            String res = httpUtil.postByJson(url, param);
            String accessToken = JSONObject.parseObject(res).getJSONObject("data").getString("accessToken");
            int rd = Math.random() > 0.5 ? 1 : 0;
            fw.write(accessToken + "," + rd + "\n");
            fw.flush();

        }
    }

    @Test
    public void test5() {
//        String s = "market_maticusdt_mark_price";
//        System.out.println(s.split("_")[1].substring(0,s.split("_")[1].length()-4));
//        System.out.println(JSONObject.parseObject("") == null);

//        System.out.println(BigDecimal.ONE.multiply(BigDecimal.valueOf(0.001)).multiply(BigDecimal.valueOf(22789.1)).divide(BigDecimal.valueOf(5)));

        System.out.println(1 * 0.001 * 22789.1 / 5);
    }

    @Test
    public void test6() {
        com.zmj.demo.HttpUtil httpUtil = new com.zmj.demo.HttpUtil();
        String authorization = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI5TmdVdkNOVzBmajIzWWV5d3pJeWNEcTRGX25RNm1xUkFuNklQajlmN3dFIn0.eyJqdGkiOiI4NmJmNDgzZS04NjlkLTQ1ZTktOTc3Yy0yNDkzZDY2NGZlMTkiLCJleHAiOjE2NjMwMzY1ODAsIm5iZiI6MCwiaWF0IjoxNjYyOTg5NzgwLCJpc3MiOiJodHRwczovL2F1dGguaXRjYXN0LmNuL2F1dGgvcmVhbG1zL2l0Y2FzdCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI1ZDU2NWZlNC0wMzNmLTQwNGUtYjYyMi1jYzg2YjE0Njk3MDYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzY3JtLXVpIiwibm9uY2UiOiJhNDMxOGI1My1kZWRkLTQ2ZWQtODFjMC1iNzg1NWUyNmEwMGUiLCJhdXRoX3RpbWUiOjE2NjI5ODk3NzYsInNlc3Npb25fc3RhdGUiOiJmYzdmYmFiMy0yYzRkLTRhNzAtOGM0OS00OTdjZDVkMWJiMjYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9zY3JtLXFhLml0Y2FzdC5jbiIsImh0dHA6Ly80Ny45My4yMjkuMjI4IiwiaHR0cDovL3Njcm0tdWF0Lml0Y2FzdC5jbiIsImh0dHBzOi8vc2NybS5pdGNhc3QuY24iLCJodHRwczovL3Njcm0tdWF0Lml0Y2FzdC5jbiIsImh0dHBzOi8vc2NybS1zZXJ2ZXIuaXRjYXN0LmNuIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZGVwYXJ0bWVudE5hbWUgZW1haWwgcHJvZmlsZSBlbXBsb3llZU51bWJlciIsImRlcGFydG1lbnROYW1lIjoiU05T5ZKo6K-i6YOoIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoi6LW1576O6Z2ZIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiemhhb21laWppbmdAaXRjYXN0LmNuIiwibG9jYWxlIjoiemgtQ04iLCJnaXZlbl9uYW1lIjoi6LW1576O6Z2ZIiwiZW1haWwiOiJ6aGFvbWVpamluZ0BpdGNhc3QuY24iLCJlbXBsb3llZU51bWJlciI6Ijk0NjkxIn0.B7GIC7Hdw27fwFsrP8dfLHdui2IqKeOC5ZG-C99RqYgFWeGBqTj6I4RPO6b_KMIWq6rMPV9vjVzKeOIZFApNNct7k9tLxYDYSK0L7P9jgHs_Vrrg52c3dxBz3gL5Nu3tuXMNuCQdQmUy8xz2W4F1_lXcFT7EAL3ORjt6hhF0Z9sDiOETLpqifijj45ZDolOJmidDIMhMDofF1xgX4R6PhrJB8c8eFwFnR5uVzQpEkE0zzDUIUlefsnleajLRPyTGjBHseXZ8vbnRAYB1-MvfnrWAxvzSDP1njVXleYJPVuny65RunEpNC2qMD9GSpAD-nkp-N52jRfuoANC1s83SyA";
        String nextVisitTime = "2022-09-12 00:00:00";
        String data = httpUtil.postByJson("https://scrm.itcast.cn/customer/list","{\"pageIndex\":1,\"pageSize\":400,\"orderBy\":null,\"where\":{\"onlineFollow\":false,\"levelList\":[],\"originTypeList\":[],\"itcastSchoolIdList\":[],\"itcastSubjectIdList\":[],\"anticipatSignupStartTime\":\"\",\"anticipatSignupEndTime\":\"\",\"lastVisitStartTime\":\"\",\"lastVisitEndTime\":\"\",\"createEndTime\":\"\",\"createStartTime\":\"\",\"nextVisitStartTime\":\"\",\"nextVisitEndTime\":\"\",\"paymentStartTime\":null,\"paymentEndTime\":null,\"experienceCourseClazzList\":[],\"itcastClazzStartTime\":null,\"itcastClazzEndTime\":null,\"originChannels\":\"\",\"appointmentOriginType\":[],\"schoolConsultation\":[],\"isSignup\":false,\"will2PublicCustomer\":true}}","{\"authorization\":\""+authorization+"\"}");
        JSONArray j = JSONObject.parseObject(data).getJSONObject("resultObject").getJSONArray("data");
        for (int i = 0; i < j.size(); i++) {
            System.out.println(httpUtil.postByJson("https://scrm.itcast.cn/customer/visitlog/add", "{\"nextVisitTime\":\""+nextVisitTime+"\",\"level\":\"" + j.getJSONObject(i).getString("level") + "\",\"content\":\"微信\",\"customerRelationshipId\":" + j.getJSONObject(i).getString("customerRelationshipId") + "}", "{\"authorization\":\""+authorization+"\"}"));

        }
    }

    @Test
    public void test7(){
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
        String token = "4ac51f77149a0654597b933cbbe7cbfc4f400a849c3d216afd7e9dbcfdb8c500";
        String header = "{\"X-Authorization\":\""+token+"\",\"Content-Type\":\"application/json\"}";
        //划转 币币->合约
        String out_url = "http://test1.123kj.top/v1/asset/transfer/out";
        String out_pa = "{\n" +
                "    \"amount\": \"100\",\n" +
                "    \"requestId\": \"F6DA5B97D8E3341D63D11CE051155F59E1411\",\n" +
                "    \"currencyId\": 6,\n" +
                "    \"toBiz\": \"CFD\"\n" +
                "}";

        com.zmj.demo.HttpUtil httpUtil = new com.zmj.demo.HttpUtil();
//        System.out.println(httpUtil.postByJson(out_url,out_pa,header));

        String open_url = "http://test1.123kj.top/v1/cfd/trade/ada/open";
        String open_pa1 = "{\n" +
                "\t\"positionType\": \"execute\",\n" +
                "\t\"quantity\": 10000,\n" +
                "\t\"direction\": \"long\",\n" +
                "\t\"leverage\": 100,\n" +
                "\t\"quantityUnit\": 1,\n" +
                "\t\"positionModel\": 1,\n" +
                "\t\"contractType\": 1\n" +
                "}";
        System.out.println(httpUtil.postByJson(open_url,open_pa1,header));
        String open_pa2 = "{\n" +
                "\t\"positionType\": \"execute\",\n" +
                "\t\"quantity\": 30,\n" +
                "\t\"direction\": \"long\",\n" +
                "\t\"leverage\": 100,\n" +
                "\t\"quantityUnit\": 1,\n" +
                "\t\"positionModel\": 1,\n" +
                "\t\"contractType\": 0\n" +
                "}";
        System.out.println(httpUtil.postByJson(open_url,open_pa2,header));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Connection conn = null;
        Statement stmt = null;
        String sql1 = "UPDATE `bib_cfd`.`user_position` set open_price=1,order_price=1 where user_id=51907505;  ";
        String sql2 = "update `bib_cfd`.`position` set open_price=1,avg_cost=1 where user_id=51907505;";

        ResultSet resultSet = null;
        try{
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 打开链接
            conn = DriverManager.getConnection("jdbc:mysql://locklevel-mysql-test1.cluster-cwgnjbpmvvmh.ap-southeast-1.rds.amazonaws.com:3306/","lltest1_master_rw","+vfxrhplu+xy!yc@jr*wz$6^b&16#4av");


            stmt = conn.createStatement();
            int update = stmt.executeUpdate(sql1);
            if (update >= 1){
                System.out.println("更新成功！");
            }


//            Thread.sleep(7000);

            update = stmt.executeUpdate(sql2);
            if (update >= 1){
                System.out.println("更新成功！");
            }


        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
            try {
                if (resultSet!=null) resultSet.close();
            }catch (SQLException se){
                se.printStackTrace();
            }
        }
    }

    @Test
    public void test8(){
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));

        com.zmj.demo.HttpUtil httpUtil = new com.zmj.demo.HttpUtil();

        String url = "https://b-47fa98e5-facd-4c12-a1cd-bc861b9192e8.mq.ap-southeast-1.amazonaws.com/api/exchanges/%2F/tfbee.test.exchange.externalLatestTicker/publish";

        String header = "{\"authorization\":\"Basic c2Fhcy1ybXEtdGVzdDpuUE9kNmV4SUdoUkZlREZwYW1s\"}";

        long time = System.currentTimeMillis();
        BigDecimal last = BigDecimal.valueOf(1234099);

        String param = "{\"vhost\":\"/\",\"name\":\"tfbee.test.exchange.externalLatestTicker\",\"properties\":{\"delivery_mode\":2,\"headers\":{}},\"routing_key\":\"\",\"delivery_mode\":\"2\",\"payload\":\"{\\\"symbolId\\\":1,\\\"eventTime\\\":"+time+",\\\"recieveTime\\\":"+time+",\\\"tickerSource\\\":\\\"BINANCE\\\",\\\"open\\\":0.3338,\\\"low\\\":0.3219,\\\"volume\\\":797297398.2,\\\"amount\\\":261551713.03,\\\"last\\\":"+last+",\\\"open\\\":0.3308}\",\"headers\":{},\"props\":{},\"payload_encoding\":\"string\"}";

        System.out.println(httpUtil.postByJson(url,param,header));

    }

    @Test
    public void test9(){
        Random random = new Random();
        int rTmp = 0;
        for (int i = 0; i < 10; i++) {
            rTmp = random.nextInt(100);
            if (rTmp >= 50) {
                System.out.println("1111==="+rTmp);
            }else {
                System.out.println("2222==="+rTmp);
            }

        }
    }

    @Test
    public void test10() {
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));

        com.zmj.demo.HttpUtil httpUtil = new com.zmj.demo.HttpUtil();

        String url = "http://test1.123kj.top/v1/cfd/trade/btc/open";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x-authorization", "e29b911ddcdbf91d6efbc54ee015c24453dc82cac4eba4153c97493480cc251d");
        String header = jsonObject.toString();
        String param = "{\n" +
                "    \"contractType\":1,\n" +
                "    \"direction\":\"short\",\n" +
                "    \"leverage\":100,\n" +
                "    \"positionModel\":\"1\",\n" +
                "    \"positionType\":\"execute\",\n" +
                "    \"quantity\":\"10000000\",\n" +
                "    \"quantityUnit\":\"1\"\n" +
                "}";
        int i = 1;
        while (true) {
            System.out.println("======第"+i+"次下单======>"+httpUtil.postByJson(url, param, header));
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }
}
