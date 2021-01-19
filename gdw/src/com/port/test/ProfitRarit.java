package com.port.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.port.util.BaseUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Set;

public class ProfitRarit {
    public static void main(String[] args) {

        String url_acount = "http://192.168.200.51:10340/usdt/dailyBalance";
        String param_acount = "{\"userId\":\"333387939\"}";
        String url_worth = "http://192.168.200.51:10340/usdt/dailyIncomAndWorth";
        String param_worth = "{\"userId\":\"333387939\"}";
        //计算日均资产
        String response_acount = BaseUtil.postByForm(url_acount,param_acount  );
        BigDecimal acount = new BigDecimal("0.000000000");
        JSONArray j_data = JSONArray.parseArray(response_acount);
        for (int i = 0 ; i < j_data.size() ; i ++){
            acount = acount.add(j_data.getJSONObject(i).getBigDecimal("totalBalance"))
                    .add(j_data.getJSONObject(i).getBigDecimal("withdraw"));
        }
        //日均资产
        BigDecimal totalAcount = acount.divide(BigDecimal.valueOf(j_data.size()),8, RoundingMode.HALF_UP);

        //计算累计盈利量，累计盈利率，权重
        String response_worth = BaseUtil.postByForm(url_worth,param_worth);
        JSONArray j_data1 = JSONArray.parseArray(response_worth);
        //收益量
        JSONObject income = new JSONObject();
        //合约价值
        JSONObject contractWorth = new JSONObject();
        //总的收益量
        BigDecimal total_income = new BigDecimal("0.00000000");
        //总的合约价值
        BigDecimal total_worth = new BigDecimal("0.00000000");
        for (int i = 0 ; i < j_data1.size() ; i ++){
            String contractId = j_data1.getJSONObject(i).getString("contractId");
            if (!income.containsKey(contractId)) {
                income.put(contractId,"0.00000000");
                income.put(contractId,income.getBigDecimal(contractId).add(j_data1.getJSONObject(i).getBigDecimal("income")));
            }else{
                income.put(contractId,income.getBigDecimal(contractId).add(j_data1.getJSONObject(i).getBigDecimal("income")));
            }
            if (!contractWorth.containsKey(contractId)) {
                contractWorth.put(contractId,"0.00000000");
                contractWorth.put(contractId,contractWorth.getBigDecimal(contractId).add(j_data1.getJSONObject(i).getBigDecimal("contractWorth")));
            }else{
                contractWorth.put(contractId,contractWorth.getBigDecimal(contractId).add(j_data1.getJSONObject(i).getBigDecimal("contractWorth")));
            }
            total_income = total_income.add(j_data1.getJSONObject(i).getBigDecimal("income"));
            total_worth = total_worth.add(j_data1.getJSONObject(i).getBigDecimal("contractWorth"));
        }
        System.out.println("总收益率("+total_income.divide(totalAcount,8, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))+")；总收益量("+total_income+")");
        Set contract = income.keySet();
        Iterator it= contract.iterator();
        while (it.hasNext()){
            String contractKey = (String) it.next();
            //权重*总资产
            BigDecimal xa = contractWorth.getBigDecimal(contractKey).divide(total_worth,8, RoundingMode.HALF_UP).multiply(totalAcount);
            System.out.println(contractKey+"收益率("+income.getBigDecimal(contractKey).divide(xa,8, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))+")；收益量("+
                    income.getBigDecimal(contractKey)+")");
        }
    }
}
