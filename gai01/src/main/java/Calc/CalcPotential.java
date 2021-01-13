package Calc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 风险报酬比计算
 */
public class CalcPotential {
    public static void main(String[] args) {

    }

    /**
     * 返回风险报酬比的计算值
     * @param businessId
     * @param userId
     * @return
     */
    public static String potential(int businessId,String userId){
        String url = "http://192.168.200.51:10320/risk/tbex-risk-engine-config/report/getDiagramDataByParams";
        String authorization = Config.AUTHORIZATION;
        String param = "{\"businessId\":"+businessId+",\"dateRange\":\"~\",\"userId\":"+userId+"}";
        String res = BaseUtils.postByJson(url,authorization,param);
        return fengxian(res);
    }

    /**
     * 返回countProfit的各个值
     * @param businessId
     * @param userId
     * @return
     */
    public static List<BigDecimal> potential_countProfit(int businessId,String userId){
        String url = "http://192.168.200.51:10320/risk/tbex-risk-engine-config/report/getDiagramDataByParams";
        String authorization = Config.AUTHORIZATION;
        String param = "{\"businessId\":"+businessId+",\"dateRange\":\"~\",\"userId\":"+userId+"}";
        String response = BaseUtils.postByJson(url,authorization,param);
        JSONArray jsona = JSONObject.parseObject(response).getJSONArray("data");
        List<BigDecimal> list = new ArrayList<BigDecimal>();
        for (int i = 0 ; i < jsona.size() ; i ++) {
            list.add(i,jsona.getJSONObject(i).getBigDecimal("count_profit"));
        }
        return list;
    }

    /**
     * 输入返回结果，计算风险报酬比的值
     * @param res
     * @return
     */
    public static String fengxian(String res){
        String response = res;
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
//    System.out.println(i+"==="+profitMax+"==="+count_profit);
            }
        }
        if(profitMax.compareTo(BigDecimal.ZERO) == -1 || profitMax.compareTo(BigDecimal.ZERO) == 0){
            profitMax = new BigDecimal(0);
        }
//        System.out.println("#######################");
//        System.out.println("最大盈利="+profitMax.toPlainString());
//        System.out.println("最大回撤="+profitMin.toString());
        if(profitMax.compareTo(BigDecimal.ZERO) == 0){
            return "0";
//            System.out.println("风险报酬比="+0);
        }else{
            if(profitMin.compareTo(BigDecimal.ZERO) != 0){
//                System.out.println("风险报酬比="+profitMax.divide(profitMin,8, BigDecimal.ROUND_HALF_UP));
                return profitMax.divide(profitMin,8, BigDecimal.ROUND_HALF_UP).toString();
            }else{
//                System.out.println("风险报酬比="+1);
                return "1";
            }
        }
//        System.out.println("#######################");
    }
}