package com.mutest.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mutest.advice.BusinessErrorException;
import com.mutest.advice.BusinessMsgEnum;
import com.mutest.model.JsonResult;
import com.mutest.utils.URLConnection;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/swap")
public class SwapController {
    public static void main(String[] args) {
        String message = "{\"data\" : {\"deth\" : {\"bids\" : [[\"9427.69\", \"700000\"], [\"9427.7\", \"700000\"], [\"9427.71\", \"700000\"], [\"9427.72\", \"700000\"] , [\"9427.97\", \"1696413810\"]],\"asks\" : [[\"9428.15\", \"197093265.00\"], [\"9428.16\", \"1653400000\"], [\"9428.17\", \"1725500000\"], [\"9428.18\", \"1700300000\"]] } } }";

        JSONObject deth = JSON.parseObject(message).getJSONObject("data").getJSONObject("deth");
        JSONArray bids = deth.getJSONArray("bids");
        JSONArray asks = deth.getJSONArray("asks");

//        List<JSONObject> askList = new ArrayList<>();
//        for (int i = 0; i < bids.size(); i++) {
//            JSONObject object = new JSONObject();
//            JSONArray arrayChild = (JSONArray) bids.get(i);
//
//            object.put("price", arrayChild.get(0));
//            object.put("size", arrayChild.get(1));
//            askList.add(object);
//        }
//
//        List<JSONObject> bidList = new ArrayList<>();
//        for (int j = asks.size() - 1; j >= 0; j--) {
//            JSONObject object = new JSONObject();
//            JSONArray arrayChild = (JSONArray) asks.get(j);
//
//            object.put("price", arrayChild.get(0));
//            object.put("size", arrayChild.get(1));
//            bidList.add(object);
//        }

        System.out.println("asks->>>>>>>>>>>>>" + asks);
        System.out.println("bids->>>>>>>>>>>>>" + bids);

        System.out.println("askList->>>>>>>>>>>");
        System.out.println("bidList->>>>>>>>>>>");
    }

    @RequestMapping(value = "/deth", method = RequestMethod.GET)
    public JsonResult getDeth(@RequestParam String contractId) {
        try {
//            String message = "{\"data\" : {\"deth\" : {\"bids\" : [[\"9427.69\", \"700000\"], [\"9427.7\", \"700000\"], [\"9427.71\", \"700000\"], [\"9427.72\", \"700000\"] , [\"9427.97\", \"1696413810\"]],\"asks\" : [[\"9428.15\", \"197093265.00\"], [\"9428.16\", \"1653400000\"], [\"9428.17\", \"1725500000\"], [\"9428.18\", \"1700300000\"]] } } }";

            String host = "http://swapsnewapi.test.58ex.com/swaps/market/order_book?contractId=" + contractId;
            String result = URLConnection.httpGet(host, new HashMap<>());
            System.out.println("result-------->>>>>>>>>>>>>>>>>" + result);
            JSONObject deth = JSON.parseObject(result).getJSONObject("data").getJSONObject("result");

            JSONArray bids = deth.getJSONArray("bids");
            JSONArray asks = deth.getJSONArray("asks");

            JSONObject data = new JSONObject();
            List<JSONObject> bidList = new ArrayList<>();
            List<JSONObject> askList = new ArrayList<>();
            for (int i = 6; i >= 0; i--) {
                JSONObject bid = new JSONObject();
                JSONObject ask = new JSONObject();

                if (bids.size() - 1 + i - 6 >= 0) {
                    JSONArray bidChild = (JSONArray) bids.get(bids.size() - 1 + i - 6);
                    bid.put("price", bidChild.get(0));
                    bid.put("size", bidChild.get(1));
                } else {
                    bid.put("price", "--");
                    bid.put("size", "--");
                }
                bidList.add(bid);

                if (i > asks.size() - 1) {
                    ask.put("price", "--");
                    ask.put("size", "--");
                } else {
                    JSONArray askChild = (JSONArray) asks.get(i);
                    ask.put("price", askChild.get(0));
                    ask.put("size", askChild.get(1));
                }
                askList.add(ask);
            }
            data.put("askList", askList);
            data.put("bidList", bidList);

            return new JsonResult(data, "操作成功！");
        } catch (Exception e) {
            return new JsonResult(new BusinessErrorException(BusinessMsgEnum.UNEXPECTED_EXCEPTION));
        }
    }

    @RequestMapping(value = "/place", method = RequestMethod.POST)
    public JsonResult place(@RequestBody JSONObject order) {
        String token = order.getString("token");
        String contractId = order.getString("contractId");
        String close = order.getString("close");
        String type = order.getString("type");
        String side = order.getString("side");
        String price = order.getString("price");
        String size = order.getString("size");
        String leverage = order.getString("leverage");

        String path = "http://swapsnewapi.test.58ex.com/swaps/order/place";
        String postData = "contractId=" + contractId + "&type=" + type + "&side=" + side + "&price=" + price + "&size=" + size + "&close=" + close + "&leverage=" + leverage;
        Map<String, String> headers = new HashMap<>();
        headers.put("access_token", token);

        String result = URLConnection.httpPost(path, postData, headers);
        return new JsonResult("0", result);
    }
}
