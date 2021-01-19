package com.port.test.swaps;

import com.port.util.BaseUtil;
import com.port.util.Config;
import org.testng.annotations.Test;

public class SwapsOrderFillList extends BaseUtil {
    @Test
    public void test_swapsPosition(){
        String url = Config.REST_CESHI_API+"/v1/swaps/order/fill/list?contractId=1&beginDate=2019-11-15%2010%3A00%3A00&endDate=2019-12-18%2010%3A00%3A00&page=1&pageSize=10";
        String[] str = {"contractId=1","beginDate=2019-11-15%2010%3A00%3A00","endDate=2019-12-18%2010%3A00%3A00","page=1","pageSize=10",Config.ACCESSKEYID+"="+X_58COIN_APIKEY,Config.SIGNATUREMETHOD+"="+
                Config.HMACSHA256,Config.SIGNATUREVERSION+"="+Config.TWO,Config.TIMESTAMP+"="+TIMESTAMP};
        System.out.println(getGetResponseBody(client, url, str));
    }
}
