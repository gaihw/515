package com.zmj.demo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.HttpUtil;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(Math.abs(r.nextGaussian()));
            System.out.println(Math.sqrt(100));
        }

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
        for (int i = 0; i < 10000; i++) {
//            System.out.println(Math.abs(r.nextGaussian()));
//            System.out.println(20.693+ r.nextGaussian() * Math.sqrt(2));
//            System.out.println(BigDecimal.valueOf(Math.abs(r.nextGaussian() * Math.sqrt(100))));
            if (Math.abs(r.nextGaussian() / 5) >= 1)
                System.out.println("有");
        }

//        System.out.println(String.format("%.2f", 3.424324444));
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

    /**
     * 批量撤单
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void test19() throws IOException, SQLException {

        String userId = "51906195";

        JSONObject user = new JSONObject();
        user.put("51906166","06065c747863e2f910dd557b379b2345bd8298bdc50973b015ac8b6d4e0cc4a6,MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMjFZndok8o9GbJwqZu936ka1zDtqna8UilbwXgvFKpDnqLqSB+4k0C0QoaMn2lGPsKyEs4Iwi0L+H2K1RI4jTBTNUDDcHF0tpRd4DMJ1vuUOJI0uxYuN8UHm4UKuI+8+2Mnh/ti564XbETalLufSwFUD/wkZMYCtFkQlnQ8Q/7zAgMBAAECgYAGcOLJKOc44T3uThP65ZwDylMmBDHoTkFah1GNIAGLNtEji92Veu/qbv4FYASLNZA04c6dooVMOaqWqHoOdBb/a9fTl1hr9qXZWxxZ31A4/eV4dK17LKsJFOi7YsDcPoRhOxXHtEElSXV790Hu3gObaNrRpXpeI1AkEwB7WmTVtQJBAOVP+corBR6WBvW9v6ixnIrPO9WqV7v7Lq6lQpYNgGmnDmBxL9utuEzuhir0nK8uwxE4UVOVuyhbv6Nh+rjlut0CQQDgIxQOo3HtzUSpAeHLrJv2ixU2OQDorvNByhX0TauegEGa0/l9BFHjuSw9jICYgdxuhsH5iSNdkbfo940xR3wPAkEA2B4l53ngG1F+QcCRj6XFSbXCSL+AbHRxLLwoI0+aRPjjPdWvKzVwy2DKJlXgDeLeia4wR7yIZaMC3DMNn5E0KQJALDF+cOx1OEgy84f1d21PSytdQVW4AikMuboY3hS6dAQh619EYAwMAXSvbmtXp7pjNj/H22XY3UgwFPVKl57arQJATQllXvkDahTjScaJq+Hro4mTa7pkvscGtWxChs1qJrHxlgsVGSnWcJIas7nT+c+t/zbWadAkwMk/aPe/aWRHvg==");
        user.put("51906155","1a9da8d194b0b7693a372a3b04073aac165f4e8a25ad17738888e90f897f35f6,MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAN2G0cjlKF2ek+g0mWZuhAptMgfFdXxdq7CXOGLK5537vkYwGdyayf8Yx6ztOF7jj7WH5WXfhqz2CxtADq8G/4zoK2Eyb6D5qmr5D7JXAfuYiVhRHYG4nsyVgFQy94BsLVf8sjDEIxomOPsGlkioKMR0bipgrLYULjzOMPGV6tbTAgMBAAECgYBA/E9ncT41tJLsMB3+LBaFHoJqQDVWbf4zWDE83gwzTsoDOhIkIq52LLVK1+5M+9HJLZwQlhGihK0+abzFALcPecWTYWG5UqyPavfEmZlTbvpEjh5oe59WEG+tDuJk8JcTblr/crCZprGdK1ujxy9k9cKFLmHoiQ4LSufavYj5vQJBAO0uVvNcJml2AJRiCWhyd1BUvdpLAv6bNISgqhr06FRa15s6vXV22zI34ziowznUnERujB+Lu8OlsuQ1Tvn5oH8CQQDvGoFqVKEnRbfSyiXXUbLKIVeDu3UlRiSkah7gvM1vjV3Jn1+T4w4kBgxYvvl8X9wIgt8OajwkF2CyMfDtNh+tAkEAn6LG5zkoqWbA8R4jEueoIqtGtTwzocY4zOOOWmJoVQ5ne5VHm14KHny7NJFyBORy4SQ+r6TPFdna0/qFU36OZQJBAIa1fTIl6gFRQyasIzOzYLKGpGKga3iLkztaWnktheSSVcOxWczdAGuFSNF7Dt5mt3XIHdlmOunujdj3UBClDKUCQQDU1sQQNiY7fjDNR5c8GMCfDN2beZA43Z+0DxUsIGoNouOpM3xTneFPp4uwhAEyiLkcUO6+pP+8SKm//NX/K4LI");
        user.put("51906156","4b56ddca4ba38a5d543fbac60f33ecdcbd49d6b15a8747332f49a28fa4eadbf5,MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN41qI5ipbWhDi/KwwdNPvU38hk8LIvWPPnJKWkghNF5Yvz1lj0w1Z1U1T3wdK8DpPzrJtw2oQuzPZC9jv4CNSC+zEYk2kcc95UsnAacrq5x9LOc3XILmapLgcRYcKXkOP66j5QhsCI4vy1FfrBWRykXrAX8WTmvuwyO2760YAOdAgMBAAECgYAMyoDv5UzNpj43URAl8PPhUL2eZ0DXMddjt4krNISAvGCO2CI0VoXNXy9BOAC3fFm4d17OyVQ+5Kv0E31SMNVW+Dh62efmmHzVorWFbPd7ThD0UOn8doGqvuEiW6lcit0QJJVhIg9vP1Xf+x15HFxCWlZmNkLX7EOqE8ayiM6+sQJBAORDyTAxtXwMHhtP7ycfWJNQgsF0dwKNVKmJdgAIepy8+T7xFFJAnZNgVJUzDwAIS6vGs9nCxiqBkOB7xQgZaWUCQQD5NYZlWyQcZRhMfnfavqYk7R0nHuuZCYj/yzswWtrRB014zSwCBTICOlm5ZEglmWACskmBjaalLjH9YPAsGqnZAkEArh8nMSQ6FE4KCTuIaod2wakAlSGKRuAYXNsGsC/HmBhu6Jxtq/CuQuWQn9866gNp9ba1Kr9w+qurIBJJ0kvQIQJBAOVe/nQLjxETe2nsfSQ4lXMnSs3XB8GzpQTKaY/4D66UqhmlvfueDIlAa5TXWBBDOrtAnadAWUFvY1NzBZuSbwECQBof9mqXqm2xdQmpHNnKiGQ2BUclJWFDAXkN7EdHX9NSPXeS0EfNNRzF9ENx3EL2PSTCf1ZntyfXCqHk3nj+FJg=");
        user.put("51906157","08f4fd168cff59008a53d151d8100b15502febebc01c6e326bb8988e8d463623,MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKXVIHJWXLCsknEAYHCyNuHIMxb7iJL6JeLsc+9AI/pjxDSJcSn9toskF+CnkiPGqnLfjeg0d40NTntfEPmEsaIOxnaqezZNy0lHlNN7Y/mNWu2NE8ZSBu9f5+nT1/v0U+EyP0rmvZFOUbe2mtcP6UwbyBHY5Pr3JnzQfkjwHiRtAgMBAAECgYAb8n7ZHhJZaP/2ANL/8xzaMK/hgesKoBMl+uTv726QymL82qPiaIJs0RygDQWNN3Quz1ueREdYk2NEWellr7XyvQ2dd/5tnp9tl1m4rLOsDggxRH4HoMkYez17XZkC/JKRJJL1jhObMewBaRyV4btjXOKJH7Q+EdX9ZSbxqMg68QJBAMal63dvnInSHZ1UVsa8UwvWzsb1PRVDEV1hmFkDcri3ZP7GLbtngmirn+YTxTfczpGS3dX2FXQaUeEHFJntMH0CQQDVtc56YpesKAhLSdVQFNgHF+2M2xn3wH6PIDtyZWe1h1F5YfegDCREVXcF5t9C4LLGP4RyV+revu6L6AqavnaxAkEAkSS4SHtPiE/y/XqbMPh3QWZBjZOwfvGTJYTiuN9RGnA9pf4NeimmWm5UFdsKNcfrUuhZmDUG9d8pRl9TCSkKTQJBAMCKVSW0UBwd0SMruRC2Rs9VQoNmHyY0epGuo4B0VhSZftESb4v1hHNAYMSjdcCGOusb7NMr3IKkmLzZ6Tvzn3ECQQDGaav8tVNairueQnjj4pPpfl3Ca960fviEVy0n8CFd7/Q7FtASf+MAn95mpG2lNVFIAvRzQtDQJkeO9U5k/4HC");
        user.put("51906161","de2403dbf08fd9add5896b89ccc9a22ca42dd65c0e5bd30f893b3efa240722c1,MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAImDSwu9qP1NL8PPoiRC1Jj0m/ppHrz5CtyvuBXEo3i9mc2cjMbv0c5tTsCcnS8uJtQGoim466rRisQeIaN69KlMxfo/VNYdmmCJokGlxdfP+W8QgZLBfH8wFmZgNBYKQUg/PRoSKEoQTT+Yqk2spETdRkWVTEdZjoYylgazdE+NAgMBAAECgYAO8pPGl8arRhJ3mqB5iD7beNdi+GiWymFuYgwaEvu2r7Fswrl2FHJ+Ga6noZPfzDNR9SiibWScoatlAbt5WEUI3wPRPSxIsfthXlJOFQ2iCdlf16Yb994nU66VSINGdM6x6tUJXytuebLOz+J2O84EvloCHM31VsK+DRSY65JftwJBAPnMMjCGTvV7LycQHdDY2K2cPEeZqDGkCYhGOtNc9Oqvll0+r+iWamlEPeHLhgth12kiEmAOdvzn1cdgq8eLfSMCQQCM7V/hbNRZW2cPS6xqnsX0hnOhQ7vYpySPfnEL3vhw7ZktDyW9R1Tvm/ECYRit5+E7MTXFqh511tHei3N7xgOPAkEAppV3TwbydyC8NEe6KoHCFh0f0fv1v40OUlPLfRL7vdp04yAf/XL56dN5lS+9569LETCIoohi74vH9BtS01MBkwJALUgwoLxZVwT5jn6gPfoaXUG+cbjT6P97zeew50GTzqVprILLe5AqCHuw6zTLu0Vgp6ZeQs8wzmhiMwHX75NmnQJBAOXqy8RZOl3I1FscWfJLip7lbHPyx/bDTQd1dcKFN5UD9D5LU4jM6KTDEoFOjkyWMUkQXWM2Gfmhsyy9MPwLbNg=");
        user.put("51906162","d2dd1070c0b689296aee2aa82a6e8a1f63c852a7f7fa1ac4794deec84d4f16ee,MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJxtioDvLBI1PNZR/whYdUVWVq8OHdr10imzrPDj6W+6yxG72BwVxdIW7CTGWy9YQXcan6aV6dhc1e2oEISw97qGdHjrzeQ3rNFR0Yg5o8dZj1dU9LIhQaq7U4rUqT6EpZU4a+j4mj2nCDz+9KbREIr54i/uaB9UIXxWQwzE9naRAgMBAAECf33IU/dUxkF7D8fn7LO+AaTBvOS6AqqJ/+s8GB4QIU1jQjwP/vv9pWKB3y8aPrqAJFDZfLOqvTPTsVoQ3l/dOW+boZ+9D4Qsor2/NdUUvKudTY20UO7Z5cPA6FQ6ekleq1x7hM8B8pk63Fp/m9an72XqZvRz4wWBdY/ElTiGrdcCQQDjqGqUnrZdz+qa9rSP59Tw4y8YFayoWDdEMlk5G9UtuPgiC74w5bMk0fkb7OknR92dzEgrsHKU2Sw6nBhGyI3nAkEAr+b8Qz4GjzLTq+Ldxfp7DbFCyr2GE7PJqlRO/agnBHa9JTVQlFf3ebAqvgT/ilIE0+KN6OCgVepK2eCGTtmYxwJBALeKn6sErljB+Q5Imlhu+2Hed6h+SP5MaPpFcWO2ic9cEkk4mHTJq/2if6lA/tSZfxXqjcETd72DvcHe+T+QD78CQEKRpfJErPLQh27LYqakDqG17dMQOoeGSNGfKm2wj2OPFDHapW4ehFeXJfXXqmXMeGs/l/kD2WnqSv19jRaWHs0CQQCb3oO/0KHfYnfSE3qB3qpCHPXXrJmv7aTNOjPSoWRmYieowR7cCfv+Xro07bx0PjXeuv7Za5ZO9gXf+YXH3EOU");
        user.put("51906195","db297b2a707e32bf075b55a0b0ed872f4008f7f95951720dab31c2b78af0efcd,MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAImhg+FaTeKoC0nZzoIN25qvuB3yEPvJs7JnZyJBC+NuH34zyk0g3u+Tsa1HIBrY2BmpGFv7Z6Af4vApj5BgNvbsKxMUGvIrtJpcWHNLUeWR/9f2n7m16ZdVMkyS5d5LlK/9dg1CctQ6UjWIJ9pxpXeC/YRgCUSOAvU29JhBWHTjAgMBAAECgYAzMv6YiruGofTxGDGWeAlF1jCsEyz2Pea3a3gWahQ+Gu1kSWfsoVnNzie+ykLLOffKM/l33mKCUtpddViO4PwQ+q3Jg4ofXvLyrNMcBhUqYOrmLTgbYrwZ6qcKDliqcfTH7/MSVyuHOcyZhr3vX3ZvMNFedwHQmBqh5nGOcZBygQJBAKV5Wsha4xUoNATmDX8sFaJLpxwxgkVuSumyTOl8QxOiST5Q++fHwlz6vOxve/hEHmNyxgbvXHbmM2CgVkg9UTECQQDU7LrtlhxZbsWFCrqLU6GWL1liMqbhw/RbBnsBi56NF6MNqAE+dywvim0bU6s9DAPTYxn93uYDFe6Ow5hA8MJTAkEAjT4co1AQ7Qo3/MZxVs/GV5XDumj4DVOrVfb/d3y9tJCdfDecDaeQ29SaViaC5tMeLTzWkyzU/BhaWmwBuf+8wQJAFRXkZQbKg+i+UrB/TLDZfZ/uwBS4Bf97wU/g+oQzDVHb6cxLYz419/dDWsNsX71C27oipsteB8wyNZ0VBMlS1QJAD+C7fpRHaBcZYi9D8aMrU58eZb0+2KotLDOjjG4FAjoo+htB9CAcZPMsp18P2V/XWWe8V4AWtbU+Y1qSs6WNfg==");
        user.put("51906163","8f999d5ff2a1047f3b5cc0e86a66b13a27a8cf6b4e30a2513e718eae9313b0f2,MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK1lDd63LzBUsK3Qzgn49GYsGl1lbwrQYjQbG6LMaUljlAmbq/RGmc8/lE5FGu/jlxf6+T9RF9pQ56C8Ut+c9stYtJgoaXb+2Yk0qPAm2qEY2VOnd9LfKhEL49/t4HkkCi4Ilhm9SRBK+jqYuQR0krxXq17BxIof2JzOT6YTMDIdAgMBAAECgYAZcmUwtE2WmDc9qxEx4XlsrThJUKOVQLPI1dmecMVa4eRN7Ky1ss7L3ZbUTcwAPLRl7o05v5ryjUERE9JNwuls/DiQRYGTkmqBhOBmZB2Sg4hXmXmsAiDpAZSvogaFzURfZMPA26JlXX9tDQ1oJr2WR3XdmSTBeyptPQfmbDeOgQJBAOipn2L7czBe8Dj9ITTKzPCKFJM9l5gRShFixOuO8tYdmLjezC2sRt43v+OEBZhW8hBajgGe8kvXDHUaUcKH0PECQQC+yYjfW8fCZI+GIjgfC42lmTX7HuId7D5r5mBoGMhELYKz3vjUW1YSFPoHma5rptDMZUg7o6IoVJju+iXyD/PtAkACuCjSwVmhURrBr8O5mD04+oQvDGM/NcSF23+tuSgBdKsaThBY4FCbvE8T27EtDKcbpdNPFYqDdGoC3GHkQi4hAkA6vm0RcP4R4cq7Xz/udy03BvSzDLBMzFfv+zBF/RN0wpqS7Z2qLTFUQfc2beBhryxtMuZJzrl4N4wbURRr8vOFAkEAv5V/nRMT5kw0wcBruZE3lkZU3TAt+AUYGR8gadhNYv4LtdJgiI0KQjRwd9GsnxvxF4vlXcEnvXFIZ5fE86vp/w==");
        user.put("51906210","ee50c189ab052ec333d74629f3d347a323918adba12467587866094fe2c3f00e,MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALjSG91Fcc5WtyaqORb7kMvrC1eNgzYBPs6PJcEs+cPgQMrKVr3MhrOhq7unLpnNOqA8KxyJ2aKHv/E9tExXGaL5Yj9ioUmLzqGffl10zj3fTuC5pmq80zoqNL5LD3V1hhJyGHcl/EoMUCAGA1m2wPKfHe7COChHLxk2qv+tPdYPAgMBAAECgYAiN7gz88b5ukFHdc2DDUlGD7q2V3ta9MkRihwl0zNcyfiE8wgzNJodyMlYQUAV9pKrlugubPEvr61gNxnZ5mhW3MVIb7XAI0SRN2zZp3L1wMqv4s3F6o2N8o1TjQiq6FJNXPDbjsxBGglG6LobwT3oIIX9Jwyw798CQ8JzjaU8gQJBAOtI3z8BrOrZ4JN4XCd30kqAbLnLjZnpZIwPNuPqD1ejNLkmo3QJzcwn/SU4dN8hIL7zQyxcsAQqq3nS/2sBHI8CQQDJF9IHlFkvx18N+/XhpdosxT1le8ykFGlTrw7b5AFGsnDenI6SoVpOr4xcSvvlSvQT9hlvOqKlx/JWNy5Ih26BAkEA5OMYYsfzeuiH446EP9T924JBwyE1WJRHP0WRxXiugjgAE1p2Sm07Ki+AwZPBhktI+tnV8DMe/H6yICpWVbdYGwJBAMQ0gbDzhbGgjZZPaLEp6Z4VNLp3+Et/vuTvwUe7R6N7J7WMe2+GD18G2aSTZQNsBooTbO2iAGBQnpqQbJBH1AECQEK3G9Kqznb/xNZjQBKdK4J+RjvT6pL4X+qq6sTstGzx9Iar0dWVhnGZLOl7HuT0iOW23w9lJUPBk5DBikSY3fA=");
        user.put("51906209","288a2de6296f67f7cd691aea3d30b58d2984e3510de6dd49761e708c7ad5defd,MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI18PIY7Q/WiuodftV+uc+EHxd+tW7Z56IVnMRAjKC0c3arl80gcMp8ixCgx1Zt7vfdxn2qnMDI1R9WjJv7VyIfiV6kt0TmFpdsYTBgeZUxn0yWwCG4XeVZ52mznqHFGSKnW7JwmmAsMdN2rNluBgZ30N/CzLYlCCqiSddr5LFr9AgMBAAECgYATCbURMNdXraJgxeGfctd337Zh/y84khXWIYkKDVkCHQC81r1SJi0vmZuAdxgxmEh7C8hyLvX+w4mbdx0vrC+T2GKa7P/7GiBoCge5z7jl0IdER7PJFZJRL1XPcn37Carus1OPgVqzDHc1F1MdoMo7q20RLWmpovA/YEmDcxvPwQJBAPVGA+1i/puv5ObK7TtEtZtDkHf1vvf2+wM6wOCHT3h/Avkn11RGnsO4XcY9ls6Hr3SrSxnuVlD48urnuz6H+KECQQCTrD/nwvNsucNvvKeDlXoPxvaDeefwTzpsRlMHKRmR0BX0xGktO9PbonVIdzWZXIhlQYdv6bgVk97VF3yIt7jdAkEA5hUl/fwNZ7KbAb1+yUhtLBc3YLDQ0f+H/MGSyY1lCV3sRhoPrukIagV7QknoPBmgh8tgChVAN3kxNxbm2YPnwQJAS+pCFAgBkgRRdzC5wQhn7pJvChnyZXSlaSIh7s1vKqmZj/Iky68083Up6+30rPmH2N3+HUc+bkFNTai8SaCBNQJAD9i7Y+Gp6B3S+jVw+jvjDgVqZlFlgkVV9/4+r1XovkK0EfrvqCKxiJUEC87tHiKt//aEY8znvb/LVn4ASN2U7A==");
        user.put("51906208","efb0f31c21b1acbb31445971f4a59c77ebc986d388e5a0a2d0fb5651d61633f4,MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIBl8x/oXtb3/WcK/020PDWTZpt47f3LH5CdtK8Nf75KcKOu/amCnjtEKWtm76nesySBkDV2TFv5I6UmwWg6xyRZUoljyTzbj9WeT5bdsJQE9NEBWP7nPsvh1L/tqlgkflWabmbuAe6aKr+VmmyIxtr3fnVJ4fXkr7O6RnUlasdXAgMBAAECgYAju/v7q+DFRFSGmpVuS1Scdo8Ogwv9X8xDo9iXBZNBYxdRPtEDHcKq7O/4H7hg1wWVieeYMXnT+RU2EACw4PbuHY+iL/j7/i5HG81VanIfYHjGISeOSzgtNMH57ELe0qDF+F1ClknLODVCO4FOLgWYhHUwwG/TCf/QkbkzMVU2UQJBAM7mM/28H1AoTpWithYjnSu1vvygiqVpC2QtzWabthYB3onhI2UvrA4QFC6s6yHLa+O+vzLCBWwJs7Jk/JDcnGcCQQCe3o7LkwykvdzU5GnRY5N9fMMKtgii5c8+N1zikkN27Dw2RNdpWTQyLBTVNDTiICLXvL8yk7t5zvRlFFuYuaeRAkAZg8YRmmt6JR5b0a8G1+mABG/DE8FFWMjw170m1DkGScC53CvTRKfBLDZw8x4kEsQBV/qYNdkZU7D3ZQDIlIwtAkBVVhD38uYQu9eaF5NroQRBnLmb0En1TmIy9kQThCNvBtgVAod/FCaAaMNL/r4FVOPCZKWDQm2qsWr/vJ6y+86xAkEAo19saZf8VptFsnQ7JsOgf3Lea8TsQlxmwH0mlE0f03+UxdrQ6koxTghDl0/G5t+v3SzC9vzMSF1A3CpxrDYU7A==");
        user.put("51906207","72bee5d05fa0d422061a1daef6f1493a09a03302ac20f4800c30a40d2ca24b0f,MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKxS1HZOzFKhTrKWTDqP8rOGC2jWcAS7x9qfU/a/Tp0jFomQalQ9pVNDplx7uWtbTxRsD1fg6nMtI0+78HV13jwd3pdlbpUaXKw9Jhe4gI5Bp2LX9/8XchRfyWVk0XEi5ZpdT4yg1VjjgZ8ExTSHv0fAG2mBl+zR7YjuFzlubpi/AgMBAAECgYA80iNQ+nDdPTfEio2vIb1cpZQDVaRF120reY6uCUma5pZ+ySzuPK/MWpUuETEZdinMIHOt5aF6M1VCQCgXnRlpc1De6RcFLCLiOGp7W76IQVrqOGlGWW3S3a8nET7/TqMjcYjjYrM2oief1oqvlZ+Sl5/6h3PBu4SQwFs23/vW8QJBAOotRAhtqpydzNu5285LEqKeO+fwbEnXfWVVVlRQXmNXIzjEe6z4vSe7Q8Hvndzwwafur2/sNQaZuAVTEcgNKDkCQQC8Ye/KU/78fSOslbTB3pTpVyM/ocRglowBQnUfpMgXdb1sofGDwOF8u2kxbjqMwcs9yVcC04OnFuXqgRKkTZi3AkEAkB4tiXZckqh25B8dDHedT2FMgvCu5D7vWEbNq3QOstp0vcIGTyyB1Qrlp1wdblTpCb3WUG4xMA+4deZqwhB+2QJAejrllpc3nGYp7oJYfW5JmrmCUnhI7g7gmrSfQD/v4GDx3nmZNk7BP+huEbee39OiZt2rylapVpIh7i1/hgIfMwJAPSdRjDK4SNIp/6axF+YOJ9G5/OE9oXEkRxEBYrLdqzXp2vJgAK7sQRsDoMMsJbE3S8spggznlxAnvNpjFOD7Rw==");
        user.put("51906206","0e4a5939f2177b19a4e645ef9c3de02c8cd34c62988439d3737e39db85b55fcc,MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJhEduIaSXIqITCtSwRAiyggLQvWGNK4xKlTKKOjCqMvrIgHOVYoyPPURiH9U76YCVCbajWNadan3YNva2EXLN7RDzmVdRCmThi3fb0Uq8i2glK3vm5tLiuXDX0qeI9ifkQtVRE273rVq+FGd85oMdmMDSTgs0JFqdNqzEFK0npxAgMBAAECgYARtB4dZkCFV0wiGHfpH+b+DJWoM2WR9H0bEcnfErkNJaEZ9LsQHzBUFylrUMSgm5Uzq8oyvUUhucU5v6XctUDYXk18f/7sDUsOmPG4uu+UKAAqmaGT59MV9KHqC5YHBA4yxWLRWEOnnugdahYkNjh22jiSEgJjdN4CHQBntZp9pQJBAMS3+CUjboTMbpPU4Uni6mMy63znaXlJ6z+C2dMExleSZVw663YBVO9C0NgSI10qpzkxe3m+VmIhgaxmHPF1W9UCQQDGJ0TiDi+Zp10vgAycjaacVshj9wcOSEKJBHIweBLrYivAOaDgCFleUXfnJqODe+BMi19ElqZRXKkpnU81xP4tAkAUOmKe1wTfTPI161b7NQUrDCpg3p7e5PX2wurJIx2OFbSshjZ+HObx72nwPfU6+E7Xt7ndq+/QCWf/JUTmo029AkAKhA79yC7ygpVJbgw9Sq8q4Nglpfhz45XUW64LsHOglkcoHmbRPOAxRzWLyNsooSxYI5VcuHn/G2HG2ZhYjCnJAkEAtKdtLcGneRINWYdj8hgA+R7lvRxo/qHRC5wOICzJ2DRchWo6AY2MiOvg7D54tneH+5R/oZ9SjHtbaQmi53PLXA==");
        user.put("51906205","7e22ea6a55db7a8ab390a48d9e0f9b37d3d04bf7ae5227a76d8c3b0495ec5ed4,MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIUYJ+sXWGK8N5BlZUwCvi1Ya+rrAcGX2l/iqZcq9gv875omoKksZ3NK8uepNGIpcYtHwTgjthmxC1rne/eG5Uu3sRARKHVbznmkbYYK2Ui1FPgU3pt1fmFThAkVz+Gj0lQ4zXW5RDYebOyxAY7+NRQaPPkmTKrow4b5hEzaWZ4PAgMBAAECgYAhCAq4LNYRwxajLpcCU1fxlxVJe0O3UUGwNGfUjaP8+6xXfKi4871E/t/kILlQd2qnn4pTrYE/RRJFYFgC6aa0UZs/kUWvqUpnKsY3JFRVxU9GGBY9tk5jXwZOmQD4q+HLpF1g6Vx+FT48jtdv+ONduBfa1O438xnaIze57vZDMQJBAPXh9q+gGH636I5Y3MDjT1atkIKHVAMfzr53zeVWKzTjPlb5O7JE2uE14K2OBRtG8AU5LKKAamF0LCxvNocGjokCQQCKkh9Od+aFIsXzqyVunDYQrraLvaqgmB7gQVt7RoSKwPH+dWCPr+W3wZ53cz4UrUkP8BAeW2884cIQz5BzpmHXAkEAs66RKv5/Z/rVQ1DE4WA7Vkg31ms2haH031OFbxZtNJTrtXskmL2ghfttWObz7F8Gf4qsh4P4OYc/0Kjk2/96oQJAEkzP454EDIsWF2L/04a/wR5Br1zB/ul4EmevPC2I7YYjr1YzBWbWcJkaY24Nwc9MudUbW9b5btHlvjCbRBqyCwJAHps19VUwtlpSFDrSFP8Y16MSz/VCt9hRk5NbAOjRBCCEqnVKXCtdQbsSfaQYBBdjHPIVIhOEbMHUHrlVd4tH+g==");
        user.put("51906204","3bcd2eee0b6f53d8f290688d665f376097a68b40e81c11b828127c5c055a8cec,MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKOJvdw0t2A6fxXLKRvOc11o6bmexKFyk06WBMHsXq8vFd2sKgJUBhQ9LFQc2rTLxzFsxAiu2vMwE8RdLqfG33lL9k3CXBRCvOpr2FowfCZOL+5ygIHSwpM2ctXJ5klPjZ4/SHPOWQT5RtsuiW7JOvE5xk3c088omDChY0rKfpMDAgMBAAECgYABYboZNnqhb2ujSFHhaomXLaLuF5DeSoYr6yxjqZya05ccKQDd3Lb4zEJttsebX0XDTw1QmuvJwgtqKoB6UprWwTP3okNa1YIYyUV5JexEbTHHTIQopj2123Qiy6ktvpNpY4YRCNQDHRZ3z+a9MJkEUXHJGMEoAzFHRjZ1TdE/YQJBAPEpg8Dj3YAoQJiAWwS2D2yqtjpOewAAdsCHYDokS7SIpo3/f47OpPXTzfuIx36MhCHpK1O15MxjgOCmHlhrsTECQQCtmZa/6MPqX1F6X78Pt2K9iFqW2AHuX4yaUfq4U/BajvdClGQCs+KCkjMQnrmrweHuasZ+fhocu7iFI5y0TxpzAkAIMPLKsHKrkbJv+5wb0ts/Q6Ug4d2HqHxgGbkJAIaZwTJ3DECW5ynvN5x9eK3d/IPYawUPuNPmpVKRTtMlCbNhAkEAqqg0JBCEmGB/zpdVUfPro3rp4yQSMe3IYOR6Xr5VqBCnTdJmxqlj4QwsGwiOaiS9IA3jd/IrZVe9O1UY9cBVtQJBAIOPE6erZrlLVTK6vHf2I5r0lRNIZfRixx0PZE6sPvutTii6fPGHAylEEjmpwk5Yk34jy8023JPwHMqm8kz4qUg=");
        user.put("51906203","ae4b24c7efb78465e9e317a3a165b6ffadb47436361fe6ff23fa923708ee55bb,MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKKP2GSsUXEov8a69v/cwO6JgTLCrMmmYl1FlcxpQ1MAm18hvPdJEDaVABpr/rNGkAKl7XuWn/MUOuj2GEnu/Od4UusYY+dUcQ3NRSb9+9z7mA2bhd/haSGgz+JVo3VZRVDeRyJxNmZTWf+U5Jn+n4ysXLgEsS/+rMHkKrFveKQVAgMBAAECfzwqgSzBmmMHcfNbv49zZPTkb60Z4FnB+yYKWycMzp4p21eaZRYhNJ7/BsrArcQsmaBhSUQ0u358PlF6xAKZMYDjQMJXRuC5b0q2d/K6yqc2D/w03EcL2IuboCLvuSLX1FzXd22vjsY7ttTemlpk0j6KVbPVd0G0UajpYte5d2kCQQDPWFj5oSyFAANbYOzUgr+beqCYxXCNtmDxMYx4umN1ibRrgw58+lAnHQOXujIJSyNhVBj1uki5sywTpEI3T+FpAkEAyLVG4+XwKyKkuiNJKYAvmvkYhuJAS+9L9Lq4UB48Q73Bz8QGCIkybnbmJd7UCnkwQLAnuLneB021nRDkc3CrzQJBAMYp7qdttanLMGomAVK9PzjugwxfLsoRsybxLwq3QJU2LbvO/orNhMtM/IEHUAj3yfGcOVqoVhc8uXRwOWS5sHkCQFYfw22ji21XDkx5O7m9MbBUVzv59hEfe/5l6RUoBEAlOvdGbDhZdvSDvLpfZamg2x3G+SSBTU7g/5+IUh9fJ7kCQQDHg/7Xvw+0XPLLWA4FOOBka34TQDpoh0KHzuoEnotd4wegULJynDf7qp4sJ7GtQGGgcyhP8dYQtcM/gvUyKxKc");
        user.put("51906202","c83071ae209f090626bb17c0807fd420bd6039c28cfc2f0d6dac83c4958b56cc,MIICdAIBADANBgkqhkiG9w0BAQEFAASCAl4wggJaAgEAAoGBALqzArbPb3eKkSDwL4r1kRsmbgRGWi2MI870kHfOGCwQV6ShVRtioati2MmLSMWdcXcbjPE9TWprMcWt7vZWZ9ePRvuaEg4fz10mlJG7HXqT9OcWAwqI49bP51Ppuh8+D6pTUhLuE4/CaDUr//rOCTuWsN2HGpYbrYq3Lqi1zPExAgMBAAECfw9Qn+hfG8WymDUbfsrnWf/gJyYynzMdS9KuPSuWvGTg8k+O7GxSqvBkK2RdCJstTyF2IRmo7bDNGkgc/H04mUvxhhfCr/6T4J5XWDnBc5oU+mQjC2IDsZd7YS3cjk4lEZMNojtaHibWLRU3yjIxcS2IQ8xkLy8Tsa7CXi9SCPUCQQDoN4f/w0rHiqC+Hwc5d9T0lOXe6NgGtG7IIcbKrQWDTE5efHWPqkFyULi3vAm21D6SP9RiW2pqPxF4xG2yoI7nAkEAzdIOnPxqx4t8ym6hqidiKpcwk1DQREyWppjrtJEpkaI15htINZPtWEk6ssT9fSRGePMZhns0sutPJWrpemX0JwJANybBMRhj0UJ2bNEXuSlhtXkLo0Zv0B/YU8XZ3db8ATQFROGQVj+cbZcIV/Lb58U+2yM3wOhvPK6f6qvUVeFWLwJAAcI5vb2qa+oVNuWbHAkaHaMNFzUpkmvVHbdQzEoxQL1SGJVDGLIa5RY7Giv0vo0AzSfGoA+Nc7nvAzLWq+UTIQJBALcI+GAqCG+Zy92svvalcO9jnz9TEnbXasC6SEqxACVISRTOOO3o60Z6qJwAyCttvZ/pK5De/g4t1qB2XT9rk64=");
        user.put("51906201","13adc9e668379fb8a6413c752756d541cdef654c7f9fb7be1dc3d7bd3072e05d,MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM4EItFxgOeSdnfVkSlAPkhGHfdsa4+2zR2Xz/umSJiSohm1Myamg4/+PPq3x7vLUFXoTBM/FjMS3CUn63eqnicraRwz8ygEjv73AbmRPr3G5qSbB4/GOA55ExGz6i2+AwEPsg7Z9i43sTTm0ue+CnwQCPoseiAc7knO/kIkP8UVAgMBAAECgYA6vCIjHV2XaAHfi2Nt49N97oOnoWEPIZpcCAazY8aJvTB67Nau+8VBfcXr4w6OQ1pdi7N4v0E4gXWDIFz5Cos486uiUvxDSbot3CvM2+mTh1C7XNQLxua3adOaTLWUMXuTlYn2BrZLhKfTAXMt2CyK62JQeEWmcHgKkN4fsORppwJBAOSaoH57X0xYvo6Y45cL+9I32yyqbFE0hagIvX4jQQ/sPEXjjm+QNUwushgtah4ndvHMDXaEMRbmRYJoYDTr1EsCQQDmtIiC0Dp3kDLhRb1k/HzuPxlesqAsRjT992Kj1yXoufLz8a21m00UikqBV34c6yoAs6zHojIoZOkRvHpfyTAfAkEApgiETrjuUzYVWN5EVl1WzazR3BP/Tc92z79hWa7jP1xVvnDy76Zuf2Fe6l55t2L0adPJZ23FdZtTms6SKX2rFwJAXOGYCBpvP421fk6Ghq7EWWLcAu0lYU3OreS4OMA4ye57Ks1FI1Vn/foDLvmbk2b2HRw5VpItYWfnEbXxCrlD/wJAF00dKw/Li98zAF3HLE9QSM/FGJjeOrmQNsyuKlRhyQKf2dHA7vMR0P/VxkGJeEnmqUXhz9qjMXK2aKkiokVzlw==");
        user.put("51906200","37739d40055b9b894eb9a722c30c3bd556a66293f2c65fb56a328ff484d50ae1,MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIIFpU7t/DGLktRSmFOe0znG4dKqeQdh+5O68kdc057KrKOqsv+kWXCnFp6f3pDm6zgVduYJoR/gk8QxlwMjKCcOIL1w4Wskl3wDOaitySq9PfHRpLoDiZGd22oCjb+1Us/sYTrbJHb6Me24b2MgmTa0mW8Z0iX96ywDC1VlbtNDAgMBAAECgYAGknHtdZRf8Wa78Xl+ZIuCItcZK/NIQqN1WfzhZ8QJ6PxXDwuaZgrDhjoivwC5QRHro2jGn/76b+YREgvzUAkAZ5bwvqNbRFrYoXWl+JI3jSVdZ+3WJ9KlQfFPshmKOOoOaWOOXDLkUQ4yUfoU+ZSAeXIwNKH8tUrtbAnTvBaUMQJBAOkg99XGJprJScNQK2Ix7EGLcZ245bTt33WbFJX2ZmR1gp+V8ptJaCsQQKHpNyPRZOocOhUmSlRhsk96pHNOnJMCQQCOxyap/gqlw31hmP+nmZ8ooa8aKe40hRtNplMXV12o9bS6FX7ARrrRZKQAshaHkt2FKQdGAok6ybjGAv1DMcyRAkBSAu4MvyNvimYvlnFcnvFc8YArmxL7/j6BMeghBUhqCMjF+Usag81ocNQ74T4rlIsqrHlmXU0CuEexHBTkF4CPAkA6DGP+1ydTD7DpPkAqzv1zkpDTbVrIhZl9L3M22TFeIU9yHI0k9Vy9B1LaaY1ZX2+q1Ox9HUjX2u3Bq7N2X0fRAkEAtUEXn1SaMD+5QQbrG8Kl0+xCVnHFHhrPkMZXR/QNJrdOYP2zQ5jSboNYdYfQQPC3MtV4BwteCp56z1zPXZF05w==");
        File plan = new File("/Users/mac/Desktop/plan.txt");
        FileWriter fw = new FileWriter(plan, false);
        JSONObject jsonObject;
        List list = new ArrayList<>();
        // 批量撤单，每笔撤单数量
        int p = 20;
        int t = 0;
        for (String s :user.keySet()) {
            int account = getPlanOrder(0,s);
            System.out.println("挂单总量="+account);
            for (int i = 5000; i < account + 5000; i = i + 5000) {
                ResultSet resultSet = getPlanOrder(0,s, i-5000, i);
                try {
                    while (resultSet.next()) {

                        jsonObject = new JSONObject();
//                        System.out.println(resultSet);
                        jsonObject.put("symbol", resultSet.getString("contract_symbol"));
                        jsonObject.put("orderId", resultSet.getInt("id"));
                        jsonObject.put("orderLinkId", resultSet.getString("client_order_id"));
                        list.add(jsonObject);
                        t++;
                        if (t == p) {
                            fw.write(user.getString(s).replace(",",";")+";"+list + "\n");
                            fw.flush();
                            list.clear();
                            t = 0;
                        }
                    }
                }finally{
                    // 关闭资源
                    try {
                        if (resultSet!=null) resultSet.close();
                    }catch (SQLException se){
                        se.printStackTrace();
                    }
                }
                list.clear();
            }
        }


    }
    @Test
    public void test19_1() throws IOException, SQLException {

        String userId = "51906164";

        File plan = new File("/Users/mac/Desktop/plan.txt");
        FileWriter fw = new FileWriter(plan, false);
        JSONObject jsonObject;
        List list = new ArrayList<>();
        // 批量撤单，每笔撤单数量
        int p = 20;
        int t = 0;
        int account = getPlanOrder(0,userId);
        System.out.println("挂单总量="+account);
        for (int i = 5000; i < account + 5000; i = i + 5000) {
            ResultSet resultSet = getPlanOrder(0,userId, i-5000, i);
            try {
                while (resultSet.next()) {

                    jsonObject = new JSONObject();
//                        System.out.println(resultSet);
                    jsonObject.put("symbol", resultSet.getString("contract_symbol"));
                    jsonObject.put("orderId", resultSet.getInt("id"));
                    jsonObject.put("orderLinkId", resultSet.getString("client_order_id"));
                    list.add(jsonObject);
                    t++;
                    if (t == p) {
                        fw.write(list + "\n");
                        fw.flush();
                        list.clear();
                        t = 0;
                    }
                }
            }finally{
                // 关闭资源
                try {
                    if (resultSet!=null) resultSet.close();
                }catch (SQLException se){
                    se.printStackTrace();
                }
            }
        }


    }

    /**
     * 仓位保存
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void test20() throws IOException, SQLException {

        String userId = "51906168";
        int account = getPlanOrder(1,userId);
        System.out.println("仓位总量="+account);

        File plan = new File("/Users/mac/Desktop/position.txt");
        FileWriter fw = new FileWriter(plan, false);
        JSONObject jsonObject;
        List list = new ArrayList<>();
        // 批量撤单，每笔撤单数量
        int p = 20;
        int t = 0;
        String symbol;
        int leverage;
        int quantity;
        int closedQuantity;
        int freezeQuantity;
        int unfreezeQuantity;
        BigDecimal openPrice;
        String type="";
        String direction;
        BigDecimal price = BigDecimal.ZERO;
        String orderLinkId;
        Random r = new Random();
        int rTmp ;
        for (int i = 5000; i < account + 5000; i = i + 5000) {
            ResultSet resultSet = getPlanOrder(1,userId, i-5000, i);
            try {
                while (resultSet.next()) {
                    rTmp = r.nextInt(100);
                    symbol = resultSet.getString("symbol");
                    leverage = resultSet.getInt("leverage");
                    openPrice = resultSet.getBigDecimal("open_price");
                    quantity = resultSet.getInt("quantity");
                    closedQuantity = resultSet.getInt("closed_quantity");
                    freezeQuantity = resultSet.getInt("freeze_quantity");
                    unfreezeQuantity = resultSet.getInt("unfreeze_quantity");
                    direction = resultSet.getString("direction");
                    orderLinkId = String.valueOf(System.currentTimeMillis());
                    for (int j = 0; j < quantity-closedQuantity-(freezeQuantity-unfreezeQuantity); j++) {
                        // 市价/限价平仓
                        if (rTmp <= 50) {
                            type = "market";
                        } else {
                            type = "limit";
                            if (direction.equalsIgnoreCase("long")) {
                                price = openPrice.multiply(BigDecimal.ONE.add(BigDecimal.valueOf(Math.abs(r.nextGaussian() / 5))));
                            } else {
                                price = openPrice.multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(Math.abs(r.nextGaussian() / 5))));
                            }
                        }
                        jsonObject = new JSONObject();
                        jsonObject.put("symbol", symbol);
                        jsonObject.put("leverage", leverage);
                        jsonObject.put("quantity", 1);
                        jsonObject.put("type", type);
                        jsonObject.put("direction", direction);
                        jsonObject.put("price", price);
                        jsonObject.put("orderLinkId", orderLinkId);
                        list.add(jsonObject);
                        t++;
                        if (t == p) {
                            fw.write(list + "\n");
                            fw.flush();
                            list.clear();
                            t = 0;
                        }
//                        if (accountTmp != 0 && accountTmp <= 20 && t == accountTmp){
//                            fw.write(list + "\n");
//                            fw.flush();
//                            list.clear();
//                            t = 0;
//                        }
                    }

                }
            }finally{
                // 关闭资源
                try {
                    if (resultSet!=null) resultSet.close();
                }catch (SQLException se){
                    se.printStackTrace();
                }
            }
        }

    }

    @Test
    public void test21() throws FileNotFoundException {
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
        com.zmj.demo.HttpUtil httpUtil = new com.zmj.demo.HttpUtil();
        String symbol = "AVAX-USDT";
        String after = "1688029200";
//        时间粒度，默认值1m
//        如 [1m/3m/5m/15m/30m/1H/2H/4H]
        String bar = "1m";
        String url = "https://www.okx.com/api/v5/market/history-candles?instId="+symbol+"&after="+after+"000&bar="+bar;
        String header = "{\"User-Agent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36\"}";
        String r = httpUtil.get(url,header);
        System.out.println(r);
        /**
        // 处理数据
        JSONArray res = JSONObject.parseObject(r).getJSONArray("data");// 开、高、低、收
        JSONObject res1 = new JSONObject();
        for (int i = 0; i < res.size(); i++) {
            JSONObject res2 = new JSONObject();
            res2.put("okopen",res.getJSONArray(i).getBigDecimal(1));
            res2.put("okhign",res.getJSONArray(i).getBigDecimal(2));
            res2.put("oklow",res.getJSONArray(i).getBigDecimal(3));
            res2.put("okclose",res.getJSONArray(i).getBigDecimal(4));
            res1.put(res.getJSONArray(i).getString(0),res2);
        }

        File websocket = new File("/Users/mac/Desktop/websocket.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(websocket));
        String d = bufferedReader.lines().collect(Collectors.toList()).get(0);
        JSONArray data = JSONObject.parseObject(d).getJSONArray("data");
        String id;
        BigDecimal open = BigDecimal.ZERO;
        BigDecimal close = BigDecimal.ZERO;
        BigDecimal hign = BigDecimal.ZERO;
        BigDecimal low = BigDecimal.ZERO;

        String okid;
        BigDecimal okopen = BigDecimal.ZERO;
        BigDecimal okclose = BigDecimal.ZERO;
        BigDecimal okhign = BigDecimal.ZERO;
        BigDecimal oklow = BigDecimal.ZERO;
        BigDecimal rate = BigDecimal.valueOf(1);
        int account = 0;
        for (int i = 0; i < 100; i++) {
            id = data.getJSONObject(i).getString("id");
            open = data.getJSONObject(i).getBigDecimal("open");
            close = data.getJSONObject(i).getBigDecimal("close");
            hign = data.getJSONObject(i).getBigDecimal("high");
            low = data.getJSONObject(i).getBigDecimal("low");

//            okid = res.getJSONArray(99-i).getString(0);
//            okopen = res.getJSONArray(99-i).getBigDecimal(1);
//            okhign = res.getJSONArray(99-i).getBigDecimal(2);
//            oklow = res.getJSONArray(99-i).getBigDecimal(3);
//            okclose = res.getJSONArray(99-i).getBigDecimal(4);
            if (res1.getJSONObject(id+"000") != null) {
                okopen = res1.getJSONObject(id + "000").getBigDecimal("okopen");
                okhign = res1.getJSONObject(id + "000").getBigDecimal("okhign");
                oklow = res1.getJSONObject(id + "000").getBigDecimal("oklow");
                okclose = res1.getJSONObject(id + "000").getBigDecimal("okclose");
            }

//            if (open.subtract(okopen).abs().compareTo(rate) == 1
//                    || close.subtract(okclose).abs().compareTo(rate) == 1
//                    || hign.subtract(okhign).abs().compareTo(rate) == 1
//                    || low.subtract(oklow).abs().compareTo(rate) == 1){
//                System.out.println("k线对比有误差! 时间:"+id+"   ok时间:"+okid+"  开【"+open+" "+okopen+"】  收【"+close+" "+okclose+"】    高【"+hign+" "+okhign+"】  低【"+low+" "+oklow+"】");
//                account++;
//            }
            if (open.compareTo(okopen) == 0
                    || close.compareTo(okclose) == 0
                    || hign.compareTo(okhign) == 0
                    || low.compareTo(oklow) == 0){
//                System.out.println("k线对比有相同值! 时间:"+id+"   开【"+open+" "+okopen+"】  收【"+close+" "+okclose+"】    高【"+hign+" "+okhign+"】  低【"+low+" "+oklow+"】");
//                System.out.println(id);
                account++;
            }else {
                System.out.println("k线对比有误差! 时间:"+id+"   开【"+open+" "+okopen+"】  收【"+close+" "+okclose+"】    高【"+hign+" "+okhign+"】  低【"+low+" "+oklow+"】");
            }
        }
        System.out.println("ok总条数:"+res.size());
        System.out.println("对比相同总条数:"+account);
        */


    }


    @Test
    public void test22() throws IOException {
        Jedis jedis = new Jedis("locklevel-redis-dev1-india.8wuaih.ng.0001.apse1.cache.amazonaws.com",6379);
        String key = "kubiex:market:orderbook:spot:linkusdt";
        String value = jedis.get(key);
        System.out.println(value);
        JSONObject jsonObject = JSONObject.parseObject(value);
        // 买
        JSONArray bids = jsonObject.getJSONArray("bids");
        // 卖
        JSONArray asks = jsonObject.getJSONArray("asks");
        File orderbook = new File("/Users/mac/Desktop/orderbook.txt");
        FileWriter fw = new FileWriter(orderbook, true);
        for (int i = 0; i < 10; i++) {
            fw.write("价格         数量\n");
            for (int j = 0; j < 10; j++) {
                fw.write(asks.getJSONArray(9-j).get(0)+"      "+asks.getJSONArray(9-j).get(1)+"\n");
            }
            fw.write("\n");
            for (int j = 0; j < 10; j++) {
                fw.write(bids.getJSONArray(j).get(0)+"      "+bids.getJSONArray(j).get(1)+"\n");
            }
            fw.write("\n\n\n");
            fw.flush();
        }

    }

    @Test
    public void test23(){
        Jedis jedis = new Jedis("locklevel-redis-dev1-india.8wuaih.ng.0001.apse1.cache.amazonaws.com",6379);
        String key = "kubiex:market:orderbook:spot:linkusdt";
        String value = jedis.get(key);
//        System.out.println(value);
        JSONObject jj = JSONObject.parseObject(value);

        JSONArray bids = jj.getJSONArray("bids");
        JSONArray asks = jj.getJSONArray("asks");
        System.out.println("bids="+bids.size());
        System.out.println("asks="+asks.size());
        BigDecimal sum1 = BigDecimal.ZERO;
        for (int i = 0; i < bids.size(); i++) {
            sum1 = sum1.add(bids.getJSONArray(i).getBigDecimal(1));
        }
        System.out.println(sum1);
        BigDecimal sum2 = BigDecimal.ZERO;
        for (int i = 0; i < asks.size(); i++) {
            sum2 = sum2.add(asks.getJSONArray(i).getBigDecimal(1));
        }
        System.out.println(sum2);
    }
    @Test
    public void test24(){
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            double number = random.nextGaussian();
            while (number < -1 || number > 1) {
                number = random.nextGaussian();
            }
            System.out.println(number);
        }
    }
    /**
     * 查询用户挂单数据
     * @param type 0-订单 1-仓位
     * @param userId
     * @param start
     * @param end
     * @return
     */
    public ResultSet getPlanOrder(int type,String userId , int start ,int end ){

        Connection conn = null;
        Statement pre = null;
        String sql = "";
        if (type == 0) {
            if (userId == ""){
                sql = "select id , user_id,client_order_id, contract_symbol from swap_order where margin_type = 'CROSSED' and price_type = 'LIMIT' and side = 'buy' and state = 'ENTRY' order by id desc limit " + start + "," + end;
            }else {
                sql = "select id , user_id,client_order_id, contract_symbol from swap_order where user_id = " + userId + " and margin_type = 'CROSSED' and price_type = 'LIMIT' and side = 'buy' and state = 'ENTRY' order by id desc limit " + start + "," + end;

            }            }else {
            sql = "select symbol , direction, leverage, open_price, quantity, closed_quantity, freeze_quantity, unfreeze_quantity from position where user_id = " + userId + "  order by id desc limit " + start + "," + end;
        }
        System.out.println(sql);
//        sql = "select id,client_order_id,contract_symbol  where user_id = " + userId +" and margin_type = 'CROSSED' and price_type = 'LIMIT' and side = 'buy' and state = 'ENTRY' ";
        ResultSet resultSet = null;
        try{
            // 注册 JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 打开链接
            conn = DriverManager.getConnection("jdbc:mysql://locklevel-mysql-dev1-india.cluster-cwgnjbpmvvmh.ap-southeast-1.rds.amazonaws.com:3306/bib_cfd","dev1_idina_master_rw","4x&v_6bu^x!x$m$#*rv_$tx6c+udfo9@");

            pre = conn.createStatement();

            resultSet = pre.executeQuery(sql);

            return resultSet;

        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 查询某个用户，所有挂单数量
     * @param type 0-订单 1-仓位
     * @param userId
     * @return
     */
    public int getPlanOrder(int type,String userId){

        Connection conn = null;
        Statement pre = null;
        String sql = "";
        if (type == 0){
            sql = "select count(1) c from swap_order where user_id = " + userId +" and margin_type = 'CROSSED' and price_type = 'LIMIT' and side = 'buy' and state = 'ENTRY' ";
        }else {
            sql = "select count(1) c from position where user_id = " + userId;
        }
//        sql = "select id,client_order_id,contract_symbol  where user_id = " + userId +" and margin_type = 'CROSSED' and price_type = 'LIMIT' and side = 'buy' and state = 'ENTRY' ";
        ResultSet resultSet = null;
        try{
            // 注册 JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 打开链接
            conn = DriverManager.getConnection("jdbc:mysql://locklevel-mysql-dev1-india.cluster-cwgnjbpmvvmh.ap-southeast-1.rds.amazonaws.com:3306/bib_cfd","dev1_idina_master_rw","4x&v_6bu^x!x$m$#*rv_$tx6c+udfo9@");

            pre = conn.createStatement();

            resultSet = pre.executeQuery(sql);

            ResultSetMetaData me = resultSet.getMetaData();
            while (resultSet.next()) {
                return resultSet.getInt("c");
            }

        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        return 0;
    }

}
