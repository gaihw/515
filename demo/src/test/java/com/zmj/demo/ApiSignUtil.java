package com.zmj.demo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.function.Function;

/**
 * @author: north
 * @Title: ApiSignUtil
 * @ProjectName: saas-api
 * @Description:
 * @date: 2022/1/19
 */
@Data
public class ApiSignUtil {
    public static String pri="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDDTP5d+TLqdxOHjGPMqwPDFF99JeT1uOhpkvwlitOPA40sMZtpL3Zn/WaMzyA696tWskerLREtKJoOc/FX28pDH7O62T2+CWnIa3Yy2hXV+x0bBW0Q5pVJwkfQvOof2Z8Qoaa+k1IVHyXTnuqfrOMqzG9DUoydc1/kv4UzNoJ17+4+uPTOS5oJCuB1fCjllunuGYFwybiLUx3W06SNdi0K2xqp8ywryVM+NWkvrt9x23LcHmdloCgZC3JZ0DlYnTl9hnDkLNDxJjVLcpDpvOQuZp+gn+dNZORvWn2bMRiLVMNBTV/8sWlnWkJY8Jlnl0nZ9jvw9e6vokp3RPqcc/CnAgMBAAECggEACKUe2WAPjetpdOWVAVlMmFUNiQelY+8kco/sE2laxgjdNeiYPYa5Ug8YfAErJxERh4vqujwLd5lFgIBHXaFACcqcdRfqsL/P6+w91jBrKQatmiWaV1Yto48zCJ1kK7bBYMYXEHaK+p5fWUe+kQThJWLZRfygdtPFmeWUatjt23SIkm12WudIrGEJZOENsMIcmrcVR3BRk9fLrYmI41s7uO5PACDK2DaAKRwgDMSltr9rxD0y0/Y3aEQYJbOZ7az9wJjgvBLfcqb5EzabrtR3NBM/EsW/DyFh7NmhVhf/vhqLdFWIL8Jcsz5B8lAHGoy0XV2Gigma8DuqYpPDHoTQ6QKBgQD0cz+ERBLFoObl0PU3kU1j0Mi4ETNstt6piATJj8l+YL7ZzpdaerGzPSESeZjp0cfVFEATg3A0zJ1uEpaQ162YvfLl/DlK395JP+RKC4dzQUN2ESeUd0HUkjchCltHSy33F3Je689Ug9uCjJGs3sM+fdsrgBwlfLel3UU9uEL9VQKBgQDMh0Hz0R1sN/vvh/v+fpMAFcEJCmCkPND6UsKj2N2QNJCRHyZBrLzgMWmob5q578AXPIgxt+nwbp/tWjheAAQ9VQIsT6yd2PF3MRaJdGOtxJfPSJ/17Mbtx9JskALs3MMH6pWvgGB/K8WX8wZJ73mDGkjIpckFfBtjXeQOOpbWCwKBgQDab/3rMHVh2nVaGc52eEYiktg5+0zrscHo1l4Iy48vej4xHbYMKSWP2GksPDKThl0+oBjeFw72b32idcPL7J74pYxfTXLY4JGe/RP/wquoJ0KkR5IJzkOqM3pF8R496AVMDsyp26hqimVmFDy8sFbiCc8G4TTFntvwwHh2PajG/QKBgQDKgY2alE1WjiPjqbem5roz1lY31k+TrieYAoN3aU0O8AzHs5jUY+zq6eHchQwk165RE30iBSAbD91HTBINeGS6OUYai5S1AU0rn63Z0SS0s7c/5H+FJrhcTIIbPYe38GkmmG02xJxGrhdJeLWcVQDx/v9bs1JTHOudOiSIthgMuwKBgFIpsPwYyAypzjFlGzhOAFAKGq6IRbNBW/rcegGNA+RZRFILLhZNQ0FiVvdr6lWYcDVrMDGOauw/8Mf1FPs+TiZ5uye1j7qQX6v2qmH2heT45SYnpi2UPY77fWQEjx43rl8RvdunCNibpqbnqh4pSRAtXbG6QyDgSVXeJ1M/fvWy";
    public static String pubKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw0z+Xfky6ncTh4xjzKsDwxRffSXk9bjoaZL8JYrTjwONLDGbaS92Z/1mjM8gOverVrJHqy0RLSiaDnPxV9vKQx+zutk9vglpyGt2MtoV1fsdGwVtEOaVScJH0LzqH9mfEKGmvpNSFR8l057qn6zjKsxvQ1KMnXNf5L+FMzaCde/uPrj0zkuaCQrgdXwo5Zbp7hmBcMm4i1Md1tOkjXYtCtsaqfMsK8lTPjVpL67fcdty3B5nZaAoGQtyWdA5WJ05fYZw5CzQ8SY1S3KQ6bzkLmafoJ/nTWTkb1p9mzEYi1TDQU1f/LFpZ1pCWPCZZ5dJ2fY78PXur6JKd0T6nHPwpwIDAQAB";
//    public static String pri = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDLwKjH6TuinYyK9TnJjNvQPfW6fKxuQ/E2WqP4M8uSClIBVt1b2uqKXqHt1N/e6ep8rlbVmOk12aa/PBAuqseZdSuaZ6FD1WBLS0v761Shm1hvi5YUTSlszg0NzA/UAapXxpGXQlPL+Gblv+YgY2QUorO+mkokvScZmbGTWTI/WwBgOyp0OduVvqyDZ7wrPyGvmhB4SW9VB88xiVOMRenQd7tDyrA5dnnSHdZY50YU1gDICmlLEU8W6GsrMUSXf5iD/2MJlO++ZyIdEttMdjpK2wzPomwmlxJAVMyrWEXW0GuEMGhG40IZZUT9jPKZB8s4ZPRGHwQclJW95fa4I38JAgMBAAECggEAJK38NHDiUXX3VRSsYIJBGA3vbLEBAaCtjdFnb0kzvoipFqCinOSeEGbU09Hcs258zhE8hJtQcGdMQ2T2rNAFurSDMvqw51tESIm3lhRZWfZzRzFjxSfW60V0yCUSPGJIXiDbGNXR7Ag9zeJr9SyvWZ+oqAlyi4aZwVwPwazvBMR04bnyF3fJGME8RUVmCA5biE5Xwa/ykO8hUq21NkzV2Ii58nVzJdk5UmszRwTCCfg/Qyrdr89Uq5Nyv3h0/Rn/McPnjXafUC+qvQEm0m3OI6hxmWiw6bYirObJ3xek42N23+5pcxmvnAaq4izqs8C/kbVzzDfvFHEWHzew2Vgv2QKBgQDnxRGoTp/j80N67XYc23sZO4DzZ9hMwCjJXheYyTpi3xdRGKllmOq5h74hgZnEUEMKUfTtJrj6PMubwRep8VDl0k5jP+G+0v9tz8rxd6/mWUPEElXyJAOoxgnTp+gHq5UjKqezvNtjJegfhl3JfoDEchNJMBQqB7A3FHWxHeSD2wKBgQDhDcGTeZ79OKDXF3K+e4uJ77Isjm4ttylbR3dJaIR4zx+y9pbR0xh2ess1NITm9E4Fcizmn76bsy7FBchw/1edemEpxEWCcgB8WSyTsmP6QN1uG40WiPREIjVM/VR4FOry3tJ+bvg7hgj+6RstxqcRe97kuYaBdNbyo8Hxpl/v6wKBgD1E9ocEyfXrwCIGFlxIlUE1XsB56k9X2TBqhFhqkdnDWhmhKF7oTtzfCp828JxaD27x/YqX2vykB7tUD1popdcrgndwUtAsXUP9U4wD9OczH9v8cOYDKUh7metvDsLAVDuosg6s1V5fjf6PecmjfIzyDzEg2aPGxom5CefZnChTAoGABOvBFNFu+Yh4c9uOZb3b0jtMgQ7oN9WucNGCOTew1ddpkQc1Swc7nLOYp+QrF8W1wwa5Fp3dlBf56NL0jAv5RXc8kqY5At4tRorIn74h04IBlXvCvQBnnunEkpdjdnC2pnLa1WrlwJ9wqOo97uVOF+LGf+fSmhgM7ydHgVoq9NsCgYBy8taA69M3TgpB90BXt9bc3NUEu+yUELzbXkF439twjMpQCAw42zmcdytBme4XomShbcPhs6Bhnxa6UTK79gkb/deB5iB82l7zqiLQb9Ff0O0unKNg76OG/KidW/2IdH5bFfDzED/hzk9Q9qPLfVUynnFGHKOCA0jLRCX0glgUHA==";
//    public static String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsG4BBS1dCNtY7XNeE6MC7/hlICZT7jhqA7iJn90BQbTdoAB0Vvqohe8gNOO/NebuNl+Y2dyf5NX46XaClJXCigHrwZOwb1QzBHu/EEfthFrMw8dw8RxH9/5kjzkMAIPAmVEa2EnrmFT4pRDU6XU9OK6g6HQhhjj6n0lGF4xnjry56u5MfVjoeTuHgfGgRrHFrOMMHpx3E/c6yHinwYmc1RPEtrRsKwQdHvjQygvAzHxWkjqiC6I0Wjzrn3evAY/icIV84/yaEukYmEmABwYgnmR+TM+t1TQNMEZg+gCt1KWn2yb2+GpxSwQ5vV6gnLQUd7+856WDoJRbe5QGG+pf4wIDAQAB";

    public static String host = "x.chimchim.top";
    public static String heard_5fu3 = "xxOi";
    public static String heard_sfg6 = "eyJhbGciOiJSUzI1NiJ9.eyJ1aWQiOiI1MTkwNjEzNyIsImVtYWlsIjoiLS0iLCJtb2JpbGUiOiIxNTUqKioqMDA0MSIsIm5pY2tuYW1lIjoiMTU1KioqKjAwNDEiLCJhcmVhIjoibnVsbCIsImludml0ZSI6IjExIiwiZXhwIjoxNjgxNDAxNzY3fQ.D1szzciQKU0SXmCVdVTt15Oxq8pNhbGLRaGa6T5N-TnEPurXLXC9sSWxWixf2rBNh6tavyU2amMK3je5vIKdm2jI2WUoguKnogYboodXAqqNWxJCicqJ_zETwrq94LFH39w_D1Hlff0e4PnOQC9Ou5HXT9EiVNRriskOqaGNat-9anDtTogS-DbVS0983DUOG-DkwkLsnrJAA-jNFFtO5jxXUlp2XbxqNHB4PxaFhv2BecttzjhS2Wp7YdcQ3_i0NiqVgGHZzM3M2W9NZW3d506yZwIg2jEPu2aaqgQxYhA4jbt47-wpVgA7lFHruBpcXrqrazT0D_5h2BCp2a_11Q" +
            "";
    public static String userId = "51906137";
    public static String svcId = "EXTEST";
    public static long timestamp = System.currentTimeMillis();

    public static void main(String[] args) {
        //因此httpclientdubug日志

        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
//        或
//        ((LoggerContext)LoggerFactory.getILoggerFactory())
//                .getLogger("org.apache.commons.httpclient")
//                .setLevel(Level.ERROR);
//        ((LoggerContext)LoggerFactory.getILoggerFactory())
//                .getLogger("httpclient.wire")
//                .setLevel(Level.ERROR);



//        token();
//        balance();

        // userId 用户ID，all为全部用户
        // type 订单类型：默认为futures合约订单, options期权订单
//        positionlist("111","options");
        String startTime = "2022-04-01 00:00:01";
        String endTime = "2024-04-01 10:00:01";
        long id = 6l;
        long pageSize = 1l;

//        positionfinishlist("1111","futures",startTime,endTime,id,pageSize);

        for (int i = 0; i <3 ; i++) {
//            System.out.println("------------"+i+"------------");
//            assetin();
            assetout();

        }
//        assetout();
    }

    /**
     * 永续/期权已完成仓位订单输出接口 (数据从旧到新排序)
     * @param userId 用户ID，all为全部用户
     * @param type 账户类型：默认为futures合约订单, options期权订单
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param id 查询起始标志：从此id开始的pagesize条，第一条开始id传0
     * @param pageSize 每页条数
     */
    private static void positionfinishlist(String userId, String type, String startTime ,String endTime , Long id , Long pageSize) {
        Map<String ,Object> tem=new HashMap<>();
        tem.put("svc_id",svcId);
        long timestamp = System.currentTimeMillis();
        tem.put("timestamp",timestamp);
        tem.put("user_id",userId);
        tem.put("type",type);
        tem.put("start_time",startTime);
        tem.put("end_time",endTime);
        tem.put("id",id);
        tem.put("pagesize",pageSize);
        // 给参数进行签名
        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign",sign);
        // 验签
        boolean verify = verify1(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\""+heard_5fu3+"\",\"sfg6\":\""+heard_sfg6+"\"}";
        String param = "{\"svc_id\":\""+svcId+"\",\"sign\":\""+sign+"\",\"timestamp\":"+timestamp+",\"user_id\":\""+userId+"\",\"type\":\""+type+"\",\"start_time\":\""+startTime+"\",\"end_time\":\""+endTime+"\",\"id\":"+id+",\"pagesize\":"+pageSize+"}";
        System.out.println("================================================================================================");
        System.out.println(param);
        System.out.println(new HttpUtil().postByJson("https://"+host+"/saasapi/positionfinishlist",param,header));
        System.out.println("================================================================================================");
    }

    /**
     * 永续/期权正在持仓的仓位订单输出接口(数据从旧到新排序)
     * @param userId 用户ID，all为全部用户
     * @param type 订单类型：默认为futures合约订单, options期权订单
     */
    private static void positionlist(String userId,String type) {
        Map<String ,Object> tem=new HashMap<>();
        tem.put("svc_id",svcId);
        long timestamp = System.currentTimeMillis();
        tem.put("timestamp",timestamp);
        tem.put("user_id",userId);
        tem.put("type",type);
        // 给参数进行签名
        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign",sign);
        // 验签
        boolean verify = verify1(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\""+heard_5fu3+"\",\"sfg6\":\""+heard_sfg6+"\"}";
        String param = "{\"svc_id\":\""+svcId+"\",\"sign\":\""+sign+"\",\"timestamp\":"+timestamp+",\"user_id\":\""+userId+"\",\"type\":\""+type+"\"}";
        System.out.println("================================================================================================");
        System.out.println(new HttpUtil().postByJson("https://"+host+"/saasapi/positionlist",param,header));
        System.out.println("================================================================================================");
    }

    /**
     * 合约转币币
     */

    public static void assetout(){
        String svc_id = svcId;
        String coin = "usdt";
        int currencyId = 6;
        Jedis jedis = new Jedis("localhost",6379);
        jedis.auth("123456");
        jedis.select(6);
        int tansferout = Integer.parseInt(jedis.get("saas:api:EXTEST_TEST_TANSFEROUT"));
        String ex_order_id = "EXTEST_test_tansferout_"+String.format("%03d",tansferout);
        BigDecimal amount = BigDecimal.valueOf(60);
        Map<String ,Object> tem=new HashMap<>();
        tem.put("svc_id",svc_id);
        tem.put("timestamp",timestamp);
        tem.put("ex_order_id",ex_order_id);
        tem.put("amount",amount);
        tem.put("coin",coin);
        tem.put("create_time","2022-08-15 09:01:02");

        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign",sign);

        boolean verify = verify1(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\""+heard_5fu3+"\",\"sfg6\":\""+heard_sfg6+"\"}";
        String param = "{\"svc_id\":\""+svc_id+"\",\"sign\":\""+sign+"\",\"timestamp\":"+timestamp+",\"ex_order_id\":\""+ex_order_id+"\",\"amount\":"+amount+",\"coin\":\""+coin+"\",\"create_time\":\"2022-08-15 09:01:02\"}";
        System.out.println(param);
        JSONObject j = JSONObject.parseObject(new HttpUtil().postByJson("https://"+host+"/saasapi/assetout",param,header));
        System.out.println("================================================================================================");
        System.out.println(j);
        System.out.println("================================================================================================");
        String saasOrderId = j.getJSONObject("data").getString("saas_order_id");
        insertData(ex_order_id,saasOrderId,userId,userId,currencyId,amount,amount,"从 EXTEST 转入"+String.format("%03d",tansferout));
        jedis.set("saas:api:EXTEST_TEST_TANSFEROUT", String.valueOf(tansferout+1));

    }
    /**
     * 币币转合约
     */
    public static void assetin(){
        int currencyId = 6;
        String coin = "usdt";
        Jedis jedis = new Jedis("localhost",6379);
        jedis.auth("123456");
        jedis.select(6);
        int tansferin = Integer.parseInt(jedis.get("saas:api:EXTEST_TEST_TANSFERIN"));
        String ex_order_id = "EXTEST_test_tansferin_"+String.format("%03d",tansferin);
        BigDecimal amount = BigDecimal.valueOf(1000);
        Map<String ,Object> tem=new HashMap<>();
        tem.put("svc_id",svcId);
        tem.put("timestamp",timestamp);
        tem.put("ex_order_id",ex_order_id);
        tem.put("amount",amount);
        tem.put("coin",coin);
        tem.put("create_time","2022-08-15 09:01:02");

        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign",sign);

        boolean verify = verify1(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\""+heard_5fu3+"\",\"sfg6\":\""+heard_sfg6+"\"}";
        String param = "{\"svc_id\":\""+svcId+"\",\"sign\":\""+sign+"\",\"timestamp\":"+timestamp+",\"ex_order_id\":\""+ex_order_id+"\",\"amount\":"+amount+",\"coin\":\""+coin+"\",\"create_time\":\"2022-08-15 09:01:02\"}";
        System.out.println("=======params======="+param);
        JSONObject j = JSONObject.parseObject(new HttpUtil().postByJson("https://"+host+"/saasapi/assetin",param,header));

        System.out.println("================================================================================================");
        System.out.println(j);
        System.out.println("================================================================================================");
        String saasOrderId = j.getJSONObject("data").getString("saas_order_id");

        insertData(ex_order_id,saasOrderId,userId,userId,currencyId,amount,amount,"转出到 EXTEST"+String.format("%03d",tansferin));
        jedis.set("saas:api:EXTEST_TEST_TANSFERIN", String.valueOf(tansferin+1));

    }

    /**
     * 获取余额
     */
    public static void balance(){
        Map<String ,Object> tem=new HashMap<>();
        tem.put("svc_id",svcId);
        long timestamp = System.currentTimeMillis();
        tem.put("timestamp",timestamp);
//        tem.put("type","futures");
        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign",sign);

        boolean verify = verify1(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\""+heard_5fu3+"\",\"sfg6\":\""+heard_sfg6+"\"}";
        String param = "{\"svc_id\":\""+svcId+"\",\"sign\":\""+sign+"\",\"timestamp\":"+timestamp+"}";
        System.out.println("================================================================================================");
        System.out.println(new HttpUtil().postByJson("https://"+host+"/saasapi/balance",param,header));
        System.out.println("================================================================================================");
    }

    /**
     * 置换token
     */
    public static void token(){
//        heard_5fu3 = "aaa";
//        heard_sfg6 = "eyJhbGciOiJSUzI1NiJ9.eyJ1aWQiOiI1MTkwNjExMSIsImVtYWlsIjoiLS0iLCJtb2JpbGUiOiIxNzgqKioqMDAwMyIsIm5pY2tuYW1lIjoiMTc4KioqKjAwMDMiLCJleHAiOjE2NjA2NTg0Nzd9.tTbdXstLV1Ok3Tq-aT263TUBr__o3y8yVujQScQxPVtsoY3G1-4j9nDhnqfGK2L5Ql_rlh1d_Hva2paEL2GZronH5Hs16_usKgaBDH2nQtf2LSRjPH5CK6PdjVp67uYQ3ouHBSTCigrCTtQO1bc7I4bstHObo5OmmAKfZCMjsVL70sr10HHE6Ym1PqGcSKl8tjwJK-2zEG1kGbP1VAZCYijgWphjhDjKDMRyxBrOmdyjC4NYTzRnlqukJi7ARKxH6-hIot1xjxaMWhY4aUOKQgoibcdG-HeFDfrxnol2yj8rf5LF-HfeG_xl0WWQmBPFYSTBDbCWrW38xac-WnbfBQ";
        String header = "{\"5fu3\":\""+heard_5fu3+"\",\"sfg6\":\""+heard_sfg6+"\"}";
        String param = null;
        System.out.println("================================================================================================");
        System.out.println(new HttpUtil().postByJson("https://"+host+"/saasapi/token",param,header));
        System.out.println("================================================================================================");
    }

    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String,Object>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        Class<?> superclass = obj.getClass();
        while (superclass != null) {
            superclass = superclass.getSuperclass();
            if (superclass.getName().equals("java.lang.Object")) {
                break;
            }
            //获取父类的属性
            Field[] superField = superclass.getDeclaredFields();
            for (Field field : superField) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                map.put(fieldName, value);

            }
        }

        return map;
    }
    public static Map<String, Object> objectToMap1(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String,Object>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }
    public static String mapToString(Map param) {
        Set set = param.keySet();
        TreeSet<String> nameSet = new TreeSet<>(set);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String name : nameSet) {
            Object value = param.get(name);
            if(value == null){
                continue;
            }
            if(value instanceof Collection){
                value = listToStr(((Collection) value));
            }
            if(value instanceof Map){
                value = listToStr(((Map) value).values());
            }
            builder.append(name).append("=").append(value);
            i++;
            if (i < nameSet.size()) {
                builder.append("&");
            }
        }
        String res = builder.toString();
        if(res.endsWith("&")){
            return res.substring(0,res.length()-1);
        }else{
            return res;
        }
    }
    private static Object listToStr(Collection value) {
        Optional reduce = value.stream().map(new Function<Object, String>() {
            @Override
            public String apply(Object b) {
                String c = null;
                if (b instanceof Collection) {
                    c = listToStr(((Collection) b)).toString();
                } else if (b instanceof Map) {
                    c = listToStr(((Map) b).values()).toString();
                } else {
                    c = b.toString();
                }
                return c;
            }
        }).reduce((a, b) -> a.toString() + b.toString());
        return reduce.get();
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }
    /**
     * 验签
     *
     * @param data 原始对象
     * @param publicKeyString 公钥字符串
     * @return 是否验签通过
     */
    public static boolean verify(Object data, String publicKeyString) {
        try {
            Map<String, Object> objectMap = objectToMap(data);
            String sign = (String) objectMap.get("sign");
            objectMap.remove("sign");
            String srcData = mapToString(objectMap);

            PublicKey publicKey = getPublicKey(publicKeyString);
            byte[] keyBytes = publicKey.getEncoded();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initVerify(key);
            signature.update(srcData.getBytes());
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        }catch (Exception e){
            return false;
        }
    }
    public static boolean verify1(Map<String, Object> objectMap, String publicKeyString) {
        try {
            String sign = (String) objectMap.get("sign");
            objectMap.remove("sign");
            String srcData = mapToString(objectMap);

            PublicKey publicKey = getPublicKey(publicKeyString);
            byte[] keyBytes = publicKey.getEncoded();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initVerify(key);
            signature.update(srcData.getBytes());
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        }catch (Exception e){
            return false;
        }
    }

//    /**
//     * 加签
//     *
//     * @param data 原始对象
//     * @param privateKeyString 私钥字符串
//     * @return 加签名
//     */
//    public static String sign(Object data, String privateKeyString){
//        try {
//            Map<String, Object> objectMap = objectToMap(data);
//            String srcData = mapToString(objectMap);
//
//            PrivateKey privateKey = getPrivateKey(privateKeyString);
//            byte[] keyBytes = null;
//            PKCS8EncodedKeySpec keySpec = null;
//            KeyFactory keyFactory = null;
//            PrivateKey key = null;
//            Signature signature = null;
//            String str = null;
//            keyBytes = privateKey.getEncoded();
//            keySpec = new PKCS8EncodedKeySpec(keyBytes);
//            keyFactory = KeyFactory.getInstance("RSA");
//            key = keyFactory.generatePrivate(keySpec);
//            signature = Signature.getInstance("SHA256withRSA");
//            signature.initSign(key);
//            signature.update(srcData.getBytes());
//            str = new String(Base64.encodeBase64(signature.sign()));
//
//            return str;
//        }catch (Exception e){
//            return null;
//        }
//    }
    public static String sign(Map<String,Object> data, String privateKeyString){
        try {
            String srcData = mapToString(data);

            PrivateKey privateKey = getPrivateKey(privateKeyString);
            byte[] keyBytes = null;
            PKCS8EncodedKeySpec keySpec = null;
            KeyFactory keyFactory = null;
            PrivateKey key = null;
            Signature signature = null;
            String str = null;
            keyBytes = privateKey.getEncoded();
            keySpec = new PKCS8EncodedKeySpec(keyBytes);
            keyFactory = KeyFactory.getInstance("RSA");
            key = keyFactory.generatePrivate(keySpec);
            signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(key);
            signature.update(srcData.getBytes());
            str = new String(Base64.encodeBase64(signature.sign()));

            return str;
        }catch (Exception e){
            return null;
        }
    }

    public static void insertData(String tradeNo, String saasOrderId, String userId, String sender,int currencyId, BigDecimal amount,BigDecimal quoteAmount ,String remark){
        Connection conn = null;
        Statement stmt = null;
//        String sql = "SELECT * FROM `bib_cfd`.`user_position_log` WHERE `id` IN ("+id+") ORDER BY `id` LIMIT 0,1000";
        ResultSet resultSet = null;
        try{
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 打开链接
            conn = DriverManager.getConnection("jdbc:mysql://locklevel-mysql-dev1-india.cluster-cwgnjbpmvvmh.ap-southeast-1.rds.amazonaws.com:3306/","dev1_idina_master_rw","4x&v_6bu^x!x$m$#*rv_$tx6c+udfo9@");

            String sql = "INSERT INTO `bib_cfd`.`asset_transfer_record` " +
                    "(`trade_no`, `saas_order_id`, `svc_id`, `user_id`, `biz_id`, `sender`, `to_biz_id`, `recipient`, `currency_id`, `amount`, `usdt_amount`, `quote_amount`, `fees`, `confirmation`, `type`, `status`, `transfer_state`, `retry`, `remark`) " +
                    "VALUES " +
                    "( ?, ?, 'EXTEST', ?, 5, ?, 101, 'EXTEST', ?, ?, 0.00000000, ?, 0.00000000, 0, 5, 100, 1, 0, ?);";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1,tradeNo);
            preparedStatement.setString(2,saasOrderId);
            preparedStatement.setString(3,userId);
            preparedStatement.setString(4,sender);
            preparedStatement.setInt(5,currencyId);
            preparedStatement.setBigDecimal(6,amount);
            preparedStatement.setBigDecimal(7,quoteAmount);
            preparedStatement.setString(8,remark);

            int update = preparedStatement.executeUpdate();
            if (update >= 1){
                System.out.println("插入成功！");
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

}
