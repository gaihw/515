import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;

import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * @Title: SaaSservice Request Exchange
 * @ProjectName: saas-api
 * @Description:
 * @date: 2022/1/19
 */
public class SaaSserviceDemo {
    //---- example start
    // timestamp
    public static long timestamp = System.currentTimeMillis();
    // ---------- SaaSservice Settings ----------
    // SaaSservice privateKey must pkcs8 format
    //public static String pri = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQPUL5Q3EAll9mSoJ5pve49pFrg5XXS+ffIL2jPKJ0XrwR83n5OPWZsl0xnMGzrbB6psGvQpc8ajej3AjtKF32NT665cRU/6kr6drI52bdQG8LSAg5NEw0FkOtUzVoWdkTVpULLK+MalFsoNNJeRkf3Oa1dFTJLCF0oFDqhp9M6HUsLEG4WthuPNDXK754bTzRyYwZTh2iqxrsahHaCrszPBDSya4mK75IF4moK92u9pnTGhiO7n/s2PhT1nY8s3+/YRCsvvOqSAAmf7Ib6DHRa/iPWso1r+e72xIMCDB6GLkE5bHc7cocZRwWhovmnUyjgmOWwnL6St/JV7NLZCX/AgMBAAECggEALaTlQ8H+yYPMbq9XeK3IqKSGQjKiwT3JA4f12uk+M3sjIr8csyw+NEQIT8HeXkCxny6UDiURfNHUB6uCu6HRki8pSbWaGpfI7heT342ytINGYJiL2aZeEwGUqw2w0fPUyNTWV3Htn46pc6m1EwQaWkXqUDOe/QZVE/YDhSPS12AgA4q15Z98g7FGhOf3KyfMpAQg8QkesQA9SBoUR1j0D7Ol5RJcRnKZoU8IcYg6Vj1rFtSjSbqpLg+JgKf7hq4P5OwR8gV0MAZu6X+s0xwxf/tLqNWnkKBEhIVKf6IBdFx5mVNSZy7T2Jsk/B2M95cfJUdWh1SB8oZ1x3FD4Mv4SQKBgQDt95lnjZjl/JsTHhYimRXqKsK3aBdzQAu0HBY+4A+lmH4dcoLZX9UpckkPzjm0EY6s06Q35bjIXGJw676+BFVApkhMdLZPrJFGVT1NULWnYsCSldYXxDWljZlE+DK79dxPYMpB/aaRfr0zoz3LS2HjIZ0zNDLH8UgTeCc7OMtZHQKBgQCbK2fQ5IQgi82uyN3DoW/XIR97a08vFKKCQAKyA1WoqVkxRZYGeUCL7IxT/3zfXE6qheBhFRJHYu4MQvTL5oiBP8hvHnPIcvLKwQto9/fvz39fQgwUdnEfTKmTcd0FV8lX58PZHCfLEh7iLSoNVZMLcSamqxTE1WI8wkVszUmsywKBgE+4KdESE43AgSb3Zkzy4e+PoamVmn2GkCUnX6ByqWs9Wcp9qS4vN5bp7WX5HUWguB5XzGTZdXzRzjysDTtU8kZ1LjVGyCiK1CQuvBmhlylbIM46mf1eQuaSOC1txHlJVSPWdguDcQC+551b64CmYUWY5jPYIcbvosi68NGbJixVAoGAQFzQwyUGaTmUJ1vKIMtlyKZEmLz6K10zyzMh6xTXAw67oGRsxmKlE2UbD/x5n2x6dODzIVHMMzAl7ZAYWjtVg8chLHZFMG/9pXxNb0zr2t0GyExtFk/KvGDgNfiU5RJ0wbGw2u2C1AjAJrD1MHK+e5CDp75u7vZTuUAb6/V7CeECgYEAp+KiMQ8wHuBGfz062zIJxiytu3a6JWi7ZIq7+EIqOmkbWxUTcgSFaw7THlYTzP6CywXuihETmSIsNcSBUWccX+MBnaCbeUBcaIlO1UaoK6sGM9t93D3ADgYnbQwy5qy5zSZBjKq+3nH7h+7f3y3XhNR06XBqXHucgQtm1EluAFo=";
    // SaaSservice svcID
    //public static String svcId = "bullbear";
    // SaaSservice 5fu3
    //public static String heard_5fu3 = "bullbear";
    // Exchange use privateKey create jwt token for SaaSservice
    public static String heard_sfg6 = "eyJhbGciOiJSUzI1NiJ9.eyJhcmVhIjoiODYiLCJ1aWQiOiI4ODg4ODg4OCIsIm1vYmlsZSI6IjEzODg4ODg4ODg4IiwiaW52aXRlIjoiIiwiZW1haWwiOiI4ODhAeHh4LmNvbSIsImV4cCI6MTY4MTEwOTgxMH0.KOvFMn2tATeLIDDE79Gz4vdJVV625LzfdiLWzxFwX8MVqe_i6rSRub8Vtjl92lw3knkOxTxMDuWbqeX83LFyBxBCTSK40GftntO9IhwQ11-9r2BDRcEaLrz8TMRDd0Dzp8efExhDeGiAKIQNINsEDgRaWxALfuQ3YJo7ypTERDAWWk97KJv8s8UgThq56ZTGgRtvZJXHoF4HkfK3klEdGRAsaGTfnXrBQ90wDsfzGRvEK7wCs9YvEsqsMA7SADSYz-lmxxPwuaG_KgBgvVcHyJHZBV59iHj8Xl04Qnzp-k-IvpTIKP-XZWr2TflEErWqAWgfQEbiTH1McyOrv1dw2A";
    //
    // ---------- Exchange Settings ----------
    // Exchange publickey
    //public static String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArU+kRM2KRkRTv+7KFkuK2JYYGNH91W7n+cuR4fhKKmJTMw53aUFbn/MkCmaxCcSWq49+d+ENfB6qHYvgvwTiCxfo8mYk2t90XDmgM8dwv6iLb4Yz5GmVRqZVTgij7UFmAxj6dggbjRuf2XwyFEYa+A+SMb6h0pLHaEJCc7LT4DQu12P0Lq3xtAmvFdo23VVUllUSIf8XVi+5PrckCE166khfY4fHBZCjmj8rRSLL/U7uPkCCH01bFGU0O4ymq32Frjwfon+++rGYQQSCoklFzNGTsUzIw8FvZAOBDq4gPSjaBpQtJ4DM7j/AzFgwpx2Sup0D/gZRQXUn6X+RZ+7r7QIDAQAB";
    // Exchange domain
    //public static String host = "https://x.chimchim.top";

    // ----------
    // song
    public static String pri = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDLwKjH6TuinYyK9TnJjNvQPfW6fKxuQ/E2WqP4M8uSClIBVt1b2uqKXqHt1N/e6ep8rlbVmOk12aa/PBAuqseZdSuaZ6FD1WBLS0v761Shm1hvi5YUTSlszg0NzA/UAapXxpGXQlPL+Gblv+YgY2QUorO+mkokvScZmbGTWTI/WwBgOyp0OduVvqyDZ7wrPyGvmhB4SW9VB88xiVOMRenQd7tDyrA5dnnSHdZY50YU1gDICmlLEU8W6GsrMUSXf5iD/2MJlO++ZyIdEttMdjpK2wzPomwmlxJAVMyrWEXW0GuEMGhG40IZZUT9jPKZB8s4ZPRGHwQclJW95fa4I38JAgMBAAECggEAJK38NHDiUXX3VRSsYIJBGA3vbLEBAaCtjdFnb0kzvoipFqCinOSeEGbU09Hcs258zhE8hJtQcGdMQ2T2rNAFurSDMvqw51tESIm3lhRZWfZzRzFjxSfW60V0yCUSPGJIXiDbGNXR7Ag9zeJr9SyvWZ+oqAlyi4aZwVwPwazvBMR04bnyF3fJGME8RUVmCA5biE5Xwa/ykO8hUq21NkzV2Ii58nVzJdk5UmszRwTCCfg/Qyrdr89Uq5Nyv3h0/Rn/McPnjXafUC+qvQEm0m3OI6hxmWiw6bYirObJ3xek42N23+5pcxmvnAaq4izqs8C/kbVzzDfvFHEWHzew2Vgv2QKBgQDnxRGoTp/j80N67XYc23sZO4DzZ9hMwCjJXheYyTpi3xdRGKllmOq5h74hgZnEUEMKUfTtJrj6PMubwRep8VDl0k5jP+G+0v9tz8rxd6/mWUPEElXyJAOoxgnTp+gHq5UjKqezvNtjJegfhl3JfoDEchNJMBQqB7A3FHWxHeSD2wKBgQDhDcGTeZ79OKDXF3K+e4uJ77Isjm4ttylbR3dJaIR4zx+y9pbR0xh2ess1NITm9E4Fcizmn76bsy7FBchw/1edemEpxEWCcgB8WSyTsmP6QN1uG40WiPREIjVM/VR4FOry3tJ+bvg7hgj+6RstxqcRe97kuYaBdNbyo8Hxpl/v6wKBgD1E9ocEyfXrwCIGFlxIlUE1XsB56k9X2TBqhFhqkdnDWhmhKF7oTtzfCp828JxaD27x/YqX2vykB7tUD1popdcrgndwUtAsXUP9U4wD9OczH9v8cOYDKUh7metvDsLAVDuosg6s1V5fjf6PecmjfIzyDzEg2aPGxom5CefZnChTAoGABOvBFNFu+Yh4c9uOZb3b0jtMgQ7oN9WucNGCOTew1ddpkQc1Swc7nLOYp+QrF8W1wwa5Fp3dlBf56NL0jAv5RXc8kqY5At4tRorIn74h04IBlXvCvQBnnunEkpdjdnC2pnLa1WrlwJ9wqOo97uVOF+LGf+fSmhgM7ydHgVoq9NsCgYBy8taA69M3TgpB90BXt9bc3NUEu+yUELzbXkF439twjMpQCAw42zmcdytBme4XomShbcPhs6Bhnxa6UTK79gkb/deB5iB82l7zqiLQb9Ff0O0unKNg76OG/KidW/2IdH5bFfDzED/hzk9Q9qPLfVUynnFGHKOCA0jLRCX0glgUHA==";
    public static String svcId = "song";
    public static String heard_5fu3 = "song";
    public static String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsG4BBS1dCNtY7XNeE6MC7/hlICZT7jhqA7iJn90BQbTdoAB0Vvqohe8gNOO/NebuNl+Y2dyf5NX46XaClJXCigHrwZOwb1QzBHu/EEfthFrMw8dw8RxH9/5kjzkMAIPAmVEa2EnrmFT4pRDU6XU9OK6g6HQhhjj6n0lGF4xnjry56u5MfVjoeTuHgfGgRrHFrOMMHpx3E/c6yHinwYmc1RPEtrRsKwQdHvjQygvAzHxWkjqiC6I0Wjzrn3evAY/icIV84/yaEukYmEmABwYgnmR+TM+t1TQNMEZg+gCt1KWn2yb2+GpxSwQ5vV6gnLQUd7+856WDoJRbe5QGG+pf4wIDAQAB";
    public static String host = "http://127.0.0.1:7002";
    //---- example end

    public static void main(String[] args) {
        /**
         * get Exchange userinfo
         */
        //userinfo();
        /**
         * get asset transfer detail from  Exchange to SaaSservice
         */
        //assetoutinfo("202206151409440002");
        /**
         * get asset transfer detail from SaaSservice to Exchange
         */
        //assetininfo("202206151409590003");
        /**
         * 总额对账接口
         */
        //assettotal();
        /**
         * 详情对账接口
         */
        //assetlist();
    }

    /**
     * 总额对账接口
      */
    public static void assettotal(){
        Map<String, Object> tem = new HashMap<>();
        tem.put("svc_id", svcId);
        tem.put("timestamp", timestamp);
        //
        String user_id = "all";
        String type = "in"; // in/out
        String coin = "usdt";
        String start_time = "2022-04-01 00:00:01";
        String end_time = "2024-04-08 00:00:01";
        String extype = "options"; // futures options
        tem.put("user_id", user_id);
        tem.put("type", type);
        tem.put("coin", coin);
        tem.put("start_time", start_time);
        tem.put("end_time", end_time);
        tem.put("extype", extype);
        //
        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign", sign);

        boolean verify = verify(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\"" + heard_5fu3 + "\",\"sfg6\":\"" + heard_sfg6 + "\"}";
        String param = "{\"svc_id\":\"" + svcId + "\",\"sign\":\"" + sign + "\",\"timestamp\":" + timestamp
                + ",\"user_id\":\"" + user_id
                + "\",\"type\":\"" + type
                + "\",\"coin\":\"" + coin
                + "\",\"start_time\":\"" + start_time
                + "\",\"end_time\":\"" + end_time
                + "\",\"extype\":\"" + extype
                + "\"}";
        //
        String response = new HttpUtil().postByJson( host + "/exapi/assettotal", param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("assettotal detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    /**
     * 详情对账接口
     */
    public static void assetlist(){
        Map<String, Object> tem = new HashMap<>();
        tem.put("svc_id", svcId);
        tem.put("timestamp", timestamp);
        //
        String user_id = "all";
        String type = "out"; // in/out
        String coin = "usdt";
        String start_time = "2022-04-01 00:00:01";
        String end_time = "2024-04-08 00:00:01";
        String extype = ""; // futures options
        Integer id = 0;
        Integer pagesize = 10;
        tem.put("user_id", user_id);
        tem.put("type", type);
        tem.put("coin", coin);
        tem.put("start_time", start_time);
        tem.put("end_time", end_time);
        tem.put("extype", extype);
        tem.put("id", id);
        tem.put("pagesize", pagesize);
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
                + "\",\"type\":\"" + type
                + "\",\"coin\":\"" + coin
                + "\",\"start_time\":\"" + start_time
                + "\",\"end_time\":\"" + end_time
                + "\",\"extype\":\"" + extype
                + "\"}";
        //
        String response = new HttpUtil().postByJson( host + "/exapi/assetlist", param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("assetlist detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    /**
     * 3.1
     * get Exchange userinfo
     */
    public static void userinfo() {
        Map<String, Object> tem = new HashMap<>();
        tem.put("svc_id", svcId);
        tem.put("timestamp", timestamp);
        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign", sign);

        boolean verify = verify(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\"" + heard_5fu3 + "\",\"sfg6\":\"" + heard_sfg6 + "\"}";
        String param = "{\"svc_id\":\"" + svcId + "\",\"sign\":\"" + sign + "\",\"timestamp\":" + timestamp + "}";
        // 住意 /exapi/userinfo/ 后面有/
        String response = new HttpUtil().postByJson(host + "/exapi/userinfo/", param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            String balance = jsonObject.path("data").toString();
            System.out.println("userinfo: "+balance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    /**
     * 3.2
     * get asset transfer detail from  Exchange to SaaSservice
     */
    public static void assetoutinfo(String ex_order_id) {
        Map<String, Object> tem = new HashMap<>();
        tem.put("svc_id", svcId);
        tem.put("timestamp", timestamp);
        tem.put("ex_order_id", ex_order_id);
        //
        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign", sign);

        boolean verify = verify(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\"" + heard_5fu3 + "\",\"sfg6\":\"" + heard_sfg6 + "\"}";
        String param = "{\"svc_id\":\"" + svcId + "\",\"sign\":\"" + sign + "\",\"timestamp\":" + timestamp + ",\"ex_order_id\":\"" + ex_order_id + "\"}";
        //
        String response = new HttpUtil().postByJson( host + "/exapi/assetoutinfo", param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("assetoutinfo detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    /**
     * 3.3
     * get asset transfer detail from SaaSservice to Exchange
     */

    public static void assetininfo(String ex_order_id) {
        Map<String, Object> tem = new HashMap<>();
        tem.put("svc_id", svcId);
        tem.put("timestamp", timestamp);
        tem.put("ex_order_id", ex_order_id);

        String sign = sign(tem, pri);
        System.out.println(sign);
        tem.put("sign", sign);

        boolean verify = verify(tem, pubKey);
        System.out.println(verify);

        String header = "{\"5fu3\":\"" + heard_5fu3 + "\",\"sfg6\":\"" + heard_sfg6 + "\"}";
        String param = "{\"svc_id\":\"" + svcId + "\",\"sign\":\"" + sign + "\",\"timestamp\":" + timestamp + ",\"ex_order_id\":\"" + ex_order_id + "\"}";

        String response = new HttpUtil().postByJson( host + "/exapi/assetininfo", param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("assetininfo: ");
            System.out.println(jsonObject);
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
