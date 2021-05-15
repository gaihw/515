package old.Calc;

import java.util.Random;

public class PlanOder {
    public static void main(String[] args) {

//String[] currencys = {"ERROR","btc","eos","eth","ltc","xrp","etc","dash"};
        String[] contractIds = {"-1","101","102","104","105","106","107","108","109"};

//int[] numbers = {1,2,3,4,5,6,7,8};  // 全币种
//        System.out.println(numbers[0]);
        int[] numbers = {1,2};  // 部分币种
        for (int i = 0; i < numbers.length; i++) {
            Random random = new Random();
            int number = random.nextInt(numbers.length);

            String contractId = contractIds[numbers[number]];  //合约Id
            System.out.println(number);
        }

//        vars.put("contractId",contractId);

// 定义截位规则，digitsIni取自生成currency&amp;id
//        DecimalFormat df = new DecimalFormat("0.00");
////----------------------------------------------------处理标记价格----------------------------------------------------
//// "btc","eos","eth","ltc","xrp","etc","dash"
//        double[] indexPriceSets = {-1,9588.57,2.465,273,44.87,0.2072,6.381,73.155,237.46,183.15};  //手动填写的标记价格，根据实际更改
//        double indexPriceIni = indexPriceSets[Integer.valueOf(contractId) - 4000];  //预设标记价格，用以容错
//
////----------------------------------------------------正常委托流程----------------------------------------------------
//        int randomNum = random.nextInt(100);  //价格和开平仓使用的随机数
//        int randomNum2 = random.nextInt(2000) - 1000;   // 价格使用的随机数,-1000~1000
//
////杠杆
//        String[] leverageArr = { "2", "3", "5", "10", "20", "33", "50", "100" };
//        String leverage = leverageArr[random.nextInt(7)];
////        vars.put("leverage",leverage);
//
//// 是否是市价单-开关
//        if (randomNum >= 50) {
////            vars.put("type","1");  // 限价单
//            System.out.println("type=1");
//        }else{
////            vars.put("type","2");  // 市价单
//            System.out.println("type=2");
//        }
//
////生成下单价格
//        double priceLower = 0;
//        double priceHigh = 0;
//        double price = 0;
//
////近似正态分布，生成下单价格
//        double first = 0.004;   // 标准价格上下差价区间
//        double second = 0.01;
//        double third = 0.02;
//        double forth = 0.04;
//        double fifth = 0.06;
//        double sixth = 0.08;
//
//        if (randomNum > 98) {
//            priceLower = indexPriceIni - indexPriceIni * sixth - indexPriceIni * sixth * randomNum / 100; // 4.6以下
//            priceHigh = indexPriceIni + indexPriceIni * sixth + indexPriceIni * sixth * randomNum / 100; // 5.4以上
//        } else if (randomNum > 95 && randomNum <= 98) {
//            priceLower = indexPriceIni - indexPriceIni * sixth + indexPriceIni * (sixth - fifth) * randomNum / 100; // 4.6-4.7
//            priceHigh = indexPriceIni + indexPriceIni * sixth - indexPriceIni * (sixth - fifth) * randomNum / 100; // 5.3-5.4
//        } else if (randomNum > 89 && randomNum <= 95) {
//            priceLower = indexPriceIni - indexPriceIni * fifth + indexPriceIni * (fifth - forth) * randomNum / 100; // 4.7-4.8
//            priceHigh = indexPriceIni + indexPriceIni * fifth - indexPriceIni * (fifth - forth) * randomNum / 100; // 5.2-5.3
//        } else if (randomNum > 79 && randomNum <= 89) {
//            priceLower = indexPriceIni - indexPriceIni * forth + indexPriceIni * (forth - third) * randomNum / 100; // 4.8-4.9
//            priceHigh = indexPriceIni + indexPriceIni * forth - indexPriceIni * (forth - third) * randomNum / 100; // 5.1-5.2
//        } else if (randomNum > 64 && randomNum <= 79) {
//            priceLower = indexPriceIni - indexPriceIni * third + indexPriceIni * (third - second) * randomNum / 100; // 4.9-4.95
//            priceHigh = indexPriceIni + indexPriceIni * third - indexPriceIni * (third - second) * randomNum / 100; // 5.05-5.1
//        } else if (randomNum > 39 && randomNum <= 64) {
//            priceLower = indexPriceIni - indexPriceIni * second + indexPriceIni * (second - first) * randomNum / 100; // 4.95-4.98
//            priceHigh = indexPriceIni + indexPriceIni * second - indexPriceIni * (second - first) * randomNum / 100; // 5.02-5.05
//        } else if (randomNum <= 39) {
//            priceLower = indexPriceIni - indexPriceIni * first + indexPriceIni * first * randomNum / 100; // 4.98-5
//            priceHigh = indexPriceIni + indexPriceIni * first - indexPriceIni * first * randomNum / 100; // 5-5.02
//        }
//
////price = indexPriceIni + indexPriceIni * second * randomNum2 * 0.001;
////String priceFormat = df.format(price);           //截取保留四位小数，生成String类型
////vars.put("price",priceFormat);
//
//        String priceLowerFormat = df.format(priceLower);
//        String priceHighFormat = df.format(priceHigh);
//
//// 成交期望
//        if (randomNum >= 70) {
//            // 高价买单||低价卖单，成交期望
//            if(randomNum %2 == 0){
////                vars.put("side","1");
////                vars.put("price",priceHighFormat);
//                System.out.println("side=1&price="+priceHighFormat);
//            }else {
////                vars.put("side","2");
////                vars.put("price",priceLowerFormat);
//                System.out.println("side=2&price="+priceLowerFormat);
//            }
//// 挂单期望
//        }else{
//            // 低价买单||高价卖单，挂单期望
//            if(randomNum % 2 == 0){
////                vars.put("type","1");
////                vars.put("side","1");
////                vars.put("price",priceLowerFormat);
//                System.out.println("type=1&side=1&price="+priceLowerFormat);
//            }else {
////                vars.put("type","1");
////                vars.put("side","2");
////                vars.put("price",priceHighFormat);
//                System.out.println("type=1&side=2&price="+priceHighFormat);
//            }
//        }
    }
}
