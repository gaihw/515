package com.test.cases;

import com.test.utils.BaseUtils;
import org.databene.benerator.anno.Source;
import org.testng.annotations.Test;

public class contract extends BaseUtils {

    @Test(dataProvider = "feeder")
    @Source("source/test.csv")
    public void test_loginpPlaceOrderPositionStoploss() {

    }
}
