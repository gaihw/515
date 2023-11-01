package com.zmj.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.HttpUtil;
import com.zmj.demo.common.dev1.UserCheckThread;
import com.zmj.demo.dao.dev.OptionsInfoDao;
import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.dev1.OptionsInfoChain;
import com.zmj.demo.service.impl.plugin.RedisService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private UserCheckThread userCheckThread;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OptionsInfoDao optionsInfoDao;

    // prometheus 收集
    @Autowired
    MeterRegistry registry;
    private Counter counter;
    private Counter simpleCounter;
    private Counter failCounter;

    private JSONObject tokenList = new JSONObject();

    @RequestMapping(value = "/v1/testfile/test.json",method = RequestMethod.GET)
    public String test(){

        return "jfVDKG2xESgL6dPYC7Nlak4bEupDkig5Kasu9wDSokdbdloHV1Zt9cYKVJngP1Vk78b93n5BPVL1z+isoUk/+rbeD1jJGfCXGx244fLAgQSpTdEK0bRA1yQkZUFElpuISALSzkd6rhfrpIabGyyYjhXARmtaRY/KWJWP6c3VAUspu+0IYenlYs4zbtEM/Qhazl5oj27h2J7zM+dr19W3WTKpjGBKcw7OocdtBJEk/2odblkAUimijXbLm2arohApQ9GXxrPf/u8TmH5UYvM6JVbUMfttCKlanGWG089YoAbgjz99CNSQF5tjcIr4zclcJoBSFxdi4qxMQepadlzsqQ==";
    }
    @RequestMapping(value = "/v1/test1",method = RequestMethod.POST)
    public JSONObject test01(){
        return JSONObject.parseObject("{\"status\":\"success\",\"errorCode\":\"\",\"errorMsg\":\"\",\"data\":{\"accessNumberList\":[24,18,1,1,1],\"userNameList\":[\"陈陈\",\"盖洪伟\",\"实体abc\",\"包岩\",\"敏感数据访问监控001敏感数据访问监控\"]}}");
    }

    @RequestMapping(value = "/v1/test2",method = RequestMethod.POST)
    public JSONObject test02(){
        return JSONObject.parseObject("{\"status\":\"success\",\"errorCode\":\"\",\"errorMsg\":\"\",\"data\":{\"timeList\":[\"2021-09-10\",\"2021-09-11\",\"2021-09-12\",\"2021-09-13\",\"2021-09-14\",\"2021-09-15\",\"2021-09-16\",\"2021-09-17\"],\"dataList\":[{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":77666666,\"fieldNumber\":18}]}}");
    }

    @RequestMapping(value = "/v1/test3",method = RequestMethod.POST)
    public JSONObject test03(){
        return JSONObject.parseObject("{\"status\":\"success\",\"errorCode\":\"\",\"errorMsg\":\"\",\"data\":{\"content\":[{\"id\":10003575,\"jobName\":\"回归a0003\",\"jobCategoryId\":null,\"createdBy\":\"盖洪伟\",\"updatedDate\":\"2021-09-27 21:22:03\",\"scheduleCycle\":\"MANUAL\",\"lastRunTime\":\"2021-09-27 20:17:11\",\"status\":\"UPDATE_FAIL\",\"open\":true,\"statistics\":35328,\"runDuration\":50000,\"nextRunTime\":null,\"description\":\"\",\"dataSourceName\":null,\"dataSourceType\":null,\"businessType\":null,\"jobId\":null,\"catalogTableId\":10010420,\"repositoryJobNodeDetail\":null,\"tableName\":null,\"tableAlias\":null}],\"pageable\":{\"sort\":{\"unsorted\":true,\"sorted\":false,\"empty\":true},\"pageSize\":10,\"pageNumber\":0,\"offset\":0,\"unpaged\":false,\"paged\":true},\"totalElements\":1,\"totalPages\":1,\"last\":true,\"sort\":{\"unsorted\":true,\"sorted\":false,\"empty\":true},\"numberOfElements\":1,\"first\":true,\"size\":10,\"number\":0,\"empty\":false}}");
    }

    @RequestMapping(value = "/v1/test4",method = RequestMethod.POST)
    public String test04(){
        int count = 100;
//        for (int i = 0; i < count; i++) {
//            userCheckThread.test("index = " + i);
//        }
        return "success";

    }

    @RequestMapping(value = "/v1/test5",method = RequestMethod.GET)
    public void test05() throws IOException {
        File mobile = new File("/Users/mac/Desktop/user.csv");
        FileWriter fw = new FileWriter(mobile,true);
        HttpUtil httpUtil = new HttpUtil();
        String url = "http://www-test1.tfbeee.shop/v1/users/membership/sign-in";
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

    @RequestMapping(value = "/v1/test6",method = RequestMethod.GET)
    public void test06(){
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 111; i++) {
            list.add(i + ",");
        }
        StringBuffer coupons = getCoupons(list, 5);
        System.out.println(coupons);
    }


    @RequestMapping(value = "/v1/test7",method = RequestMethod.GET)
    public JsonResult test07(@RequestParam("highestRate") BigDecimal highestRate,
                             @RequestParam("lowestRate") BigDecimal lowestRate,
                             @RequestParam("instrument") String instrument,
                             @RequestParam("users") String users,
                             @RequestParam("token") String token,
                             @RequestParam("host") String host,
                             @RequestParam("isTransfer") boolean isTransfer) throws Exception {
        // 域名
//        String host = "https://www.hpxshop.me";//https://x.chimchim.top";
//        String host = "https://www-demo.wq6tfggsb8.info";
        HttpUtil httpUtil = new HttpUtil();
//        String token = "";
        String param = "";
        String headers = "";

        // 用户数量
        if (users == null || users.equalsIgnoreCase("0") || users.equalsIgnoreCase("")){
            users = "16630000001,16630000002,16630000003,16630000004,16630000005,16630000006,16630000007,16630000008,16630000009,16630000010," +
                    "16630000011,16630000012,16630000013,16630000014,16630000015,16630000016,16630000017,16630000018,16630000019,16630000020," +
                    "16630000021,16630000022,16630000023,16630000024,16630000025,16630000026,16630000027,16630000028,16630000029,16630000030," +
                    "16630000031,16630000032,16630000033,16630000034,16630000035,16630000036,16630000037,16630000038,16630000039,16630000040," +
                    "16630000041,16630000042,16630000043,16630000044,16630000045,16630000046,16630000047,16630000048,16630000049,16630000050," +
                    "16630000051,16630000052,16630000053,16630000054,16630000055,16630000056,16630000057,16630000058,16630000059,16630000060," +
                    "16630000061,16630000062,16630000063,16630000064,16630000065,16630000066,16630000067,16630000068,16630000069,16630000070," +
                    "16630000071,16630000072,16630000073,16630000074,16630000075,16630000076,16630000077,16630000078,16630000079,16630000080," +
                    "16630000081,16630000082,16630000083,16630000084,16630000085,16630000086,16630000087,16630000088,16630000089,16630000090," +
                    "16630000091,16630000092,16630000093,16630000094,16630000095,16630000096,16630000097,16630000098,16630000100,16630000101";
        }
        // 币种数量
        String[] instruments = instrument.split(",");
//        String[] instruments = {"btc","eth","ltc","eos","bch","trx","fil","link","dot","xrp","doge","shib","ada","matic","sol"};

        Random r = new Random();
        BigDecimal markPrice ;
        BigDecimal highestPrice ;
        BigDecimal lowestPrice ;
        int directionId;
        int gridNum ;
        int propertyId;

        int rTmp;
        for (String u: users.split(",")) {
            param = "{\"password\": \"ghw111111\",\"username\": \""+u+"\"}";
            // 登录
            if (token.equalsIgnoreCase(null)){
                token = JSONObject.parseObject(httpUtil.postByJson(host+"/v1/users/membership/sign-in",param)).getJSONObject("data").getString("accessToken");
            }
            headers = "{\"Content-Type\":\"application/json\",\"X-Authorization\":\""+token+"\",\"5fu3\":\"xxOi\"}";
            // 创建网格
            for (String i:instruments) {
                rTmp = r.nextInt(100);
                markPrice = BigDecimal.valueOf(Double.valueOf((String) redisService.getValue(i))).setScale(16,BigDecimal.ROUND_DOWN);
                highestPrice = markPrice.multiply(highestRate);
                lowestPrice = markPrice.multiply(lowestRate);
                //做多策略
                if (rTmp <=33){
                    directionId = 2;
                }else if (rTmp > 33 && rTmp <= 66){//做空策略
                    directionId = 3;
                }else {//中性
                    directionId = 1;
                }
                rTmp = r.nextInt(100);
                if (rTmp <=20){
                    gridNum = 10;
                }else if (rTmp > 20 && rTmp <= 40){
                    gridNum = 20;
                }else if (rTmp > 60 && rTmp <= 80){
                    gridNum = 30;
                }else {
                    gridNum = 40;
                }
                rTmp = r.nextInt(100);
                if (rTmp <=50){
                    propertyId = 1;
                }else{
                    propertyId = 2;
                }
                gridNum = 150;
                System.out.println("币种:"+i+"--->标记价格:"+markPrice+",最高价格:"+highestPrice+",最低价格:"+lowestPrice+",方向:"+directionId);
                param = "{\"directionId\":"+directionId+",\"gridNum\":"+gridNum+",\"highestPrice\":"+highestPrice+",\"instrument\":\""+i+"\",\"leverage\":50,\"lowestPrice\":"+lowestPrice+",\"margin\":2000,\"propertyId\":"+propertyId+",\"stopLoss\":null,\"stopProfit\":null,\"triggerPrice\":null}";
                System.out.println(httpUtil.postByJson(host+"/v1/cfd/order/grid/trade/add",param,headers));
                System.out.println("===========================");
            }
            if(isTransfer){
                Thread.sleep(5000);
                JSONArray rows = JSONObject.parseObject(httpUtil.get(host+"/v1/cfd/order/grid/trade/query?gridStatus=1&page=1&pageSize=20",headers)).getJSONObject("data").getJSONArray("rows");
                for (int i = 0 ; i < rows.size() ; i++) {
                    param = "{\"amount\": 5000,\"id\": "+rows.getJSONObject(i).getString("id")+",\"requestId\": \""+System.currentTimeMillis()+"\"}";
                    System.out.println(httpUtil.postByJson(host+"/v1/cfd/order/grid/trade/transfer/in",param,headers));
                }
            }

        }

        return new JsonResult<>(0,"success");
    }


    @RequestMapping(value = "/v1/test8",method = RequestMethod.GET)
    public JsonResult test08(@RequestParam("users") String users,
                             @RequestParam("token") String token,
                             @RequestParam("host") String host) throws Exception {
        // 用户数量
        if (users == null || users.equalsIgnoreCase("0") || users.equalsIgnoreCase("")){
            users = "16630000001,16630000002,16630000003,16630000004,16630000005,16630000006,16630000007,16630000008,16630000009,16630000010," +
                    "16630000011,16630000012,16630000013,16630000014,16630000015,16630000016,16630000017,16630000018,16630000019,16630000020," +
                    "16630000021,16630000022,16630000023,16630000024,16630000025,16630000026,16630000027,16630000028,16630000029,16630000030," +
                    "16630000031,16630000032,16630000033,16630000034,16630000035,16630000036,16630000037,16630000038,16630000039,16630000040," +
                    "16630000041,16630000042,16630000043,16630000044,16630000045,16630000046,16630000047,16630000048,16630000049,16630000050," +
                    "16630000051,16630000052,16630000053,16630000054,16630000055,16630000056,16630000057,16630000058,16630000059,16630000060," +
                    "16630000061,16630000062,16630000063,16630000064,16630000065,16630000066,16630000067,16630000068,16630000069,16630000070," +
                    "16630000071,16630000072,16630000073,16630000074,16630000075,16630000076,16630000077,16630000078,16630000079,16630000080," +
                    "16630000081,16630000082,16630000083,16630000084,16630000085,16630000086,16630000087,16630000088,16630000089,16630000090," +
                    "16630000091,16630000092,16630000093,16630000094,16630000095,16630000096,16630000097,16630000098,16630000100,16630000101";
        }
        // 域名
//        String host = "https://www.hpxshop.me";//https://x.chimchim.top";
        HttpUtil httpUtil = new HttpUtil();
        String param = "";
        String headers = "";
        for (String u: users.split(",")) {
            param = "{\"password\": \"ghw111111\",\"username\": \""+u+"\"}";
            // 登录
            if (token.equalsIgnoreCase(null))
            {
                token = JSONObject.parseObject(httpUtil.postByJson(host+"/v1/users/membership/sign-in",param)).getJSONObject("data").getString("accessToken");
            }
            headers = "{\"Content-Type\":\"application/json\",\"X-Authorization\":\""+token+"\"}";
            JSONArray rows = JSONObject.parseObject(httpUtil.get(host+"/v1/cfd/order/grid/trade/query?gridStatus=1&page=1&pageSize=20",headers)).getJSONObject("data").getJSONArray("rows");
            for (int i = 0 ; i < rows.size() ; i++) {
                param = "{\"id\": "+rows.getJSONObject(i).getString("id")+"}";
                System.out.println(httpUtil.postByForm(host+"/v1/cfd/order/grid/trade/stopCurrentGrid",param,headers));
                Thread.sleep(2000);
            }
        }
        return new JsonResult<>(0,"success");
    }
    @RequestMapping(value = "/v1/test9",method = RequestMethod.GET)
    public JsonResult test09(@RequestParam("users") String users,
                             @RequestParam("token") String token,
                             @RequestParam("isToken") boolean isToken,
                             @RequestParam("host") String host,
                             @RequestParam("openNum") int openNum,
                             @RequestParam("symbol") String symbol
                             ) throws Exception {
        // 域名
//        String host = "https://www.hpxshop.me";//https://x.chimchim.top";
//        String host = "https://www-demo.wq6tfggsb8.info";
        HttpUtil httpUtil = new HttpUtil();
//        String token = "";
        String param = "";
        String headers = "";

        // 用户数量
        if (users == null || users.equalsIgnoreCase("0") || users.equalsIgnoreCase("")){
            users = "16610000001,16610000002,16610000003,16610000004,16610000005,16610000006,16610000007,16610000008,16610000009,16610000010," +
                    "16610000011,16610000012,16610000013,16610000014,16610000015,16610000016,16610000017,16610000018,16610000019,16610000020," +
                    "16610000021,16610000022,16610000023,16610000024,16610000025,16610000026,16610000027,16610000028,16610000029,16610000030," +
                    "16610000031,16610000032,16610000033,16610000034,16610000035,16610000036,16610000037,16610000038,16610000039,16610000040," +
                    "16610000041,16610000042,16610000043,16610000044,16610000045,16610000046,16610000047,16610000048,16610000049,16610000050," +
                    "16610000051,16610000052,16610000053,16610000054,16610000055,16610000056,16610000057,16610000058,16610000059,16610000060," +
                    "16610000061,16610000062,16610000063,16610000064,16610000065,16610000066,16610000067,16610000068,16610000069,16610000070," +
                    "16610000071,16610000072,16610000073,16610000074,16610000075,16610000076,16610000077,16610000078,16610000079,16610000080," +
                    "16610000081,16610000082,16610000083,16610000084,16610000085,16610000086,16610000087,16610000088,16610000089,16610000090," +
                    "16610000091,16610000092,16610000093,16610000094,16610000095,16610000096,16610000097,16610000098,16610000100,16610000101";
        }
        // 获取期权币种
        List<OptionsInfoChain> optionsInfoChains = optionsInfoDao.getList();
        // 期权标记价格和delta
        JSONObject optionsTicker;
        // 期权标记价格
        BigDecimal ticker;
        // delta
        double delta;
        // 期权盘口
        JSONObject optionsDepthStep0;
        // 期权卖盘
        JSONArray asks ;
        // 期权买盘
        JSONArray bids ;
        // 指数价格
        BigDecimal indexPrice;
        // 虚值
        BigDecimal OTM;
        // 调整系数
        BigDecimal V;
        // 最高下单价格
        BigDecimal highestPrice ;
        //最低下单价格
        BigDecimal lowestPrice ;
        // Max (1, 4 ×[1- abs (delta)])
        BigDecimal D;
        Random r = new Random();
        int rTmp;
        for (int i = 0; i < openNum; i++) {
            rTmp = r.nextInt(users.split(",").length);
            if (!isToken) {
                if (tokenList.keySet().contains(users.split(",")[rTmp])) {
                    token = tokenList.getString(users.split(",")[rTmp]);
                } else {
                    param = "{\"password\": \"ghw111111\",\"username\": \"" + users.split(",")[rTmp] + "\"}";
                    // 登录
                    if (token.equalsIgnoreCase(null) || token == "") {
                        token = JSONObject.parseObject(httpUtil.postByJson(host + "/v1/users/membership/sign-in", param)).getJSONObject("data").getString("accessToken");
                    }
                    tokenList.put(users.split(",")[rTmp], token);
                }
            }
            headers = "{\"Content-Type\":\"application/json\",\"X-Authorization\":\""+token+"\",\"5fu3\":\"xxOi\"}";
            rTmp = r.nextInt(optionsInfoChains.size());
//            if (symbol == null )
//                symbol = optionsInfoChains.get(rTmp).getInstrumentName();
            symbol = optionsInfoChains.get(rTmp).getInstrumentName();
            // 期权标记价格和delta
            optionsTicker = JSONObject.parseObject(redisService.get("options::ticker::"+symbol+"_ticker").toString());
            ticker = optionsTicker.getBigDecimal("mp");
            delta = optionsTicker.getDoubleValue("d");
            // 期权盘口 options::depth_step0::BTC-230314-18500-P_depth_step0
            optionsDepthStep0 = JSONObject.parseObject(redisService.get("options::depth_step0::"+symbol+"_depth_step0").toString());
            asks = optionsDepthStep0.getJSONArray("asks");
            bids = optionsDepthStep0.getJSONArray("bids");
            // contract::index_price::btcusdt
            indexPrice = JSONObject.parseObject(redisService.get("contract::index_price::btcusdt").toString()).getBigDecimal("ip");

            //计算最低最高价格
            // 最大下单价= 期权标记价格+ 调整系数× Max (1, 4 ×[1- abs (delta)])
            // 最小下单价= 期权标记价格- 调整系数× Max (1, 4 × [1- abs (delta)])
            // 调整系数= min( 指数价格* 0.06, (指数价格* 0.06+OTM amount* 0.4) ）
            // OTM amount为虚值金额，看涨期权=max(0,（指数价格-行权价格）)；看跌期权=max(0,（行权价格-指数价格)）。
            if (symbol.split("-")[3].equalsIgnoreCase("c")){
                OTM = (indexPrice.subtract(BigDecimal.valueOf(Integer.valueOf(symbol.split("-")[2])))).compareTo(BigDecimal.ZERO) == 1 ?
                        indexPrice.subtract(BigDecimal.valueOf(Integer.valueOf(symbol.split("-")[2]))) : BigDecimal.ZERO;
            }else {
                OTM = (BigDecimal.valueOf(Integer.valueOf(symbol.split("-")[2])).subtract(indexPrice)).compareTo(BigDecimal.ZERO) == 1 ?
                        BigDecimal.valueOf(Integer.valueOf(symbol.split("-")[2])).subtract(indexPrice) : BigDecimal.ZERO;
            }
            // 调整系数= min( 指数价格* 0.06, (指数价格* 0.06+OTM amount* 0.4) ）
            V = (indexPrice.multiply(BigDecimal.valueOf(0.06))).compareTo((indexPrice.multiply(BigDecimal.valueOf(0.06)).add(OTM.multiply(BigDecimal.valueOf(0.4))))) == 1 ?
                    indexPrice.multiply(BigDecimal.valueOf(0.06)).add(OTM.multiply(BigDecimal.valueOf(0.4))) : indexPrice.multiply(BigDecimal.valueOf(0.06));
            // Max (1, 4 × [1- abs (delta)])
            D = (BigDecimal.valueOf(4).multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(Math.abs(delta))))).compareTo(BigDecimal.ONE) == 1 ?
                    BigDecimal.valueOf(4).multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(Math.abs(delta)))) : BigDecimal.ONE ;

            highestPrice = ticker.add(V.multiply(D));
            lowestPrice = ticker.subtract(V.multiply(D)).compareTo(BigDecimal.valueOf(5)) == 1 ? ticker.subtract(V.multiply(D)) : BigDecimal.valueOf(5);

            // 卖盘有盘口
            if (asks.size() >= 1){
                rTmp = r.nextInt(100);
                // 挂买盘
                if (rTmp <= 30){
                    param = "{\"direction\":\"long\",\"openPrice\":"+BigDecimal.valueOf(Double.valueOf(asks.getJSONArray(0).get(0).toString())).subtract(BigDecimal.valueOf(5))+",\"optionsName\":\""+symbol+"\",\"priceType\":\"LIMIT\",\"quantity\":"+r.nextInt(100)+"}";
                }else if (rTmp > 30 && rTmp <= 60){ // 部分成交
                    param = "{\"direction\":\"long\",\"openPrice\":"+BigDecimal.valueOf(Double.valueOf(asks.getJSONArray(0).get(0).toString()))+",\"optionsName\":\""+symbol+"\",\"priceType\":\"LIMIT\",\"quantity\":"+BigDecimal.valueOf(Double.valueOf(asks.getJSONArray(0).get(1).toString())).setScale(0,BigDecimal.ROUND_DOWN).subtract(BigDecimal.ONE)+"}";
                }else {// 完全成交
                    param = "{\"direction\":\"long\",\"openPrice\":"+BigDecimal.valueOf(Double.valueOf(asks.getJSONArray(0).get(0).toString()))+",\"optionsName\":\""+symbol+"\",\"priceType\":\"LIMIT\",\"quantity\":"+BigDecimal.valueOf(Double.valueOf(asks.getJSONArray(0).get(1).toString())).setScale(0,BigDecimal.ROUND_DOWN).subtract(BigDecimal.ONE)+"}";
                }
            }else {
                // 挂买单
                param = "{\"direction\":\"long\",\"openPrice\":"+lowestPrice.divide(BigDecimal.valueOf(5),0,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(5)).add(BigDecimal.valueOf(r.nextInt(10)).multiply(BigDecimal.valueOf(5)))+",\"optionsName\":\""+symbol+"\",\"priceType\":\"LIMIT\",\"quantity\":"+r.nextInt(100)+"}";
            }
            System.out.println(param);
            System.out.println(httpUtil.postByJson(host+"/v1/cfd/options/open",param,headers));
            System.out.println("=============开仓==============");

        }
        return new JsonResult<>(0,"success");
    }

    @RequestMapping(value = "/v1/test10",method = RequestMethod.GET)
    public JsonResult test10(@RequestParam("users") String users,
                             @RequestParam("token") String token,
                             @RequestParam("isToken") boolean isToken,
                             @RequestParam("host") String host,
                             @RequestParam("closeNum") int closeNum,
                             @RequestParam("symbol") String symbol
    ) throws Exception {
        // 域名
//        String host = "https://www.hpxshop.me";//https://x.chimchim.top";
//        String host = "https://www-demo.wq6tfggsb8.info";
        HttpUtil httpUtil = new HttpUtil();
//        String token = "";
        String param = "";
        String headers = "";

        // 用户数量
        if (users == null || users.equalsIgnoreCase("0") || users.equalsIgnoreCase("")){
            users = "16610000001,16610000002,16610000003,16610000004,16610000005,16610000006,16610000007,16610000008,16610000009,16610000010," +
                    "16610000011,16610000012,16610000013,16610000014,16610000015,16610000016,16610000017,16610000018,16610000019,16610000020," +
                    "16610000021,16610000022,16610000023,16610000024,16610000025,16610000026,16610000027,16610000028,16610000029,16610000030," +
                    "16610000031,16610000032,16610000033,16610000034,16610000035,16610000036,16610000037,16610000038,16610000039,16610000040," +
                    "16610000041,16610000042,16610000043,16610000044,16610000045,16610000046,16610000047,16610000048,16610000049,16610000050," +
                    "16610000051,16610000052,16610000053,16610000054,16610000055,16610000056,16610000057,16610000058,16610000059,16610000060," +
                    "16610000061,16610000062,16610000063,16610000064,16610000065,16610000066,16610000067,16610000068,16610000069,16610000070," +
                    "16610000071,16610000072,16610000073,16610000074,16610000075,16610000076,16610000077,16610000078,16610000079,16610000080," +
                    "16610000081,16610000082,16610000083,16610000084,16610000085,16610000086,16610000087,16610000088,16610000089,16610000090," +
                    "16610000091,16610000092,16610000093,16610000094,16610000095,16610000096,16610000097,16610000098,16610000100,16610000101";
        }
        // 获取期权币种
        List<OptionsInfoChain> optionsInfoChains = optionsInfoDao.getList();
        // 期权标记价格和delta
        JSONObject optionsTicker;
        // 期权标记价格
        BigDecimal ticker;
        // delta
        double delta;
        // 期权盘口
        JSONObject optionsDepthStep0;
        // 期权卖盘
        JSONArray asks ;
        // 期权买盘
        JSONArray bids ;
        // 指数价格
        BigDecimal indexPrice;
        // 虚值
        BigDecimal OTM;
        // 调整系数
        BigDecimal V;
        // 最高下单价格
        BigDecimal highestPrice ;
        //最低下单价格
        BigDecimal lowestPrice ;
        // Max (1, 4 ×[1- abs (delta)])
        BigDecimal D;
        Random r = new Random();
        int rTmp;
        int rPTmp;
        // 当前委托列表
        JSONArray resCurrentOrders;
        // 当前仓位列表
        JSONArray resCurrentPositions;
        String user;
        for (int i = 0; i < closeNum; i++) {
            rTmp = r.nextInt(users.split(",").length);
            user = users.split(",")[rTmp];
            if (!isToken) {
                if (tokenList.keySet().contains(user)) {
                    token = tokenList.getString(user);
                } else {
                    param = "{\"password\": \"ghw111111\",\"username\": \"" + user + "\"}";
                    // 登录
//                if (token.equalsIgnoreCase(null) || token == ""){
                    token = JSONObject.parseObject(httpUtil.postByJson(host + "/v1/users/membership/sign-in", param)).getJSONObject("data").getString("accessToken");
//                }
                    tokenList.put(user, token);
                }
            }
            headers = "{\"Content-Type\":\"application/json\",\"X-Authorization\":\""+token+"\",\"5fu3\":\"xxOi\"}";
            // 获取当前委托列表
            resCurrentOrders = JSONObject.parseObject(httpUtil.get(host+"/v1/cfd/options/current/orders?pageSize=1111&page=1",headers)).getJSONObject("data").getJSONArray("rows");
            // 获取当前仓位列表
            resCurrentPositions = JSONObject.parseObject(httpUtil.get(host+"/v1/cfd/options/current/positions?pageSize=1111&page=1",headers)).getJSONObject("data").getJSONArray("rows");
//            if (resCurrentOrders.size() >= 1 && r.nextInt(100) >= 50){
//                param = "{\"id\":"+resCurrentOrders.getJSONObject(resCurrentOrders.size()-1).getString("orderId")+"}";
//                System.out.println(param);
//                System.out.println(httpUtil.postByJson(host+"/v1/cfd/options/cancel",param,headers));
//                System.out.println(user+"============撤单==============="+tokenList.getString(user));
//            }
            if (resCurrentPositions.size() == 0){
                continue;
            }
            rPTmp = r.nextInt(resCurrentPositions.size());
            if (symbol == null)
                symbol = resCurrentPositions.getJSONObject(rPTmp).getString("optionName");
            // 期权标记价格和delta
            optionsTicker = JSONObject.parseObject(redisService.get("options::ticker::"+symbol+"_ticker").toString());
            ticker = optionsTicker.getBigDecimal("mp");
            delta = optionsTicker.getDoubleValue("d");
            // 期权盘口 options::depth_step0::BTC-230314-18500-P_depth_step0
            optionsDepthStep0 = JSONObject.parseObject(redisService.get("options::depth_step0::"+symbol+"_depth_step0").toString());
            asks = optionsDepthStep0.getJSONArray("asks");
            bids = optionsDepthStep0.getJSONArray("bids");
            // contract::index_price::btcusdt
            indexPrice = JSONObject.parseObject(redisService.get("contract::index_price::btcusdt").toString()).getBigDecimal("ip");

            //计算最低最高价格
            // 最大下单价= 期权标记价格+ 调整系数× Max (1, 4 ×[1- abs (delta)])
            // 最小下单价= 期权标记价格- 调整系数× Max (1, 4 × [1- abs (delta)])
            // 调整系数= min( 指数价格* 0.06, (指数价格* 0.06+OTM amount* 0.4) ）
            // OTM amount为虚值金额，看涨期权=max(0,（指数价格-行权价格）)；看跌期权=max(0,（行权价格-指数价格)）。
            if (symbol.split("-")[3].equalsIgnoreCase("c")){
                OTM = (indexPrice.subtract(BigDecimal.valueOf(Integer.valueOf(symbol.split("-")[2])))).compareTo(BigDecimal.ZERO) == 1 ?
                        indexPrice.subtract(BigDecimal.valueOf(Integer.valueOf(symbol.split("-")[2]))) : BigDecimal.ZERO;
            }else {
                OTM = (BigDecimal.valueOf(Integer.valueOf(symbol.split("-")[2])).subtract(indexPrice)).compareTo(BigDecimal.ZERO) == 1 ?
                        BigDecimal.valueOf(Integer.valueOf(symbol.split("-")[2])).subtract(indexPrice) : BigDecimal.ZERO;
            }
            // 调整系数= min( 指数价格* 0.06, (指数价格* 0.06+OTM amount* 0.4) ）
            V = (indexPrice.multiply(BigDecimal.valueOf(0.06))).compareTo((indexPrice.multiply(BigDecimal.valueOf(0.06)).add(OTM.multiply(BigDecimal.valueOf(0.4))))) == 1 ?
                    indexPrice.multiply(BigDecimal.valueOf(0.06)).add(OTM.multiply(BigDecimal.valueOf(0.4))) : indexPrice.multiply(BigDecimal.valueOf(0.06));
            // Max (1, 4 × [1- abs (delta)])
            D = (BigDecimal.valueOf(4).multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(Math.abs(delta))))).compareTo(BigDecimal.ONE) == 1 ?
                    BigDecimal.valueOf(4).multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(Math.abs(delta)))) : BigDecimal.ONE ;

            highestPrice = ticker.add(V.multiply(D));
            lowestPrice = ticker.subtract(V.multiply(D)).compareTo(BigDecimal.valueOf(5)) == 1 ? ticker.subtract(V.multiply(D)) : BigDecimal.valueOf(5);

            // 买盘有盘口
            if (bids.size() >= 1){
                rTmp = r.nextInt(100);
                // 挂卖盘
                if (rTmp <= 50){
                    param = "{\"closePrice\":"+BigDecimal.valueOf(Double.valueOf(bids.getJSONArray(0).get(0).toString())).add(BigDecimal.valueOf(5))+"," +
                            "\"id\":"+resCurrentPositions.getJSONObject(rPTmp).getString("positionId")+"," +
                            "\"priceType\":\"LIMIT\"," +
                            "\"quantity\":"+resCurrentPositions.getJSONObject(rPTmp).getBigDecimal("quantity").subtract(resCurrentPositions.getJSONObject(rPTmp).getBigDecimal("doneQuantity"))+"}";
                }else {
                    param = "{\"closePrice\":"+BigDecimal.valueOf(Double.valueOf(bids.getJSONArray(0).get(0).toString()))+"," +
                            "\"id\":"+resCurrentPositions.getJSONObject(rPTmp).getString("positionId")+"," +
                            "\"priceType\":\"LIMIT\"," +
                            "\"quantity\":"+resCurrentPositions.getJSONObject(rPTmp).getBigDecimal("quantity").subtract(resCurrentPositions.getJSONObject(rPTmp).getBigDecimal("doneQuantity"))+"}";
                }
            }else {
                // 挂卖单
                param = "{\"closePrice\":"+highestPrice.divide(BigDecimal.valueOf(5),0,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(5)).subtract(BigDecimal.valueOf(r.nextInt(10)).multiply(BigDecimal.valueOf(5)))+"," +
                        "\"id\":"+resCurrentPositions.getJSONObject(rPTmp).getString("positionId")+"," +
                        "\"priceType\":\"LIMIT\"," +
                        "\"quantity\":"+resCurrentPositions.getJSONObject(rPTmp).getBigDecimal("quantity").subtract(resCurrentPositions.getJSONObject(rPTmp).getBigDecimal("doneQuantity"))+"}";
            }
            System.out.println(param);
            System.out.println(httpUtil.postByJson(host+"/v1/cfd/options/close",param,headers));
            System.out.println(user+"============平仓==============="+tokenList.getString(user));
        }
        return new JsonResult<>(0,"success",tokenList,0);
    }


    /**
     * 撮合做市 币种名称：BTC-11APR23-28500-P
     * @param symbol 28500-P
     * @param date 11
     * @param expiredtime 过期时间
     * @return
     */
    @RequestMapping(value = "/v1/test11",method = RequestMethod.GET)
    public JsonResult test11(@RequestParam("symbol") String symbol,
                             @RequestParam("date") String date,
                             @RequestParam("expiredtime") int expiredtime,
                             @RequestParam("token") String token,
                             @RequestParam("host") String host,
                             @RequestParam("num") int num,
                             @RequestParam("isExchang") boolean isExchang){
        HttpUtil httpUtil = new HttpUtil();
        String headers = "{\"Content-Type\":\"application/json\",\"X-Authorization\":\""+token+"\",\"5fu3\":\"xxOi\"}";
        Jedis jedis = new Jedis("ec2-54-255-156-51.ap-southeast-1.compute.amazonaws.com",7098);
        jedis.select(0);
        JSONObject spiderDvolBtc;
        JSONObject externalTickerOption;
        // 标记价格
        BigDecimal markPrice ;
        //dvol
        BigDecimal volatility ;
        // DVOL系数 (55,65,100;0.07,0.1,0.13)
        BigDecimal dvolRange55 = BigDecimal.valueOf(55);
        BigDecimal dvolRange55Rate = BigDecimal.valueOf(0.07);

        BigDecimal dvolRange65 = BigDecimal.valueOf(65);
        BigDecimal dvolRange65Rate = BigDecimal.valueOf(0.1);

        BigDecimal dvolRange100 = BigDecimal.valueOf(100);
        BigDecimal dvolRange100Rate = BigDecimal.valueOf(0.13);
        // expiredtime_buy_range 买盘过期时间系数. (1,3,6,24;0.5,0.3,0.1,0.02)
        BigDecimal expiredtimeBuyFactor = BigDecimal.ZERO;
        // expiredtime_sell_range 卖盘过期时间系数. (1;0.1,1)
        BigDecimal expiredtimeSellFactor = BigDecimal.ONE;
        if (expiredtime <= 1){
            expiredtimeBuyFactor = BigDecimal.valueOf(0.5);
            expiredtimeSellFactor = BigDecimal.valueOf(1.1);
        }else  if (expiredtime > 1 && expiredtime <= 3){
            expiredtimeBuyFactor = BigDecimal.valueOf(0.3);
        }else  if (expiredtime > 3 && expiredtime <= 6){
            expiredtimeBuyFactor = BigDecimal.valueOf(0.1);
        }else  if (expiredtime > 6 && expiredtime <= 24){
            expiredtimeBuyFactor = BigDecimal.valueOf(0.02);
        }else {
            expiredtimeBuyFactor = BigDecimal.valueOf(0.02);
        }
        BigDecimal buyPrice = BigDecimal.ZERO;
        BigDecimal externalBuyPrice = BigDecimal.ZERO;
        BigDecimal externalSellPrice = BigDecimal.ZERO;
        BigDecimal sellPrice = BigDecimal.ZERO;
        List<JSONObject> res = new ArrayList<>();
        List<OptionsInfoChain> optionsInfoChainList = optionsInfoDao.getList();
        spiderDvolBtc = JSONObject.parseObject(jedis.get("kubiex:market:spider:dvol:btc"),JSONObject.class);
        JSONObject j = new JSONObject();
        j.put("spiderDvolBtc",spiderDvolBtc);
        res.add(j);
        for (OptionsInfoChain o:optionsInfoChainList) {
//            externalTickerOption = JSONObject.parseObject(jedis.get("kubiex:market:external:ticker:option:BTC-"+date+"APR23-"+symbol),JSONObject.class);
            externalTickerOption = JSONObject.parseObject(jedis.get("kubiex:market:external:ticker:option:"+o.getExternalInstrumentName()),JSONObject.class);
            JSONObject res1 = new JSONObject();

//            res1.put("externalTickerOption",externalTickerOption);

            // 标记价格
            markPrice = externalTickerOption.getBigDecimal("markPrice");
            //dvol
            volatility = spiderDvolBtc.getBigDecimal("volatility");

            // 定价系数
            BigDecimal dvolFactor = BigDecimal.ZERO;
            if (volatility.compareTo(dvolRange55) < 0){
                dvolFactor = dvolRange55Rate;
            }else if (volatility.compareTo(dvolRange55) >=0 && volatility.compareTo(dvolRange65) <0 ){
                dvolFactor = dvolRange65Rate;
            }else if (volatility.compareTo(dvolRange65) >=0 && volatility.compareTo(dvolRange100) <0 ){
                dvolFactor = dvolRange100Rate;
            }else {
                dvolFactor = dvolRange100Rate;
            }

            buyPrice = markPrice.multiply((BigDecimal.ONE.subtract(dvolFactor))).multiply((BigDecimal.ONE.subtract(expiredtimeBuyFactor)));
            sellPrice = markPrice.multiply(BigDecimal.ONE.add(dvolFactor)).multiply(expiredtimeSellFactor);
    //        buyPrice = buyPrice.divide(BigDecimal.valueOf(5),0,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(5));
    //        sellPrice = sellPrice.divide(BigDecimal.valueOf(5),0,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(5)).add(BigDecimal.valueOf(5));
            System.out.println("买一订价:"+buyPrice);
            System.out.println("卖一订价:"+sellPrice);
            externalBuyPrice = externalTickerOption.getBigDecimal("bid");
            externalSellPrice = externalTickerOption.getBigDecimal("ask");
            res1.put("externalBuyPrice",externalBuyPrice);
            res1.put("externalSellPrice",externalSellPrice);
            res1.put("buyPrice",buyPrice);
            res1.put("sellPrice",sellPrice);
            res1.put("markPrice",markPrice);
            res1.put("symbol",o.getInstrumentName());
            if (externalBuyPrice.compareTo(BigDecimal.ZERO) != 0 && externalBuyPrice.subtract(buyPrice).abs().compareTo(BigDecimal.valueOf(15)) == 1){
                res1.put("买盘异常","买一价格超出外盘价格，浮动大于了15");
            }
            if (externalSellPrice.compareTo(BigDecimal.ZERO) != 0 && externalSellPrice.subtract(sellPrice).abs().compareTo(BigDecimal.valueOf(15)) == 1){
                res1.put("卖盘异常","卖一价格超出外盘价格，浮动大于了15");
            }
            res.add(res1);
        }
        // 是否交易
        if (isExchang) {
            Random r = new Random();
            int rTmp;
            String param;
            JSONArray resCurrentOrders;
            JSONArray resCurrentPositions;
            // 获取当前仓位列表
            resCurrentPositions = JSONObject.parseObject(httpUtil.get(host + "/v1/cfd/options/current/positions?pageSize=1111&page=1", headers)).getJSONObject("data").getJSONArray("rows");
            symbol = "BTC-2304"+date+"-"+symbol;
            for (int i = 0; i < num; i++) {
                rTmp = r.nextInt(100);
                // 买
                if (rTmp <= 30) {
                    param = "{\"direction\":\"long\",\"openPrice\":" + sellPrice + ",\"optionsName\":\"" + symbol + "\",\"priceType\":\"LIMIT\",\"quantity\":" + r.nextDouble() + "}";
                } else if (rTmp > 30 && rTmp <= 60) { // 部分成交
                    param = "{\"direction\":\"long\",\"openPrice\":" + sellPrice.add(BigDecimal.valueOf(5)) + ",\"optionsName\":\"" + symbol + "\",\"priceType\":\"LIMIT\",\"quantity\":" + r.nextDouble() + "}";
                } else {// 挂单
                    param = "{\"direction\":\"long\",\"openPrice\":" + buyPrice.subtract(BigDecimal.valueOf(5)) + ",\"optionsName\":\"" + symbol + "\",\"priceType\":\"LIMIT\",\"quantity\":" + r.nextDouble() + "}";
                }
                System.out.println(param);
                System.out.println(httpUtil.postByJson(host + "/v1/cfd/options/open", param, headers));
                System.out.println("=============开仓==============");
                // 卖
                rTmp = r.nextInt(100);
                // 获取当前委托列表
                resCurrentOrders = JSONObject.parseObject(httpUtil.get(host + "/v1/cfd/options/current/orders?pageSize=1111&page=1", headers)).getJSONObject("data").getJSONArray("rows");
                if (rTmp <= 50) {
                    param = "{\"closePrice\":" + buyPrice + "," +
                            "\"id\":" + resCurrentPositions.getJSONObject(0).getString("positionId") + "," +
                            "\"priceType\":\"LIMIT\"," +
//                        "\"quantity\":"+r.ne(resCurrentPositions.getJSONObject(0).getBigDecimal("quantity").subtract(resCurrentPositions.getJSONObject(0).getBigDecimal("doneQuantity")).doubleValue())+"}";
                            "\"quantity\":" + r.nextDouble() + "}";
                } else {
                    param = "{\"closePrice\":" + sellPrice.add(BigDecimal.valueOf(5)) + "," +
                            "\"id\":" + resCurrentPositions.getJSONObject(0).getString("positionId") + "," +
                            "\"priceType\":\"LIMIT\"," +
                            "\"quantity\":" + r.nextDouble() + "}";
//                        "\"quantity\":"+resCurrentPositions.getJSONObject(0).getBigDecimal("quantity").subtract(resCurrentPositions.getJSONObject(0).getBigDecimal("doneQuantity"))+"}";
                }
                System.out.println(param);
                System.out.println(httpUtil.postByJson(host + "/v1/cfd/options/close", param, headers));
                System.out.println("============平仓===============");

                // 撤单
                rTmp = r.nextInt(100);
                if (resCurrentOrders.size() >= 1 && r.nextInt(100) >= 50) {
                    param = "{\"id\":" + resCurrentOrders.getJSONObject(r.nextInt(resCurrentOrders.size())).getString("orderId") + "}";
                    System.out.println(param);
                    System.out.println(httpUtil.postByJson(host + "/v1/cfd/options/cancel", param, headers));
                    System.out.println("============撤单===============");
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return new JsonResult<>(0,"success",res,0);
    }

    /**
     * 撮合做市 币种名称：BTC-11APR23-28500-P
     * @param symbol 28500-P
     * @param date 11
     * @param expiredtime 过期时间
     * @return
     */
    @RequestMapping(value = "/v1/test12",method = RequestMethod.GET)
    public JsonResult test12(@RequestParam("symbol") String symbol,
                             @RequestParam("date") String date,
                             @RequestParam("expiredtime") int expiredtime,
                             @RequestParam("token") String token,
                             @RequestParam("host") String host,
                             @RequestParam("num") int num,
                             @RequestParam("isExchang") boolean isExchang){
        HttpUtil httpUtil = new HttpUtil();
        String headers = "{\"Content-Type\":\"application/json\",\"X-Authorization\":\""+token+"\",\"5fu3\":\"xxOi\"}";
        Jedis jedis = new Jedis("ec2-54-255-156-51.ap-southeast-1.compute.amazonaws.com",7098);
        jedis.select(0);
        JSONObject spiderDvolBtc;
        JSONObject externalTickerOption;
        // 标记价格
        BigDecimal markPrice ;
        //dvol
        BigDecimal volatility ;
        // DVOL系数 (55,65,100;0.07,0.1,0.13)
        BigDecimal dvolRange55 = BigDecimal.valueOf(55);
        BigDecimal dvolRange55Rate = BigDecimal.valueOf(0.07);

        BigDecimal dvolRange65 = BigDecimal.valueOf(65);
        BigDecimal dvolRange65Rate = BigDecimal.valueOf(0.1);

        BigDecimal dvolRange100 = BigDecimal.valueOf(100);
        BigDecimal dvolRange100Rate = BigDecimal.valueOf(0.13);
        // expiredtime_buy_range 买盘过期时间系数. (1,3,6,24;0.5,0.3,0.1,0.02)
        BigDecimal expiredtimeBuyFactor = BigDecimal.ZERO;
        // expiredtime_sell_range 卖盘过期时间系数. (1;0.1,1)
        BigDecimal expiredtimeSellFactor = BigDecimal.ONE;
        if (expiredtime <= 1){
            expiredtimeBuyFactor = BigDecimal.valueOf(0.5);
            expiredtimeSellFactor = BigDecimal.valueOf(1.1);
        }else  if (expiredtime > 1 && expiredtime <= 3){
            expiredtimeBuyFactor = BigDecimal.valueOf(0.3);
        }else  if (expiredtime > 3 && expiredtime <= 6){
            expiredtimeBuyFactor = BigDecimal.valueOf(0.1);
        }else  if (expiredtime > 6 && expiredtime <= 24){
            expiredtimeBuyFactor = BigDecimal.valueOf(0.02);
        }else {
            expiredtimeBuyFactor = BigDecimal.valueOf(0.02);
        }
        BigDecimal buyPrice = BigDecimal.ZERO;
        BigDecimal externalBuyPrice = BigDecimal.ZERO;
        BigDecimal externalSellPrice = BigDecimal.ZERO;
        BigDecimal sellPrice = BigDecimal.ZERO;
        JSONObject res = new JSONObject();

        spiderDvolBtc = JSONObject.parseObject(jedis.get("kubiex:market:spider:dvol:btc"),JSONObject.class);
        res.put("spiderDvolBtc",spiderDvolBtc);
        externalTickerOption = JSONObject.parseObject(jedis.get("kubiex:market:external:ticker:option:BTC-"+date+"APR23-"+symbol),JSONObject.class);
        res.put("externalTickerOption",externalTickerOption);

        // 标记价格
        markPrice = externalTickerOption.getBigDecimal("markPrice");
        //dvol
        volatility = spiderDvolBtc.getBigDecimal("volatility");

        // 定价系数
        BigDecimal dvolFactor = BigDecimal.ZERO;
        if (volatility.compareTo(dvolRange55) < 0){
            dvolFactor = dvolRange55Rate;
        }else if (volatility.compareTo(dvolRange55) >=0 && volatility.compareTo(dvolRange65) <0 ){
            dvolFactor = dvolRange65Rate;
        }else if (volatility.compareTo(dvolRange65) >=0 && volatility.compareTo(dvolRange100) <0 ){
            dvolFactor = dvolRange100Rate;
        }else {
            dvolFactor = dvolRange100Rate;
        }

        buyPrice = markPrice.multiply((BigDecimal.ONE.subtract(dvolFactor))).multiply((BigDecimal.ONE.subtract(expiredtimeBuyFactor)));
        sellPrice = markPrice.multiply(BigDecimal.ONE.add(dvolFactor)).multiply(expiredtimeSellFactor);
        //        buyPrice = buyPrice.divide(BigDecimal.valueOf(5),0,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(5));
        //        sellPrice = sellPrice.divide(BigDecimal.valueOf(5),0,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(5)).add(BigDecimal.valueOf(5));
        System.out.println("买一订价:"+buyPrice);
        System.out.println("卖一订价:"+sellPrice);
        externalBuyPrice = externalTickerOption.getBigDecimal("bid");
        externalSellPrice = externalTickerOption.getBigDecimal("ask");
        res.put("externalBuyPrice",externalBuyPrice);
        res.put("externalSellPrice",externalSellPrice);
        res.put("buyPrice",buyPrice);
        res.put("sellPrice",sellPrice);
        res.put("markPrice",markPrice);
        if (externalBuyPrice.compareTo(BigDecimal.ZERO) != 0 && externalBuyPrice.subtract(buyPrice).abs().compareTo(BigDecimal.valueOf(15)) == 1){
            res.put("买盘异常","买一价格超出外盘价格，浮动大于了15");
        }
        if (externalSellPrice.compareTo(BigDecimal.ZERO) != 0 && externalSellPrice.subtract(sellPrice).abs().compareTo(BigDecimal.valueOf(15)) == 1){
            res.put("卖盘异常","卖一价格超出外盘价格，浮动大于了15");
        }
        // 是否交易
        if (isExchang) {
            Random r = new Random();
            int rTmp;
            String param;
            JSONArray resCurrentOrders;
            JSONArray resCurrentPositions;
            // 获取当前仓位列表
            resCurrentPositions = JSONObject.parseObject(httpUtil.get(host + "/v1/cfd/options/current/positions?pageSize=1111&page=1", headers)).getJSONObject("data").getJSONArray("rows");
            symbol = "BTC-2304"+date+"-"+symbol;
            for (int i = 0; i < num; i++) {
                rTmp = r.nextInt(100);
                // 买
                if (rTmp <= 30) {
                    param = "{\"direction\":\"long\",\"openPrice\":" + sellPrice + ",\"optionsName\":\"" + symbol + "\",\"priceType\":\"LIMIT\",\"quantity\":" + r.nextDouble() + "}";
                } else if (rTmp > 30 && rTmp <= 60) { // 部分成交
                    param = "{\"direction\":\"long\",\"openPrice\":" + sellPrice.add(BigDecimal.valueOf(5)) + ",\"optionsName\":\"" + symbol + "\",\"priceType\":\"LIMIT\",\"quantity\":" + r.nextDouble() + "}";
                } else {// 挂单
                    param = "{\"direction\":\"long\",\"openPrice\":" + buyPrice.subtract(BigDecimal.valueOf(5)) + ",\"optionsName\":\"" + symbol + "\",\"priceType\":\"LIMIT\",\"quantity\":" + r.nextDouble() + "}";
                }
                System.out.println(param);
                System.out.println(httpUtil.postByJson(host + "/v1/cfd/options/open", param, headers));
                System.out.println("=============开仓==============");
                // 卖
                rTmp = r.nextInt(100);
                // 获取当前委托列表
                resCurrentOrders = JSONObject.parseObject(httpUtil.get(host + "/v1/cfd/options/current/orders?pageSize=1111&page=1", headers)).getJSONObject("data").getJSONArray("rows");
                if (rTmp <= 50) {
                    param = "{\"closePrice\":" + buyPrice + "," +
                            "\"id\":" + resCurrentPositions.getJSONObject(0).getString("positionId") + "," +
                            "\"priceType\":\"LIMIT\"," +
//                        "\"quantity\":"+r.ne(resCurrentPositions.getJSONObject(0).getBigDecimal("quantity").subtract(resCurrentPositions.getJSONObject(0).getBigDecimal("doneQuantity")).doubleValue())+"}";
                            "\"quantity\":" + r.nextDouble() + "}";
                } else {
                    param = "{\"closePrice\":" + sellPrice.add(BigDecimal.valueOf(5)) + "," +
                            "\"id\":" + resCurrentPositions.getJSONObject(0).getString("positionId") + "," +
                            "\"priceType\":\"LIMIT\"," +
                            "\"quantity\":" + r.nextDouble() + "}";
//                        "\"quantity\":"+resCurrentPositions.getJSONObject(0).getBigDecimal("quantity").subtract(resCurrentPositions.getJSONObject(0).getBigDecimal("doneQuantity"))+"}";
                }
                System.out.println(param);
                System.out.println(httpUtil.postByJson(host + "/v1/cfd/options/close", param, headers));
                System.out.println("============平仓===============");

                // 撤单
                rTmp = r.nextInt(100);
                if (resCurrentOrders.size() >= 1 && r.nextInt(100) >= 50) {
                    param = "{\"id\":" + resCurrentOrders.getJSONObject(r.nextInt(resCurrentOrders.size())).getString("orderId") + "}";
                    System.out.println(param);
                    System.out.println(httpUtil.postByJson(host + "/v1/cfd/options/cancel", param, headers));
                    System.out.println("============撤单===============");
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return new JsonResult<>(0,"success",res,0);
    }

    @RequestMapping(value = "/v1/test13",method = RequestMethod.GET)
    public JsonResult test13(@RequestParam("id") int id){
        if (id == 2 || id == 5 ) {
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new JsonResult<>(0,"success");
    }

    @PostConstruct
    private void init(){
        counter = registry.counter("http_requests_add_total","save","carson");
        failCounter=  registry.counter("http_requests_add_fail_total","save","carson");

        registry.config()
                .meterFilter(MeterFilter.denyNameStartsWith("jvm"))
                .meterFilter(MeterFilter.denyNameStartsWith("hikaricp"))
                .meterFilter(MeterFilter.denyNameStartsWith("rabbitmq"))
                .meterFilter(MeterFilter.denyNameStartsWith("tomcat"))
                .meterFilter(MeterFilter.denyNameStartsWith("system"))
                .meterFilter(MeterFilter.denyNameStartsWith("process"))
                .meterFilter(MeterFilter.denyNameStartsWith("jdbc"));
    }

    @RequestMapping(value="/heap/test",method = RequestMethod.GET)
    public String testHeapUsed(@RequestParam("name") String name,@RequestParam("age")  Integer age) {
        counter.increment();
        if(RandomUtils.nextInt(0,1000)<185){
            failCounter.increment();
            return "error";
        }else{
            return "name="+name+"  age="+age;
        }
    }

    @RequestMapping(value="/heap/SimpleMeterRegistry",method = RequestMethod.GET)
    public String SimpleMeterRegistry() {
        return "success";
    }


    public static StringBuffer getCoupons(List<String> list , final int threadNum){
        int size = list.size();
        if (size == 0 || list == null){
            return null;
        }
        StringBuffer bf = new StringBuffer();
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        List<Future<String>> futures = new ArrayList<Future<String>>(size);
        for (int i = 0; i < threadNum; i++) {
            //将数据分成threadNum份，线程同时执行
            final List<String> subList = list.subList(size / threadNum * i, size / threadNum * (i + 1));
            Callable<String> task = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(Thread.currentThread().getName()+">>>");
                    //对每个线程(线程中的每份数据)的逻辑操作
                    for (String subString : subList) {
//                        System.out.println(subString);
                        buffer.append(subString);
                    }
                    buffer.append("\n");
                    return buffer.toString();
                }
            };
            //添加线程到队列
            futures.add(executorService.submit(task));
        }

        for (int i = 0; i < futures.size(); i++) {
            try {
                bf.append(futures.get(i).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //结束线程执行
        executorService.shutdown();
        return bf;
    }
}
