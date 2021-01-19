package com.mutest.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author guozhengMu
 * @version 1.0
 * @date 2018/12/20 13:07
 * @description 根据自定义的字符串解析提取json中的特定内容
 * @modify
 */
public class JSONExtractor {
    public static void main(String[] args) {
        String str = "{\"data\" : {\"deth\" : {\"bids\" : [[\"3.637\", \"360000\"]],\"asks\" : [[\"4.273\", \"662\"],[[{\"a\":[1,2]}]]]}}}";
//        String result = jsonPathExpression("{\"status\" : 200,\"employees\" : [{\"a\":[4]},{\"c\":\"2\"}]}", "$.employees[0].a");
        String result = jsonExtractor(str, "$.data.deth.asks[1].[0].[0].a[1]");
        System.out.println(result);
    }

    /**
     * 根据路径表达式解析JSON
     *
     * @param jsonString 待处理的字符串
     * @param matcher    路径表达式
     * @return
     */
    public static String jsonExtractor(String jsonString, String matcher) {
        String[] jsons = matcher.split("\\.");

        JSONObject object = JSON.parseObject(jsonString);
        JSONArray array = new JSONArray();
        String result = "";
        int index;

        for (int i = 1; i < jsons.length; i++) {
            if (jsons[i].contains("[")) {
                // 解析数字
                index = getIndex(jsons[i]);
                if (i == jsons.length - 1) { // 最后一层
                    // 特殊情况处理
                    if (jsons[i].length() <= 3) {
                        // []必然是从array中取值
                        result = array.getString(index);
                    } else {
                        array = object.getJSONArray(jsons[i].split("\\[")[0]);
                        result = array.getString(index);
                    }
                } else { // 不是最后一层
                    if (jsons[i].length() <= 3) {
                        try {
                            array = array.getJSONArray(index);
                        } catch (Exception e) {
                            object = array.getJSONObject(index);
                        }
                    } else {
                        // 不知道下一层是array还是object
                        try {
                            array = object.getJSONArray(jsons[i].split("\\[")[0]).getJSONArray(index);
                        } catch (Exception e) {
                            object = object.getJSONArray(jsons[i].split("\\[")[0]).getJSONObject(index);
                        }
                    }
                }
            } else {
                if (i != jsons.length - 1) {
                    object = object.getJSONObject(jsons[i]);
                } else {
                    result = object.getString(jsons[i]);
                }
            }
        }
        return result;
    }

    /**
     * 将字符串中的数字解析出来
     *
     * @param string:待处理的字符串
     * @return
     */
    public static int getIndex(String string) {
        try {
            String regEx = "[^0-9]";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(string);
            String index = matcher.replaceAll("").trim();
            return Integer.valueOf(index);
        } catch (Exception e) {
            return 0;
        }
    }
}
