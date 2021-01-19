package test;

import org.databene.benerator.anno.Source;
import org.testng.annotations.Test;

public class NewTest1 extends NewTest{
	@Test(dataProvider = "feeder")
	@Source("source/contract/usdt/placeOrderPositionComission.csv")
  public void f(String num,String phone,String password,
			String price,String size,String lever,String buyOrSell,String moreOrLess,
			String p_index,String stopLossClose,String triggerPrice,String limiteOrmarket,String executePrice,String parity,
			String index,String cancel) {
		System.out.println(cancel);
  }
}
