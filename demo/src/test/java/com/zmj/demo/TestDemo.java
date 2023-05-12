package com.zmj.demo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.HttpUtil;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

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
        File mobile = new File("/Users/mac/Desktop/mobile-1663.txt");
        FileWriter fw = new FileWriter(mobile, true);
        for (int i = 0; i < 500; i++) {
            fw.write("16630000" + String.format("%03d", i) + "\n");
            fw.flush();
        }
    }

    @Test
    public void test4() throws IOException {
        File mobile = new File("/Users/mac/Desktop/user-1661.csv");
        FileWriter fw = new FileWriter(mobile, true);
        HttpUtil httpUtil = new HttpUtil();
        String url = "https://www-demo.hpx.today/v1/users/membership/sign-in";
        String param = "";
        for (int i = 0; i < 500; i++) {
            param = "{\"username\":\"16610000" + String.format("%03d", i) + "\",\"password\":\"ghw111111\"}";
            String res = httpUtil.postByJson(url, param);
            String accessToken = JSONObject.parseObject(res).getJSONObject("data").getString("accessToken");
            int rd = Math.random() > 0.5 ? 1 : 0;
            fw.write(accessToken + "," + rd + "\n");
            fw.flush();

        }
    }

    @Test
    public void test5() {
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
        com.zmj.demo.HttpUtil httpUtil = new com.zmj.demo.HttpUtil();
        String host = "https://x.chimchim.top";
        String res = "";
        String accessToken = "";
        String param = "";
        String mobile = "";
        String header = "";
        String emailCode = "";
        String smsCode = "";
        for (int i = 100; i < 500; i++) {
            mobile = "16610000" + String.format("%03d", i);
            // 登录
            param = "{\"username\":\"" + mobile + "\",\"password\":\"ghw111111\"}";
            res = httpUtil.postByJson(host+"/v1/users/membership/sign-in", param);
            System.out.println("登录--->"+res);
            accessToken = JSONObject.parseObject(res).getJSONObject("data").getString("accessToken");
            header = "{\"X-Authorization\":\""+accessToken+"\",\"Content-Type\":\"application/json\"}";

            // 发送邮箱验证码
            param = "{\"behavior\":3041,\"email\":\""+mobile+"@163.com\"}";
            res = httpUtil.postByJson(host+"/v1/users/security/messages/verification-code/email", param,header);
            System.out.println("发送邮箱验证码--->"+res);
            emailCode = getSms(0,mobile);
            System.out.println("数据库邮箱验证码--->"+emailCode);

            // 绑定邮箱验证码
            param = "{\"email\":\""+mobile+"@163.com\",\"verificationCode\":\""+emailCode+"\"}";
            res = httpUtil.postByJson(host+"/v1/users/security/email/bind-pre", param,header);
            System.out.println("绑定邮箱验证码--->"+res);

            // 发送手机验证码
            param = "{\"behavior\":3035,\"mobile\":\"\",\"areaCode\":86}";
            res = httpUtil.postByJson(host+"/v1/users/security/messages/verification-code/mobile", param,header);
            System.out.println("发送手机验证码--->"+res);

            // 查询短信验证码
            smsCode = getSms(1,mobile);
            System.out.println("数据库手机验证码--->"+smsCode);

            // 绑定手机验证码
            param = "{\"verificationCode\":\""+smsCode+"\",\"mobile\":\"\",\"areaCode\":86}";
            res = httpUtil.postByJson(host+"/v1/users/security/messages/mobile/check", param,header);
            System.out.println("绑定手机验证码--->"+res);

            // 绑定
            param = "{\"email\":\""+mobile+"@163.com\",\"verificationCode\":\""+emailCode+"\",\"behavior\":3041,\"securityCode\":\""+smsCode+"\"}";
            res = httpUtil.postByJson(host+"/v1/users/security/email/bind-new", param,header);
            System.out.println("绑定--->"+res);

        }
    }

    /**
     *
     * @param t 0-email 1-sms
     * @param data
     * @return
     */
    public String getSms(int t,String data){

        Connection conn = null;
        Statement pre = null;
        String sql = "";
        if (t == 0){
            sql = "select id,created_date,mobile,email,template_params,status from intgr_message where email=\""+data+"@163.com\" order by id desc limit 1";
        }else {
            sql = "select id,created_date,mobile,email,template_params,status from intgr_message where mobile=\""+data+"\" order by id desc limit 1";
        }

        ResultSet resultSet = null;
        try{
            // 注册 JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 打开链接
            conn = DriverManager.getConnection("jdbc:mysql://locklevel-mysql-test1.cluster-cwgnjbpmvvmh.ap-southeast-1.rds.amazonaws.com:3306/bib_cfd","lltest1_master_rw","+vfxrhplu+xy!yc@jr*wz$6^b&16#4av");

            pre = conn.createStatement();

            resultSet = pre.executeQuery(sql);

            ResultSetMetaData me = resultSet.getMetaData();

            if (resultSet.next()){
                return JSONObject.parseObject(resultSet.getString("template_params")).getString("code");
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
                if(pre!=null) pre.close();
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
        return null;
    }

    @Test
    public void test6() {
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
        com.zmj.demo.HttpUtil httpUtil = new com.zmj.demo.HttpUtil();
        String authorization = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI5TmdVdkNOVzBmajIzWWV5d3pJeWNEcTRGX25RNm1xUkFuNklQajlmN3dFIn0.eyJqdGkiOiI5ZGQxN2NiOS1kZDk3LTQ0NjUtYjVlZC04OTY3OGEzZTk5NDgiLCJleHAiOjE2NzE4NDUyNjcsIm5iZiI6MCwiaWF0IjoxNjcxNzk4NDY3LCJpc3MiOiJodHRwczovL2F1dGguaXRjYXN0LmNuL2F1dGgvcmVhbG1zL2l0Y2FzdCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI1ZDU2NWZlNC0wMzNmLTQwNGUtYjYyMi1jYzg2YjE0Njk3MDYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzY3JtLXVpIiwibm9uY2UiOiJlZjRlMTVkOS1jNThkLTRkNDYtODRjYi0xMDlhZjRkY2UwMzYiLCJhdXRoX3RpbWUiOjE2NzE3OTg0NjcsInNlc3Npb25fc3RhdGUiOiIwZGFkNDRjNC0wMDVjLTRlM2YtOWJhMi00OGRlY2M5YzQ1NDUiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9zY3JtLXFhLml0Y2FzdC5jbiIsImh0dHA6Ly80Ny45My4yMjkuMjI4IiwiaHR0cDovL3Njcm0tdWF0Lml0Y2FzdC5jbiIsImh0dHBzOi8vc2NybS5pdGNhc3QuY24iLCJodHRwczovL3Njcm0tdWF0Lml0Y2FzdC5jbiIsImh0dHBzOi8vc2NybS1zZXJ2ZXIuaXRjYXN0LmNuIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZGVwYXJ0bWVudE5hbWUgZW1haWwgcHJvZmlsZSBlbXBsb3llZU51bWJlciIsImRlcGFydG1lbnROYW1lIjoiU05T5ZKo6K-i6YOoIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoi6LW1576O6Z2ZIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiemhhb21laWppbmdAaXRjYXN0LmNuIiwibG9jYWxlIjoiemgtQ04iLCJnaXZlbl9uYW1lIjoi6LW1576O6Z2ZIiwiZW1haWwiOiJ6aGFvbWVpamluZ0BpdGNhc3QuY24iLCJlbXBsb3llZU51bWJlciI6Ijk0NjkxIn0.QVElqNaLhXaivTf7nYSvg7wqomTyKzgE73goMRTtt-iLWPfg2oGSsy5n6FxpbTdF6eoOvlbmjp4DBRLr9H_TmW36qoJ2oaK2JwtxNM7gCIxqkWRJpyoQiI5VhzVvdqcBcRsRuH3QAQwU4g1hkY-Vb7KCYMNDqecAUFYTjYn24uzISHihgVDkTIixdsT-iVWcJVc3pgddFGcr4mE7hyQoiItCbYM85GgV36BCdHvwZI6wzcSoyNW-OI7KBU9byZhBcJ_wWR-x3oglzQIwZGD273O8lpXZEDODpZkuypxjzgUsESsxaUnyCJJoGW3jQci2XgAVlUw6HiZQSXdPPsv8WA";
        String nextVisitTime = "2022-12-23 00:00:00";
        String data = httpUtil.postByJson("https://scrm.itcast.cn/customer/list","{\"pageIndex\":1,\"pageSize\":400,\"orderBy\":null,\"where\":{\"onlineFollow\":false,\"levelList\":[],\"originTypeList\":[],\"itcastSchoolIdList\":[],\"itcastSubjectIdList\":[],\"anticipatSignupStartTime\":\"\",\"anticipatSignupEndTime\":\"\",\"lastVisitStartTime\":\"\",\"lastVisitEndTime\":\"\",\"createEndTime\":\"\",\"createStartTime\":\"\",\"nextVisitStartTime\":\"\",\"nextVisitEndTime\":\"\",\"paymentStartTime\":null,\"paymentEndTime\":null,\"experienceCourseClazzList\":[],\"itcastClazzStartTime\":null,\"itcastClazzEndTime\":null,\"originChannels\":\"\",\"appointmentOriginType\":[],\"schoolConsultation\":[],\"isSignup\":false,\"will2PublicCustomer\":true}}","{\"authorization\":\""+authorization+"\"}");
        JSONArray j = JSONObject.parseObject(data).getJSONObject("resultObject").getJSONArray("data");
        for (int i = 0; i < j.size(); i++) {
            System.out.println("第"+i+"条--->"+httpUtil.postByJson("https://scrm.itcast.cn/customer/visitlog/add", "{\"nextVisitTime\":\""+nextVisitTime+"\",\"level\":\"" + j.getJSONObject(i).getString("level") + "\",\"content\":\"微信\",\"customerRelationshipId\":" + j.getJSONObject(i).getString("customerRelationshipId") + "}", "{\"authorization\":\""+authorization+"\"}"));

        }
        data = httpUtil.postByJson("https://scrm.itcast.cn/customer/list","{\"pageIndex\":1,\"pageSize\":400,\"orderBy\":null,\"where\":{\"onlineFollow\":false,\"isSignup\":true,\"levelList\":[],\"originTypeList\":[],\"itcastSchoolIdList\":[],\"itcastSubjectIdList\":[],\"anticipatSignupStartTime\":\"\",\"anticipatSignupEndTime\":\"\",\"lastVisitStartTime\":\"\",\"lastVisitEndTime\":\"\",\"createEndTime\":\"\",\"createStartTime\":\"\",\"nextVisitStartTime\":\"\",\"nextVisitEndTime\":\"\",\"paymentStartTime\":\"\",\"paymentEndTime\":\"\",\"itcastClazzStartTime\":\"\",\"itcastClazzEndTime\":\"\",\"experienceCourseClazzList\":[],\"originChannels\":\"\",\"appointmentOriginType\":[],\"schoolConsultation\":[],\"will2PublicCustomer\":true}}","{\"authorization\":\""+authorization+"\"}");
        j = JSONObject.parseObject(data).getJSONObject("resultObject").getJSONArray("data");
        for (int i = 0; i < j.size(); i++) {
            System.out.println("第"+i+"条--->"+httpUtil.postByJson("https://scrm.itcast.cn/customer/visitlog/add", "{\"nextVisitTime\":\""+nextVisitTime+"\",\"level\":\"" + j.getJSONObject(i).getString("level") + "\",\"content\":\"微信\",\"customerRelationshipId\":" + j.getJSONObject(i).getString("customerRelationshipId") + "}", "{\"authorization\":\""+authorization+"\"}"));

        }
    }

    @Test
    public void test7(){
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
        String token = "6641d03e37daeea04fc80aa37fc82787638cdb228e3e86ac27f53e1b24f4bde5";
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

        String url = "http://mq-demo.saas-inf.com/api/exchanges/%2F/tfbee.demo.exchange.externalLatestTicker/publish";

        String header = "{\"authorization\":\"Basic c2Fhcy1ybXEtZGVtbzp6NTlqZzlCdUloS0NwODJm\"}";

        long time = System.currentTimeMillis();
        BigDecimal last = BigDecimal.valueOf(0.5);

        String param;
        Random random = new Random();
        int rTmp = 0;
        int i = 1;
        while (true){
            rTmp = random.nextInt(100);
            if (rTmp <= 20) {
                param = "{\"vhost\":\"/\",\"name\":\"tfbee.test.exchange.externalLatestTicker\",\"properties\":{\"delivery_mode\":2,\"headers\":{}},\"routing_key\":\"\",\"delivery_mode\":\"2\",\"payload\":\"{\\\"symbolId\\\":21,\\\"eventTime\\\":"+time+",\\\"recieveTime\\\":"+time+",\\\"tickerSource\\\":\\\"BINANCE\\\",\\\"open\\\":0.3338,\\\"low\\\":0.3219,\\\"volume\\\":797297398.2,\\\"amount\\\":261551713.03,\\\"last\\\":"+last+",\\\"open\\\":0.3308}\",\"headers\":{},\"props\":{},\"payload_encoding\":\"string\"}";
            }else if (rTmp >20 && rTmp <= 50){
                last = BigDecimal.valueOf(0.7);
                param = "{\"vhost\":\"/\",\"name\":\"tfbee.test.exchange.externalLatestTicker\",\"properties\":{\"delivery_mode\":2,\"headers\":{}},\"routing_key\":\"\",\"delivery_mode\":\"2\",\"payload\":\"{\\\"symbolId\\\":21,\\\"eventTime\\\":"+time+",\\\"recieveTime\\\":"+time+",\\\"tickerSource\\\":\\\"BINANCE\\\",\\\"open\\\":0.3338,\\\"low\\\":0.3219,\\\"volume\\\":797297398.2,\\\"amount\\\":261551713.03,\\\"last\\\":"+last+",\\\"open\\\":0.3308}\",\"headers\":{},\"props\":{},\"payload_encoding\":\"string\"}";
            }else if (rTmp >50 && rTmp <= 70){
                last = BigDecimal.valueOf(0.8);
                param = "{\"vhost\":\"/\",\"name\":\"tfbee.test.exchange.externalLatestTicker\",\"properties\":{\"delivery_mode\":2,\"headers\":{}},\"routing_key\":\"\",\"delivery_mode\":\"2\",\"payload\":\"{\\\"symbolId\\\":21,\\\"eventTime\\\":"+time+",\\\"recieveTime\\\":"+time+",\\\"tickerSource\\\":\\\"BINANCE\\\",\\\"open\\\":0.3338,\\\"low\\\":0.3219,\\\"volume\\\":797297398.2,\\\"amount\\\":261551713.03,\\\"last\\\":"+last+",\\\"open\\\":0.3308}\",\"headers\":{},\"props\":{},\"payload_encoding\":\"string\"}";
            }else {
                last = BigDecimal.valueOf(0.9);
                param = "{\"vhost\":\"/\",\"name\":\"tfbee.test.exchange.externalLatestTicker\",\"properties\":{\"delivery_mode\":2,\"headers\":{}},\"routing_key\":\"\",\"delivery_mode\":\"2\",\"payload\":\"{\\\"symbolId\\\":21,\\\"eventTime\\\":"+time+",\\\"recieveTime\\\":"+time+",\\\"tickerSource\\\":\\\"BINANCE\\\",\\\"open\\\":0.3338,\\\"low\\\":0.3219,\\\"volume\\\":797297398.2,\\\"amount\\\":261551713.03,\\\"last\\\":"+last+",\\\"open\\\":0.3308}\",\"headers\":{},\"props\":{},\"payload_encoding\":\"string\"}";
            }
            System.out.println("======第"+i+"推消息======>"+httpUtil.postByJson(url,param,header));
            i ++ ;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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

//        String url = "http://test1.123kj.top/v1/cfd/trade/btc/open";
//        String tt = "https://x.chimchim.top";
        String token;
        String tt = "https://x.chimchim.top";
        String url ;
        String param;
        String res ;
        for (long i = 16630000334l; i <= 16630000347l; i++) {
            param = "{\"password\": \"ghw111111\",\"username\": \""+i+"\"}";
            token = JSONObject.parseObject(httpUtil.postByJson(tt+"/v1/users/membership/sign-in",param)).getJSONObject("data").getString("accessToken");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("x-authorization", token);
            String header = jsonObject.toString();
            url = tt+"/v1/cfd/trade/btc/open";
            param = "{\"contractType\":1,\"direction\":\"long\",\"leverage\":100,\"positionModel\":\"1\",\"positionType\":\"execute\",\"quantity\":\"200\",\"quantityUnit\":\"1\"}";
            res = httpUtil.postByJson(url, param, header);
            System.out.println(res);
            url = tt+"/v1/cfd/trade/eth/open";
            param = "{\"contractType\":1,\"direction\":\"long\",\"leverage\":100,\"positionModel\":\"1\",\"positionType\":\"execute\",\"quantity\":\"300\",\"quantityUnit\":\"1\"}";
            res = httpUtil.postByJson(url, param, header);
            System.out.println(res);
            url = tt+"/v1/cfd/trade/ltc/open";
            param = "{\"contractType\":1,\"direction\":\"long\",\"leverage\":100,\"positionModel\":\"1\",\"positionType\":\"execute\",\"quantity\":\"100\",\"quantityUnit\":\"1\"}";
            res = httpUtil.postByJson(url, param, header);
            System.out.println(res);
            url = tt+"/v1/cfd/trade/eos/open";
            param = "{\"contractType\":1,\"direction\":\"long\",\"leverage\":100,\"positionModel\":\"1\",\"positionType\":\"execute\",\"quantity\":\"100\",\"quantityUnit\":\"1\"}";
            res = httpUtil.postByJson(url, param, header);
            System.out.println(res);
            url = tt+"/v1/cfd/trade/trx/open";
            param = "{\"contractType\":1,\"direction\":\"long\",\"leverage\":100,\"positionModel\":\"1\",\"positionType\":\"execute\",\"quantity\":\"100\",\"quantityUnit\":\"1\"}";
            res = httpUtil.postByJson(url, param, header);
            System.out.println(res);
        }


//        int rTmp;
//        int i = 1;
//        Random random = new Random();
//        String res;
//        while (true) {
//            rTmp = random.nextInt(100);
//            if (rTmp >= 50) {
//                param = "{\"contractType\":1,\"direction\":\"long\",\"leverage\":100,\"positionModel\":\"1\",\"positionType\":\"execute\",\"quantity\":\"10000\",\"quantityUnit\":\"1\",\"stopProfitRate\":0.01,\"stopLossRate\":0.01}";
//            }else {
//                param = "{\"contractType\":1,\"direction\":\"short\",\"leverage\":100,\"positionModel\":\"1\",\"positionType\":\"execute\",\"quantity\":\"10000\",\"quantityUnit\":\"1\",\"stopProfitRate\":0.01,\"stopLossRate\":0.01}";
//            }
//
//            res = httpUtil.postByJson(url, param, header);
//            if (9044 == JSONObject.parseObject(res).getInteger("code")){
//                res = httpUtil.postByJson("https://www-demo.sefvbs.com/v1/cfd/trade/close/all/0?contractType=1",null,header);
//                System.out.println("======平仓======"+res);
//                continue;
//
//            }
//            System.out.println("======第"+i+"次下单======>"+res);
//
//            try {
//                Thread.sleep(1200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            i++;
//        }
    }

    @Test
    public void test11(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("symbolId","21");
        jsonObject.put("eventTime",System.currentTimeMillis());
        jsonObject.put("recieveTime",System.currentTimeMillis());
        jsonObject.put("tickerSource","BINANCE");
        jsonObject.put("high",1);
        jsonObject.put("low",2);
        jsonObject.put("volume",33);
        jsonObject.put("amount",44);
        jsonObject.put("open",3);
        jsonObject.put("last",0.5);
        System.out.println(jsonObject);

        System.out.println(String.valueOf("0.07100000000000001").substring(0,6));

        BigDecimal bigDecimal = BigDecimal.valueOf(0.07100000000000001);
        System.out.println(String.valueOf(bigDecimal.multiply(BigDecimal.valueOf(0.001)).setScale(8,BigDecimal.ROUND_UP)));
    }

    @Test
    public void test12() {
//        测试
//          String channel = "GjFt";
//
////        模拟
////        String channel = "Ch3Q";
//        String[] currency = {"btc", "eth",  "ltc", "bch","trx", "fil", "link", "dot", "xrp", "doge", "shib", "ada", "matic", "sol", "bnb", "avax", "mana", "axs", "uni", "ftt"};
////        订阅列表
//        List<String> subList = new ArrayList<>();
//
//        for(String c : currency){
////            深度
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_"+channel+"." + c + "usdt_depth_step0\", \"cb_id\": \"10001\", \"asks\": 150, \"bids\": 150}}");
////            k线
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_"+channel+"." + c + "usdt_kline_1min\", \"cb_id\": \"10001\"}}");
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_"+channel+"." + c + "usdt_kline_5min\", \"cb_id\": \"10001\"}}");
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_"+channel+"." + c + "usdt_kline_15min\", \"cb_id\": \"10001\"}}");
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_"+channel+"." + c + "usdt_kline_30min\", \"cb_id\": \"10001\"}}");
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_"+channel+"." + c + "usdt_kline_60min\", \"cb_id\": \"10001\"}}");
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_"+channel+"." + c + "usdt_kline_1hour\", \"cb_id\": \"10001\"}}");
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_"+channel+"." + c + "usdt_kline_4hour\", \"cb_id\": \"10001\"}}");
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_"+channel+"." + c + "usdt_kline_1day\", \"cb_id\": \"10001\"}}");
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_"+channel+"." + c + "usdt_kline_1week\", \"cb_id\": \"10001\"}}");
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_"+channel+"." + c + "usdt_kline_1month\", \"cb_id\": \"10001\"}}");
////            标价价格
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_" + c + "usdt_mark_price\", \"cb_id\": \"10001\"}}");
////            指数价格
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_" + c + "usdt_index_price\", \"cb_id\": \"10001\"}}");
////            资金费率
//            subList.add("{\"event\": \"sub\", \"params\": {\"channel\": \"market_" + c + "usdt_funding_rate\", \"cb_id\": \"10001\"}}");
//        }
//        JSONObject jsonObject = JSONObject.parseObject("{\"event\": \"sub\", \"params\": {\"channel\": \"market_usdt_kline_1min\", \"cb_id\": \"10001\"}}");
//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.put("event","unsub");
//        jsonObject1.put("params",jsonObject.getJSONObject("params"));
//        System.out.println(jsonObject1.toString());


        // 测试
        String channel = "GjFt";

        String[] currency = {"btc", "eth", "ltc", "bch", "trx", "fil", "link", "dot", "xrp", "doge", "shib", "ada", "matic", "sol", "bnb", "avax", "mana", "axs", "uni", "ftt"};
// 订阅列表
        List subList = new ArrayList<>();

        for (String c : currency) {
//  成交历史
            subList.add("{\"event\": \"req\", \"params\": {\"channel\": \"market_" + channel + "." + c + "usdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}");
//  k线
            subList.add("{\"event\": \"req\", \"params\": {\"channel\": \"market_" + channel + "." + c + "usdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}");
            subList.add("{\"event\": \"req\", \"params\": {\"channel\": \"market_" + channel + "." + c + "usdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}");
            subList.add("{\"event\": \"req\", \"params\": {\"channel\": \"market_" + channel + "." + c + "usdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}");
            subList.add("{\"event\": \"req\", \"params\": {\"channel\": \"market_" + channel + "." + c + "usdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}");
            subList.add("{\"event\": \"req\", \"params\": {\"channel\": \"market_" + channel + "." + c + "usdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}");
            subList.add("{\"event\": \"req\", \"params\": {\"channel\": \"market_" + channel + "." + c + "usdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}");
            subList.add("{\"event\": \"req\", \"params\": {\"channel\": \"market_" + channel + "." + c + "usdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}");
            subList.add("{\"event\": \"req\", \"params\": {\"channel\": \"market_" + channel + "." + c + "usdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}");
            subList.add("{\"event\": \"req\", \"params\": {\"channel\": \"market_" + channel + "." + c + "usdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}");
            subList.add("{\"event\": \"req\", \"params\": {\"channel\": \"market_" + channel + "." + c + "usdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}");
//     首页全部行情
            subList.add("{\"event\": \"req\", \"params\": {\"channel\": \"market_review_" + channel + "\"}}");
        }
//        Random r = new Random();
//        System.out.println(subList.get(r.nextInt(240)));
        System.out.println(subList);
        JSONObject jsonObject = JSONObject.parseObject("{\"data\":[{\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.btcusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.btcusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.btcusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.btcusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.btcusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.btcusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.btcusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.btcusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.btcusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.btcusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.btcusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ethusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ethusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ethusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ethusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ethusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ethusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ethusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ethusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ethusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ethusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ethusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ltcusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ltcusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ltcusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ltcusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ltcusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ltcusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ltcusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ltcusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ltcusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ltcusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.ltcusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bchusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bchusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bchusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bchusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bchusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bchusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bchusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bchusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bchusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bchusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bchusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.trxusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.trxusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.trxusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.trxusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.trxusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.trxusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.trxusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.trxusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.trxusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.trxusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.trxusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.filusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.filusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.filusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.filusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.filusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.filusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.filusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.filusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.filusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.filusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.filusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.linkusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.linkusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.linkusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.linkusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.linkusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.linkusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.linkusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.linkusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.linkusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.linkusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.linkusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dotusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dotusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dotusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dotusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dotusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dotusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dotusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dotusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dotusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dotusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dotusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.xrpusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.xrpusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.xrpusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.xrpusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.xrpusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.xrpusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.xrpusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.xrpusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.xrpusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.xrpusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.xrpusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dogeusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dogeusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dogeusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dogeusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dogeusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dogeusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dogeusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dogeusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dogeusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dogeusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.dogeusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.shibusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.shibusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.shibusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.shibusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.shibusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.shibusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.shibusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.shibusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.shibusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.shibusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.shibusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.adausdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.adausdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.adausdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.adausdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.adausdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.adausdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.adausdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.adausdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.adausdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.adausdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.adausdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.maticusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.maticusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.maticusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.maticusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.maticusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.maticusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.maticusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.maticusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.maticusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.maticusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.maticusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.solusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.solusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.solusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.solusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.solusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.solusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.solusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.solusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.solusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.solusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.solusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bnbusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bnbusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bnbusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bnbusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bnbusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bnbusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bnbusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bnbusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bnbusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bnbusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.bnbusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.avaxusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.avaxusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.avaxusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.avaxusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.avaxusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.avaxusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.avaxusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.avaxusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.avaxusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.avaxusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.avaxusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.manausdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.manausdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.manausdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.manausdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.manausdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.manausdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.manausdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.manausdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.manausdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.manausdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.manausdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.axsusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.axsusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.axsusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.axsusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.axsusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.axsusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.axsusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.axsusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.axsusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.axsusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.axsusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.uniusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.uniusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.uniusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.uniusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.uniusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.uniusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.uniusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.uniusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.uniusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.uniusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.uniusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.fttusdt_trade_ticker\", \"cb_id\": \"10001\", \"top\": 20}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.fttusdt_kline_1min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.fttusdt_kline_5min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.fttusdt_kline_15min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.fttusdt_kline_30min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.fttusdt_kline_60min\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.fttusdt_kline_1hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.fttusdt_kline_4hour\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.fttusdt_kline_1day\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.fttusdt_kline_1week\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_GjFt.fttusdt_kline_1month\", \"cb_id\": \"10001\",\"endIdx\":1665393129,\"pageSize\":200}}, {\"event\": \"req\", \"params\": {\"channel\": \"market_review_GjFt\"}}]}");

        Random r = new Random();
        String j = jsonObject.getJSONArray("data").getString(r.nextInt(280));
        System.out.println(j.replace("10001","aaaaa"));
//        String.valueOf()

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("pong",1);
        jsonObject1.put("cb_id",1);
        jsonObject1.toString();


    }
    @Test
    public void test13() {
//        System.out.println(BigDecimal.valueOf(11).compareTo(BigDecimal.valueOf(111)));
        System.out.println("abcdefghijklmnopqrstuvwxyz".toCharArray()[new Random().nextInt("abcdefghijklmnopqrstuvwxyz".toCharArray().length)]);
        String params = "{\"beginTime\":\"2022-11-29 00:00:00\",\"endTime\":\"2022-11-30 00:00:00\"}";
        HashMap<String,Object> map = JSONObject.parseObject(params,HashMap.class);
        System.out.println(map);
        System.out.println((BigDecimal.valueOf(16768.3000000000000000).subtract(BigDecimal.valueOf(16772.5190476190476190))	).multiply(BigDecimal.valueOf(0.021)));
        System.out.println(BigDecimal.valueOf(16768.3000000000000000).subtract(BigDecimal.valueOf(16772.5190476190476190))	);
    }

    @Test
    public void test14(){
//        for (int i = 0; i < 1000; i++) {
            ((LoggerContext) LoggerFactory.getILoggerFactory())
                    .getLoggerList()
                    .forEach(logger -> logger.setLevel(Level.ERROR));

            com.zmj.demo.HttpUtil httpUtil = new com.zmj.demo.HttpUtil();

//        String url = "http://test1.123kj.top/v1/cfd/trade/btc/open";
//            String url = "https://www.hpxshop.me/v1/cfd/trade/btc/open";
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("x-authorization", "117aa953149992c0636d61daa3ba631c98bf5c25c892eed7111a3a6ea64fb169");
//            String header = jsonObject.toString();
//            String param = "{\"contractType\":1,\"direction\":\"long\",\"leverage\":100,\"positionModel\":\"0\",\"positionType\":\"execute\",\"quantity\":\"1\",\"quantityUnit\":\"1\",\"stopProfitTriggerType\":\"MARKET\",\"stopProfitTriggerPrice\":\"\",\"stopProfitRate\":0.01}";
//            String res = httpUtil.postByJson(url, param, header);
//            System.out.println(res);
            String url = "https://x.chimchim.top/v1/cfd/trade/positions?contractType=1&page1&pageSize=1000&positionModel=0&positionType=execute";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("x-authorization", "26bcfe38b8974a0dd2a445867be5347748baefbe4bfc5c5f87ed11c00d7314fd");
            String header = jsonObject.toString();
            JSONArray res_position = JSONObject.parseObject(httpUtil.get(url, header)).getJSONObject("data").getJSONArray("rows");
            String param = "";
            for (int j = 0; j < res_position.size(); j++) {
                url = "https://x.chimchim.top/v1/cfd/trade/"+res_position.getJSONObject(j).getString("instrument")+"/changeStopPrice/"+res_position.getJSONObject(j).getString("id");
                if (res_position.getJSONObject(j).getString("direction").equalsIgnoreCase("long"))
                    param = "{\"stopProfitPrice\": \"17300\",\"contractType\": 1,\"stopLossPrice\": \"16600\"}";
                else
                    param = "{\"stopProfitPrice\": \"16600\",\"contractType\": 1,\"stopLossPrice\": \"17300\"}";
                System.out.println(httpUtil.postByJson(url,param,header));
            }
//        }
    }
    @Test
    public void test15(){
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
        com.zmj.demo.HttpUtil httpUtil = new com.zmj.demo.HttpUtil();
        String host = "https://www.hpxshop.me";
        String res = "";
        String accessToken = "";
        String param = "";
        String mobile = "";
        String header = "";
        String emailCode = "";
        String smsCode = "";
        for (int i = 1; i < 500; i++) {
            mobile = "16600000" + String.format("%03d", i);
            // 登录
            param = "{\"username\":\"" + mobile + "\",\"password\":\"ghw111111\"}";
            res = httpUtil.postByJson(host+"/v1/users/membership/sign-in", param);
            System.out.println("登录--->"+res);
            accessToken = JSONObject.parseObject(res).getJSONObject("data").getString("accessToken");
            header = "{\"X-Authorization\":\""+accessToken+"\",\"Content-Type\":\"application/json\"}";

            // 发送邮箱验证码
            param = "{\"behavior\":3041,\"email\":\""+mobile+"@163.com\"}";
            res = httpUtil.postByJson(host+"/v1/users/security/messages/verification-code/email", param,header);
            System.out.println("发送邮箱验证码--->"+res);
            emailCode = getSmsDemo(0,mobile);
            System.out.println("数据库邮箱验证码--->"+emailCode);

            // 绑定邮箱验证码
            param = "{\"email\":\""+mobile+"@163.com\",\"verificationCode\":\""+emailCode+"\"}";
            res = httpUtil.postByJson(host+"/v1/users/security/email/bind-pre", param,header);
            System.out.println("绑定邮箱验证码--->"+res);

            // 发送手机验证码
            param = "{\"behavior\":3035,\"mobile\":\"\",\"areaCode\":86}";
            res = httpUtil.postByJson(host+"/v1/users/security/messages/verification-code/mobile", param,header);
            System.out.println("发送手机验证码--->"+res);

            // 查询短信验证码
            smsCode = getSmsDemo(1,mobile);
            System.out.println("数据库手机验证码--->"+smsCode);

            // 绑定手机验证码
            param = "{\"verificationCode\":\""+smsCode+"\",\"mobile\":\"\",\"areaCode\":86}";
            res = httpUtil.postByJson(host+"/v1/users/security/messages/mobile/check", param,header);
            System.out.println("绑定手机验证码--->"+res);

            // 绑定
            param = "{\"email\":\""+mobile+"@163.com\",\"verificationCode\":\""+emailCode+"\",\"behavior\":3041,\"securityCode\":\""+smsCode+"\"}";
            res = httpUtil.postByJson(host+"/v1/users/security/email/bind-new", param,header);
            System.out.println("绑定--->"+res);

        }

    }

    @Test
    public void test16(){
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
        com.zmj.demo.HttpUtil httpUtil = new com.zmj.demo.HttpUtil();
        String host = "https://x.chimchim.top";
        String res = "";
        String param = "";
        String header = "";
        BigDecimal profitSum = BigDecimal.ZERO;
        BigDecimal openFee ;
        BigDecimal closeFee ;
        BigDecimal openPrice;
        BigDecimal closePrice;
        BigDecimal quantity;
        param = "{\"password\": \"ghw111111\",\"username\": \"16600000046\"}";
        String token = JSONObject.parseObject(httpUtil.postByJson(host+"/v1/users/membership/sign-in",param)).getJSONObject("data").getString("accessToken");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x-authorization", token);
        header = jsonObject.toString();
        int id = 1655;
        BigDecimal baseSize = BigDecimal.valueOf(100);
        int direction = -1;
        res = httpUtil.get(host+"/v1/cfd/order/grid/trade/getTransactionRecordById?page=1&pageSize=200&id="+id,header);
        JSONArray rows = JSONObject.parseObject(res).getJSONObject("data").getJSONObject("list").getJSONArray("rows");
        for (int i = 0; i < rows.size(); i++) {
            openFee = rows.getJSONObject(i).getJSONObject("buyOrder").getBigDecimal("fee");
            openPrice = rows.getJSONObject(i).getJSONObject("buyOrder").getBigDecimal("avgCost");
            closeFee = rows.getJSONObject(i).getJSONObject("sellOrder") == null ? BigDecimal.ZERO : rows.getJSONObject(i).getJSONObject("sellOrder").getBigDecimal("fee");
            closePrice = rows.getJSONObject(i).getJSONObject("sellOrder") == null ? BigDecimal.ZERO : rows.getJSONObject(i).getJSONObject("sellOrder") .getBigDecimal("avgCost");
            quantity = rows.getJSONObject(i).getJSONObject("buyOrder").getBigDecimal("quantity");
            BigDecimal p;
            if (closeFee.compareTo(BigDecimal.ZERO) == 0) {
                p = BigDecimal.ZERO.subtract(openFee);
            }else {
                if (direction == 1) {
                    p = (closePrice.subtract(openPrice)).multiply(quantity).multiply(baseSize).subtract(openFee).subtract(closeFee);
                } else {
                    p = (openPrice.subtract(closePrice)).multiply(quantity).multiply(baseSize).subtract(openFee).subtract(closeFee);
                }
            }
            System.out.println(rows.getJSONObject(i));
            System.out.println("计算网格利润:"+p+"-----接口返回:"+rows.getJSONObject(i).getBigDecimal("profit")+"---->"+p.compareTo(rows.getJSONObject(i).getBigDecimal("profit")));
            profitSum = profitSum.add(p);

        }
        System.out.println("总利润："+profitSum);

    }
    @Test
    public void test17() throws InterruptedException {
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
        com.zmj.demo.HttpUtil httpUtil = new com.zmj.demo.HttpUtil();
        String host = "https://x.chimchim.top";
        String param;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x-authorization", "77d08795d82da83391f83facd26c54479c567bd829088bb99b33cb41ead78889");
        String header = jsonObject.toString();
        for (int i = 0; i < 100; i++) {
            System.out.println("-------"+i+"-------");
            param = "{\n" +
                    "    \"contractType\": 1,\n" +
                    "    \"direction\": \"short\",\n" +
                    "    \"leverage\": 100,\n" +
                    "    \"positionModel\": \"1\",\n" +
                    "    \"positionType\": \"execute\",\n" +
                    "    \"quantity\": \"177\",\n" +
                    "    \"quantityUnit\": \"1\"\n" +
                    "}";
            System.out.println(httpUtil.postByJson(host+"/v1/cfd/trade/ftt/open",param,header));
            Thread.sleep(1000);
            param = "{\n" +
                    "    \"contractType\": 1,\n" +
                    "    \"direction\": \"long\",\n" +
                    "    \"leverage\": 100,\n" +
                    "    \"positionModel\": \"1\",\n" +
                    "    \"positionType\": \"execute\",\n" +
                    "    \"quantity\": \"177\",\n" +
                    "    \"quantityUnit\": \"1\"\n" +
                    "}";
            System.out.println(httpUtil.postByJson(host+"/v1/cfd/trade/ftt/open",param,header));
            Thread.sleep(1000);
        }
    }

    @Test
    public void test18() {
        Random r = new Random();
        double openPrice = 47080;
//        for (int i = 0; i < 100; i++) {
//            double v = r.nextGaussian() * Math.sqrt(100) + openPrice;
//            System.out.println(v);
//        }
//        System.out.println(Math.sqrt(0.0000000009));
        for (int i = 0; i < 10; i++) {
//            System.out.println(Math.abs(r.nextGaussian()));
//            System.out.println(20.693+ r.nextGaussian() * Math.sqrt(2));
            System.out.println(BigDecimal.valueOf(Math.abs(r.nextGaussian() * Math.sqrt(100))));
        }

        System.out.println(String.format("%.2f", 3.424324444));
    }

    /**
     * 模拟环境，查询数据库
     * @param i
     * @param data
     * @return
     */
    private String getSmsDemo(int i, String data) {
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
        com.zmj.demo.HttpUtil httpUtil = new com.zmj.demo.HttpUtil();
        String url = "http://dbms-aws-demo.saas-inf.com/query/";
        JSONObject param = new JSONObject();
        if (i == 0){
            param.put("instance_name","hpx-mysql-demo-s");
            param.put("db_name","bib_cfd_hpx");
            param.put("schema_name","");
            param.put("tb_name","intgr_message");
            param.put("sql_content","select * from intgr_message where email=\""+data+"@163.com\" order by id desc limit 1");
            param.put("limit_num",100);

//            param = "instance_name=hpx-mysql-demo-s&db_name=bib_cfd_hpx&schema_name=&tb_name=&sql_content=select+*+from+intgr_message+where+email%3D%22"+data+"%40163.com%22+order+by+id+desc+limit+1%0A&limit_num=100";
        }else {
            param.put("instance_name", "hpx-mysql-demo-s");
            param.put("db_name", "bib_cfd_hpx");
            param.put("schema_name", "");
            param.put("tb_name", "intgr_message");
            param.put("sql_content", "select * from intgr_message where mobile=\""+data+"\" order by id desc limit 1");
            param.put("limit_num", 100);
//            param = "instance_name=hpx-mysql-demo-s&db_name=bib_cfd_hpx&schema_name=&tb_name=&sql_content=select+*+from+intgr_message+where+mobile%3D%22"+data+"%22+order+by+id+desc+limit+1%0A&limit_num=100";
        }
        String header = "{\"X-CSRFToken\":\"nnChA7Jch6FDJ0KiwHBSBtOyv0cCUqJ6yrnamPvTouEJtRJPPyI3lu8Z4n9qlilC\",\"Content-Type\":\"application/x-www-form-urlencoded\",\"X-Requested-With\":\"XMLHttpRequest\",\"Cookie\":\"csrftoken=nnChA7Jch6FDJ0KiwHBSBtOyv0cCUqJ6yrnamPvTouEJtRJPPyI3lu8Z4n9qlilC; sessionid=vqlfm1stsv0lajxu7r2z17768i0jq2ys\"}";
        String res = httpUtil.postByForm(url,param.toString(),header);
        System.out.println("dbms数据库查询："+res);
        List<String> list = (List<String>) JSONObject.parseObject(res).getJSONObject("data").getJSONArray("rows").get(0);
        String code = JSONObject.parseObject(list.get(8)).getString("code");
        return code;
    }


}
