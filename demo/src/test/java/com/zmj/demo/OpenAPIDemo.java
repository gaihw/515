package com.zmj.demo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.kafka.common.protocol.types.Field;
import org.json.JSONException;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.function.Function;

/**
 * @Title: OpenAPI Request Demo
 * @ProjectName: saas-api
 * @Description:
 * @date: 2022/1/19
 */
public class OpenAPIDemo {
    //---- example start
    // timestamp
    public static long timestamp = System.currentTimeMillis();
    // ---------- SaaSservice Settings ----------
    // api key
    public static String apiKey = "de2403dbf08fd9add5896b89ccc9a22ca42dd65c0e5bd30f893b3efa240722c1";
    // private key
    public static String pri = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAImDSwu9qP1NL8PPoiRC1Jj0m/ppHrz5CtyvuBXEo3i9mc2cjMbv0c5tTsCcnS8uJtQGoim466rRisQeIaN69KlMxfo/VNYdmmCJokGlxdfP+W8QgZLBfH8wFmZgNBYKQUg/PRoSKEoQTT+Yqk2spETdRkWVTEdZjoYylgazdE+NAgMBAAECgYAO8pPGl8arRhJ3mqB5iD7beNdi+GiWymFuYgwaEvu2r7Fswrl2FHJ+Ga6noZPfzDNR9SiibWScoatlAbt5WEUI3wPRPSxIsfthXlJOFQ2iCdlf16Yb994nU66VSINGdM6x6tUJXytuebLOz+J2O84EvloCHM31VsK+DRSY65JftwJBAPnMMjCGTvV7LycQHdDY2K2cPEeZqDGkCYhGOtNc9Oqvll0+r+iWamlEPeHLhgth12kiEmAOdvzn1cdgq8eLfSMCQQCM7V/hbNRZW2cPS6xqnsX0hnOhQ7vYpySPfnEL3vhw7ZktDyW9R1Tvm/ECYRit5+E7MTXFqh511tHei3N7xgOPAkEAppV3TwbydyC8NEe6KoHCFh0f0fv1v40OUlPLfRL7vdp04yAf/XL56dN5lS+9569LETCIoohi74vH9BtS01MBkwJALUgwoLxZVwT5jn6gPfoaXUG+cbjT6P97zeew50GTzqVprILLe5AqCHuw6zTLu0Vgp6ZeQs8wzmhiMwHX75NmnQJBAOXqy8RZOl3I1FscWfJLip7lbHPyx/bDTQd1dcKFN5UD9D5LU4jM6KTDEoFOjkyWMUkQXWM2Gfmhsyy9MPwLbNg=";
    // public key
    public static String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIxWZ3aJPKPRmycKmbvd+pGtcw7ap2vFIpW8F4LxSqQ56i6kgfuJNAtEKGjJ9pRj7CshLOCMItC/h9itUSOI0wUzVAw3BxdLaUXeAzCdb7lDiSNLsWLjfFB5uFCriPvPtjJ4f7YueuF2xE2pS7n0sBVA/8JGTGArRZEJZ0PEP+8wIDAQAB";
    //
    public static String recv = "5000";
    // Exchange domain
//    public static String host = "https://test1.chimchim.top";
    public static String host = "https://dev1.123kj.top";
    //---- example end

    public static void main(String[] args) throws JSONException {
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
        /**
         * 下单
         */
//        create();
        /**
         * 批量下单
         */
        batch_create_open();
        /**
         * 平仓
         */
//        close();
        /**
         * 撤单
         */
//        cancel();
        /**
         * 委托列表
         */
        current();
        /**
         * 持仓列表
         */
//        positions();
        /**
         * 查询余额
         */
//        balance();
        /**
         * 转账
         */
//        transfer();
        /**
         * 查询币种
         */
//        symbol();


    }

    private static void batch_create_open() throws JSONException {

        String url = host + "/openapi/v1/futures/order/batch_create/buy";

        String param = "[" +
                "{" +
                "\"category\":\"futures\"," +
                "\"symbol\":\"btc\"," +
                "\"side\":\"buy\"," +
                "\"positionType\":\"limit\"," +
                "\"positionSide\":\"long\"," +
                "\"leverage\":\"100\"," +
                "\"quantity\":\"1\"," +
                "\"orderLinkId\":\"Test1\"," +
                "\"price\":\"20000\"" +
                "}," +
                "{" +
                "\"category\":\"futures\"," +
                "\"symbol\":\"btc\"," +
                "\"side\":\"buy\"," +
                "\"positionType\":\"limit\"," +
                "\"positionSide\":\"long\"," +
                "\"leverage\":\"100\"," +
                "\"quantity\":\"11\"," +
                "\"orderLinkId\":\"Test2\"," +
                "\"price\":\"20001\"" +
                "}"+
                "]";

        List list = new ArrayList<>();
        LinkedJsonObject jb = new LinkedJsonObject();
        jb.put("category","futures");
        jb.put("symbol","btc");
        jb.put("side","buy");
        jb.put("positionType","limit");
        jb.put("positionSide","long");
        jb.put("leverage","100");
        jb.put("quantity","1");
        jb.put("orderLinkId","Test1");
        jb.put("price","20000");
        list.add(0,jb);

        LinkedJsonObject jb1 = new LinkedJsonObject();
        jb1.put("category","futures");
        jb1.put("symbol","btc");
        jb1.put("side","buy");
        jb1.put("positionType","limit");
        jb1.put("positionSide","long");
        jb1.put("leverage","100");
        jb1.put("quantity","11");
        jb1.put("orderLinkId","Test2");
        jb1.put("price","20001");
        list.add(1,jb1);

        String sign = signByBatch(timestamp+apiKey+recv, list.toString().replaceAll("\\s*", ""), pri);
        System.out.println("签名["+sign+"]");


        String header = "{\"X-SAASAPI-API-KEY\":\"" + apiKey + "\",\"X-SAASAPI-TIMESTAMP\":\"" + timestamp + "\",\"X-SAASAPI-SIGN\":\"" + sign + "\",\"X-SAASAPI-RECV-WINDOW\":\"" + recv + "\"}";

        //
        String response = new HttpUtil().postByJson( url, list.toString().replaceAll("\\s*", ""), header);
        System.out.println("参数["+list.toString().replaceAll("\\s*", ""));
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    private static void symbol() {
        String url = host + "/openapi/v1/symbol";
        Map<String, Object> tem = new HashMap<>();

        String sign = sign(timestamp+apiKey+recv, tem, pri);
        System.out.println("签名["+sign+"]");

        boolean verify = verify(sign, timestamp+apiKey+recv, tem, pubKey);
        System.out.println(verify);

        String header = "{\"X-SAASAPI-API-KEY\":\"" + apiKey + "\",\"X-SAASAPI-TIMESTAMP\":\"" + timestamp + "\",\"X-SAASAPI-SIGN\":\"" + sign + "\",\"X-SAASAPI-RECV-WINDOW\":\"" + recv + "\"}";

        //
        String response = new HttpUtil().get( url, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    private static void transfer() {
        String url = host + "/openapi/v1/transfer";

        String fromBiz = "futures"; // 业务：futures永续合约,options期权,spot现货
        String toBiz = "spot"; // 目标业务：futures永续合约,options期权,spot现货
        String amount = "1"; // 划转数量

        Map<String, Object> tem = new HashMap<>();
        //签名
        tem.put("fromBiz", fromBiz);
        tem.put("toBiz", toBiz);
        tem.put("amount", amount);

        String sign = sign(timestamp+apiKey+recv, tem, pri);
        System.out.println("签名["+sign+"]");

        boolean verify = verify(sign, timestamp+apiKey+recv, tem, pubKey);
        System.out.println(verify);

        String header = "{\"X-SAASAPI-API-KEY\":\"" + apiKey + "\",\"X-SAASAPI-TIMESTAMP\":\"" + timestamp + "\",\"X-SAASAPI-SIGN\":\"" + sign + "\",\"X-SAASAPI-RECV-WINDOW\":\"" + recv + "\"}";
        String param = "{\"fromBiz\":\"" + fromBiz
                + "\",\"toBiz\":\"" + toBiz
                + "\",\"amount\":\"" + amount
                + "\"}";

        //
        String response = new HttpUtil().postByJson( url, param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    private static void balance() {
        String url = host + "/openapi/v1/balance";
        Map<String, Object> tem = new HashMap<>();

        String sign = sign(timestamp+apiKey+recv, tem, pri);
        System.out.println("签名["+sign+"]");

        boolean verify = verify(sign, timestamp+apiKey+recv, tem, pubKey);
        System.out.println(verify);

        String header = "{\"X-SAASAPI-API-KEY\":\"" + apiKey + "\",\"X-SAASAPI-TIMESTAMP\":\"" + timestamp + "\",\"X-SAASAPI-SIGN\":\"" + sign + "\",\"X-SAASAPI-RECV-WINDOW\":\"" + recv + "\"}";

        //
        String response = new HttpUtil().get( url, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    private static void positions() {
        String url = host + "/openapi/v1/order/positions";

        String category = "futures"; // 订单类别：futures永续合约,options期权,spot现货
        String symbol = "btc"; // 币对名称, 如 btc,eth
        String positionModel = "fix"; // 仓位模式 fix=逐仓 cross=全仓
        int page = 1;
        int pageSize = 10;

        Map<String, Object> tem = new HashMap<>();
        //签名
        tem.put("category", category);
        tem.put("symbol", symbol);
        tem.put("positionModel", positionModel);
        tem.put("page", page);
        tem.put("pageSize", pageSize);

        String sign = sign(timestamp+apiKey+recv, tem, pri);
        System.out.println("签名["+sign+"]");

        boolean verify = verify(sign, timestamp+apiKey+recv, tem, pubKey);
        System.out.println(verify);

        String header = "{\"X-SAASAPI-API-KEY\":\"" + apiKey + "\",\"X-SAASAPI-TIMESTAMP\":\"" + timestamp + "\",\"X-SAASAPI-SIGN\":\"" + sign + "\",\"X-SAASAPI-RECV-WINDOW\":\"" + recv + "\"}";

        String response = new HttpUtil().get( url+"?category="+category+"&symbol="+symbol+"&positionModel="+positionModel+"&page="+page+"&pageSize="+pageSize, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    public static void current(){
        String url = host + "/openapi/v1/order/current";

        String category = "futures"; // 订单类别：futures永续合约,options期权,spot现货
        String symbol = "btc"; // 币对名称, 如 btc,eth
        String positionModel = "fix"; // 仓位模式 fix=逐仓 cross=全仓
        int page = 1;
        int pageSize = 10;

        Map<String, Object> tem = new HashMap<>();
        //签名
        tem.put("category", category);
        tem.put("symbol", symbol);
        tem.put("positionModel", positionModel);
        tem.put("page", page);
        tem.put("pageSize", pageSize);

        String sign = sign(timestamp+apiKey+recv, tem, pri);
        System.out.println("签名["+sign+"]");

        boolean verify = verify(sign, timestamp+apiKey+recv, tem, pubKey);
        System.out.println(verify);

        String header = "{\"X-SAASAPI-API-KEY\":\"" + apiKey + "\",\"X-SAASAPI-TIMESTAMP\":\"" + timestamp + "\",\"X-SAASAPI-SIGN\":\"" + sign + "\",\"X-SAASAPI-RECV-WINDOW\":\"" + recv + "\"}";

        String response = new HttpUtil().get( url+"?category="+category+"&symbol="+symbol+"&positionModel="+positionModel+"&page="+page+"&pageSize="+pageSize, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }
    private static void cancel() {
        String url = host + "/openapi/v1/order/cancel";

        String category = "futures"; // 订单类别：futures永续合约,options期权,spot现货
        String symbol = "btc"; // 币对名称, 如 btc,eth
        String id = "386809"; // 订单ID
        String positionType = "market"; // 订单类型：limit=限价单,market=市价单


        Map<String, Object> tem = new HashMap<>();
        tem.put("category", category);
        tem.put("symbol", symbol);
        tem.put("positionType", positionType);
        tem.put("id", id);

        String sign = sign(timestamp+apiKey+recv, tem, pri);
        System.out.println("签名["+sign+"]");

        boolean verify = verify(sign, timestamp+apiKey+recv, tem, pubKey);
        System.out.println(verify);

        String header = "{\"X-SAASAPI-API-KEY\":\"" + apiKey + "\",\"X-SAASAPI-TIMESTAMP\":\"" + timestamp + "\",\"X-SAASAPI-SIGN\":\"" + sign + "\",\"X-SAASAPI-RECV-WINDOW\":\"" + recv + "\"}";
        String param = "{\"category\":\"" + category
                + "\",\"symbol\":\"" + symbol
                + "\",\"positionType\":\"" + positionType
                + "\",\"id\":\"" + id
                + "\"}";
        //
        String response = new HttpUtil().postByJson( url, param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    public static void close(){
        String url = host + "/openapi/v1/order/close";

        String category = "futures"; // 订单类别：futures永续合约,options期权,spot现货
        String symbol = "btc"; // 币对名称, 如 btc,eth
        String closeType = "689859396487745536"; // 平仓类型：all全平,687748042004402176(仓位id) 非全平
        String positionType = "limit"; // 订单类型：limit=限价单,market=市价单
        String closeRate = "0.1";// 平仓比例
        String closeNum = "3";// 	平仓数量
        String price = "-27001";// 平仓价格(限价单必传)

        Map<String, Object> tem = new HashMap<>();
        tem.put("category", category);
        tem.put("symbol", symbol);
        tem.put("positionType", positionType);
        tem.put("closeType", closeType);
//        tem.put("closeRate", closeRate);
        tem.put("closeNum", closeNum);
        tem.put("price", price);

        String sign = sign(timestamp+apiKey+recv, tem, pri);
        System.out.println("签名["+sign+"]");

        boolean verify = verify(sign, timestamp+apiKey+recv, tem, pubKey);
        System.out.println(verify);

        String header = "{\"X-SAASAPI-API-KEY\":\"" + apiKey + "\",\"X-SAASAPI-TIMESTAMP\":\"" + timestamp + "\",\"X-SAASAPI-SIGN\":\"" + sign + "\",\"X-SAASAPI-RECV-WINDOW\":\"" + recv + "\"}";
        String param = "{\"category\":\"" + category
                + "\",\"symbol\":\"" + symbol
                + "\",\"positionType\":\"" + positionType
                + "\",\"closeType\":\"" + closeType
                + "\",\"closeNum\":\""+closeNum
                + "\",\"price\":\""+price+"\""
                + "}";
        //
        String response = new HttpUtil().postByJson( url, param, header);
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("detail: ");
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================================");
    }

    /**
     * 下单
     */
    public static void create(){
        String url = host + "/openapi/v1/order/create";

        Map<String, Object> tem = new HashMap<>();
        String category = "futures"; // 订单类别：futures永续合约,options期权,spot现货
        String symbol = "eth"; // 币对名称, 如 btc,eth
        String positionType = "limit"; // 订单类型：limit=限价单,market=市价单
        String positionModel = "cross"; // 仓位模式 fix=逐仓 cross=全仓
        String positionSide = "short"; // 持仓方向：long=买多,short=买空
        BigDecimal leverage = BigDecimal.valueOf(11.12).setScale(16,BigDecimal.ROUND_UP); // 杠杆 整数, 最小为1
        BigDecimal quantity = BigDecimal.valueOf(10); // 开仓数量, 最小为1
        String quantityUnit = "cont"; // 合约下单单位：usdt=按u下单/cont=按张下单，默认是usdt
        BigDecimal price = BigDecimal.valueOf(1829).setScale(16,BigDecimal.ROUND_UP); // 订单价格：限价才需要，市价不需要
        tem.put("category", category);
        tem.put("symbol", symbol);
        tem.put("positionType", positionType);
        tem.put("positionModel", positionModel);
        tem.put("positionSide", positionSide);
        tem.put("leverage", leverage);
        tem.put("quantity", quantity);
        tem.put("quantityUnit", quantityUnit);
        tem.put("price", price);
        //
        String sign = sign(timestamp+apiKey+recv, tem, pri);
        System.out.println("签名["+sign+"]");

        boolean verify = verify(sign, timestamp+apiKey+recv, tem, pubKey);
        System.out.println(verify);

        String header = "{\"X-SAASAPI-API-KEY\":\"" + apiKey + "\",\"X-SAASAPI-TIMESTAMP\":\"" + timestamp + "\",\"X-SAASAPI-SIGN\":\"" + sign + "\",\"X-SAASAPI-RECV-WINDOW\":\"" + recv + "\"}";
        String param = "{\"category\":\"" + category
                + "\",\"symbol\":\"" + symbol
                + "\",\"positionType\":\"" + positionType
                + "\",\"positionModel\":\"" + positionModel
                + "\",\"positionSide\":\"" + positionSide
                + "\",\"leverage\":\"" + leverage
                + "\",\"quantity\":\"" + quantity
                + "\",\"quantityUnit\":\"" + quantityUnit
                + "\",\"price\":\"" + price
                + "\"}";
        //
        String response = new HttpUtil().postByJson( url, param, header);
        System.out.println("参数["+param+"]");
        System.out.println("================================================================================================");
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            //
            System.out.println("detail: ");
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

    public static boolean verify(String sign, String header, Map<String, Object> objectMap, String publicKeyString) {
        try {
            String srcData = header+mapToString(objectMap);
//            System.out.println("签名参数["+srcData+"]");
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

    public static String sign(String header, Map<String, Object> data, String privateKeyString) {
        try {
            String srcData = header+mapToString(data);
            System.out.println("签名参数["+srcData+"]");
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

    /**
     * 批量下单、批量撤单签名方法
     * @param header
     * @param data
     * @param privateKeyString
     * @return
     */
    public static String signByBatch(String header, String data, String privateKeyString) {
        try {
            String srcData = header+data;
            System.out.println("签名参数["+srcData+"]");
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
