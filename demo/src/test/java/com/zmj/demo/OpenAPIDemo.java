package com.zmj.demo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
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
    public static String apiKey = "06065c747863e2f910dd557b379b2345bd8298bdc50973b015ac8b6d4e0cc4a6";
    // private key
    public static String pri = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMjFZndok8o9GbJwqZu936ka1zDtqna8UilbwXgvFKpDnqLqSB+4k0C0QoaMn2lGPsKyEs4Iwi0L+H2K1RI4jTBTNUDDcHF0tpRd4DMJ1vuUOJI0uxYuN8UHm4UKuI+8+2Mnh/ti564XbETalLufSwFUD/wkZMYCtFkQlnQ8Q/7zAgMBAAECgYAGcOLJKOc44T3uThP65ZwDylMmBDHoTkFah1GNIAGLNtEji92Veu/qbv4FYASLNZA04c6dooVMOaqWqHoOdBb/a9fTl1hr9qXZWxxZ31A4/eV4dK17LKsJFOi7YsDcPoRhOxXHtEElSXV790Hu3gObaNrRpXpeI1AkEwB7WmTVtQJBAOVP+corBR6WBvW9v6ixnIrPO9WqV7v7Lq6lQpYNgGmnDmBxL9utuEzuhir0nK8uwxE4UVOVuyhbv6Nh+rjlut0CQQDgIxQOo3HtzUSpAeHLrJv2ixU2OQDorvNByhX0TauegEGa0/l9BFHjuSw9jICYgdxuhsH5iSNdkbfo940xR3wPAkEA2B4l53ngG1F+QcCRj6XFSbXCSL+AbHRxLLwoI0+aRPjjPdWvKzVwy2DKJlXgDeLeia4wR7yIZaMC3DMNn5E0KQJALDF+cOx1OEgy84f1d21PSytdQVW4AikMuboY3hS6dAQh619EYAwMAXSvbmtXp7pjNj/H22XY3UgwFPVKl57arQJATQllXvkDahTjScaJq+Hro4mTa7pkvscGtWxChs1qJrHxlgsVGSnWcJIas7nT+c+t/zbWadAkwMk/aPe/aWRHvg==";
    // public key
    public static String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIxWZ3aJPKPRmycKmbvd+pGtcw7ap2vFIpW8F4LxSqQ56i6kgfuJNAtEKGjJ9pRj7CshLOCMItC/h9itUSOI0wUzVAw3BxdLaUXeAzCdb7lDiSNLsWLjfFB5uFCriPvPtjJ4f7YueuF2xE2pS7n0sBVA/8JGTGArRZEJZ0PEP+8wIDAQAB";
    //
    public static String recv = "5000";
    // Exchange domain
//    public static String host = "https://test1.chimchim.top";
    public static String host = "https://dev1.123kj.top";
    //---- example end

    public static void main(String[] args) {
        ((LoggerContext) LoggerFactory.getILoggerFactory())
                .getLoggerList()
                .forEach(logger -> logger.setLevel(Level.ERROR));
        /**
         * 下单
         */
        create();
    }


    /**
     * 详情对账接口
     */
    public static void create(){
        String url = host + "/openapi/v1/order/create";

        timestamp = 1683772968951l;
        Map<String, Object> tem = new HashMap<>();
        String category = "futures"; // 订单类别：futures永续合约,options期权,spot现货
        String symbol = "btc"; // 币对名称, 如 btc,eth
        String positionType = "limit"; // 订单类型：limit=限价单,market=市价单
        String positionModel = "cross"; // 仓位模式 fix=逐仓 cross=全仓
        String positionSide = "short"; // 持仓方向：long=买多,short=买空
        BigDecimal leverage = new BigDecimal(71); // 杠杆 整数, 最小为1
        BigDecimal quantity = new BigDecimal(1); // 开仓数量, 最小为1
        String quantityUnit = "cont"; // 合约下单单位：usdt=按u下单/cont=按张下单，默认是usdt
        BigDecimal price = new BigDecimal(27710.690332049962); // 订单价格：限价才需要，市价不需要
//        {"category":"futures","symbol":"btc","positionType":"limit","positionModel":"cross","positionSide":"short","leverage":71,"quantity":1,"quantityUnit":"cont","price":27710.690332049962}
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
                + "\",\"leverage\":" + leverage
                + ",\"quantity\":" + quantity
                + ",\"quantityUnit\":\"" + quantityUnit
                + "\",\"price\":" + price
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
}
