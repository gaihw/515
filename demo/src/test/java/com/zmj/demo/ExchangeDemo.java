package com.zmj.demo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * @Title: Exchange Request SaaSservice
 * @ProjectName: saas-api
 * @Description:
 * @date: 2022/1/19
 */
public class ExchangeDemo {
    //---- example start
    // timestamp
    public static long timestamp = System.currentTimeMillis();
    // ---------- Exchange Settings ----------
    // Exchange privateKey must pkcs8 format
    public static String pri = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDLwKjH6TuinYyK9TnJjNvQPfW6fKxuQ/E2WqP4M8uSClIBVt1b2uqKXqHt1N/e6ep8rlbVmOk12aa/PBAuqseZdSuaZ6FD1WBLS0v761Shm1hvi5YUTSlszg0NzA/UAapXxpGXQlPL+Gblv+YgY2QUorO+mkokvScZmbGTWTI/WwBgOyp0OduVvqyDZ7wrPyGvmhB4SW9VB88xiVOMRenQd7tDyrA5dnnSHdZY50YU1gDICmlLEU8W6GsrMUSXf5iD/2MJlO++ZyIdEttMdjpK2wzPomwmlxJAVMyrWEXW0GuEMGhG40IZZUT9jPKZB8s4ZPRGHwQclJW95fa4I38JAgMBAAECggEAJK38NHDiUXX3VRSsYIJBGA3vbLEBAaCtjdFnb0kzvoipFqCinOSeEGbU09Hcs258zhE8hJtQcGdMQ2T2rNAFurSDMvqw51tESIm3lhRZWfZzRzFjxSfW60V0yCUSPGJIXiDbGNXR7Ag9zeJr9SyvWZ+oqAlyi4aZwVwPwazvBMR04bnyF3fJGME8RUVmCA5biE5Xwa/ykO8hUq21NkzV2Ii58nVzJdk5UmszRwTCCfg/Qyrdr89Uq5Nyv3h0/Rn/McPnjXafUC+qvQEm0m3OI6hxmWiw6bYirObJ3xek42N23+5pcxmvnAaq4izqs8C/kbVzzDfvFHEWHzew2Vgv2QKBgQDnxRGoTp/j80N67XYc23sZO4DzZ9hMwCjJXheYyTpi3xdRGKllmOq5h74hgZnEUEMKUfTtJrj6PMubwRep8VDl0k5jP+G+0v9tz8rxd6/mWUPEElXyJAOoxgnTp+gHq5UjKqezvNtjJegfhl3JfoDEchNJMBQqB7A3FHWxHeSD2wKBgQDhDcGTeZ79OKDXF3K+e4uJ77Isjm4ttylbR3dJaIR4zx+y9pbR0xh2ess1NITm9E4Fcizmn76bsy7FBchw/1edemEpxEWCcgB8WSyTsmP6QN1uG40WiPREIjVM/VR4FOry3tJ+bvg7hgj+6RstxqcRe97kuYaBdNbyo8Hxpl/v6wKBgD1E9ocEyfXrwCIGFlxIlUE1XsB56k9X2TBqhFhqkdnDWhmhKF7oTtzfCp828JxaD27x/YqX2vykB7tUD1popdcrgndwUtAsXUP9U4wD9OczH9v8cOYDKUh7metvDsLAVDuosg6s1V5fjf6PecmjfIzyDzEg2aPGxom5CefZnChTAoGABOvBFNFu+Yh4c9uOZb3b0jtMgQ7oN9WucNGCOTew1ddpkQc1Swc7nLOYp+QrF8W1wwa5Fp3dlBf56NL0jAv5RXc8kqY5At4tRorIn74h04IBlXvCvQBnnunEkpdjdnC2pnLa1WrlwJ9wqOo97uVOF+LGf+fSmhgM7ydHgVoq9NsCgYBy8taA69M3TgpB90BXt9bc3NUEu+yUELzbXkF439twjMpQCAw42zmcdytBme4XomShbcPhs6Bhnxa6UTK79gkb/deB5iB82l7zqiLQb9Ff0O0unKNg76OG/KidW/2IdH5bFfDzED/hzk9Q9qPLfVUynnFGHKOCA0jLRCX0glgUHA==";
    // song
    // Exchange 5fu3
    public static String heard_5fu3 = "song";
    // Exchange use privateKey create jwt token for SaaSservice
    public static String heard_sfg6 = "=eyJhbGciOiJSUzI1NiJ9.eyJ1aWQiOiI1MTkwNjE2OCIsImVtYWlsIjoiLS0iLCJtb2JpbGUiOiIxNjYqKioqMDAwOSIsIm5pY2tuYW1lIjoiMTY2KioqKjAwMDkiLCJhcmVhIjoibnVsbCIsImludml0ZSI6IjYzMDIzMTEyIiwiZXhwIjoxNjgzMTcwMzgzfQ.RrXocevOEUCZRmHPVKZc4Ha_SuvrKE_BbINK1IaTNWbLAdcPx2GfrtAqFOh4eU56ffeg9Ym43a06im1BGEaDTFN-dnry4Yq9mTfmRnQgq8uoiJIw2eibs3SQLJprqwsaxaYjcI1Gk5KOGk8VrzPGfTCK6cXjWeND6QlasVY--rfY2gm0VGmIx8OdojO1MiwekhvHPqWs5hjqr4cZpF1397-1b3D-zKT0cJCjIPF-FJgScbAtyQ9aJbYQIKPFRnGHkQ2ridZAY5kpbwatcCBTkwpeXcsYW68GxDio_tUxxzAigshc9B1xenqzwOb5GORccEVz6tQFF6cjtOCL3L75XA";
    //
    // ---------- SaaSservice Settings ----------
    // SaaSservice svcID
    public static String svcId = "song";
    // SaaSservice publickey
    public static String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsG4BBS1dCNtY7XNeE6MC7/hlICZT7jhqA7iJn90BQbTdoAB0Vvqohe8gNOO/NebuNl+Y2dyf5NX46XaClJXCigHrwZOwb1QzBHu/EEfthFrMw8dw8RxH9/5kjzkMAIPAmVEa2EnrmFT4pRDU6XU9OK6g6HQhhjj6n0lGF4xnjry56u5MfVjoeTuHgfGgRrHFrOMMHpx3E/c6yHinwYmc1RPEtrRsKwQdHvjQygvAzHxWkjqiC6I0Wjzrn3evAY/icIV84/yaEukYmEmABwYgnmR+TM+t1TQNMEZg+gCt1KWn2yb2+GpxSwQ5vV6gnLQUd7+856WDoJRbe5QGG+pf4wIDAQAB";
    // SaaSservice domain
    public static String host = "https://x.chimchim.top";
//    public static String host = "https://dev1.123kj.top";

    //public static String host = "http://127.0.0.1:7002";
    //---- example end

    /**
     * 流程：
     * 1，商户生成jwt的token传给合约sdk，这一步需要打开app-sdk
     * 2，sdk调用合约服务的api，自动注册用户并返回合约内部token，这一步会完成合约用户的自动注册
     * 3，商户用户可以进行合约查询余额或者进行划转。如果没有进行1和2，直接进行3，会返回用户不存在。
     *
     * 商户=Exchange  合约服务=SaaSservice
     */
    public static void main(String[] args) {
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
        /**
         * 生成jwt-token示例
         */
        createJwtToken();
        /**
         * use TinyTrader’s perpetual SDK to your app , must get sdk token: accessToken
         * 可以使用此方法传入jwt-token模拟app-sdk的自动注册和登录
         */
//        getToken();
        /**
         * get SaaSservice balance
          */
//        balance();
        /**
         * asset from  Exchange to SaaSservice
          */
        //assetin();
        /**
        * asset from SaaSservice to Exchange
         */
//        assetout();
        /**
         * 永续/期权正在持仓的仓位订单输出接口(数据从旧到新排序)
         */
        positionlist();
        /**
         * 永续/期权已完成仓位订单输出接口 (数据从旧到新排序)
         */
//        positionfinishlist();
        /**
         * 永续/期权支持的币对列表输出接口
         */
//        positionsymbol();
    }

    /**
     * 永续/期权支持的币对列表输出接口
     */
    public static void positionsymbol(){
        Map<String, Object> tem = new HashMap<>();
        tem.put("svc_id", svcId);
        tem.put("timestamp", timestamp);
        //
        String type = "options"; // futures options
        tem.put("type", type);
        //
        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign", sign);

        boolean verify = verify(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\"" + heard_5fu3 + "\",\"sfg6\":\"" + heard_sfg6 + "\"}";
        String param = "{\"svc_id\":\"" + svcId + "\",\"sign\":\"" + sign + "\",\"timestamp\":" + timestamp
                + ",\"type\":\"" + type
                + "\"}";
        //
        String response = new HttpUtil().postByJson( host + "/saasapi/positionsymbol", param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("positionsymbol detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    /**
     * 永续/期权正在持仓的仓位订单输出接口(数据从旧到新排序)
     */
    public static void positionlist(){
        Map<String, Object> tem = new HashMap<>();
        tem.put("svc_id", svcId);
        tem.put("timestamp", timestamp);
        //
        String user_id = "51905041";
        String type = "futures"; // futures options
        tem.put("user_id", user_id);
        tem.put("type", type);
        // 币种
        String symbol = ""; // 币对 btc, eth等
        // 方向
        String direction = ""; // LONG:多;SHORT:空
        // 租户用户id
        String tenant_user_id = "";
        //
        if (!symbol.equals("")) {
            tem.put("symbol", symbol);
        }
        if (!direction.equals("")) {
            tem.put("direction", direction);
        }
        if (!tenant_user_id.equals("")) {
            tem.put("tenant_user_id", tenant_user_id);
        }
        //
        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign", sign);

        boolean verify = verify(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\"" + heard_5fu3 + "\",\"sfg6\":\"" + heard_sfg6 + "\"}";
        String param = "{\"svc_id\":\"" + svcId + "\",\"sign\":\"" + sign + "\",\"timestamp\":" + timestamp
                + ",\"user_id\":\"" + user_id
                + (!symbol.equals("") ? "\",\"symbol\":\"" + symbol : "")
                + (!direction.equals("") ? "\",\"direction\":\"" + direction : "")
                + (!tenant_user_id.equals("") ? "\",\"tenant_user_id\":\"" + tenant_user_id : "")
                + "\",\"type\":\"" + type
                + "\"}";
        //
        String response = new HttpUtil().postByJson( host + "/saasapi/positionlist", param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("positionlist detail: ");
            System.out.println(jsonObject);
            for (JsonNode j: jsonObject.get("data").get("list")) {
                System.out.println(""+j.get("symbol")+"==="+j.get("net_profit"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    /**
     * 永续/期权已完成仓位订单输出接口 (数据从旧到新排序)
     */
    public static void positionfinishlist(){
        Map<String, Object> tem = new HashMap<>();
        tem.put("svc_id", svcId);
        tem.put("timestamp", timestamp);
        //
        String user_id = "all";
        String type = "futures"; // futures options
        String start_time = "2022-04-01 00:00:01";
        String end_time = "2024-04-08 00:00:01";
        Integer id = 0;
        Integer pagesize = 20;
        tem.put("user_id", user_id);
        tem.put("type", type);
        tem.put("start_time", start_time);
        tem.put("end_time", end_time);
        tem.put("id", id);
        tem.put("pagesize", pagesize);
        // 币种
        String symbol = ""; // 币对 btc, eth等
        // 方向
        String direction = ""; // LONG:多;SHORT:空
        // 租户用户id
        String tenant_user_id = "2985";
        //
        if (!symbol.equals("")) {
            tem.put("symbol", symbol);
        }
        if (!direction.equals("")) {
            tem.put("direction", direction);
        }
        if (!tenant_user_id.equals("")) {
            tem.put("tenant_user_id", tenant_user_id);
        }
        //
        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign", sign);

        boolean verify = verify(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\"" + heard_5fu3 + "\",\"sfg6\":\"" + heard_sfg6 + "\"}";
        String param = "{\"svc_id\":\"" + svcId + "\",\"sign\":\"" + sign + "\",\"timestamp\":" + timestamp
                + ",\"id\":" + id
                + ",\"pagesize\":" + pagesize
                + ",\"user_id\":\"" + user_id
                + (!symbol.equals("") ? "\",\"symbol\":\"" + symbol : "")
                + (!direction.equals("") ? "\",\"direction\":\"" + direction : "")
                + (!tenant_user_id.equals("") ? "\",\"tenant_user_id\":\"" + tenant_user_id : "")
                + "\",\"type\":\"" + type
                + "\",\"start_time\":\"" + start_time
                + "\",\"end_time\":\"" + end_time
                + "\"}";
        //
        String response = new HttpUtil().postByJson( host + "/saasapi/positionfinishlist", param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("positionfinishlist detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    public static void createJwtToken() {
        System.out.println("================================================================================================");
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] decodedKey = Base64.decodeBase64(pri.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        PrivateKey privateKey = null;
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        // 超期时间 毫秒 有效期一年
        Long exp = System.currentTimeMillis() + 3600*24*365*1000;
        Date expDate = new Date(exp);
        Map<String, Object> claims = new HashMap<>();
        // uid,email,mobile,area,invite,exp
        claims.put("uid", "20232023");
        claims.put("email", "20232023@xxx.com");
        claims.put("mobile", "13820232023");
        claims.put("area", "86");
        claims.put("invite", "");
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.RS256,privateKey)
                .compact();
        System.out.println("jwt token: \n");
        System.out.println(token);
        System.out.println("================================================================================================");
    }

    /**
     * get sdk token
     * use TinyTrader’s perpetual SDK to your app , must get sdk token: accessToken
     * rerturn:  accessToken  is sdk token
     */
    public static void getToken() {
        String header = "{\"5fu3\":\"" + heard_5fu3 + "\",\"sfg6\":\"" + heard_sfg6 + "\"}";
        String param = null;
        String response = new HttpUtil().postByJson(host + "/saasapi/token", param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            String accessToken = jsonObject.path("data").path("accessToken").asText();
            System.out.println("accessToken: "+accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    /**
     * get balance
     * get SaaSservice balance
     */
    public static void balance() {
        // 可选参数 type账户类型:  futures合约(默认值), options期权
        String type = "futures";
        //
        String typeParam = "";
        //
        Map<String, Object> tem = new HashMap<>();
        tem.put("svc_id", svcId);
        tem.put("timestamp", timestamp);
        // V1.2
        tem.put("type", type);
        typeParam = ",\"type\":\"" + type + "\"";
        //
        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign", sign);

        boolean verify = verify(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\"" + heard_5fu3 + "\",\"sfg6\":\"" + heard_sfg6 + "\"}";
        String param = "{\"svc_id\":\"" + svcId + "\""+typeParam+",\"sign\":\"" + sign + "\",\"timestamp\":" + timestamp + "}";
        String response = new HttpUtil().postByJson(host + "/saasapi/balance", param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            String balance = jsonObject.path("data").toString();
            System.out.println("balance: "+balance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    /**
     * 4.2 SaaS services transfer in interface
     * asset from  Exchange to SaaSservice
     */
    public static void assetin() {
        // 可选参数 type账户类型:  futures合约(默认值), options期权
        String type = "futures";
        //
        String typeParam = "";
        //
        String coin = "usdt";
        String ex_order_id = svcId + "_tansferin_" + String.valueOf(timestamp);
        BigDecimal amount = BigDecimal.valueOf(888);
        Map<String, Object> tem = new HashMap<>();
        tem.put("svc_id", svcId);
        tem.put("timestamp", timestamp);
        tem.put("ex_order_id", ex_order_id);
        tem.put("amount", amount);
        tem.put("coin", coin);
        // V1.2
        tem.put("type", type);
        typeParam = ",\"type\":\"" + type + "\"";
        //
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        tem.put("create_time", simpleDateFormat.format(date));

        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign", sign);

        boolean verify = verify(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\"" + heard_5fu3 + "\",\"sfg6\":\"" + heard_sfg6 + "\"}";
        String param = "{\"svc_id\":\"" + svcId + "\""+typeParam+",\"sign\":\"" + sign + "\",\"timestamp\":" + timestamp + ",\"ex_order_id\":\"" + ex_order_id + "\",\"amount\":" + amount + ",\"coin\":\"" + coin + "\",\"create_time\":\""+simpleDateFormat.format(date)+"\"}";
        //
        String response = new HttpUtil().postByJson( host + "/saasapi/assetin", param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            String saasOrderId = jsonObject.path("data").path("saas_order_id").asText();
            System.out.println("saasOrderId: "+saasOrderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    /**
     * 4.3 SaaS services transfer out interface
     */

    public static void assetout() {
        // 可选参数 type账户类型:  futures合约(默认值), options期权
        String type = "options";
        //
        String typeParam = "";
        //
        String coin = "usdt";
        String ex_order_id = svcId + "_tansferout_" + String.valueOf(timestamp);
        BigDecimal amount = BigDecimal.valueOf(888);
        Map<String, Object> tem = new HashMap<>();
        tem.put("svc_id", svcId);
        tem.put("timestamp", timestamp);
        tem.put("ex_order_id", ex_order_id);
        tem.put("amount", amount);
        tem.put("coin", coin);
        // V1.2
        tem.put("type", type);
        typeParam = ",\"type\":\"" + type + "\"";
        //
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        tem.put("create_time", simpleDateFormat.format(date));

        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign", sign);

        boolean verify = verify(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\"" + heard_5fu3 + "\",\"sfg6\":\"" + heard_sfg6 + "\"}";
        String param = "{\"svc_id\":\"" + svcId + "\"" + typeParam + ",\"sign\":\"" + sign + "\",\"timestamp\":" + timestamp + ",\"ex_order_id\":\"" + ex_order_id + "\",\"amount\":" + amount + ",\"coin\":\"" + coin + "\",\"create_time\":\""+simpleDateFormat.format(date)+"\"}";

        String response = new HttpUtil().postByJson( host + "/saasapi/assetout", param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            String saasOrderId = jsonObject.path("data").path("saas_order_id").asText();
            System.out.println("saasOrderId: "+saasOrderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    public static String mapToString(Map param) {
        Set set = param.keySet();
        TreeSet<String> nameSet = new TreeSet<>(set);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String name : nameSet) {
            Object value = param.get(name);
            if (value == null) {
                continue;
            }
            if (value instanceof Collection) {
                value = listToStr(((Collection) value));
            }
            if (value instanceof Map) {
                value = listToStr(((Map) value).values());
            }
            builder.append(name).append("=").append(value);
            i++;
            if (i < nameSet.size()) {
                builder.append("&");
            }
        }
        String res = builder.toString();
        if (res.endsWith("&")) {
            return res.substring(0, res.length() - 1);
        } else {
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
     * get publicKey
     *
     * @param publicKey
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * get privateKey
     *
     * @param privateKey privateKey string need pkcs8 format
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    public static boolean verify(Map<String, Object> objectMap, String publicKeyString) {
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
        } catch (Exception e) {
            return false;
        }
    }

    public static String sign(Map<String, Object> data, String privateKeyString) {
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
        } catch (Exception e) {
            return null;
        }
    }
}
