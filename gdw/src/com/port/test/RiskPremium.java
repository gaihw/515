package com.port.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

public class RiskPremium {
    public static void main(String[] args) {
        profit_Max_Min();
    }
    /**
     * 获取用户盈利的最大回撤和最大盈利
     */
    public static void profit_Max_Min(){
        /**
         * 报表
         */
//		HttpClient client = new HttpClient();
//		String url = "http://192.168.200.51:10320/risk/tbex-risk-engine-config/report/getDiagramDataByParams";
//		JSONObject param = new JSONObject();
//
//		String businessId = "2";
//		String userId = "54361";
//
//		param.put("businessId", businessId);
//		param.put("dateRange", "～");
//		param.put("userId", userId);
//		String response = BaseUtil.getPostResponseBody_json(client, url, param);
//		JSONArray jsona = JSONObject.parseObject(response).getJSONArray("data");
        /**
         * 模版
         */
        String response = "{\"message\":\"成功\",\"code\":\"00000000\",\"success\":true,\"data\":[{\"count_profit\":-0.2426196609090909,\"close_fill_time\":1578539921000,\"id\":1.0,\"profit\":-0.24261966090909090910},{\"count_profit\":-0.3032745836363636,\"close_fill_time\":1578540407000,\"id\":2.0,\"profit\":-0.06065492272727272728},{\"count_profit\":-0.3318344336363636,\"close_fill_time\":1578558291000,\"id\":3.0,\"profit\":-0.02855985000000000000},{\"count_profit\":-0.54580893,\"close_fill_time\":1578558319000,\"id\":4.0,\"profit\":-0.21397449636363636364},{\"count_profit\":-0.652796176,\"close_fill_time\":1578559122000,\"id\":5.0,\"profit\":-0.10698724600000000000},{\"count_profit\":-0.7597834219999999,\"close_fill_time\":1578559789000,\"id\":6.0,\"profit\":-0.10698724600000000000},{\"count_profit\":-0.83110825,\"close_fill_time\":1578559789000,\"id\":7.0,\"profit\":-0.07132482800000000000},{\"count_profit\":-1.0094203215384616,\"close_fill_time\":1578566891000,\"id\":8.0,\"profit\":-0.17831207153846153847},{\"count_profit\":-1.2947196418181819,\"close_fill_time\":1578566910000,\"id\":9.0,\"profit\":-0.28529932027972027973},{\"count_profit\":-1.4017068885714286,\"close_fill_time\":1578566934000,\"id\":10.0,\"profit\":-0.10698724675324675325},{\"count_profit\":-1.5443565492307691,\"close_fill_time\":1578567074000,\"id\":11.0,\"profit\":-0.14264966065934065935},{\"count_profit\":-1.8653182799999999,\"close_fill_time\":1578567074000,\"id\":12.0,\"profit\":-0.32096173076923076924},{\"count_profit\":-2.25760485,\"close_fill_time\":1578567152000,\"id\":13.0,\"profit\":-0.39228657000000000000},{\"count_profit\":-2.6498914144444443,\"close_fill_time\":1578567237000,\"id\":14.0,\"profit\":-0.39228656444444444445},{\"count_profit\":-3.22049005,\"close_fill_time\":1578567237000,\"id\":15.0,\"profit\":-0.57059863555555555556},{\"count_profit\":-3.34179989,\"close_fill_time\":1578627273000,\"id\":16.0,\"profit\":-0.12130984000000000000},{\"count_profit\":-3.4631097299999998,\"close_fill_time\":1578635007000,\"id\":17.0,\"profit\":-0.12130984000000000000},{\"count_profit\":5.678933070000001,\"close_fill_time\":1578635741000,\"id\":18.0,\"profit\":9.14204280000000000000},{\"count_profit\":14.820975870000002,\"close_fill_time\":1578639752000,\"id\":19.0,\"profit\":9.14204280000000000000},{\"count_profit\":-17.828624129999998,\"close_fill_time\":1578641685000,\"id\":20.0,\"profit\":-32.64960000000000000000},{\"count_profit\":-6555.93022413,\"close_fill_time\":1578658634000,\"id\":21.0,\"profit\":-6538.10160000000000000000},{\"count_profit\":-6571.23020973,\"close_fill_time\":1578899752000,\"id\":22.0,\"profit\":-15.29998560000000000000},{\"count_profit\":-6572.84073453,\"close_fill_time\":1578899830000,\"id\":23.0,\"profit\":-1.61052480000000000000},{\"count_profit\":-6581.76019053,\"close_fill_time\":1578900201000,\"id\":24.0,\"profit\":-8.91945600000000000000},{\"count_profit\":-6589.19307053,\"close_fill_time\":1578900201000,\"id\":25.0,\"profit\":-7.43288000000000000000},{\"count_profit\":-6595.88266253,\"close_fill_time\":1578900712000,\"id\":26.0,\"profit\":-6.68959200000000000000},{\"count_profit\":-6605.54540653,\"close_fill_time\":1578901247000,\"id\":27.0,\"profit\":-9.66274400000000000000},{\"count_profit\":-6606.28869453,\"close_fill_time\":1578901297000,\"id\":28.0,\"profit\":-0.74328800000000000000},{\"count_profit\":-6612.978286529999,\"close_fill_time\":1578901729000,\"id\":29.0,\"profit\":-6.68959200000000000000},{\"count_profit\":-6628.10722913,\"close_fill_time\":1578905785000,\"id\":30.0,\"profit\":-15.12894260000000000000},{\"count_profit\":-6654.80536313,\"close_fill_time\":1578905785000,\"id\":31.0,\"profit\":-26.69813400000000000000},{\"count_profit\":-7543.85322533,\"close_fill_time\":1578906034000,\"id\":32.0,\"profit\":-889.04786220000000000000},{\"count_profit\":-8432.901087530001,\"close_fill_time\":1578906034000,\"id\":33.0,\"profit\":-889.04786220000000000000},{\"count_profit\":-9321.948949730002,\"close_fill_time\":1578906034000,\"id\":34.0,\"profit\":-889.04786220000000000000}]}";
        JSONArray jsona = JSONObject.parseObject(response).getJSONArray("data");
        BigDecimal profitMax = new BigDecimal(0);
        BigDecimal profitMin = new BigDecimal(0);
        for (int i = 0 ; i < jsona.size() ; i ++){
            JSONObject jsonb = jsona.getJSONObject(i);
            BigDecimal count_profit = new BigDecimal(jsonb.getString("count_profit"));
            /**
             * if(count_profit.subtract(profitMax).compareTo(BigDecimal.ZERO)==0) //等于
             * if(count_profit.subtract(profitMax).compareTo(BigDecimal.ZERO)==1) //大于
             * if(count_profit.subtract(profitMax).compareTo(BigDecimal.ZERO)==-1) //小于
             */
            if( count_profit.subtract(profitMax).compareTo(BigDecimal.ZERO) == 1){
                profitMax = count_profit;
            }
            if(profitMax.subtract(count_profit).subtract(profitMin).compareTo(BigDecimal.ZERO) == 1){
                profitMin = profitMax.subtract(count_profit) ;
//				System.out.println(i+"==="+profitMax+"==="+count_profit);
            }
        }
        if(profitMax.compareTo(BigDecimal.ZERO) == -1 || profitMax.compareTo(BigDecimal.ZERO) == 0){
            profitMax = new BigDecimal(0);
        }
        System.out.println("#######################");
        System.out.println("最大盈利="+profitMax.toPlainString());
        System.out.println("最大回撤="+profitMin.toString());
        if(profitMax.compareTo(BigDecimal.ZERO) == 0){
            System.out.println("风险报酬比="+0);
        }else{
            if(profitMin.compareTo(BigDecimal.ZERO) != 0){
                System.out.println("风险报酬比="+profitMax.divide(profitMin,8, BigDecimal.ROUND_HALF_UP));
            }else{
                System.out.println("风险报酬比="+1);
            }
        }
        System.out.println("#######################");
    }
}
