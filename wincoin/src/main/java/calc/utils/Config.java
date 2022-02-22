package calc.utils;

import java.math.BigDecimal;

public class Config {

    //用户ID
    public static final String userId = "51905033";

    //token
    public static final String token = "bb459aa2d947e320a4004f8f159ef61244537a36d156c367a24a28a891d350b4";

    public static final String ip = "https://dev1.tfbeee.com";
    //下单接口
    public static final String openUrl = ip + "/v1/cfd/trade/btc/open";

    //持仓接口
    public static final String positionUrl = ip + "/v1/cfd/trade/positions";

    //手续费率
    public static final BigDecimal taker = new BigDecimal(0.0004);


}
