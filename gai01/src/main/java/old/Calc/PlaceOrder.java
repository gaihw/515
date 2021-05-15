package old.Calc;

public class PlaceOrder {
    public static void main(String[] args) {
        for (int i = 0 ; i <=14 ; i ++) {
//            mixPalceOrder();
//            swpasPalceOrder();
//            futurePalceOrder();
            regularPalceOrder();
        }
    }

    /**
     * 币本位下单
     */
    public static void swpasPalceOrder(){
        String url_swaps = "http://swapapi.test.58ex.com/order/place";
        //side
        int side = (int) (Math.random() * 2 + 1);
        //size
        int size = (int) (Math.random() * 100 + 1);
        //close
        int close = (int) (Math.random() *2);
        String params_swaps = "contractId=1&type=2&side=" + side + "&price=&size=" + size + "&amount=&close="+close+"&orderFrom=0&clientOid=";
        //14922220000  CQvKhE642pkY25wzkFBADFgUJi03oTdEKDAMdJmG2020`
        System.out.println(BaseUtils.postByForm(url_swaps, "CQvKhE642pkY25wzkFBADFgUJi03oTdEKDAMdJmG2020", params_swaps));
        //14922221110  LA8RlLPMGTTG26TCKnlonZkWQK03pEcWCszrw3w12020
//        System.out.println(BaseUtils.postByForm(url_swaps, "LA8RlLPMGTTG26TCKnlonZkWQK03pEcWCszrw3w12020", params_swaps));
        System.out.println(params_swaps);
    }

    /**
     * usdt下单
     */
    public static void futurePalceOrder(){
        String url_future = "http://usdtfuture.test.58ex.com/usdt/order/place";
        //side
        int side = (int) (Math.random() * 2 + 1);
        //size
        int size = (int) (Math.random() * 100 + 1);
        //close
        int close = (int)(Math.random()*2);
        String params_future = "contractId=1001&type=2&price=&size="+size+"&side="+side+"&leverage=100&close="+close;
        //14922220000  CQvKhE642pkY25wzkFBADFgUJi03oTdEKDAMdJmG2020`
        System.out.println(BaseUtils.postByForm(url_future, "CQvKhE642pkY25wzkFBADFgUJi03oTdEKDAMdJmG2020", params_future));
        //14922221110  LA8RlLPMGTTG26TCKnlonZkWQK03pEcWCszrw3w12020
//        System.out.println(BaseUtils.postByForm(url_future, "LA8RlLPMGTTG26TCKnlonZkWQK03pEcWCszrw3w12020", params_future));
        System.out.println(params_future);
    }
    /**
     * 混合合约下单
     */
    public static void mixPalceOrder(){
        String url_mix = "http://xusdt.test.58ex.com/mix/order/place";
        //side
        int side = (int) (Math.random() * 2 + 1);
        //size
        int size = (int) (Math.random() * 100 + 1);
        //close
        int close = (int)(Math.random()*2);
        String params_mix = "contractId=4001&type=2&price=&size="+size+"&side="+side+"&leverage=100&close="+close;
        //14922220000  CQvKhE642pkY25wzkFBADFgUJi03oTdEKDAMdJmG2020`
        System.out.println(BaseUtils.postByForm(url_mix, "CQvKhE642pkY25wzkFBADFgUJi03oTdEKDAMdJmG2020", params_mix));
        //14922221110  LA8RlLPMGTTG26TCKnlonZkWQK03pEcWCszrw3w12020
        System.out.println(BaseUtils.postByForm(url_mix, "LA8RlLPMGTTG26TCKnlonZkWQK03pEcWCszrw3w12020", params_mix));
        System.out.println(params_mix);
    }
    /**
     * 交割合约下单
     */
    public static void regularPalceOrder(){
        String url_regular = "http://rusdtfuture.test.58ex.com/rusdt/order/place";
        //side
        int side = (int) (Math.random() * 2 + 1);
        //size
        int size = (int) (Math.random() * 50 + 1);
        //close
        int close = (int)(Math.random()*2);
        String params_regular = "contractId=2001&type=2&price=&size="+size+"&side="+side+"&leverage=100&close="+close;
        //14922220000  CQvKhE642pkY25wzkFBADFgUJi03oTdEKDAMdJmG2020`
        System.out.println(BaseUtils.postByForm(url_regular, "8NUKqsvztGKT27lLMSnBJ9EOwP070NAuJGqjFCxT2020", params_regular));
        //14922221110  LA8RlLPMGTTG26TCKnlonZkWQK03pEcWCszrw3w12020
        System.out.println(BaseUtils.postByForm(url_regular, "8NUKqsvztGKT27lLMSnBJ9EOwP070NAuJGqjFCxT2020", params_regular));
        System.out.println(params_regular);
    }
}
